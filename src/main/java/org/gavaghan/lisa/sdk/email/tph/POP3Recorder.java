package org.gavaghan.lisa.sdk.email.tph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.GREET;

/**
 * VSE recorder for POP3.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class POP3Recorder extends MailRecorder
{
	/** Logger. */
	static private final Log LOG = LogFactory.getLog(POP3Recorder.class);

	/** Recording strategy. */
	private POP3TransactionRecorder mRecorder;

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
	public POP3Recorder(POP3TransactionRecorder recorder, MailServer server, String targetHost, int targetPort, boolean sslToServer)
	{
		super(server, targetHost, targetPort, sslToServer);
		mRecorder = recorder;
	}

	/**
	 * Manage the conversation between a client and server.
	 * 
	 * @param session
	 * @param client
	 * @param server
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
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

		// loop until client disconnects or a shutdown is requested
		while (!isShutdown())
		{
			String line = null;
			POP3Request request = null;
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
				rawRequest = line;
				request = POP3Request.parse(line);

				// forward to server
				serverStreamWriter.write(line);
				serverStreamWriter.write(EmailConstants.CRLF);
				serverStreamWriter.flush();
			}			
			// ***********************************************************************************************
			
			// get first line of response
			start = System.currentTimeMillis();
			line = serverReader.readLine();
			
			// calculate think time
			elapsed = System.currentTimeMillis() - start;
			
			clientStreamWriter.write(line);
			clientStreamWriter.write(EmailConstants.CRLF);
			clientStreamWriter.flush();
			
			boolean success = line.startsWith("+");
			int space = line.indexOf(' ');
			if (space < 0)
			{
				line = "";
			}
			else
			{
				line = line.substring(space+1).trim();
			}
			POP3Response response = new POP3Response(success, line);
			
			// do multiline read
			if (success && request.isMultiLineResponse())
			{
				StringBuilder builder = new StringBuilder();
				
				// keep reading lines until end marker.
				for (;;)
				{
					line = serverReader.readLine();
					if (line == null)  break;
					
					clientStreamWriter.write(line);
					clientStreamWriter.write(EmailConstants.CRLF);
					if (line.equals("."))  break;
					
					if (line.startsWith(".."))  line = line.substring(1);
					
					builder.append(line);
					builder.append(EmailConstants.CRLF);
				}
				
				clientStreamWriter.flush();
				response.setMoreContent(builder.toString());
			}

			// build and record the response object
			if (mRecorder != null) mRecorder.record(session, request, response, rawRequest, (int) elapsed);
		}
	}
}
