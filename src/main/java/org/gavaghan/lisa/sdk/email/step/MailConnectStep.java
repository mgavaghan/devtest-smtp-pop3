package org.gavaghan.lisa.sdk.email.step;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.util.XMLUtils;

/**
 * Base implementation for mail server connections.
 * 
 * @author mike@gavaghan.org
 */
public abstract class MailConnectStep extends BaseStep
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(MailConnectStep.class);

	/** Server host and port. */
	private String mServerAddress;

	/** SSL Flag. */
	private boolean mSSL;

	/**
	 * Initialize from a test file.
	 */
	@Override
	public void initialize(TestCase testCase, Element elem) throws TestDefException
	{
		setServerAddress(XMLUtils.findChildGetItsText(elem, "serverAddress"));
		setSSL(Boolean.parseBoolean(XMLUtils.findChildGetItsText(elem, "ssl")));
	}

	/**
	 * Save to test file.
	 */
	@Override
	public void writeSubXML(PrintWriter pw)
	{
		XMLUtils.streamTagAndChild(pw, "serverAddress", getServerAddress());
		XMLUtils.streamTagAndChild(pw, "ssl", Boolean.toString(mSSL));
	}

	/**
	 * 
	 * @param testExec
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	protected Socket createSocket(TestExec testExec) throws UnknownHostException, IOException
	{
		String serverAddress = testExec.parseInState(mServerAddress);
		String server;
		int port;

		// extract server and port
		int colon = serverAddress.indexOf(':');
		if (colon < 0)  throw new RuntimeException("Server address does not indicate a port");
		server = serverAddress.substring(0, colon).trim();
		port = Integer.parseInt(serverAddress.substring(colon + 1).trim());

		// create socket
		Socket socket;

		if (getSSL())
		{
			socket = SSLSocketFactory.getDefault().createSocket(server, port);
		}
		else
		{
			socket = new Socket(server, port);
		}

		// get read timeout
		int timeout = 5000;
		String timeoutStr = (String) testExec.getStateValue("smtp.read.tiemout");

		if (timeoutStr != null)
		{
			try
			{
				timeout = Integer.parseInt(timeoutStr);
			}
			catch (NumberFormatException exc)
			{
				LOG.error("Failed to parse 'smtp.read.tiemout' value of '" + timeoutStr + "'.  Using default of " + timeout);
			}
		}

		socket.setSoTimeout(timeout);

		testExec.setStateObject(getSocketKey(), socket);
		return socket;
	}

	/**
	 * 
	 * @param testExec
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	protected void createStreams(TestExec testExec) throws UnknownHostException, IOException
	{
		LOG.debug("Creating streams");

		// create new socket
		Socket socket = createSocket(testExec);

		// create the reader
		InputStreamReader isr = new InputStreamReader(socket.getInputStream(), EmailConstants.MAIL_ENCODING);
		BufferedReader reader = new CorrectedBufferedReader(isr);
		testExec.setStateObject(getReaderKey(), reader);

		// create the writer
		OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), EmailConstants.MAIL_ENCODING);
		BufferedWriter writer = new BufferedWriter(osw);
		testExec.setStateObject(getWriterKey(), writer);
	}

	/**
	 * Get key for socket in TestExec.
	 * 
	 * @return key for socket in TestExec
	 */
	protected abstract String getSocketKey();

	/**
	 * Get key for reader in TestExec.
	 * 
	 * @return key for reader in TestExec
	 */
	protected abstract String getReaderKey();

	/**
	 * Get key for writer in TestExec.
	 * 
	 * @return key for writer in TestExec
	 */
	protected abstract String getWriterKey();

	/**
	 * Get the server address.
	 * 
	 * @return the server address
	 */
	public String getServerAddress()
	{
		return mServerAddress;
	}

	/**
	 * Set the server address.
	 * 
	 * @param value
	 */
	public void setServerAddress(String value)
	{
		mServerAddress = value;
	}

	/**
	 * Get the SSL flag.
	 * 
	 * @return the SSL flag
	 */
	public boolean getSSL()
	{
		return mSSL;
	}

	/**
	 * Set the SSL flag.
	 * 
	 * @param value
	 */
	public void setSSL(boolean value)
	{
		mSSL = value;
	}

}
