package org.gavaghan.lisa.sdk.email.tph.smtpcommands;

/**
 * HELO and EHLO.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class HELO extends SpecificCommand
{
	@SuppressWarnings("deprecation")
	public HELO(String command, String args)
	{
		super(command, args);

		getArguments().put("Domain", args);
	}
}
