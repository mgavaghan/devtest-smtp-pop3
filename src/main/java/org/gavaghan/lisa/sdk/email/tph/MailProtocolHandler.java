package org.gavaghan.lisa.sdk.email.tph;

import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.protocol.TransportProtocol;
import com.itko.util.XMLUtils;

/**
 * Base implementation for mail server transport protocol handlers.
 * 
 * @author mike@gavaghan.org
 */
public abstract class MailProtocolHandler extends TransportProtocol
{
   /** Logger. */
   static private final Log LOG = LogFactory.getLog(MailProtocolHandler.class);

   /** Port to listen on (as a parseable String). */
   private String mListenPortStr;

   /** Port to listen on. */
   private int mListenPort;

   /** Target port for recording (as a parseable String). */
   private String mTargetPortStr;

   /** Target host for recording. */
   private String mTargetHost = "";

   /** SSL to Server flag. */
   private boolean mSSLtoServer;

   /** Recording Runnable. */
   private MailRecorder mRecorder;

   /**
    * Create protocol-specific mail recorder.
    * 
    * @param server
    * @param targetHost
    * @param targetPort
    * @param sslToServer
    * @return
    */
   protected abstract MailRecorder createMailRecorder(MailServer server, String targetHost, int targetPort, boolean sslToServer);

   /**
    * Get the listen port.
    * 
    * @return the listen port
    */
   public String getListenPort()
   {
      return mListenPortStr;
   }

   /**
    * Set the listen port.
    * 
    * @param value
    */
   public void setListenPort(String value)
   {
      mListenPortStr = value;
   }

   /**
    * Get the target host for recording.
    * 
    * @return the target host for recording
    */
   public String getTargetHost()
   {
      return mTargetHost;
   }

   /**
    * Set the target host for recording.
    * 
    * @param value
    */
   public void setTargetHost(String value)
   {
      mTargetHost = value;
   }

   /**
    * Get the target port for recording.
    * 
    * @return the target port for recording
    */
   public String getTargetPort()
   {
      return mTargetPortStr;
   }

   /**
    * Set the target port for recording.
    * 
    * @param value
    */
   public void setTargetPort(String value)
   {
      mTargetPortStr = value;
   }

   /**
    * Get the target port for recording.
    * 
    * @return the target port for recording
    */
   public boolean getSSLtoServer()
   {
      return mSSLtoServer;
   }

   /**
    * Set the target port for recording.
    * 
    * @param value
    */
   public void setSSLtoServer(boolean value)
   {
      mSSLtoServer = value;
   }

   /**
    * Initialize for a VRS file.
    */
   @Override
   public void initialize(Element elem)
   {
      setListenPort(XMLUtils.findChildGetItsText(elem, "listenPort"));
      setTargetHost(XMLUtils.findChildGetItsText(elem, "targetHost"));
      setTargetPort(XMLUtils.findChildGetItsText(elem, "targetPort"));
      setSSLtoServer(Boolean.parseBoolean(XMLUtils.findChildGetItsText(elem, "sslToServer")));
   }

   /**
    * Save to a VRS file.
    */
   @Override
   public void writeSubXML(PrintWriter pw)
   {
      XMLUtils.streamTagAndChild(pw, "listenPort", getListenPort());
      XMLUtils.streamTagAndChild(pw, "targetHost", getTargetHost());
      XMLUtils.streamTagAndChild(pw, "targetPort", getTargetPort());
      XMLUtils.streamTagAndChild(pw, "sslToServer", Boolean.toString(getSSLtoServer()));
   }

   /**
    * Kickoff the recording thread.
    */
   @Override
   protected void beginRecordProcess(TestExec testExec) throws Exception
   {
      LOG.info("Beginning recording process.");
      mListenPort = Integer.parseInt(testExec.parseInState(mListenPortStr));
      String targetHost = testExec.parseInState(mTargetHost);
      int targetPort = Integer.parseInt(testExec.parseInState(mTargetPortStr));

      MailServer server = MailServer.create(mListenPort);
      mRecorder = createMailRecorder(server, targetHost, targetPort, mSSLtoServer);

      Thread thread = new Thread(mRecorder);
      thread.setName("MailRecorder");
      thread.setDaemon(true);
      thread.start();
   }

   /**
    * Stop the recorder and stop listening on the listener port.
    */
   @Override
   protected void endRecordProcess()
   {
      LOG.info("Ending recording process.");
      mRecorder.shutdown();
      if (LOG.isDebugEnabled()) LOG.debug("Removing listen port: " + mListenPort);
      MailServer.remove(mListenPort);
   }
}
