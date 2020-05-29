package org.gavaghan.lisa.sdk.email.step.smtp.test;

import org.gavaghan.devtest.autostep.AutoStep;
import org.gavaghan.devtest.autostep.Property;
import org.gavaghan.devtest.autostep.TypeName;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itko.lisa.test.TestExec;

/**
 * SMTP DATA Step.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@TypeName("SMTP DATA")
@Property(name = "Headers", multiline = true, rows = 10)
@Property(name = "Body", multiline = true, rows = 20)
public class SMTPDATAStep extends AutoStep
{
   /** Our logger. */
   static private Logger LOG = LoggerFactory.getLogger(SMTPDATAStep.class);

   /**
    * On SMTPException, set the last response to the message of an
    * <code>SMTPException</code>.
    * 
    * @param exc
    */
   @Override
   protected void onException(Exception exc)
   {
      if (exc instanceof SMTPException)
      {
         setLastResponse("Data command failed with response: " + exc.getMessage());
      }
      else
      {
         super.onException(exc);
      }
   }

   /**
    * DATA command implementation.
    * 
    * @param testExec
    * @return
    * @throws Exception
    */
   @Override
   protected Object doNodeLogic(TestExec testExec) throws Exception
   {
      LOG.debug("About to send DATA command");
      String dataResp = SMTPClientStep.doRequestResponse(testExec, "DATA");

      // if response code doesn't start with '3', something went wrong.
      if (!dataResp.startsWith("3")) throw new SMTPException(dataResp);

      // get email content
      String header = testExec.parseInState(getProperty("Headers").toString()).trim();
      String body = testExec.parseInState(getProperty("Body").toString());

      StringBuilder payload = new StringBuilder(header.length() + body.length() + 10);

      // FIXME accommodate https://cr.yp.to/smtp/message.html

      payload.append(header);
      payload.append(EmailConstants.CRLF);
      payload.append(EmailConstants.CRLF);
      payload.append(body);

      // if body does not end with CRLF, add it
      if (!body.endsWith(EmailConstants.CRLF))
      {
         payload.append(EmailConstants.CRLF);
      }

      payload.append('.');

      // send data
      // FIXME handle foreign characters outside of LATIN1
      return SMTPClientStep.doRequestResponse(testExec, payload.toString());
   }
}
