package org.gavaghan.lisa.sdk.email.step;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class POP3ClientEditor extends MailCientEditor
{
	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		POP3ClientController controller = (POP3ClientController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		POP3ClientStep step = (POP3ClientStep) controller.getAttribute(POP3ClientController.STEP_KEY);

		step.setCommand(getCommand().getText());
	}

	/**
	 * Render the GUI.
	 */
	@Override
	public void display()
	{
		setupEditor();

		POP3ClientController controller = (POP3ClientController) getController();
		POP3ClientStep step = (POP3ClientStep) controller.getAttribute(POP3ClientController.STEP_KEY);

		getCommand().setText(step.getCommand());
	}
}
