package org.gavaghan.lisa.sdk.email.step.smtp;

import org.gavaghan.devtest.autostep.AutoController;
import org.gavaghan.devtest.autostep.EditorName;
import org.gavaghan.devtest.autostep.HelpString;

/**
 * SMTP AUTH PLAIN Controller.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@EditorName("SMTP AUTH PLAIN Editor")
@HelpString("SMTP AUTH PLAIN Editor")
public class SMTPAuthPLAINController extends AutoController<SMTPAuthPLAINStep>
{
   public SMTPAuthPLAINController()
   {
      super(SMTPAuthPLAINStep.class);
   }
}
