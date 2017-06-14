package org.gavaghan.lisa.sdk.email.tph.smtpcommands;

import org.gavaghan.lisa.sdk.email.tph.SMTPRequest;

/**
 * Encapsulates a stage in multistage authentication.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public interface MultistageAUTH
{
	/**
	 * Create a pseudorequest based on the next line from the client. Returns
	 * null if message is incomplete.
	 * 
	 * @param message
	 * @return a new request, or 'null' if more data needed
	 */
	public SMTPRequest createRequest(String message);

	/**
	 * If a request was successfully created, this method returns the next stage.
	 * 
	 * @return next AUTH stage
	 */
	public MultistageAUTH getNextStage();
}
