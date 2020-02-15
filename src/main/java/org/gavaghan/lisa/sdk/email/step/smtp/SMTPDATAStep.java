package org.gavaghan.lisa.sdk.email.step.smtp;

import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.lisa.sdk.email.step.BaseStep;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.util.XMLUtils;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPDATAStep extends BaseStep
{
   /** Our logger. */
   static private Log LOG = LogFactory.getLog(BaseStep.class);
   
   /** Header portion of the mail. */
   public String mHeaders;

   /** Body portion of the mail. */
   public String mBody;

   /**
    * Get the headers.
    * 
    * @return the headers
    */
   public String getHeaders()
   {
      return mHeaders;
   }

   /**
    * Set the headers.
    * 
    * @param value the headers
    */
   public void setHeaders(String value)
   {
      mHeaders = value;
   }

   /**
    * Get the body.
    * 
    * @return the body
    */
   public String getBody()
   {
      return mBody;
   }

   /**
    * Set the body.
    * 
    * @param value
    */
   public void setBody(String value)
   {
      mBody = value;
   }

   /**
    * Initialize from a test file.
    */
   @Override
   public void initialize(TestCase testCase, Element elem) throws TestDefException
   {
      setHeaders(XMLUtils.findChildGetItsText(elem, "headers"));
      setBody(XMLUtils.findChildGetItsText(elem, "body"));
   }

   /**
    * Save to test file.
    */
   @Override
   public void writeSubXML(PrintWriter pw)
   {
      XMLUtils.streamTagAndChild(pw, "headers", getHeaders());
      XMLUtils.streamTagAndChild(pw, "body", getBody());
   }
   
   @Override
   public String getTypeName() throws Exception
   {
      return "SMTP DATA";
   }

   @Override
   protected Object doNodeLogic(TestExec testExec) throws Exception
   {
      return null;
   }
}
