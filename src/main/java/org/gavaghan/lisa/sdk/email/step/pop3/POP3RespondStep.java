package org.gavaghan.lisa.sdk.email.step.pop3;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.step.MailListenStep;
import org.gavaghan.lisa.sdk.email.step.MailRespondStep;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;
import org.gavaghan.lisa.sdk.email.tph.POP3ProtocolHandler;

import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.model.TransientResponse;

/**
 * Respond to a POP3 request.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class POP3RespondStep extends MailRespondStep
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(POP3RespondStep.class);

	@Override
	public String getTypeName() throws Exception
	{
		return "POP3 Respond";
	}

	/**
	 * Find the open socket and close it.
	 */
	@Override
	public void destroy(TestExec testExec)
	{
		POP3ListenStep.closePOP3Connection(testExec);

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
		if (responseBody != null)
		{
			responseBody = testExec.parseInState(responseBody).trim();
		}
		else
		{
			responseBody = "";
		}

		String status = response.getMetaData().get(POP3ProtocolHandler.STATUS_NAME);

		if (status == null)
		{
			status = "-ERR";
			responseBody = "TransientResponse " + response.getId() + " contains illegal status value.";
		}

		// think, think, think
		processThinkTime(testExec, response.getThinkTimeSpec(), "Think for POP3 Response", "Think for POP3 Response", true);

		// get reader and writer
		try
		{
			Writer writer = getWriter(testExec);
			StringReader sr = new StringReader(responseBody);
			BufferedReader br = new BufferedReader(sr);
			String line = br.readLine();

			// send first line of the response
			if (LOG.isDebugEnabled()) LOG.debug("Sending: " + line);
			writer.write(status);
			if ((line != null) && (line.length() > 0))
			{
				writer.write(' ');
				writer.write(line);
			}
			writer.write(EmailConstants.CRLF);

			// send the multiline response
			boolean multiline = false;

			if (status.equals("+OK"))
			{
				for (;;)
				{
					line = br.readLine();
					if (line == null) break;

					// write next line
					if (LOG.isDebugEnabled()) LOG.debug("Sending: " + line);
					if (line.startsWith(".")) writer.write(".");
					writer.write(line);
					writer.write(EmailConstants.CRLF);
					multiline = true;
				}

				if (multiline)
				{
					writer.write(".");
					writer.write(EmailConstants.CRLF);
				}
			}

			writer.flush();

			// check for QUIT
			if (testExec.getStateObject(MailListenStep.QUIT_KEY) != null)
			{
				POP3ListenStep.closePOP3Connection(testExec);
			}
		}
		catch (Exception exc)
		{
			LOG.debug("Respond expection", exc);

			// if anything fails, quietly cleanup and leave
			POP3ListenStep.closePOP3Connection(testExec);
		}
	}
}
