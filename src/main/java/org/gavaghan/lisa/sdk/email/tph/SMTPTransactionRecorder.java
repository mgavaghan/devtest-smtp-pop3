package org.gavaghan.lisa.sdk.email.tph;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public interface SMTPTransactionRecorder
{
	/**
	 * Record a captured request/response pair.
	 * 
	 * @param session
	 * @param request
	 * @param response
	 * @param rawRequest
	 *           raw recorded data
	 * @param thinkTime
	 */
	public void record(String session, SMTPRequest request, SMTPResponse response, String rawRequest, int thinkTime);
}
