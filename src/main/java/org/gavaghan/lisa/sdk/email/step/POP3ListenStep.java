package org.gavaghan.lisa.sdk.email.step;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.tph.POP3Request;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.GREET;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.QUIT;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.model.Request;

/**
 * Step to listen on a POP3 port
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class POP3ListenStep extends MailListenStep
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(POP3ListenStep.class);

	/** Key to current in TestExec. */
	static public final String COMMAND_KEY = "pop3.command";

	@Override
	public String getTypeName() throws Exception
	{
		return "POP3 Listen";
	}

	/**
	 * Build node from XML test case.
	 */
	@Override
	public void initialize(TestCase testCase, Element elem)
	{
		super.initialize(testCase, elem);
		setVSResourceName("POP3 Port: " + getListenPort(), null);
	}

	/**
	 * Find the open socket and close it.
	 */
	@Override
	public void destroy(TestExec testExec)
	{
		closePOP3Connection(testExec);
		super.destroy(testExec);
	}
	/**
	 * 
	 * @return
	 */
	@Override
	protected String getReadTimeoutProperty()
	{
		return "pop3.read.timeout";
	}

	/**
	 * 
	 * @param reader
	 * @param priorState
	 * @return
	 * @throws IOException
	 *            for failures including timeout
	 */
	private POP3Request getRequest(BufferedReader reader, ConnectionState priorState, TestExec testExec) throws IOException
	{
		POP3Request request = null;

		// if we just connected, make the request a GREET
		if (priorState == null)
		{
			request = new GREET();
			testExec.setStateObject(STATE_KEY, ConnectionState.OPEN);
		}
		// else, it's a one liner
		else
		{
			String line = reader.readLine();
			if (line == null) throw new IOException("Out of data");
			request = POP3Request.parse(line);

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
	@Override
	protected Object doNodeLogic(TestExec testExec) throws IOException
	{
		LOG.debug("ENTER: doNodeLogic");

		POP3Request requestMessage;

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
			closePOP3Connection(testExec);

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
	 * Close everything related to this POP3 connection.
	 * 
	 * @param testExec
	 */
	static public void closePOP3Connection(TestExec testExec)
	{
		MailListenStep.closeMailConnection(testExec);
	}
}
