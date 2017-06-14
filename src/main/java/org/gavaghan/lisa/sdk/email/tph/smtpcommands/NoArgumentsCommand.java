package org.gavaghan.lisa.sdk.email.tph.smtpcommands;

/**
 * Generic specific command that takes no arguments.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class NoArgumentsCommand extends SpecificCommand
{
	public NoArgumentsCommand(String command, String args)
	{
		super(command, args);
	}
}
