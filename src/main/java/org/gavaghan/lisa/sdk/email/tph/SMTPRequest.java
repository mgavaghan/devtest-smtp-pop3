package org.gavaghan.lisa.sdk.email.tph;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.AUTH;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.DATA;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.HELO;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.MAIL;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.NoArgumentsCommand;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.QUIT;
import org.gavaghan.lisa.sdk.email.tph.smtpcommands.RCPT;

import com.itko.util.ParameterList;

/**
 * Encapsulates an SMTP request.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class SMTPRequest
{
	/** Logger. */
	static private final Log LOG = LogFactory.getLog(SMTPRequest.class);

	/** Name to apply to generic argument string. */
	static private final String ARGUMENTS_ARG_NAME = "args";

	/** Parameters to a command constructor. */
	static private Class[] CTX_PARAMS = new Class[] { String.class, String.class };

	/** Map command names to their constructors. */
	static private Map<String, Constructor<?>> sCommands = new HashMap<String, Constructor<?>>();

	static
	{
		// populate command map of specific subtypes
		addCommand("AUTH", AUTH.class);
		addCommand("DATA", DATA.class);
		addCommand("MAIL", MAIL.class);
		addCommand("QUIT", QUIT.class);
		addCommand("RCPT", RCPT.class);
		addCommand("HELO", HELO.class);
		addCommand("EHLO", HELO.class);

		// populate specific commands that take no arguments
		addCommand("NOOP", NoArgumentsCommand.class);
	}

	/**
	 * Add a command to the lookup map.
	 * 
	 * @param name
	 * @param type
	 */
	static private void addCommand(String name, Class<?> type)
	{
		try
		{
			if (!SMTPRequest.class.isAssignableFrom(type))
			{
				throw new RuntimeException(type.getName() + " is not a subtype of SMTPRequest");
			}

			Constructor<?> ctx = type.getConstructor(CTX_PARAMS);
			sCommands.put(name.toUpperCase(), ctx);
		}
		catch (Exception exc)
		{
			LOG.fatal("Failed to find constructor for command of type: " + type.getSimpleName(), exc);
			throw new RuntimeException("Failed to find constructor for command of type: " + type.getSimpleName());
		}
	}

	/**
	 * Create an SMTPRequest instance based on command type.
	 * 
	 * @param command
	 * @param arguments
	 * @return
	 */
	static private SMTPRequest createSMTPRequest(String command, String arguments)
	{
		SMTPRequest request;
		String commandUpper = command.toUpperCase();
		Constructor<?> ctx = sCommands.get(commandUpper);

		if (ctx == null)
		{
			request = new SMTPRequest(commandUpper, arguments);
		}
		else
		{
			try
			{
				request = (SMTPRequest) ctx.newInstance(commandUpper, arguments);
			}
			catch (Exception exc)
			{
				throw new RuntimeException("Failed to instantiate a command object for: " + commandUpper, exc);
			}
		}

		return request;
	}

	/** SMTP commands. */
	private String mCommand;

	/** Generic argument string. */
	private String mArgumentString;

	/** Parsed argument list. */
	private ParameterList mArguments = new ParameterList();

	/**
	 * Build a new SMTPRequest.
	 * 
	 * @param command
	 * @param arguments
	 */
	protected SMTPRequest(String command, String arguments)
	{
		mCommand = command;
		mArgumentString = arguments;
		if (mArgumentString.length() == 0)
		{
			mArguments.put(ARGUMENTS_ARG_NAME, arguments);
		}
	}

	/**
	 * Get the command.
	 * 
	 * @return the command
	 */
	public String getCommand()
	{
		return mCommand;
	}

	/**
	 * Get the generic argument string.
	 * 
	 * @return the argument string
	 */
	public String getArgumentString()
	{
		return mArgumentString;
	}

	/**
	 * Get the arguments.
	 * 
	 * @return the arguments list
	 */
	public ParameterList getArguments()
	{
		return mArguments;
	}

	/**
	 * Get string representation.
	 */
	@Override
	public String toString()
	{
		return mCommand + " " + mArgumentString;
	}

	/**
	 * Parse an SMTP request line.
	 * 
	 * @param line
	 * @return a new SMTPRequest
	 */
	static public SMTPRequest parse(String line)
	{
		String command;
		String arguments;

		int space = line.indexOf(' ');
		if (space < 0)
		{
			command = line;
			arguments = "";
		}
		else
		{
			command = line.substring(0, space);
			arguments = line.substring(space + 1).trim();
		}

		// construct instance based on command type
		return createSMTPRequest(command, arguments);
	}
}
