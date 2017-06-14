package org.gavaghan.lisa.sdk.email.tph;

import java.util.List;

/**
 * Encapsulate an SMTP response.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPResponse
{
	/** Carriage return and line feed combination. */
	static private final String CRLF = "\r\n";

	/** Status code. */
	private String mStatus;

	/** Response lines (without leading status code). */
	private List<String> mLines;

	/**
	 * Create a new SMTPResponse.
	 * 
	 * @param status
	 * @param lines
	 */
	public SMTPResponse(String status, List<String> lines)
	{
		mStatus = status;
		mLines = lines;
	}

	/**
	 * Get the status code.
	 * 
	 * @return status code
	 */
	public String getStatus()
	{
		return mStatus;
	}

	/**
	 * Get the response lines.
	 * 
	 * @return response lines
	 */
	public List<String> getLines()
	{
		return mLines;
	}

	/**
	 * Get string representation.
	 */
	@Override
	public String toString()
	{
		return getContentWithStatus();
	}

	/**
	 * Get content without status codes.
	 * 
	 * @return content
	 */
	public String getContent()
	{
		StringBuilder builder = new StringBuilder();

		for (String line : mLines)
		{
			builder.append(line);
			builder.append(CRLF);
		}

		return builder.toString();
	}

	/**
	 * Get content with status codes.
	 * 
	 * @return content
	 */
	public String getContentWithStatus()
	{
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < mLines.size(); i++)
		{
			builder.append(mStatus);

			if (i == (mLines.size() - 1)) builder.append(" ");
			else builder.append("-");

			builder.append(mLines.get(i));
			builder.append(CRLF);
		}

		return builder.toString();
	}
}
