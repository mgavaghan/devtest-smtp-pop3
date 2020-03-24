package org.gavaghan.lisa.sdk.email.step.smtp.vse;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.step.MailListenStep;
import org.gavaghan.lisa.sdk.email.step.MailRespondStep;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;
import org.gavaghan.lisa.sdk.email.tph.SMTPProtocolHandler;

import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.model.TransientResponse;

/**
 * Respond to an SMTP request.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class SMTPRespondStep extends MailRespondStep
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(SMTPRespondStep.class);
	
	@Override
	public String getTypeName() throws Exception
	{
		return "SMTP Respond";
	}

	/**
	 * Find the open socket and close it.
	 */
	@Override
	public void destroy(TestExec testExec)
	{
		SMTPListenStep.closeSMTPConnection(testExec);

		super.destroy(testExec);
	}

	@Override
	protected void respond(TestExec testExec) throws Exception
	{
		LOG.debug("Responding");

		// get response
		List<?> responses = (List<?>) testExec.getStateObject(getResponsePropertyKey());
		TransientResponse response = (TransientResponse) responses.get(0);
		String responseBody = response.getBodyAsString();
		if (LOG.isDebugEnabled())  LOG.debug("Response body = " + responseBody);
		if (LOG.isDebugEnabled())  LOG.debug("SMTP command  = " + testExec.getStateValue(SMTPListenStep.COMMAND_KEY));
		responseBody = testExec.parseInState(responseBody).trim();
		if (LOG.isDebugEnabled())  LOG.debug("After parsing = " + testExec.getStateValue(SMTPListenStep.COMMAND_KEY));
		
		String statusCode = response.getMetaData().get(SMTPProtocolHandler.STATUS_NAME);

		if ((statusCode == null) || (statusCode.length() != 3))
		{
			statusCode = "550";
			responseBody = "TransientResponse " + response.getId() + " contains illegal status code.";
		}

		// think, think, think
		processThinkTime(testExec, response.getThinkTimeSpec(), "Think for SMTP Response", "Think for SMTP Response", true);

		// get reader and writer
		try
		{
			@SuppressWarnings("resource")
         Writer writer = getWriter(testExec);
			StringReader sr = new StringReader(responseBody);
			BufferedReader br = new BufferedReader(sr);
			String pending = br.readLine();
			String line;

			// send the response
			for (;;)
			{
				line = br.readLine();

				// write a line
				if (LOG.isDebugEnabled()) LOG.debug("Sending: " + pending);
				writer.write(statusCode);
				writer.write((line == null) ? ' ' : '-');
				writer.write(pending);
				writer.write(EmailConstants.CRLF);

				// get next line
				if (line == null) break;
				pending = line;
			}

			writer.flush();

			// check for QUIT
			if (testExec.getStateObject(MailListenStep.QUIT_KEY) != null)
			{
				SMTPListenStep.closeSMTPConnection(testExec);
			}
		}
		catch (Exception exc)
		{
			// if anything fails, quietly cleanup and leave
			SMTPListenStep.closeSMTPConnection(testExec);
		}
	}
}