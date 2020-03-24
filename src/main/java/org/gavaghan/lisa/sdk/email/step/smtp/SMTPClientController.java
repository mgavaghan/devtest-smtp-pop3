package org.gavaghan.lisa.sdk.email.step.smtp;

import org.gavaghan.devtest.autostep.AutoController;
import org.gavaghan.devtest.autostep.EditorName;
import org.gavaghan.devtest.autostep.HelpString;

/**
 * SMTP Client Command controller
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@EditorName("SMTP Client Command")
@HelpString("SMTP Client Command")
public class SMTPClientController extends AutoController<SMTPClientStep>
{
   public SMTPClientController()
   {
      super(SMTPClientStep.class);
   }
}
