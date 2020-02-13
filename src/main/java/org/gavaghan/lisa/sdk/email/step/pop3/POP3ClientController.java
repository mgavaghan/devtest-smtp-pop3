package org.gavaghan.lisa.sdk.email.step.pop3;

import java.io.PrintWriter;

import javax.swing.Icon;

import com.itko.lisa.core.ModuleLegacy;
import com.itko.lisa.editor.TestNodeInfo;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class POP3ClientController extends TestNodeInfo
{
	static final String STEP_KEY = "lisa.POP3Step.key";

	@Override
	public void initNewOne()
	{
		POP3ClientStep node = new POP3ClientStep();
		putAttribute(STEP_KEY, node);
	}

	@Override
	public Icon getLargeIcon()
	{
		return ModuleLegacy.resources.getIcon("icon.tctree.ftpnode.lg", new Object[0]);
	}

	@Override
	public Icon getSmallIcon()
	{
		return ModuleLegacy.resources.getIcon("icon.tctree.ftpnode", new Object[0]);
	}

	@Override
	public void writeSubXML(PrintWriter pw)
	{
		POP3ClientStep node = (POP3ClientStep) getAttribute(STEP_KEY);
		node.writeSubXML(pw);
	}

	@Override
	public void migrate(Object obj)
	{
		POP3ClientStep node = (POP3ClientStep) obj;
		putAttribute(STEP_KEY, node);
	}

	@Override
	public String getEditorName()
	{
		return "POP3 Client";
	}

	/**
	 */
	@Override
	public String getHelpString()
	{
		return "POP3 Client";
	}
}
