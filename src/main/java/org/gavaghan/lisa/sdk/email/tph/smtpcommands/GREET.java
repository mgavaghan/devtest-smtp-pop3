package org.gavaghan.lisa.sdk.email.tph.smtpcommands;

/**
 * This is a pseudorequest. It represents the client making an initial
 * connection and waiting for a greeting message.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class GREET extends SpecificCommand
{
	public GREET()
	{
		super("GREET", "");
	}
}
