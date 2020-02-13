package org.gavaghan.lisa.sdk.email.step.pop3;

import java.io.PrintWriter;

import javax.swing.Icon;

import com.itko.lisa.core.ModuleLegacy;
import com.itko.lisa.editor.TestNodeInfo;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class POP3ConnectController extends TestNodeInfo
{
	public static final String STEP_KEY = POP3ConnectStep.class.getName() + ".key";

	@Override
	public Icon getSmallIcon()
	{
		return ModuleLegacy.resources.getIcon("icon.tctree.vse", new Object[0]);
	}

	@Override
	public Icon getLargeIcon()
	{
		return ModuleLegacy.resources.getIcon("icon.tctree.vse.lg", new Object[0]);
	}

	@Override
	public String getEditorName()
	{
		return "POP3 Connect";
	}

	@Override
	public String getHelpString()
	{
		return "POP3 Connect";
	}

	/**
	 * This method is used to create a new step instance.
	 */
	@Override
	public void initNewOne()
	{
		if (getAttribute(STEP_KEY) == null)
		{
			POP3ConnectStep step = new POP3ConnectStep();

			setThinkTime(0, 0);

			putAttribute(STEP_KEY, step);
		}
	}

	/**
	 * This method is used to attach a created step object to our attribute set.
	 * 
	 * @param object
	 *           the step to attach. It must be an instance of
	 *           <code>HttpRespondStep</code>.
	 */
	@Override
	public void migrate(Object object)
	{
		POP3ConnectStep step = (POP3ConnectStep) object;

		putAttribute(STEP_KEY, step);
	}

	/**
	 * This method is used to write out the XML for the step we are controlling.
	 * 
	 * @param out
	 *           the print writer to write to.
	 */
	@Override
	public void writeSubXML(PrintWriter out)
	{
		((POP3ConnectStep) getAttribute(STEP_KEY)).writeSubXML(out);
	}
}
