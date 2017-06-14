package org.gavaghan.lisa.sdk.email.assertion;

import org.w3c.dom.Element;

import com.itko.lisa.test.CheckResult;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.util.Parameter;
import com.itko.util.ParameterList;
import com.itko.util.XMLUtils;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class SMTPResponseAssertion extends CheckResult
{
	/** Status code key. */
	static private final String STATUS_CODE = "STATUS";

	/** Response code key. */
	static private final String RESPONSE_CODE = "RESPONSE";

	/** Status code to assert. */
	private String mStatus;

	/** Response code to assert, or empty string. */
	private String mResponse;

	@Override
	public String getTypeName() throws Exception
	{
		return "Check SMTP Response";
	}

	@Override
	public ParameterList getCustomParameters()
	{
		ParameterList pl = new ParameterList();
		pl.addParameter(new Parameter("Status: ", STATUS_CODE, mStatus, java.lang.String.class));
		pl.addParameter(new Parameter("Response: ", RESPONSE_CODE, mResponse, java.lang.String.class));
		return pl;
	}

	@Override
	public void initialize(Element elem) throws TestDefException
	{
		mStatus = XMLUtils.getChildText(XMLUtils.findChildElement(elem, STATUS_CODE));
		mResponse = XMLUtils.getChildText(XMLUtils.findChildElement(elem, RESPONSE_CODE));
	}

	@Override
	protected boolean evaluate(TestExec testExec, Object arg)
	{
		if (!(arg instanceof String))
		{
			testExec.log("Assert fired", "Argument is not a string");
			return false;
		}

		String line = (String) arg;
		String status = testExec.parseInState(mStatus);
		String response = testExec.parseInState(mResponse);

		// check status
		if ((status != null) && (status.length() > 0))
		{
			if (!line.startsWith(status))
			{
				testExec.log("Assert fired", "Did not get expected status code: " + line);
				return false;
			}
		}
		
		// check response
		if ((response != null) && (response.length() > 0))
		{
			int idx = line.indexOf("[ID=");

			if (idx >= 0)
			{
				String code = line.substring(idx + 4);
				idx = code.indexOf("]");
				if (idx > 0)  code = code.substring(0, idx);
				
				if (!mResponse.equals(code))
				{
					testExec.log("Assert fired", "Did not get expected response code: " + line);
					return false;
				}
			}
		}

		return true;
	}
}
