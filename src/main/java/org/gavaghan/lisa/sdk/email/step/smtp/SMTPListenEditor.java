package org.gavaghan.lisa.sdk.email.step.smtp;

import org.gavaghan.lisa.sdk.email.step.MailListenEditor;

/**
 * Editor for SMTPListenStep.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPListenEditor extends MailListenEditor
{
	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		SMTPListenController controller = (SMTPListenController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		SMTPListenStep step = (SMTPListenStep) controller.getAttribute(SMTPListenController.STEP_INSTANCE_KEY);

		step.setListenPort(getListenPort().getText());
	}

	@Override
	public void display()
	{
		setupEditor();

		SMTPListenController controller = (SMTPListenController) getController();
		SMTPListenStep step = (SMTPListenStep) controller.getAttribute(SMTPListenController.STEP_INSTANCE_KEY);

		getListenPort().setText(step.getListenPort());
	}
}
