package org.gavaghan.lisa.sdk.email.step.pop3;

import org.gavaghan.lisa.sdk.email.step.MailConnectEditor;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class POP3ConnectEditor extends MailConnectEditor
{
	@Override
	protected String getLabel()
	{
		return "POP3 Server Address: ";
	}
	
	@Override
	public String isEditorValid()
	{
		if (mServerAddress.getText().trim().length() == 0) return "Please specify a POP3 server address";
		return null;
	}

	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		POP3ConnectController controller = (POP3ConnectController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		POP3ConnectStep step = (POP3ConnectStep) controller.getAttribute(POP3ConnectController.STEP_KEY);

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

		POP3ConnectController controller = (POP3ConnectController) getController();
		POP3ConnectStep step = (POP3ConnectStep) controller.getAttribute(POP3ConnectController.STEP_KEY);

		getServerAddress().setText(step.getServerAddress());
		getSSL().setSelected(step.getSSL());
	}
}
