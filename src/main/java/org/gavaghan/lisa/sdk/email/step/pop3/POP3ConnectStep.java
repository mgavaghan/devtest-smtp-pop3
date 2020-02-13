package org.gavaghan.lisa.sdk.email.step.pop3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.step.MailConnectStep;

import com.itko.lisa.test.TestExec;

/**
 * Step to establish an POP3 connection.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class POP3ConnectStep extends MailConnectStep
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(POP3ConnectStep.class);

	/** Key for socket in TestExec. */
	static private final String SOCKET_KEY = POP3ClientStep.class.getName() + ".SOCKET";

	/** Key for reader in TestExec. */
	static final String READER_KEY = POP3ClientStep.class.getName() + ".READER";

	/** Key for writer in TestExec. */
	static final String WRITER_KEY = POP3ClientStep.class.getName() + ".WRITER";

	/** Key for POP3 status in TestExec. */
	static final String STATUS_KEY = "pop3.status";

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
		closePOP3Connection(testExec);
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
	static public void closePOP3Connection(TestExec testExec)
	{
		LOG.debug("Closing POP3 connection");
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
		closePOP3Connection(testExec);

		super.destroy(testExec);
	}

	/**
	 * Get the type name.
	 */
	@Override
	public String getTypeName() throws Exception
	{
		return "POP3 Connect";
	}

	/**
	 * Send command and get response.
	 */
	@Override
	protected Object doNodeLogic(TestExec testExec) throws Exception
	{
		// create the POP3 streams
		createStreams(testExec);

		// get the response
		BufferedReader reader = (BufferedReader) testExec.getStateObject(READER_KEY);
		String line = reader.readLine();

		// parse the response
		if ((line == null) || (line.length() < 3)) throw new IOException("Invalid POP3 greeting received.");

		String status = line.substring(0, 3);
		String response = line.substring(4).trim();

		testExec.setStateObject(STATUS_KEY, status);

		return response;
	}
}
