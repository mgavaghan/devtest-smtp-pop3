package org.gavaghan.lisa.sdk.email.step;


/**
 * Editor for POP3ListenStep.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class POP3ListenEditor extends MailListenEditor
{
	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		POP3ListenController controller = (POP3ListenController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		POP3ListenStep step = (POP3ListenStep) controller.getAttribute(POP3ListenController.STEP_INSTANCE_KEY);

		step.setListenPort(getListenPort().getText());
	}

	@Override
	public void display()
	{
		setupEditor();

		POP3ListenController controller = (POP3ListenController) getController();
		POP3ListenStep step = (POP3ListenStep) controller.getAttribute(POP3ListenController.STEP_INSTANCE_KEY);

		getListenPort().setText(step.getListenPort());
	}
}
