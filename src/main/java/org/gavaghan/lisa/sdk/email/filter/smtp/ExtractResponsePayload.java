package org.gavaghan.lisa.sdk.email.filter.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.w3c.dom.Element;

import com.itko.lisa.test.FilterBaseImpl;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;
import com.itko.util.Parameter;
import com.itko.util.ParameterList;
import com.itko.util.XMLUtils;

/**
 * Get only the payload portion of an SMTP response (without the status code at
 * the start of each line). This allows the payload to be parsed as JSON, XML, etc.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class ExtractResponsePayload extends FilterBaseImpl
{
   static private final String SMTP_RESPONSE = "smtpResponse";
   static private final String SAVE_TO_PROPERTY = "smtpPayload";

   /** The response to parse. */
   static private String mSMTPResponse;

   /** Property to store the response to. */
   static private String mSaveToProperty;

   /**
    * Create a new <code>ExtractResponsePayload</code> filter.
    */
   public ExtractResponsePayload()
   {
      markFilterAsGlobal(false);
      setThreadSafe(true);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.itko.lisa.test.FilterBaseImpl#getTypeName()
    */
   @Override
   public String getTypeName()
   {
      return "Extract SMTP Response Payload";
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.itko.lisa.test.FilterBaseImpl#supportsDesignTimeExecution()
    */
   @Override
   public boolean supportsDesignTimeExecution()
   {
      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.itko.lisa.test.FilterBaseImpl#supportsDynamicResponseToFilter()
    */
   @Override
   public boolean supportsDynamicResponseToFilter()
   {
      return false;
   }

   @Override
   public ParameterList getParameters()
   {
      ParameterList p = new ParameterList();
      p.addParameter(new Parameter("SMTP Response", SMTP_RESPONSE, mSMTPResponse, String.class));
      p.addParameter(new Parameter("Save to Property", SAVE_TO_PROPERTY, mSaveToProperty, String.class));
      return p;
   }

   @Override
   public void initialize(Element elem) throws TestDefException
   {
      mSMTPResponse = XMLUtils.findChildGetItsText(elem, SMTP_RESPONSE);
      mSaveToProperty = XMLUtils.findChildGetItsText(elem, SAVE_TO_PROPERTY);

      if (mSMTPResponse == null) mSMTPResponse = "{{LASTRESPONSE}}";
      if (mSaveToProperty == null) mSaveToProperty = "";
   }

   @Override
   public boolean subPreFilter(TestExec testExec) throws TestRunException
   {
      return false;
   }

   @Override
   public boolean subPostFilter(TestExec testExec) throws TestRunException
   {
      String response = testExec.parseInState(mSMTPResponse);
      String key = testExec.parseInState(mSaveToProperty);

      StringBuilder payload = new StringBuilder(64);

      // pull out the payload
      try (BufferedReader reader = new BufferedReader(new StringReader(response)))
      {
         String line;

         // skip the first line
         line = reader.readLine();

         // loop over remaining lines and truncate the first 4 characters
         while ((line = reader.readLine()) != null)
         {
            payload.append(line.substring(4));
            payload.append("\r\n");
         }
      }
      catch (IOException exc)
      {
         throw new TestRunException("Unexpected reader failure", exc);
      }
      
      // serialize the response and save it
      testExec.setStateValue(key, payload.toString());

      return false;
   }
}
