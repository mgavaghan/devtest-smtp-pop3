package org.gavaghan.lisa.sdk.email.step.smtp.vse;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import com.itko.lisa.editor.CustomEditor;

/**
 * Editor for SMTPRespondStep.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPRespondEditor extends CustomEditor
{
	/** Initialized flag. */
	private boolean mInit = false;

	@Override
	public String isEditorValid()
	{
		return null;
	}

	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		//SMTPRespondController controller = (SMTPRespondController) getController();
		//controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		//SMTPRespondStep step = (SMTPRespondStep) controller.getAttribute(SMTPRespondController.STEP_INSTANCE_KEY);
	}

	@Override
	public void display()
	{
		if (!mInit) setupEditor();

		//SMTPRespondController controller = (SMTPRespondController) getController();
		//SMTPRespondStep step = (SMTPRespondStep) controller.getAttribute(SMTPRespondController.STEP_INSTANCE_KEY);
	}

	/**
	 * 
	 */
	private void setupEditor()
	{
		mInit = true;

		GridBagConstraints gbc;

		// build the main editor panel
		JPanel mainPanel = new JPanel(new GridBagLayout());

		// add main panel to editor
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(mainPanel, gbc);
	}
}
