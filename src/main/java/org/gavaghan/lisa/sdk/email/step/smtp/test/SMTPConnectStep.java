package org.gavaghan.lisa.sdk.email.step.smtp.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.devtest.autostep.AutoStep;
import org.gavaghan.devtest.autostep.Property;
import org.gavaghan.devtest.autostep.TypeName;
import org.gavaghan.lisa.sdk.email.step.CorrectedBufferedReader;
import org.gavaghan.lisa.sdk.email.tph.EmailConstants;

import com.itko.lisa.test.TestExec;

/**
 * Step to establish an SMTP connection.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@TypeName("SMTP Connect")
@Property(index = 0, name = "Server Address", mandatory = true)
@Property(index = 1, name = "Is SSL", type = boolean.class)
public class SMTPConnectStep extends AutoStep
{
   /** Our logger. */
   static private Log LOG = LogFactory.getLog(SMTPConnectStep.class);

   /** Key for socket in TestExec. */
   static final String SOCKET_KEY = "SMTPConnectStep.SOCKET";

   /** Key for reader in TestExec. */
   static final String READER_KEY = "SMTPConnectStep.READER";

   /** Key for writer in TestExec. */
   static final String WRITER_KEY = "SMTPConnectStep.WRITER";

   /** Key for SMTP status in TestExec. */
   static final String STATUS_KEY = "smtp.status";

   /**
    * 
    * @param testExec
    * @return
    * @throws UnknownHostException
    * @throws IOException
    */
   private Socket createSocket(TestExec testExec) throws UnknownHostException, IOException
   {
      String serverAddress = getParsedProperty(testExec, "Server Address");
      String server;
      int port;

      // extract server and port
      int colon = serverAddress.indexOf(':');
      if (colon < 0)  throw new RuntimeException("Server address does not indicate a port");
      server = serverAddress.substring(0, colon).trim();
      port = Integer.parseInt(serverAddress.substring(colon + 1).trim());

      // create socket
      Socket socket;

      if (((Boolean) getProperty("Is SSL")).booleanValue())
      {
         socket = SSLSocketFactory.getDefault().createSocket(server, port);
      }
      else
      {
         socket = new Socket(server, port);
      }

      // get read timeout
      int timeout;
      String timeoutStr = testExec.parseInState(testExec.getStateString("smtp.read.timeout", "5000"));

      try
      {
         timeout = Integer.parseInt(timeoutStr);
      }
      catch (NumberFormatException exc)
      {
         throw new RuntimeException("Failed to parse 'smtp.read.tiemout' value of '" + timeoutStr + "'");
      }

      socket.setSoTimeout(timeout);

      // add socket to the state
      testExec.setStateObject(SOCKET_KEY, socket);
      return socket;
   }

   /**
    * Create the stream.
    * 
    * @param testExec
    * @throws UnknownHostException
    * @throws IOException
    */
   @SuppressWarnings("resource")
   protected void createStreams(TestExec testExec) throws UnknownHostException, IOException
   {
      LOG.debug("Creating streams");

      // close previous stream if it was left open
      closeSMTPConnection(testExec);

      // create new socket
      Socket socket = createSocket(testExec);

      // create the reader
      InputStreamReader isr = new InputStreamReader(socket.getInputStream(), EmailConstants.MAIL_ENCODING);
      BufferedReader reader = new CorrectedBufferedReader(isr);
      testExec.setStateObject(READER_KEY, reader);

      // create the writer
      OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), EmailConstants.MAIL_ENCODING);
      BufferedWriter writer = new BufferedWriter(osw);
      testExec.setStateObject(WRITER_KEY, writer);
   }

   /**
    * Close connection to the server.
    * 
    * @param testExec
    * @throws IOException
    */
   @SuppressWarnings("resource")
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
    * Send command and get response.
    */
   @SuppressWarnings("resource")
   @Override
   protected Object doNodeLogic(TestExec testExec) throws Exception
   {
      // ensure previous state was closed
      closeSMTPConnection(testExec);
      
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
