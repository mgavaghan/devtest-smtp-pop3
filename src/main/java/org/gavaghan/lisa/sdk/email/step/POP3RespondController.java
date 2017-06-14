package org.gavaghan.lisa.sdk.email.step;

import java.io.PrintWriter;

import javax.swing.Icon;

import com.itko.lisa.core.ModuleLegacy;
import com.itko.lisa.editor.TestNodeInfo;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class POP3RespondController extends TestNodeInfo
{
	public static final String STEP_INSTANCE_KEY = POP3RespondStep.class.getName() + ".key";

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
		return "POP3 Respond Editor";
	}

	@Override
	public String getHelpString()
	{
		return "POP3 Responder";
	}

	/**
	 * This method is used to create a new step instance.
	 */
	@Override
	public void initNewOne()
	{
		if (getAttribute(STEP_INSTANCE_KEY) == null)
		{
			POP3RespondStep step = new POP3RespondStep();

			setThinkTime(0, 0);
			setNextNode(this);

			putAttribute(STEP_INSTANCE_KEY, step);
		}
	}

	/**
	 * This method is used to attach a created step object to our attribute set.
	 * 
	 * @param object
	 */
	@Override
	public void migrate(Object object)
	{
		POP3RespondStep step = (POP3RespondStep) object;

		putAttribute(STEP_INSTANCE_KEY, step);
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
		((POP3RespondStep) getAttribute(STEP_INSTANCE_KEY)).writeSubXML(out);
	}
}
