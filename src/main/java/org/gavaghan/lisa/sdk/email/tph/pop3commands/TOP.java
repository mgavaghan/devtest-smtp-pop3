package org.gavaghan.lisa.sdk.email.tph.pop3commands;

/**
 * TOP.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class TOP extends SpecificCommand
{
	/**
	 * Create a new UIDL.
	 * @param command
	 * @param args
	 */
	public TOP(String command, String args)
	{
		super(command, args);

		int space = args.indexOf(' ');
		if (space < 0)
		{
			getArguments().put("MessageNumber", args);
			getArguments().put("LineCount", "0");
		}
		else
		{
			getArguments().put("MessageNumber", args.substring(0,space));
			String remaining = args.substring(space+1).trim();
			getArguments().put("LineCount", remaining);
		}
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
