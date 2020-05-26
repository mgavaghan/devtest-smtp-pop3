package org.gavaghan.lisa.sdk.email.step.smtp.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.devtest.autostep.AutoStep;
import org.gavaghan.devtest.autostep.Property;
import org.gavaghan.devtest.autostep.TypeName;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;

import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;

/**
 * SMTP client step.
 *  
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@TypeName("SMTP Client Command")
@Property(name = "Command", mandatory = true)
public class SMTPClientStep extends AutoStep 
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
	static private String getSMTPResponse(TestExec testExec, BufferedReader reader) throws IOException
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
	 * Get the command string.
	 * 
	 * @param testExec
	 * @return
	 * @throws TestRunException
	 */
	protected String getCommand(TestExec testExec) throws TestRunException
	{
		String command = getParsedProperty(testExec, "Command");
		if (command.length() < 4) throw new TestRunException("command is too short: " + command, null);
		return command;
	}

   @Override
   protected Object doNodeLogic(TestExec testExec) throws Exception
   {
      return doRequestResponse(testExec, getCommand(testExec));
   }
   
	/**
	 * Send command and get response.
	 */
	@SuppressWarnings("resource")
	static protected String doRequestResponse(TestExec testExec, String command) throws Exception
	{
		// get reader and writer
		BufferedReader reader = (BufferedReader) testExec.getStateObject(SMTPConnectStep.READER_KEY);
		BufferedWriter writer = (BufferedWriter) testExec.getStateObject(SMTPConnectStep.WRITER_KEY);

		if ((reader == null) || (writer == null)) throw new TestRunException("Not connected to an SMTP server.", null);

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
