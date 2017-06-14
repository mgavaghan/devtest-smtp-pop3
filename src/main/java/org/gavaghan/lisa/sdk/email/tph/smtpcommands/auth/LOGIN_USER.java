package org.gavaghan.lisa.sdk.email.tph.smtpcommands.auth;

import org.gavaghan.lisa.sdk.email.tph.EmailConstants;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.SpecificCommand;

import com.itko.util.Base64;

/**
 * LOGIN_USER pseudorequest as a stage of AUTH LOGIN.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class LOGIN_USER extends SpecificCommand
{
	public LOGIN_USER(String command, String args)
	{
		super(command, args);

		String decoded = new String(Base64.decode(args), EmailConstants.MAIL_ENCODING);

		getArguments().put("Username", decoded);
	}
}
