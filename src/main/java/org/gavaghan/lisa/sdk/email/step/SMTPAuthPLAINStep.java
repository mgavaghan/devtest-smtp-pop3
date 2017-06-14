package org.gavaghan.lisa.sdk.email.step;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Base64;

import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;
import com.itko.util.XMLUtils;

/**
 * SMTP AUTH PLAIN step.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPAuthPLAINStep extends SMTPClientStep
{
	/** Username character set. */
	static public final Charset USERNAME_ENC = Charset.forName("ISO-8859-1");

	/** Password character set. */
	static public final Charset PASSWORD_ENC = Charset.forName("UTF-8");

	/** Username to login as. */
	private String mUsername;

	/** The password. */
	private String mPassword;

	public void setUsername(String username)
	{
		mUsername = username;
	}

	public String getUsername()
	{
		return mUsername;
	}

	public void setPassword(String password)
	{
		mPassword = password;
	}

	public String getPassword()
	{
		return mPassword;
	}

	/**
	 * Initialize from a test file.
	 */
	@Override
	public void initialize(TestCase testCase, Element elem) throws TestDefException
	{
		setUsername(XMLUtils.findChildGetItsText(elem, "username"));
		setPassword(XMLUtils.findChildGetItsText(elem, "password"));
	}

	/**
	 * Save to test file.
	 */
	@Override
	public void writeSubXML(PrintWriter pw)
	{
		XMLUtils.streamTagAndChild(pw, "username", getUsername());
		XMLUtils.streamTagAndChild(pw, "password", getPassword());
	}

	/**
	 * Get the type name.
	 */
	@Override
	public String getTypeName() throws Exception
	{
		return "SMTP AUTH PLAIN";
	}

	@Override
	protected String getCommand(TestExec testExec) throws TestRunException
	{
		byte[] bytes;

		StringBuilder builder = new StringBuilder();
		builder.append("AUTH PLAIN ");

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			baos.write(0);
			baos.write(testExec.parseInState(mUsername).getBytes(USERNAME_ENC));
			baos.write(0);
			baos.write(testExec.parseInState(mPassword).getBytes(PASSWORD_ENC));

			bytes = baos.toByteArray();
		}
		catch (IOException exc)
		{
			throw new RuntimeException("Seriously?  ByteArrayOutputStream failed?", exc);
		}

		builder.append(Base64.getEncoder().encodeToString(bytes));

		return builder.toString();
	}
}
