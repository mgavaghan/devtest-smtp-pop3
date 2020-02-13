package org.gavaghan.lisa.sdk.email.step.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.step.MailListenStep;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;
import org.gavaghan.lisa.sdk.email.tph.SMTPRequest;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.AUTH;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.DATA;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.GREET;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.MultistageAUTH;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.PAYLOAD;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.QUIT;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.model.Request;

/**
 * Step to listen on an SMTP port
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class SMTPListenStep extends MailListenStep
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(SMTPListenStep.class);

	/** Key to our multistage AUTH in TestExec. */
	static private final String AUTH_KEY = MailListenStep.class.getName() + ".AUTH";

	/** Key to current in TestExec. */
	static public final String COMMAND_KEY = "smtp.command";

	@Override
	public String getTypeName() throws Exception
	{
		return "SMTP Listen";
	}

	/**
	 * Build node from XML test case.
	 */
	@Override
	public void initialize(TestCase testCase, Element elem)
	{
		super.initialize(testCase, elem);
		setVSResourceName("SMTP Port: " + getListenPort(), null);
	}

	/**
	 * Find the open socket and close it.
	 */
	@Override
	public void destroy(TestExec testExec)
	{
		closeSMTPConnection(testExec);
		super.destroy(testExec);
	}
	/**
	 * 
	 * @return
	 */
	@Override
	protected String getReadTimeoutProperty()
	{
		return "smtp.read.timeout";
	}

	/**
	 * Setup a newly accepted client socket.
	 * 
	 * @param socket
	 * @param testExec
	 * @return reader wrapping the socket
	 * @throws SocketException
	 * @throws IOException
	 */
	@Override
	protected BufferedReader initializeSocket(Socket socket, TestExec testExec) throws SocketException, IOException
	{
		testExec.setStateObject(STATE_KEY, ConnectionState.OPEN);

		return super.initializeSocket(socket, testExec);		
	}

	/**
	 * 
	 * @param reader
	 * @param priorState
	 * @return
	 * @throws IOException
	 *            for failures including timeout
	 */
	private SMTPRequest getRequest(BufferedReader reader, ConnectionState priorState, TestExec testExec) throws IOException
	{
		SMTPRequest request = null;
		MultistageAUTH auth = (MultistageAUTH) testExec.getStateValue(AUTH_KEY);

		// if we just connected, make the request a GREET
		if (priorState == null)
		{
			request = new GREET();
		}
		// else, if we're in Multistage AUTH
		else if (auth != null)
		{
			do
			{
				String line = reader.readLine();
				if (line == null) throw new IOException("Out of data");

				request = auth.createRequest(line);
			} while (request == null);

			// get next AUTH stage
			auth = auth.getNextStage();

			// save next AUTH stage
			testExec.setStateValue(AUTH_KEY, auth);
		}
		// if we're in data mode
		else if (priorState == ConnectionState.DATA)
		{
			StringBuilder builder = new StringBuilder();

			for (;;)
			{
				String line = reader.readLine();
				if (line == null) throw new IOException("Out of data");

				if (line.equals("."))
				{
					request = new PAYLOAD(builder.toString());
					testExec.setStateObject(STATE_KEY, ConnectionState.OPEN);
					break;
				}

				builder.append(line);
				builder.append(EmailConstants.CRLF);
			}
		}
		// else, it's a one liner
		else
		{
			String line = reader.readLine();
			if (line == null) throw new IOException("Out of data");
			request = SMTPRequest.parse(line);

			// check if we're starting DATA mode
			if (request instanceof DATA)
			{
				testExec.setStateObject(STATE_KEY, ConnectionState.DATA);
			}

			// check if we're starting AUTH mode
			if (request instanceof AUTH)
			{
				AUTH authCmd = (AUTH) request;

				// if next stage is NULL, AUTH was completed in one step
				testExec.setStateValue(AUTH_KEY, authCmd.getNextStage());
			}

			// check if this was the QUIT command
			testExec.setStateObject(QUIT_KEY, (request instanceof QUIT) ? Boolean.TRUE : null);
		}

		testExec.setStateObject(COMMAND_KEY, request.getCommand());
		return request;
	}

	/**
	 * 
	 * @param testExec
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
   @Override
	protected Object doNodeLogic(TestExec testExec) throws IOException
	{
		LOG.debug("ENTER: doNodeLogic");

		SMTPRequest requestMessage;

		// get the socket
		ConnectionState priorState;
		BufferedReader reader;

		try
		{
			priorState = (ConnectionState) testExec.getStateObject(STATE_KEY);
			reader = getReader(testExec);
			requestMessage = getRequest(reader, priorState, testExec);
		}
		// if the connection was closed, listen for another
		catch (IOException exc)
		{
			LOG.debug("Prior connection closed, so we'll listen for another.");
			closeSMTPConnection(testExec);

			priorState = (ConnectionState) testExec.getStateObject(STATE_KEY);
			reader = getReader(testExec);
			requestMessage = getRequest(reader, priorState, testExec);
		}

		if (LOG.isDebugEnabled()) LOG.debug("Message: " + requestMessage.toString());

		// create the request
		Request request = new Request();
		request.setOperation(requestMessage.getCommand());
		request.setBody(requestMessage.getArgumentString());
		request.getArguments().addAll(requestMessage.getArguments());
		request.getMetaData().put("lisa.vse.session.key", (String) testExec.getStateObject(SESSION_KEY));
		testExec.setStateObject(DEFAULT_REQUEST_PROPERTY, request);

		LOG.debug("EXIT: doNodeLogic");
		return requestMessage.toString();
	}

	/**
	 * Close everything related to this SMTP connection.
	 * 
	 * @param testExec
	 */
	static public void closeSMTPConnection(TestExec testExec)
	{
		LOG.info("Closing SMTP connection.");

		LOG.debug("Removing SMTP state.");
		testExec.removeState(AUTH_KEY);

		MailListenStep.closeMailConnection(testExec);
	}
}
