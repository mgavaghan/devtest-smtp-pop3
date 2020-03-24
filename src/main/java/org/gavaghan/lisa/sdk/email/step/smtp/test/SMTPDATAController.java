package org.gavaghan.lisa.sdk.email.step.smtp.test;

import java.io.PrintWriter;

import javax.swing.Icon;

import com.itko.lisa.core.ModuleLegacy;
import com.itko.lisa.editor.TestNodeInfo;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPDATAController extends TestNodeInfo
{
   public static final String STEP_KEY = SMTPDATAController.class.getName() + ".key";

	@Override
	public void initNewOne()
	{
		SMTPDATAStep node = new SMTPDATAStep();
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
		SMTPDATAStep node = (SMTPDATAStep) getAttribute(STEP_KEY);
		node.writeSubXML(pw);
	}

	@Override
	public void migrate(Object obj)
	{
		SMTPDATAStep node = (SMTPDATAStep) obj;
		putAttribute(STEP_KEY, node);
	}

	@Override
	public String getEditorName()
	{
		return "SMTP DATA";
	}

	/**
	 */
	@Override
	public String getHelpString()
	{
		return "SMTP DATA";
	}
}
