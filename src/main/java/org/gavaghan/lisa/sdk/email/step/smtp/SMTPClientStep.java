package org.gavaghan.lisa.sdk.email.step.smtp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.step.MailClientStep;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;

import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;

/**
 * SMTP client step.
 *  
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPClientStep extends MailClientStep
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(SMTPClientStep.class);

	/**
	 * Get the SMTP response.
	 * 
	 * @param testExec
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private String getSMTPResponse(TestExec testExec, BufferedReader reader) throws IOException
	{
		LOG.debug("Reading SMTP response");
		testExec.removeState("smtp.status");

		// capture the first line of data
		StringBuilder builder = new StringBuilder();
		String line = reader.readLine();
		if (LOG.isDebugEnabled()) LOG.debug("line = " + line);
		if (line == null)
		{
			LOG.debug("getSMTPResponse() detected end of input");
			throw new IOException("getSMTPResponse() detected end of input");
		}
		builder.append(line);
		builder.append(EmailConstants.CRLF);

		// set status code
		testExec.setStateValue("smtp.status", line.substring(0, 3));

		// keep going until we get an EOM indicator
		while (line.charAt(3) == '-')
		{
			line = reader.readLine();
			if (line == null)
			{
				LOG.debug("Exiting because we're out of data");
				break;
			}
			builder.append(line);
			builder.append(EmailConstants.CRLF);
		}

		String response = builder.toString();

		if (LOG.isDebugEnabled()) LOG.debug("SMTP Response: " + response);

		return response;
	}

	/**
	 * Get the type name.
	 */
	@Override
	public String getTypeName() throws Exception
	{
		return "SMTP Client";
	}

	/**
	 * Get the command string.
	 * 
	 * @param testExec
	 * @return
	 * @throws TestRunException
	 */
	protected String getCommand(TestExec testExec) throws TestRunException
	{
		String command = testExec.parseInState(getCommand()).trim();
		if (command.length() < 4) throw new TestRunException("command is too short: " + command, null);
		return command;
	}

	/**
	 * Send command and get response.
	 */
	@Override
	protected Object doNodeLogic(TestExec testExec) throws Exception
	{
		// get reader and writer
		BufferedReader reader = (BufferedReader) testExec.getStateObject(SMTPConnectStep.READER_KEY);
		BufferedWriter writer = (BufferedWriter) testExec.getStateObject(SMTPConnectStep.WRITER_KEY);

		if ((reader == null) || (writer == null)) throw new TestRunException("Not connected to an SMTP server.", null);

		// expand the command
		LOG.debug("About to execute SMTP command.");
		String command = getCommand(testExec);

		// send the command
		if (LOG.isDebugEnabled()) LOG.debug("Sending: " + command);
		writer.write(command);
		writer.write(EmailConstants.CRLF);
		writer.flush();

		// get the response
		String response = getSMTPResponse(testExec, reader);

		// if it was a QUIT, close the connection
		if (command.substring(0, 4).toUpperCase().equals("QUIT"))
		{
			SMTPConnectStep.closeSMTPConnection(testExec);
		}

		return response;
	}
}
