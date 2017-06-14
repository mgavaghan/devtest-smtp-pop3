package org.gavaghan.lisa.sdk.email.tph.pop3commands;

import org.gavaghan.lisa.sdk.email.tph.POP3Request;

/**
 * Subtype of a specific command. This ensures the argument list is cleared.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class SpecificCommand extends POP3Request
{
	public SpecificCommand(String command, String args)
	{
		super(command, args);

		getArguments().clear();
	}
}