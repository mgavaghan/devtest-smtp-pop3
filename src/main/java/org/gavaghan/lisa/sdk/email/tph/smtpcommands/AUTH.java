package org.gavaghan.lisa.sdk.email.tph.smtpcommands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.auth.LOGIN_USER_Stage;

import com.itko.util.Base64;

/**
 * Handle an AUTH request.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class AUTH extends SpecificCommand
{
	/** Logger. */
	static private final Log LOG = LogFactory.getLog(AUTH.class);

	/** First stage of the multistage AUTH LOGIN. */
	static private final MultistageAUTH sLOGINstart = new LOGIN_USER_Stage();

	/** First AUTH stage */
	private MultistageAUTH mMultistageStart = null;

	/**
	 * Handle AUTH PLAIN.
	 * 
	 * @param args
	 */
	private void authPLAIN(String args)
	{
		byte[] decoded = Base64.decode(args);
		String text = new String(decoded, EmailConstants.MAIL_ENCODING);
		int zero = text.indexOf(0, 1);

		String username;
		String password;

		if (zero < 0)
		{
			username = text.substring(1);
			password = "";
		}
		else
		{
			username = text.substring(1, zero);
			password = text.substring(zero + 1);
		}

		getArguments().put("Username", username);
		getArguments().put("Password", password);
	}

	/**
	 * Create a new AUTH.
	 * 
	 * @param command
	 * @param args
	 */
	public AUTH(String command, String args)
	{
		super(command, args);

		// get AUTH type
		String method;
		String authArgs;
		int space = args.indexOf(' ');

		if (space < 0)
		{
			method = args.toUpperCase();
			authArgs = "";
		}
		else
		{
			method = args.substring(0, space).toUpperCase();
			authArgs = args.substring(space + 1).trim();
		}

		getArguments().put("Method", method);

		// AUTH PLAIN
		if (method.equals("PLAIN"))
		{
			authPLAIN(authArgs);
		}
		// AUTH LOGIN
		else if (method.equals("LOGIN"))
		{
			mMultistageStart = sLOGINstart;
		}
		// Unrecognized AUTH
		else
		{
			LOG.warn("Unrecognized AUTH method: " + method);
			getArguments().put("authArgs", authArgs);
		}
	}

	/**
	 * Get the starting AUTH stage if authentication is multistage.
	 * 
	 * @return next AUTH stage
	 */
	public MultistageAUTH getNextStage()
	{
		return mMultistageStart;
	}
}
