package org.gavaghan.lisa.sdk.email.tph;

import java.nio.charset.Charset;

/**
 * Email system constant.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public interface EmailConstants
{
	/** SMTP and POP3 character encoding. */
	static public final Charset MAIL_ENCODING = Charset.forName("ISO-8859-1");

	/** Carriage return and line feed combination. */
	static public final String CRLF = "\r\n";
}
