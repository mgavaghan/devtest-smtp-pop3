package org.gavaghan.lisa.sdk.email.step.smtp.test;

import javax.swing.Icon;

import org.gavaghan.devtest.autostep.AutoController;
import org.gavaghan.devtest.autostep.EditorName;
import org.gavaghan.devtest.autostep.HelpString;
import org.gavaghan.lisa.sdk.email.step.MailIcons;

/**
 * SMTP AUTH PLAIN Controller.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@EditorName("SMTP AUTH LOGIN Editor")
@HelpString("SMTP AUTH LOGIN Editor")
public class SMTPAuthLOGINController extends AutoController<SMTPAuthLOGINStep>
{
   public SMTPAuthLOGINController()
   {
      super(SMTPAuthLOGINStep.class);
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
