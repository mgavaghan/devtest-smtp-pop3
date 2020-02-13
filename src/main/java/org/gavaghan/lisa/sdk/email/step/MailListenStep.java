package org.gavaghan.lisa.sdk.email.step;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;
import org.gavaghan.lisa.sdk.email.tph.MailServer;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestEvent;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;
import com.itko.lisa.vse.stateful.BaseListenStep;
import com.itko.util.XMLUtils;

/**
 * Base implementation for Mail listeners.
 * 
 * @author mike@gavaghan.org
 */
public abstract class MailListenStep extends BaseListenStep
{
   /** Our logger. */
   static private Log LOG = LogFactory.getLog(MailListenStep.class);

   /** Key to our open socket in TestExec. */
   static final String SOCKET_KEY = MailListenStep.class.getName() + ".SOCKET";

   /** Key to our connection in TestExec. */
   static final public String STATE_KEY = MailListenStep.class.getName() + ".STATE";

   /** Key to our QUIT flag in TestExec. */
   static final public String QUIT_KEY = MailListenStep.class.getName() + ".QUIT";

   /** Key to our open reader in TestExec. */
   static final String READER_KEY = MailListenStep.class.getName() + ".READER";

   /** Key to our session ID in TestExec. */
   static final public String SESSION_KEY = MailListenStep.class.getName() + ".SESSION";

   /** Key to our listen port in TestExec. */
   static final String PORT_KEY = MailListenStep.class.getName() + ".PORT";

   /** The port we'll listen on. */
   private String mListenPort;

   /** Enumeration connection state. */
   public enum ConnectionState
   {
      OPEN, DATA
   }

   /**
    * Setup a newly accepted client socket.
    * 
    * @param socket
    * @param testExec
    * @return reader wrapping the socket
    * @throws SocketException
    * @throws IOException
    */
   protected BufferedReader initializeSocket(Socket socket, TestExec testExec) throws SocketException, IOException
   {
      BufferedReader reader;

      // set read timeout
      int timeout = getReadTimeout(testExec);
      socket.setSoTimeout(timeout);

      // wrap with a reader
      InputStreamReader isr = new InputStreamReader(socket.getInputStream(), EmailConstants.MAIL_ENCODING);
      reader = new CorrectedBufferedReader(isr);

      // save critical elements
      testExec.setStateObject(SOCKET_KEY, socket);
      testExec.setStateObject(READER_KEY, reader);
      testExec.setStateObject(SESSION_KEY, UUID.randomUUID().toString());

      return reader;
   }

   /**
    * Get or create client reader.
    * 
    * @param testExec
    * @return
    * @throws IOException
    */
   @SuppressWarnings("resource")
   protected BufferedReader getReader(TestExec testExec) throws IOException
   {
      LOG.debug("Getting reader.");
      Socket socket = (Socket) testExec.getStateObject(SOCKET_KEY);
      BufferedReader reader = (BufferedReader) testExec.getStateObject(READER_KEY);

      // if no socket, open the connection
      if (socket == null)
      {
         LOG.debug("No socket and reader found, so we'll create them");
         int port = Integer.parseInt(testExec.parseInState(getListenPort()));
         testExec.setStateValue(PORT_KEY, new Integer(port));
         MailServer server = MailServer.getOrCreate(port);

         if (LOG.isDebugEnabled()) LOG.debug("Waiting to accept a connection on port " + port);
         socket = server.accept();

         if (socket != null) reader = initializeSocket(socket, testExec);
      }

      return reader;
   }

   /**
    * Get the listen port.
    * 
    * @return the listen port
    */
   public String getListenPort()
   {
      return mListenPort;
   }

   /**
    * Set the listen port.
    * 
    * @param value
    */
   public void setListenPort(String value)
   {
      mListenPort = value;
   }

   /**
    * Get the socket read timeout.
    * 
    * @param testExec
    * @return read timeout in milliseconds
    */
   public int getReadTimeout(TestExec testExec)
   {
      int timeout = 5000;
      String timeoutStr = (String) testExec.getStateValue(getReadTimeoutProperty());

      if (timeoutStr != null)
      {
         try
         {
            timeout = Integer.parseInt(timeoutStr);
         }
         catch (NumberFormatException exc)
         {
            LOG.error("Failed to parse '" + getReadTimeoutProperty() + "' value of '" + timeoutStr + "'.  Using default of " + timeout);
         }
      }

      return timeout;
   }

   /**
    * Build node from XML test case.
    */
   @Override
   public void initialize(TestCase testCase, Element elem)
   {
      setListenPort(XMLUtils.findChildGetItsText(elem, "listenPort"));
   }

   /**
    * Find the open socket and close it.
    */
   @Override
   public void destroy(TestExec testExec)
   {
      int port = Integer.parseInt(testExec.parseInState(getListenPort()));
      MailServer.remove(port);

      super.destroy(testExec);
   }

   /**
    * Write to XML test case.
    * 
    * @param pw
    */
   @Override
   public void writeSubXML(PrintWriter pw)
   {
      XMLUtils.streamTagAndChild(pw, "listenPort", getListenPort());
   }

   /**
    * 
    * @param testExec
    * @return
    * @throws IOException
    */
   protected abstract Object doNodeLogic(TestExec testExec) throws IOException;

   /**
    * 
    * @return
    */
   protected abstract String getReadTimeoutProperty();

   /**
    * Execute the step.
    */
   @Override
   protected void execute(TestExec testExec) throws TestRunException
   {
      try
      {
         testExec.setLastResponse(doNodeLogic(testExec));
      }
      catch (Exception exc)
      {
         testExec.setLastResponse(exc.getMessage());
         testExec.raiseEvent(TestEvent.EVENT_ABORT, getClass().getName() + " transaction failed.", exc.getMessage() + "\n" + exc.getStackTrace(), exc);
         testExec.setNextNode("abort");
         LOG.error(getClass().getName() + " transaction failed.", exc);
      }
   }

   /**
    * Close everything related to this mail server connection.
    * 
    * @param testExec
    */
   static public void closeMailConnection(TestExec testExec)
   {
      LOG.info("Closing Mail connection.");

      // remove all state information
      LOG.debug("Removing Mail state.");
      testExec.removeState(STATE_KEY);
      testExec.removeState(SESSION_KEY);
      testExec.removeState(QUIT_KEY);

      // if we had a socket, close it and related objects
      @SuppressWarnings("resource")
      Socket socket = (Socket) testExec.getStateObject(SOCKET_KEY);

      if (socket != null)
      {
         // get the objects
         try (BufferedReader reader = (BufferedReader) testExec.getStateObject(READER_KEY);
               Writer writer = (Writer) testExec.getStateObject(MailRespondStep.WRITER_KEY))
         {
            // remove from state
            testExec.removeState(READER_KEY);
            testExec.removeState(MailRespondStep.WRITER_KEY);
            testExec.removeState(SOCKET_KEY);
         }
         catch (IOException exc)
         {
            LOG.warn("Failed to shutdown socket", exc);
         }

         // close the socket
         LOG.debug("Closing the socket.");
         Integer listenPort = (Integer) testExec.getStateObject(PORT_KEY);
         if (listenPort != null)
         {
            int port = listenPort.intValue();
            MailServer server = MailServer.get(port);
            if (server != null) server.close(socket);
         }
      }
   }
}
