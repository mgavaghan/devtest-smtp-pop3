package org.gavaghan.lisa.sdk.email.step.pop3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.step.MailClientStep;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;
import org.gavaghan.lisa.sdk.email.tph.POP3Request;

import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;

/**
 * POP3 client step.
 *  
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class POP3ClientStep extends MailClientStep
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(POP3ClientStep.class);

	/**
	 * Get the POP3 response.
	 * 
	 * @param testExec
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private String getPOP3Response(TestExec testExec, BufferedReader reader, boolean multiline) throws IOException
	{
		LOG.debug("Reading POP3 response");
		testExec.removeState("pop3.status");

		// capture the first line of data
		String line = reader.readLine();
		if (LOG.isDebugEnabled()) LOG.debug("line = " + line);
		if (line == null)
		{
			LOG.debug("getPOP3Response() detected end of input");
			throw new IOException("getPOP3Response() detected end of input");
		}
		if (!multiline)  return line;
		
		// get multiline response
		StringBuilder builder = new StringBuilder();
		builder.append(line);
		builder.append(EmailConstants.CRLF);

		// set status code
		testExec.setStateValue("pop3.status", line.substring(0, 3));

		// keep going until we get an EOM indicator
		for (;;)
		{
			line = reader.readLine();
			if (line == null)
			{
				LOG.debug("Exiting because we're out of data");
				break;
			}
			if (line.equals(".")) break;
			
			if (line.startsWith(".."))  line = line.substring(1);
			
			builder.append(line);
			builder.append(EmailConstants.CRLF);
		}

		String response = builder.toString();

		if (LOG.isDebugEnabled()) LOG.debug("POP3 Response: " + response);

		return response;
	}

	/**
	 * Get the type name.
	 */
	@Override
	public String getTypeName() throws Exception
	{
		return "POP3 Client";
	}

	/**
	 * Send command and get response.
	 */
	@Override
	protected Object doNodeLogic(TestExec testExec) throws Exception
	{
		// get reader and writer
		BufferedReader reader = (BufferedReader) testExec.getStateObject(POP3ConnectStep.READER_KEY);
		BufferedWriter writer = (BufferedWriter) testExec.getStateObject(POP3ConnectStep.WRITER_KEY);

		if ((reader == null) || (writer == null)) throw new TestRunException("Not connected to a POP3 server.", null);

		// expand the command
		LOG.debug("About to execute POP3 command.");
		String command = testExec.parseInState(getCommand()).trim();
		if (command.length() < 4) throw new TestRunException("command is too short: " + command, null);

		// send the command
		if (LOG.isDebugEnabled()) LOG.debug("Sending: " + command);
		writer.write(command);
		writer.write(EmailConstants.CRLF);
		writer.flush();
		
		// parse the command
		POP3Request request = POP3Request.parse(command);

		// get the response
		String response = getPOP3Response(testExec, reader, request.isMultiLineResponse());

		// if it was a QUIT, close the connection
		if (command.substring(0, 4).toUpperCase().equals("QUIT"))
		{
			POP3ConnectStep.closePOP3Connection(testExec);
		}

		return response;
	}
}
