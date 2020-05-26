package org.gavaghan.lisa.sdk.email.tph;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base implementation for mail server recordings.
 * 
 * @author mike@gavaghan.org
 */
public abstract class MailRecorder implements Runnable
{
	/** Logger. */
	static private final Log LOG = LogFactory.getLog(MailRecorder.class);

	/** Target SMTP server manager. */
	private MailServer mServer;

	/** Target host. */
	private String mTargetHost;

	/** Target port. */
	private int mTargetPort;

	/** SSL to server flag. */
	private boolean mSSLtoServer;

	/** Shutdown flag. */
	private AtomicBoolean mShutdown = new AtomicBoolean();

	/**
	 * Request the recorder shutdown.
	 */
	public void shutdown()
	{
		LOG.debug("Shutting down recorder");
		mShutdown.set(true);
	}

	/**
	 * Accept a new client connection.
	 * 
	 * @return client socket
	 */
	private Socket accept()
	{
		Socket client;

		// accept a new client
		try
		{
			LOG.info("About to accept a new connection from the MailServer");
			client = mServer.accept();
			if (client == null) LOG.info("Exiting after server was shutdown");
			client.setSoTimeout(10000);
		}
		catch (IOException exc)
		{
			LOG.error("Could not accept a new connection", exc);
			client = null;
			shutdown();
		}

		return client;
	}

	/**
	 * Start managing the conversation once the connection is made.
	 * 
	 * @param seesion
	 * @param client
	 * @param server
	 * @throws IOException
	 */
	protected abstract void manageConversation(String seesion, Socket client, Socket server) throws IOException;

	/**
	 * Create a new MailRecorder
	 * @param server
	 *           SMTP server manager
	 * @param targetHost
	 *           target host
	 * @param targetPort
	 *           target port
	 * @param sslToServer
	 */
	protected MailRecorder(MailServer server, String targetHost, int targetPort, boolean sslToServer)
	{
		mServer = server;
		mTargetHost = targetHost;
		mTargetPort = targetPort;
		mSSLtoServer = sslToServer;
		mShutdown.set(false);
	}
	
	/**
	 * Determine if record has been shutdown.
	 * @return shutdown flag
	 */
	public boolean isShutdown()
	{
		return mShutdown.get();
	}

	/**
	 * Thread work loop.
	 */
	@SuppressWarnings("resource")
   @Override
	public void run()
	{
		LOG.info("Begin recording");

		try
		{
			// loop until a shutdown has been requested
			while (!mShutdown.get())
			{
				LOG.info("About to accept a new client");

				// accept client connection
				Socket server = null;
				Socket client = accept();
				if (client == null) continue;

				// manage the conversation
				try
				{
					if (LOG.isInfoEnabled()) LOG.info("Creating connection to " + mTargetHost + ":" + mTargetPort);

					if (mSSLtoServer)
					{
						server = SSLSocketFactory.getDefault().createSocket(mTargetHost, mTargetPort);
					}
					else
					{
						server = new Socket(mTargetHost, mTargetPort);
					}

					manageConversation(UUID.randomUUID().toString(), client, server);
				}
				catch (IOException exc)
				{
					LOG.info("Closing connection on read failure", exc);
				}
				finally
				{
					if (server != null)
					{
						try
						{
							server.close();
						}
						catch (IOException ignored)
						{
						}
					}
					mServer.close(client);
				}
			}
		}
		catch (Exception exc)
		{
			LOG.error("Recorder failed", exc);
		}

		LOG.info("End recording");
	}

}
