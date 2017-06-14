package org.gavaghan.lisa.sdk.email.tph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.AUTH;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.DATA;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.GREET;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.MultistageAUTH;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.PAYLOAD;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.QUIT;

/**
 * VSE recorder for SMTP.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPRecorder extends MailRecorder
{
	/** Logger. */
	static private final Log LOG = LogFactory.getLog(SMTPRecorder.class);

	/** Recording strategy. */
	private SMTPTransactionRecorder mRecorder;

	/**
	 * Create a new recorder.
	 * 
	 * @param recorder
	 *           recording strategy
	 * @param server
	 *           SMTP server manager
	 * @param targetHost
	 *           target host
	 * @param targetPort
	 *           target port
	 * @param sslToServer
	 */
	public SMTPRecorder(SMTPTransactionRecorder recorder, MailServer server, String targetHost, int targetPort, boolean sslToServer)
	{
		super(server, targetHost, targetPort, sslToServer);
		mRecorder = recorder;
	}

	/**
	 * Fully read a multiline SMTP response.
	 * 
	 * @param client
	 * @param reader
	 * @param lines
	 * @return
	 * @throws IOException
	 */
	private String fullyReadResponse(Writer client, BufferedReader reader, List<String> lines) throws IOException
	{
		String line;
		String status = null;

		for (;;)
		{
			// get and forward next line of response
			line = reader.readLine();
			if (line == null)
			{
				LOG.warn("Unexpectedly out of data reading response");
				status = null; // clear status on incomplete response
				break;
			}
			if (LOG.isDebugEnabled()) LOG.debug("Server sent: " + line);

			client.write(line);
			client.write(EmailConstants.CRLF);
			client.flush();

			// this should never happen, but consider it EOM
			if (line.length() < 3)
			{
				LOG.warn("Response line too short: " + line);
				status = null;
				break;
			}

			// get the status code off of the first line
			if (status == null)
			{
				status = line.substring(0, 3);
			}

			// add this to the lines of response
			lines.add(line.substring(4));
			if (line.charAt(3) != '-') break;
		}

		return status;
	}

	/**
	 * Manage the conversation between a client and server.
	 * 
	 * @param session
	 * @param client
	 * @param server
	 * @throws IOException
	 */
	@Override
	protected void manageConversation(String session, Socket client, Socket server) throws IOException
	{
		// turn all four streams into readers and writers
		InputStream clientIS = client.getInputStream();
		BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientIS, EmailConstants.MAIL_ENCODING));

		OutputStream clientOS = client.getOutputStream();
		OutputStreamWriter clientStreamWriter = new OutputStreamWriter(clientOS, EmailConstants.MAIL_ENCODING);

		InputStream serverIS = server.getInputStream();
		BufferedReader serverReader = new BufferedReader(new InputStreamReader(serverIS, EmailConstants.MAIL_ENCODING));

		OutputStream serverOS = server.getOutputStream();
		OutputStreamWriter serverStreamWriter = new OutputStreamWriter(serverOS, EmailConstants.MAIL_ENCODING);

		long start = 0; // request start time
		long elapsed = 0; // think time measurement
		boolean greetingReceived = false; // flag if connection greeting was received
		boolean dataMode = false; // flag if DATA was received and we're expecting a mail payload
		MultistageAUTH authStage = null; // next stage in multistage AUTH
		StringBuilder payload = null; // mail payload

		// loop until client disconnects or a shutdown is requested
		while (!isShutdown())
		{
			String line = null;
			SMTPRequest request = null;
			String rawRequest = null;

			// if the sign-on greeting hasn't been received, yet
			if (!greetingReceived)
			{
				// make an implicit request object
				greetingReceived = true;
				request = new GREET();
			}
			// otherwise, wait for a conventional request
			else
			{
				// get incoming record
				line = clientReader.readLine();
				if (line == null)
				{
					LOG.info("Client disconnected");
					break;
				}
				if (LOG.isDebugEnabled()) LOG.debug("line = " + line);

				// if we're in the middle of AUTH, pass it along
				if (authStage != null)
				{
					rawRequest = line;
					request = authStage.createRequest(line);
				}
				// if not in DATA mode, parse the request
				else if (!dataMode)
				{
					rawRequest = line;
					request = SMTPRequest.parse(line);
				}
				// else, collect content during DATA mode
				else
				{
					// if end of DATA mode
					if (line.equals("."))
					{
						rawRequest = payload.toString();
						request = new PAYLOAD(rawRequest + EmailConstants.CRLF + "." + EmailConstants.CRLF);
						dataMode = false;
						payload = null;
					}
					// else, append to payload
					else
					{
						payload.append(line);
						payload.append(EmailConstants.CRLF);
					}
				}

				// forward to server
				serverStreamWriter.write(line);
				serverStreamWriter.write(EmailConstants.CRLF);
				serverStreamWriter.flush();
			}
			// ***********************************************************************************************
			// if we're in the middle of AUTH
			if (authStage != null)
			{
				// if we have a complete request
				if (request != null)
				{
					// start the think time clock
					start = System.currentTimeMillis();

					// fully read the response
					List<String> lines = new ArrayList<String>();
					String status = fullyReadResponse(clientStreamWriter, serverReader, lines);
					if (status == null) throw new RuntimeException("Unable to read response");

					// calculate think time
					elapsed = System.currentTimeMillis() - start;

					// build and record the response object
					SMTPResponse response = new SMTPResponse(status, lines);
					if (mRecorder != null) mRecorder.record(session, request, response, rawRequest, (int) elapsed);

					// get next stage
					authStage = authStage.getNextStage();
				}
			}
			// else, if we're not in data mode, complete request
			else if (!dataMode)
			{
				// start the think time clock
				if (!(request instanceof PAYLOAD))
				{
					start = System.currentTimeMillis();
				}

				// fully read the response
				List<String> lines = new ArrayList<String>();
				String status = fullyReadResponse(clientStreamWriter, serverReader, lines);
				if (status == null) throw new RuntimeException("Unable to read response");

				// calculate think time
				elapsed = System.currentTimeMillis() - start;

				// build and record the response object
				SMTPResponse response = new SMTPResponse(status, lines);
				if (mRecorder != null) mRecorder.record(session, request, response, rawRequest, (int) elapsed);
			}

			// determine if we've now entered DATA mode
			if (!dataMode)
			{
				// start the think time clock
				start = System.currentTimeMillis();

				dataMode = request instanceof DATA;
				if (dataMode) payload = new StringBuilder();
			}

			// did we begin AUTH?
			if (request instanceof AUTH)
			{
				AUTH auth = (AUTH) request;
				authStage = auth.getNextStage();
			}

			// request to quit?
			if (request instanceof QUIT) break;
		}
	}
}
