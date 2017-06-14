package org.gavaghan.lisa.sdk.email.tph.smtpcommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.gavaghan.lisa.sdk.email.tph.EmailConstants;

/**
 * This is a pseudorequest capturing the client sending the actual email payload
 * following a DATA transaction.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class PAYLOAD extends SpecificCommand
{
	public PAYLOAD(String content)
	{
		super("PAYLOAD", content);

		StringReader reader = new StringReader(content);
		BufferedReader br = new BufferedReader(reader);
		String line;

		try
		{
			// read headers
			for (;;)
			{
				line = br.readLine();
				if (line == null) break;
				if (line.length() == 0) break;

				String name;
				String value;
				int colon = line.indexOf(':');

				if (colon < 0)
				{
					name = line;
					value = "";
				}
				else
				{
					name = line.substring(0, colon).trim();
					value = line.substring(colon + 1).trim();
				}

				getArguments().put(name, value);
			}

			// read body
			StringBuilder body = new StringBuilder();
			for (;;)
			{
				line = br.readLine();
				if (line == null) break;
				body.append(line);
				body.append(EmailConstants.CRLF);
			}

			getArguments().put("Body", body.toString());
		}
		catch (IOException exc)
		{
			// will never happen
		}
	}
}
