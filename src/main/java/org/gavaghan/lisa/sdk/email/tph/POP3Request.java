package org.gavaghan.lisa.sdk.email.tph;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.DELE;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.LIST;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.NoArgumentsCommand;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.PASS;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.QUIT;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.RETR;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.STAT;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.TOP;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.UIDL;
import org.gavaghan.lisa.sdk.email.tph.pop3commands.USER;

import com.itko.util.ParameterList;

/**
 * Encapsulates a POP3 request.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class POP3Request
{
	/** Logger. */
	static private final Log LOG = LogFactory.getLog(POP3Request.class);

	/** Name to apply to generic argument string. */
	static private final String ARGUMENTS_ARG_NAME = "args";

	/** Parameters to a command constructor. */
	static private Class[] CTX_PARAMS = new Class[] { String.class, String.class };

	/** Map command names to their constructors. */
	static private Map<String, Constructor<?>> sCommands = new HashMap<String, Constructor<?>>();

	static
	{
		// populate command map of specific subtypes
		addCommand("USER", USER.class);
		addCommand("PASS", PASS.class);
		addCommand("QUIT", QUIT.class);
		addCommand("STAT", STAT.class);
		addCommand("LIST", LIST.class);
		addCommand("UIDL", UIDL.class);
		addCommand("RETR", RETR.class);
		addCommand("DELE", DELE.class);
		addCommand("TOP", TOP.class);
		// APOP

		// populate specific commands that take no arguments
		addCommand("CAPA", NoArgumentsCommand.class);
		addCommand("RSET", NoArgumentsCommand.class);
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
			if (!POP3Request.class.isAssignableFrom(type))
			{
				throw new RuntimeException(type.getName() + " is not a subtype of POP3Request");
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
	static private POP3Request createPOP3Request(String command, String arguments)
	{
		POP3Request request;
		String commandUpper = command.toUpperCase();
		Constructor<?> ctx = sCommands.get(commandUpper);

		if (ctx == null)
		{
			request = new POP3Request(commandUpper, arguments);
		}
		else
		{
			try
			{
				request = (POP3Request) ctx.newInstance(commandUpper, arguments);
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
	 * Build a new POP3Request.
	 * 
	 * @param command
	 * @param arguments
	 */
	protected POP3Request(String command, String arguments)
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
	 * Determine if this request produces a multiline response.
	 * @return multiline flag
	 */
	public boolean isMultiLineResponse()
	{
		return false;
	}

	/**
	 * Parse a POP3 request line.
	 * 
	 * @param line
	 * @return newly instantiated POP3Reuest
	 */
	static public POP3Request parse(String line)
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
		return createPOP3Request(command, arguments);
	}
}
