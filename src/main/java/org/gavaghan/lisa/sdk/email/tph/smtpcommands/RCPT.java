package org.gavaghan.lisa.sdk.email.tph.smtpcommands;

/**
 * RCPT.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class RCPT extends SpecificCommand
{
	public RCPT(String command, String args)
	{
		super(command, args);

		// find out who the fail is from
		int lt = args.indexOf("<");
		int gt = args.indexOf(">");

		if (lt < 0)
		{
			getArguments().put("To", args.trim());
		}
		else if (gt < 0)
		{
			getArguments().put("To", args.substring(lt + 1).trim());
		}
		else
		{
			getArguments().put("To", args.substring(lt + 1, gt).trim());
		}
	}
}
