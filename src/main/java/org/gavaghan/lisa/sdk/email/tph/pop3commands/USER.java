package org.gavaghan.lisa.sdk.email.tph.pop3commands;

/**
 * USER.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class USER extends SpecificCommand
{
	public USER(String command, String args)
	{
		super(command, args);

		getArguments().put("Username", args);
	}
}