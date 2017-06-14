package org.gavaghan.lisa.sdk.email.tph.pop3commands;

/**
 * RETR.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class RETR extends SpecificCommand
{
	/**
	 * Create a new LIST.
	 * @param command
	 * @param args
	 */
	public RETR(String command, String args)
	{
		super(command, args);
		
			getArguments().put("MessageNumber", args);
	}

	/**
	 * Indicate if this request will have a multiline response.
	 */
	@Override
	public boolean isMultiLineResponse()
	{
		return true;
	}
}
