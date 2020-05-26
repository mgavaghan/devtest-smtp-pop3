package org.gavaghan.lisa.sdk.email.step.smtp.test;

import javax.swing.Icon;

import org.gavaghan.devtest.autostep.AutoController;
import org.gavaghan.devtest.autostep.EditorName;
import org.gavaghan.devtest.autostep.HelpString;
import org.gavaghan.lisa.sdk.email.step.MailIcons;

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

   @Override
   public Icon getLargeIcon()
   {
      return MailIcons.getLargeIcon();
   }

   @Override
   public Icon getSmallIcon()
   {
      return MailIcons.getSmallIcon();
   }
}
