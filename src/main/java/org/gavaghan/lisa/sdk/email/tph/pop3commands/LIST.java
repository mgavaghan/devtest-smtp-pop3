package org.gavaghan.lisa.sdk.email.tph.pop3commands;

/**
 * LIST.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class LIST extends SpecificCommand
{
	private boolean mMultiline;
	
	/**
	 * Create a new LIST.
	 * @param command
	 * @param args
	 */
	public LIST(String command, String args)
	{
		super(command, args);
		
		if (args.length() == 0)
		{
			mMultiline = true;
		}
		else
		{
			mMultiline = false;
			
			getArguments().put("MessageNumber", args);
		}
	}

	/**
	 * Indicate if this request will have a multiline response.
	 */
	@Override
	public boolean isMultiLineResponse()
	{
		return mMultiline;
	}
}
