package org.gavaghan.lisa.sdk.email.tph;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public interface POP3TransactionRecorder
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
	public void record(String session, POP3Request request, POP3Response response, String rawRequest, int thinkTime);
}
