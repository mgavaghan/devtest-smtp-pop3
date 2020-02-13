package org.gavaghan.lisa.sdk.email.step.smtp;

import org.gavaghan.lisa.sdk.email.step.MailCientEditor;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPClientEditor extends MailCientEditor
{
	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		SMTPClientController controller = (SMTPClientController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		SMTPClientStep step = (SMTPClientStep) controller.getAttribute(SMTPClientController.STEP_KEY);

		step.setCommand(getCommand().getText());
	}

	/**
	 * Render the GUI.
	 */
	@Override
	public void display()
	{
		setupEditor();

		SMTPClientController controller = (SMTPClientController) getController();
		SMTPClientStep step = (SMTPClientStep) controller.getAttribute(SMTPClientController.STEP_KEY);

		getCommand().setText(step.getCommand());
	}
}
