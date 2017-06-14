package org.gavaghan.lisa.sdk.email.step;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;

import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestEvent;
import com.itko.lisa.test.TestExec;
import com.itko.util.XMLUtils;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class MailClientIteratorStep extends BaseStep
{
	/** Identifies the test command reader. */
	static private final String READER_KEY = "MailClientIteratorStep.READER";

	/** The commands to send. */
	private String mCommands;

	/**
	 * Initialize from a test file.
	 */
	@Override
	public void initialize(TestCase testCase, Element elem) throws TestDefException
	{
		setCommands(XMLUtils.findChildGetItsText(elem, "command"));
	}

	/**
	 * Save to test file.
	 */
	@Override
	public void writeSubXML(PrintWriter pw)
	{
		XMLUtils.streamTagAndChild(pw, "command", getCommands());
	}

	/**
	 * Get the commands.
	 * 
	 * @return the commands
	 */
	public String getCommands()
	{
		return mCommands;
	}

	/**
	 * Set the commands.
	 * 
	 * @param value
	 */
	public void setCommands(String value)
	{
		mCommands = value;
	}

	/**
	 * Get the type name.
	 */
	@Override
	public String getTypeName() throws Exception
	{
		return "Mail Client Iterator";
	}

	/**
	 * Step through test command lines.
	 */
	@Override
	protected Object doNodeLogic(TestExec testExec) throws Exception
	{
		// get or create the reader
		BufferedReader reader = (BufferedReader) testExec.getStateObject(READER_KEY);
		if (reader == null)
		{
			String testCommands = testExec.parseInState(getCommands());
			reader = new BufferedReader(new StringReader(testCommands));
			testExec.setStateObject(READER_KEY, reader);
		}

		// get next line
		String line = reader.readLine();

		// check if out of data
		if ((line == null) || (line.trim().length() == 0))
		{
			testExec.log("Out of test commands");
			testExec.removeState(READER_KEY);
			return null;
		}

		// parse the line
		String[] args = line.split(",");
		if (args.length < 3)
		{
			testExec.raiseEvent(TestEvent.EVENT_ABORT, "Not enough fields", line);
			return null;
		}

		testExec.setStateObject("mailclient.command", args[0].trim());
		testExec.setStateObject("mailclient.status", args[1].trim());
		testExec.setStateObject("mailclient.response", args[2].trim());

		return line;
	}
}
