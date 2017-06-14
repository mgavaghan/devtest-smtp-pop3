package org.gavaghan.lisa.sdk.email.tph.smtpcommands;

import org.gavaghan.lisa.sdk.email.tph.SMTPRequest;

/**
 * Subtype of a specific command. This ensures the argument list is cleared.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class SpecificCommand extends SMTPRequest
{
	public SpecificCommand(String command, String args)
	{
		super(command, args);

		getArguments().clear();
	}
}