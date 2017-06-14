package org.gavaghan.lisa.sdk.email.step;

import java.io.PrintWriter;

import javax.swing.Icon;

import com.itko.lisa.core.ModuleLegacy;
import com.itko.lisa.editor.TestNodeInfo;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class MailClientIteratorController extends TestNodeInfo
{
	static final String STEP_KEY = "lisa.POP3Step.key";

	@Override
	public void initNewOne()
	{
		MailClientIteratorStep node = new MailClientIteratorStep();
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
		MailClientIteratorStep node = (MailClientIteratorStep) getAttribute(STEP_KEY);
		node.writeSubXML(pw);
	}

	@Override
	public void migrate(Object obj)
	{
		MailClientIteratorStep node = (MailClientIteratorStep) obj;
		putAttribute(STEP_KEY, node);
	}

	@Override
	public String getEditorName()
	{
		return "Mail Client Iterator";
	}

	/**
	 */
	@Override
	public String getHelpString()
	{
		return "Mail Client Iterator";
	}
}
