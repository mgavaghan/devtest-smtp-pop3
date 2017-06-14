package org.gavaghan.lisa.sdk.email.tph;

/**
 * Encapsulates a POP3 response message.
 * 
 * @author mike@gavaghan.org
 */
public class POP3Response
{
	/** Success flag. */
	private final boolean mSuccess;

	/** Line 1 response. */
	private final String mLine1;

	/** Remainder of multiline response. */
	private String mContent;

	/**
	 * Create a new POP3Response.
	 * 
	 * @param success
	 * @param line1
	 */
	public POP3Response(boolean success, String line1)
	{
		mSuccess = success;
		mLine1 = line1;
	}

	/**
	 * Get success flag.
	 * 
	 * @return success flag
	 */
	public boolean getSuccess()
	{
		return mSuccess;
	}

	/**
	 * Get first line of response.
	 * 
	 * @return first line of response
	 */
	public String getLine1()
	{
		return mLine1;
	}

	/**
	 * Get response content.
	 * 
	 * @return response content
	 */
	public String getContent()
	{
		StringBuilder builder = new StringBuilder();

		builder.append(mLine1);
		builder.append(EmailConstants.CRLF);

		if (mContent != null)
		{
			builder.append(mContent.trim());
			builder.append(EmailConstants.CRLF);
		}

		return builder.toString();
	}

	/**
	 * Set subsequent lines of a multiline response.
	 * 
	 * @param content
	 */
	public void setMoreContent(String content)
	{
		mContent = content;
	}

	/**
	 * Get if response is multiline.
	 * 
	 * @return multiline flag
	 */
	public boolean isMultiline()
	{
		return mContent != null;
	}

	@Override
	public String toString()
	{
		return getContent();
	}
}
