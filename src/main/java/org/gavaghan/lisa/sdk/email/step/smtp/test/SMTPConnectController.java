package org.gavaghan.lisa.sdk.email.step.smtp.test;

import org.gavaghan.devtest.autostep.AutoController;
import org.gavaghan.devtest.autostep.EditorName;
import org.gavaghan.devtest.autostep.HelpString;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@EditorName("SMTP Connect Editor")
@HelpString("SMTP Connect Editor")
public class SMTPConnectController extends AutoController<SMTPConnectStep>
{
   public SMTPConnectController()
   {
      super(SMTPConnectStep.class);
   }
}
