package org.gavaghan.lisa.sdk.email.tph.smtpcommands;


/**
 * MAIL.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class MAIL extends SpecificCommand
{
	public MAIL(String command, String args)
	{
		super(command, args);
		
		// find out who the mail is from
		int lt = args.indexOf("<");
		int gt = args.indexOf(">");

		if (lt < 0)
		{
			getArguments().put("From", args.trim());
		}
		else if (gt < 0)
		{
			getArguments().put("From", args.substring(lt + 1).trim());
		}
		else
		{
			getArguments().put("From", args.substring(lt + 1, gt).trim());

			// grab SIZE (or any other parameter)
			int sp = args.indexOf(' ', gt + 1);

			if (sp > 0)
			{
				String remaining = args.substring(sp + 1).trim();
				int eq = remaining.indexOf('=');

				if (eq < 0)
				{
					getArguments().put(remaining, "");
				}
				else
				{
					getArguments().put(remaining.substring(0, eq).trim(), remaining.substring(eq + 1).trim());
				}
			}
		}
	}
}
