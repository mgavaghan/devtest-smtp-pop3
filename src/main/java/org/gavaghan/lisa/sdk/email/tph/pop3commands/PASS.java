package org.gavaghan.lisa.sdk.email.tph.pop3commands;

/**
 * PASS.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class PASS extends SpecificCommand
{
	public PASS(String command, String args)
	{
		super(command, args);

		getArguments().put("Password", args);
	}
}