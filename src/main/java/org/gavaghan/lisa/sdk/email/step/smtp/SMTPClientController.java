package org.gavaghan.lisa.sdk.email.step.smtp;

import java.io.PrintWriter;

import javax.swing.Icon;

import com.itko.lisa.core.ModuleLegacy;
import com.itko.lisa.editor.TestNodeInfo;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPClientController extends TestNodeInfo
{
	static final String STEP_KEY = "lisa.SMTPStep.key";

	@Override
	public void initNewOne()
	{
		SMTPClientStep node = new SMTPClientStep();
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
		SMTPClientStep node = (SMTPClientStep) getAttribute(STEP_KEY);
		node.writeSubXML(pw);
	}

	@Override
	public void migrate(Object obj)
	{
		SMTPClientStep node = (SMTPClientStep) obj;
		putAttribute(STEP_KEY, node);
	}

	@Override
	public String getEditorName()
	{
		return "SMTP Client";
	}

	/**
	 */
	@Override
	public String getHelpString()
	{
		return "SMTP Client";
	}
}
