package org.gavaghan.lisa.sdk.email.tph;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Manager for multiple client connections to an SMTP or POP3 server.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class MailServer
{
	/** Logger. */
	static private final Log LOG = LogFactory.getLog(MailServer.class);

	/** Maps ports to server managers. */
	static private final Map<Integer, MailServer> sServers = new HashMap<Integer, MailServer>();

	/** Server listener. */
	private ServerSocket mSocket;

	/** open/close flag. */
	private boolean mOpen;

	/** List of active client connections. */
	private List<Socket> mClients = new ArrayList<Socket>();

	/**
	 * Create a new SMTPServer.
	 * 
	 * @param port
	 * @throws IOException
	 */
	private MailServer(int port) throws IOException
	{
		mSocket = new ServerSocket(port);
		mOpen = true;
		if (LOG.isDebugEnabled()) LOG.debug("Created mail server on port " + port);
	}

	/**
	 * Get the server for a port.
	 * 
	 * @param port
	 * @return 'null' if not created
	 */
	static public synchronized MailServer get(int port)
	{
		if (LOG.isInfoEnabled()) LOG.info("Acquiring mail server on port " + port);
		MailServer server = sServers.get(new Integer(port));

		return server;
	}

	/**
	 * Create a new server.
	 * 
	 * @param port
	 * @return new mail server for port
	 * @throws IOException
	 */
	static public synchronized MailServer create(int port) throws IOException
	{
		if (LOG.isInfoEnabled()) LOG.info("Creating mail server on port " + port);
		MailServer server = get(port);
		if (server != null) throw new RuntimeException("Server already on port " + port);

		server = new MailServer(port);
		sServers.put(new Integer(port), server);

		return server;
	}

	/**
	 * Get the server for a port - creating the listener if necessary.
	 * 
	 * @param port
	 * @return mail server for port
	 * @throws IOException
	 */
	static public synchronized MailServer getOrCreate(int port) throws IOException
	{
		if (LOG.isInfoEnabled()) LOG.info("Get or create mail server on port " + port);
		MailServer server = get(port);
		if (server == null)
		{
			if (LOG.isDebugEnabled()) LOG.debug("Need to create mail server on port " + port);
			server = create(port);
		}

		return server;
	}

	/**
	 * Shutdown server on a port and remove from list.
	 * 
	 * @param port
	 * @return SMTP server for port
	 */
	static public synchronized MailServer remove(int port)
	{
		if (LOG.isInfoEnabled()) LOG.info("Removing mail server on port " + port);
		MailServer server = get(port);
		if (server != null)
		{
			if (LOG.isDebugEnabled()) LOG.debug("Removing port " + port + " from list of servers.");
			sServers.remove(new Integer(port));
			if (LOG.isDebugEnabled()) LOG.debug("Shutting down server on port " + port);
			server.shutdown();
		}
		return server;
	}

	/**
	 * Shutdown the server.
	 */
	public void shutdown()
	{
		if (mOpen)
		{
			mOpen = false;

			try
			{
				if (LOG.isInfoEnabled()) LOG.info("Closing server socket on port " + mSocket.getLocalPort());
				mSocket.close();
			}
			catch (IOException ignored)
			{
			}

			synchronized (this)
			{
				for (Socket socket : mClients)
				{
					try
					{
						socket.close();
					}
					catch (IOException ignored)
					{
					}
				}

				mClients.clear();
			}
		}
	}

	/**
	 * Accept a client connection.
	 * 
	 * @return client socket
	 * @throws IOException
	 */
	public Socket accept() throws IOException
	{
		Socket client;

		try
		{
			if (LOG.isInfoEnabled()) LOG.info("Accepting connections on port " + mSocket.getLocalPort());
			client = mSocket.accept();
			if (client != null)
			{
				if (LOG.isInfoEnabled()) LOG.info("Connection accepted from " + client.getRemoteSocketAddress());
				synchronized (this)
				{
					mClients.add(client);
				}
			}
			else
			{
				LOG.info("Stopped accepting new connections because the socket was shutdown.");
			}
		}
		catch (IOException exc)
		{
			if (!mOpen) client = null;
			else throw exc;
		}

		return client;
	}

	/**
	 * Close a client connection and remove it from the active list.
	 * 
	 * @param client
	 */
	public void close(Socket client)
	{
		try
		{
			client.close();
		}
		catch (IOException ignored)
		{
		}

		synchronized (this)
		{
			mClients.remove(client);
		}
	}
}
