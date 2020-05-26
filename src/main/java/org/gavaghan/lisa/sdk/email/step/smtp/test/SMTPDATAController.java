package org.gavaghan.lisa.sdk.email.step.smtp.test;

import javax.swing.Icon;

import org.gavaghan.devtest.autostep.AutoController;
import org.gavaghan.devtest.autostep.EditorName;
import org.gavaghan.devtest.autostep.HelpString;
import org.gavaghan.lisa.sdk.email.step.MailIcons;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@EditorName("SMTP DATA Editor")
@HelpString("SMTP DATA Editor")
public class SMTPDATAController extends AutoController<SMTPDATAStep>
{
   public SMTPDATAController()
   {
      super(SMTPDATAStep.class);
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
