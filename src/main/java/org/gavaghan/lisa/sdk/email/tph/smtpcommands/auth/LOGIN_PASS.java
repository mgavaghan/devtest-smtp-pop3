package org.gavaghan.lisa.sdk.email.tph.smtpcommands.auth;

import org.gavaghan.lisa.sdk.email.tph.EmailConstants;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.SpecificCommand;

import com.itko.util.Base64;

/**
 * LOGIN_PASS pseudorequest as a stage of AUTH LOGIN.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class LOGIN_PASS extends SpecificCommand
{
	public LOGIN_PASS(String command, String args)
	{
		super(command, args);

		String decoded = new String(Base64.decode(args), EmailConstants.MAIL_ENCODING);

		getArguments().put("Password", decoded);
	}
}
