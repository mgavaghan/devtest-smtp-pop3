package org.gavaghan.lisa.sdk.email.tph.pop3commands;

/**
 * UIDL.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class UIDL extends SpecificCommand
{
	private boolean mMultiline;
	
	/**
	 * Create a new UIDL.
	 * @param command
	 * @param args
	 */
	public UIDL(String command, String args)
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
