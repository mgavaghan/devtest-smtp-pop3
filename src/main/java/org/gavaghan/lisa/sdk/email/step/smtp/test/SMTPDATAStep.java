package org.gavaghan.lisa.sdk.email.step.smtp.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gavaghan.devtest.autostep.AutoStep;
import org.gavaghan.devtest.autostep.Property;
import org.gavaghan.devtest.autostep.TypeName;

import com.itko.lisa.test.TestExec;

/**
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

   @Override
   protected Object doNodeLogic(TestExec testExec) throws Exception
   {
      // TODO Auto-generated method stub
      return null;
   }   
}
