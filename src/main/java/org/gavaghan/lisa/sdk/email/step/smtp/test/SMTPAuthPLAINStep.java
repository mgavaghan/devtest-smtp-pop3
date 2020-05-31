package org.gavaghan.lisa.sdk.email.step.smtp.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

import org.gavaghan.devtest.autostep.AutoStep;
import org.gavaghan.devtest.autostep.Property;
import org.gavaghan.devtest.autostep.TypeName;

import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;

/**
 * SMTP AUTH PLAIN step.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@TypeName("AUTH PLAIN")
@Property(name = "Username", mandatory = false)
@Property(name = "Password", mandatory = false, sensitive = true)
public class SMTPAuthPLAINStep extends AutoStep
{
   /** Username character set. */
   static public final Charset USERNAME_ENC = Charset.forName("ISO-8859-1");

	/** Password character set. */
	static public final Charset PASSWORD_ENC = Charset.forName("UTF-8");

	/**
	 * Build the command "AUTH PLAIN <user/pass>.
	 * 
	 * @param testExec
	 * @return
	 * @throws TestRunException
	 */
	protected String getCommand(TestExec testExec) throws TestRunException
	{
		byte[] bytes;

		StringBuilder builder = new StringBuilder();
		builder.append("AUTH PLAIN ");

		// Base64 encode user/pass
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			baos.write(0);
			baos.write(getParsedProperty(testExec, "Username").getBytes(USERNAME_ENC));
			baos.write(0);
			baos.write(getParsedProperty(testExec, "Password").getBytes(PASSWORD_ENC));

			bytes = baos.toByteArray();
		}
		catch (IOException exc)
		{
			throw new RuntimeException("Seriously?  ByteArrayOutputStream failed?", exc);
		}

		// append base64 to command
		builder.append(Base64.getEncoder().encodeToString(bytes));

		return builder.toString();
	}

   @Override
   protected Object doNodeLogic(TestExec testExec) throws Exception
   {
      return SMTPClientStep.doRequestResponse(testExec, getCommand(testExec));
   }
}
