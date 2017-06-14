package org.gavaghan.lisa.sdk.email.step;

import java.io.PrintWriter;

import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.util.XMLUtils;

/**
 * Base implementation of a mail server client step.
 * 
 * @author mike@gavaghan.org
 */
public abstract class MailClientStep extends BaseStep
{
	/** The command to send. */
	private String mCommand;

	/**
	 * Initialize from a test file.
	 */
	@Override
	public void initialize(TestCase testCase, Element elem) throws TestDefException
	{
		setCommand(XMLUtils.findChildGetItsText(elem, "command"));
	}

	/**
	 * Save to test file.
	 */
	@Override
	public void writeSubXML(PrintWriter pw)
	{
		XMLUtils.streamTagAndChild(pw, "command", getCommand());
	}

	/**
	 * Get the command.
	 * 
	 * @return the mail server command
	 */
	public String getCommand()
	{
		return mCommand;
	}

	/**
	 * Set the command.
	 * 
	 * @param value
	 */
	public void setCommand(String value)
	{
		mCommand = value;
	}
}
