package org.gavaghan.lisa.sdk.email.step;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itko.lisa.test.TestExec;

/**
 * Step to establish an SMTP connection.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPConnectStep extends MailConnectStep
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(SMTPConnectStep.class);

	/** Key for socket in TestExec. */
	static private final String SOCKET_KEY = SMTPClientStep.class.getName() + ".SOCKET";

	/** Key for reader in TestExec. */
	static final String READER_KEY = SMTPClientStep.class.getName() + ".READER";

	/** Key for writer in TestExec. */
	static final String WRITER_KEY = SMTPClientStep.class.getName() + ".WRITER";

	/** Key for SMTP status in TestExec. */
	static final String STATUS_KEY = "smtp.status";

	/**
	 * Create the stream.
	 * 
	 * @param testExec
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	@Override
	protected void createStreams(TestExec testExec) throws UnknownHostException, IOException
	{
		closeSMTPConnection(testExec);
		super.createStreams(testExec);
	}

	/**
	 * Get key for socket in TestExec.
	 * @return key for socket in TestExec
	 */
	@Override
	protected String getSocketKey()
	{
		return SOCKET_KEY;
	}

	@Override
	protected String getReaderKey()
	{
		return READER_KEY;
	}
	
	@Override
	protected String getWriterKey()
	{
		return WRITER_KEY;
	}
	
	/**
	 * Close connection to the server.
	 * 
	 * @param testExec
	 * @throws IOException
	 */
	static public void closeSMTPConnection(TestExec testExec)
	{
		LOG.debug("Closing SMTP connection");
		BufferedReader reader = (BufferedReader) testExec.getStateObject(READER_KEY);
		BufferedWriter writer = (BufferedWriter) testExec.getStateObject(WRITER_KEY);
		Socket socket = (Socket) testExec.getStateObject(SOCKET_KEY);

		testExec.removeState(READER_KEY);
		testExec.removeState(WRITER_KEY);
		testExec.removeState(SOCKET_KEY);

		if (reader != null)
		{
			try
			{
				reader.close();
			}
			catch (IOException exc)
			{
				// ignored
			}
		}

		if (writer != null)
		{
			try
			{
				writer.close();
			}
			catch (IOException exc)
			{
				// ignored
			}
		}

		if (socket != null)
		{
			try
			{
				socket.close();
			}
			catch (IOException exc)
			{
				// ignored
			}
		}
	}

	/**
	 * Close all connections on destroy.
	 */
	@Override
	public void destroy(TestExec testExec)
	{
		closeSMTPConnection(testExec);

		super.destroy(testExec);
	}

	/**
	 * Get the type name.
	 */
	@Override
	public String getTypeName() throws Exception
	{
		return "SMTP Connect";
	}

	/**
	 * Send command and get response.
	 */
	@Override
	protected Object doNodeLogic(TestExec testExec) throws Exception
	{
		// create the SMTP streams
		createStreams(testExec);

		// get the response
		BufferedReader reader = (BufferedReader) testExec.getStateObject(READER_KEY);
		String line = reader.readLine();

		// parse the response
		if ((line == null) || (line.length() < 4)) throw new IOException("Invalid SMTP greeting received: " + line);

		String status = line.substring(0, 3);
		String response = line.substring(4).trim();

		testExec.setStateObject(STATUS_KEY, status);

		return response;
	}
}
