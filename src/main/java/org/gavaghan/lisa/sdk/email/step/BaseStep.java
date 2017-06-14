package org.gavaghan.lisa.sdk.email.step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itko.lisa.test.TestEvent;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.util.CloneImplemented;

/**
 * Base of all customer steps.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public abstract class BaseStep extends TestNode implements CloneImplemented
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(BaseStep.class);

	/**
	 * @param testExec
	 * @return
	 * @throws Exception
	 */
	protected abstract Object doNodeLogic(TestExec testExec) throws Exception;

	/**
	 * Execute this node.
	 */
	@Override
	public void execute(TestExec testExec) throws TestRunException
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
}
