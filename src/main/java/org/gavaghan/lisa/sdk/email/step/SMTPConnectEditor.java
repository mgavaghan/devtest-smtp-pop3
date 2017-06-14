package org.gavaghan.lisa.sdk.email.step;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPConnectEditor extends MailConnectEditor
{
	@Override
	protected String getLabel()
	{
		return "SMTP Server Address: ";
	}
	
	@Override
	public String isEditorValid()
	{
		if (mServerAddress.getText().trim().length() == 0) return "Please specify an SMTP server address";
		return null;
	}

	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		SMTPConnectController controller = (SMTPConnectController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		SMTPConnectStep step = (SMTPConnectStep) controller.getAttribute(SMTPConnectController.STEP_KEY);

		step.setServerAddress(getServerAddress().getText());
		step.setSSL(getSSL().isSelected());
	}

	/**
	 * Render the GUI.
	 */
	@Override
	public void display()
	{
		setupEditor();

		SMTPConnectController controller = (SMTPConnectController) getController();
		SMTPConnectStep step = (SMTPConnectStep) controller.getAttribute(SMTPConnectController.STEP_KEY);

		getServerAddress().setText(step.getServerAddress());
		getSSL().setSelected(step.getSSL());
	}
}
