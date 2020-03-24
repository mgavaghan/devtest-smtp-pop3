package org.gavaghan.lisa.sdk.email.step;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Fix a bug in the readLine() method that causes it to return null when an
 * underlying socket has closed.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class CorrectedBufferedReader extends BufferedReader
{
	/** Pending character to use, if any. */
	private Integer mPending = null;

	/**
	 * Create a new CorrectedBufferedReader
	 * 
	 * @param reader
	 */
	public CorrectedBufferedReader(Reader reader)
	{
		super(reader);
	}

	/**
	 * Read the next character
	 */
	@Override
	public int read() throws IOException
	{
		int c;

		// if there's a pending character, use it
		if (mPending != null)
		{
			c = mPending.intValue();
			mPending = null;
		}
		// otherwise, go to superclass
		else
		{
			c = super.read();
		}

		return c;
	}

	/**
	 * Read all characters up to a CRLF
	 */
	@Override
	public String readLine() throws IOException
	{
		StringBuilder builder = null;

		// check first character for end of data
		int c = read();

		// if not end of data
		if (c >= 0)
		{
			builder = new StringBuilder();

			for (;;)
			{
				builder.append((char) c);
				c = read();

				// if out of data
				if (c < 0) break;

				// if carriage return
				if (c == '\r')
				{
					int peek = read();
					if ((peek >= 0) && (peek != '\n'))
					{
						mPending = new Integer(peek);
					}
					break;
				}

				// if line feed
				if (c == '\n') break;
			}
		}

		// if we got a complete line, show it
		return (builder != null) ? builder.toString() : null;
	}
}
