package org.gavaghan.lisa.sdk.email.step.smtp;

import java.io.PrintWriter;

import javax.swing.Icon;

import com.itko.lisa.core.ModuleLegacy;
import com.itko.lisa.editor.TestNodeInfo;

/**
 * SMTP AUTH PLAIN Controller.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPAuthPLAINController extends TestNodeInfo
{
	static final String STEP_KEY = "lisa.SMTPAuthPlain.key";

	@Override
	public void initNewOne()
	{
		SMTPAuthPLAINStep node = new SMTPAuthPLAINStep();
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
		SMTPAuthPLAINStep node = (SMTPAuthPLAINStep) getAttribute(STEP_KEY);
		node.writeSubXML(pw);
	}

	@Override
	public void migrate(Object obj)
	{
		SMTPAuthPLAINStep node = (SMTPAuthPLAINStep) obj;
		putAttribute(STEP_KEY, node);
	}

	@Override
	public String getEditorName()
	{
		return "SMTP AUTH PLAIN";
	}

	/**
	 */
	@Override
	public String getHelpString()
	{
		return "SMTP AUTH PLAIN";
	}

}
