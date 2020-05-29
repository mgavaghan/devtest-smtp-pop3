package org.gavaghan.lisa.sdk.email.tph;

/**
 * API for recording POP3 request/response pairs.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public interface POP3TransactionRecorder
{
   /**
    * Record a captured request/response pair.
    * 
    * @param session stateful conversation token
    * @param request request object
    * @param response response object
    * @param rawRequest raw recorded data
    * @param thinkTime measured think time
    */
	public void record(String session, POP3Request request, POP3Response response, String rawRequest, int thinkTime);
}
