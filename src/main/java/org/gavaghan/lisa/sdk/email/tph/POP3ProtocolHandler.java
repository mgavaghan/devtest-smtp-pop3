package org.gavaghan.lisa.sdk.email.tph;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.step.pop3.POP3ListenStep;
import org.gavaghan.lisa.sdk.email.step.pop3.POP3RespondStep;

import com.itko.lisa.gui.WizardStep;
import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.vse.stateful.ConversationalStep;
import com.itko.lisa.vse.stateful.model.Request;
import com.itko.lisa.vse.stateful.model.Response;
import com.itko.lisa.vse.stateful.model.Transaction;
import com.itko.lisa.vse.stateful.recorder.RecordingSession;
import com.itko.lisa.vse.stateful.recorder.RecordingWizard;
import com.itko.lisa.vse.stateful.recorder.WizardPhase;

/**
 * POP3 Protocol Handler.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class POP3ProtocolHandler extends MailProtocolHandler implements POP3TransactionRecorder
{
	/** Logger. */
	static private final Log LOG = LogFactory.getLog(POP3ProtocolHandler.class);

	/** Name of status code value in metadata. */
	static public final String STATUS_NAME = "POP3 Status";

	/**
	 * Create a POP3Recorder
	 */
	@Override
	protected MailRecorder createMailRecorder(MailServer server, String targetHost, int targetPort, boolean sslToServer)
	{
		return new POP3Recorder(this, server, targetHost, targetPort, sslToServer);
	}

	/**
	 * Create response to use with unknown conversational response.
	 */
	@Override
	protected Response createUnknownConversationalResponse()
	{
		Response response = new Response();
		response.setBody("Unknown command: {{" + POP3ListenStep.COMMAND_KEY + "}}\r\n");
		response.getMetaData().put(STATUS_NAME, "-ERR");
		return response;
	}

	/**
	 * Create response to use with unknown stateless response.
	 */
	@Override
	protected Response createUnknownStatelessResponse()
	{
		return createUnknownConversationalResponse();
	}

	/**
	 * Create the listener step.
	 */
	@Override
	public TestNode createListenStep(TestCase testCase)
	{
		POP3ListenStep step = (POP3ListenStep) RecordingSession.createStep(testCase, "POP3 Listen", POP3ListenStep.class);
		step.setListenPort(getListenPort());
		return step;
	}

	/**
	 * Cretate the respond step.
	 */
	@Override
	public TestNode createRespondStep(TestCase testCase)
	{
		POP3RespondStep step = (POP3RespondStep) RecordingSession.createStep(testCase, "POP3 Respond", POP3RespondStep.class);
		step.setQuiet(false);
		return step;
	}

	/**
	 * Configure the listener.
	 */
	@Override
	public WizardStep[] getWizardSteps(RecordingWizard recording, WizardPhase phase)
	{
		switch (phase)
		{
		case CONFIGURATION:
			return new WizardStep[] { new MailConnectionPanel(this, "Target POP3 Server") };

		case FINALIZE:
			break;

		case SESSION_ID:
			break;

		default:
			return null;
		}

		return null;
	}

	/**
	 * Record a request/response pair.
	 */
	@Override
	public synchronized void record(String session, POP3Request requestMessage, POP3Response responseMessage, String rawRequest, int thinkTime)
	{
		if (LOG.isInfoEnabled()) LOG.info("Recording a transaction: " + rawRequest);

		// create request
		Request request = new Request();
		request.setOperation(requestMessage.getCommand());
		request.setBody(requestMessage.getArgumentString());
		request.getArguments().addAll(requestMessage.getArguments());
		request.getMetaData().put(ConversationalStep.SESSION_KEY, session);
		request.getAttributes().put("raw_request", rawRequest);

		// create response
		Response response = new Response();
		response.setBody(responseMessage.getContent());
		response.getMetaData().put(STATUS_NAME, responseMessage.getSuccess() ? "+OK" : "-ERR");
		response.setThinkTimeSpec("" + thinkTime);

		// create transaction
		Transaction txn = Transaction.promote(request, response);

		// add transaction
		addTransaction(txn);
	}
}
