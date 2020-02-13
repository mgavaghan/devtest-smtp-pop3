package org.gavaghan.lisa.sdk.email.step.smtp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.step.MailClientStep;

import com.itko.lisa.test.TestExec;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPDATAStep extends MailClientStep
{
   /** Our logger. */
   static private Log LOG = LogFactory.getLog(SMTPClientStep.class);

   @Override
   public String getTypeName() throws Exception
   {
      return "SMTP DATA";
   }

   @Override
   protected Object doNodeLogic(TestExec testExec) throws Exception
   {
      return null;
   }
}
