package org.gavaghan.lisa.sdk.email.tph.smtpcommands.auth;

import org.gavaghan.lisa.sdk.email.tph.SMTPRequest;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.MultistageAUTH;

/**
 * Handle the USER stage of AUTH LOGIN.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class LOGIN_USER_Stage implements MultistageAUTH
{
	/** After USER, expect PASS. */
	static private final MultistageAUTH sNextStage = new LOGIN_PASS_Stage();

	/**
	 * Creates the LOGIN_PASS pseudo request.
	 */
	@Override
	public SMTPRequest createRequest(String message)
	{
		SMTPRequest request = new LOGIN_USER("LOGIN_USER", message.substring(4).trim());
		
		return request;
	}

	/**
	 * Returns that the next stage is LOGIN_PASS_Stage.
	 */
	@Override
	public MultistageAUTH getNextStage()
	{
		return sNextStage;
	}
}
