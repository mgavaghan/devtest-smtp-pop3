package org.gavaghan.lisa.sdk.email.step;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.BaseRespondStep;
import com.itko.util.XMLUtils;

/**
 * Base implementation of mail server respond steps.
 * 
 * @author mike@gavaghan.org
 */
public abstract class MailRespondStep extends BaseRespondStep
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(MailRespondStep.class);

	/** Key to our open writer in TestExec. */
	static final String WRITER_KEY = MailRespondStep.class.getName() + ".WRITER";

	/**
	 * Build node from XML test case.
	 */
	@Override
	public void initialize(TestCase arg0, Element arg1) throws TestDefException
	{
		// no-op
	}

	/**
	 * Write to XML test case.
	 * 
	 * @param pw
	 */
	@Override
	public void writeSubXML(PrintWriter pw)
	{
		XMLUtils.streamTagAndChild(pw, "nop", "nop");
	}

	@Override
	protected String getResponsePropertyKey()
	{
		return DEFAULT_RESPONSE_PROPERTY;
	}

	/**
	 * Get or create client writer.
	 * 
	 * @param testExec
	 * @return
	 * @throws IOException
	 */
	protected Writer getWriter(TestExec testExec) throws IOException
	{
		LOG.debug("Getting writer.");
		Writer writer = (Writer) testExec.getStateObject(WRITER_KEY);

		if (writer == null)
		{
			LOG.debug("Creating new writer.");
			Socket socket = (Socket) testExec.getStateObject(MailListenStep.SOCKET_KEY);
			OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), EmailConstants.MAIL_ENCODING);
			writer = new BufferedWriter(osw);
			testExec.setStateValue(WRITER_KEY, writer);
		}

		return writer;
	}

}
