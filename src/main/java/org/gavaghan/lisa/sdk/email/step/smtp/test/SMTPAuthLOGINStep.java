package org.gavaghan.lisa.sdk.email.step.smtp.test;

import java.nio.charset.Charset;
import java.util.Base64;

import org.gavaghan.devtest.autostep.AutoStep;
import org.gavaghan.devtest.autostep.Property;
import org.gavaghan.devtest.autostep.TypeName;

import com.itko.lisa.test.TestExec;

/**
 * SMTP AUTH LOGIN step.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@TypeName("AUTH LOGIN")
@Property(name = "Username", mandatory = false)
@Property(name = "Password", mandatory = false, sensitive = true)
public class SMTPAuthLOGINStep extends AutoStep
{
   /** Username character set. */
   static public final Charset USERNAME_ENC = Charset.forName("UTF-8");

	/** Password character set. */
	static public final Charset PASSWORD_ENC = Charset.forName("UTF-8");

   @Override
   protected Object doNodeLogic(TestExec testExec) throws Exception
   {
      String userAndDomain = getParsedProperty(testExec, "Username");
      String password = getParsedProperty(testExec, "Password");
      
      // Announce AUTH LOGIN
      String response = SMTPClientStep.doRequestResponse(testExec, "AUTH LOGIN");
      if (!response.startsWith("3"))  throw new RuntimeException("AUTH LOGIN failed to prompt for username: " + response);
      
      // Send userAndDomain
      String userAndDomainBase64 = Base64.getEncoder().encodeToString(userAndDomain.getBytes(USERNAME_ENC));
      response = SMTPClientStep.doRequestResponse(testExec, userAndDomainBase64);
      if (!response.startsWith("3"))  throw new RuntimeException("AUTH LOGIN failed to accept username: " + response);
      
      // Send password
      String passwordBase64 = Base64.getEncoder().encodeToString(password.getBytes(USERNAME_ENC));
      return SMTPClientStep.doRequestResponse(testExec, passwordBase64);
   }
}
