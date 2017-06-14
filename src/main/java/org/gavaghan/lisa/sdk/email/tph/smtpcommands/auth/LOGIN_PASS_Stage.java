package org.gavaghan.lisa.sdk.email.tph.smtpcommands.auth;

import org.gavaghan.lisa.sdk.email.tph.SMTPRequest;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.MultistageAUTH;

/**
 * Handle the PASS stage of AUTH LOGIN.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class LOGIN_PASS_Stage implements MultistageAUTH
{
	/**
	 * Creates the LOGIN_PASS pseudo request.
	 */
	@Override
	public SMTPRequest createRequest(String message)
	{
		SMTPRequest request = new LOGIN_PASS("LOGIN_PASS", message.substring(4).trim());
		
		return request;
	}

	/**
	 * There are no more stages. We're done.
	 */
	@Override
	public MultistageAUTH getNextStage()
	{
		return null;
	}
}
