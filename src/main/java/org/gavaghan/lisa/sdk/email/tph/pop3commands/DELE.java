package org.gavaghan.lisa.sdk.email.tph.pop3commands;

/**
 * DELE.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class DELE extends SpecificCommand
{
	/**
	 * Create a new LIST.
	 * @param command
	 * @param args
	 */
	public DELE(String command, String args)
	{
		super(command, args);
		
			getArguments().put("MessageNumber", args);
	}
}
