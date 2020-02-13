package org.gavaghan.lisa.sdk.email.step.pop3;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import com.itko.lisa.editor.CustomEditor;

/**
 * Editor for POP3RespondStep.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class POP3RespondEditor extends CustomEditor
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
		//POP3RespondController controller = (POP3RespondController) getController();
		//controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		//POP3RespondStep step = (POP3RespondStep) controller.getAttribute(POP3RespondController.STEP_INSTANCE_KEY);
	}

	@Override
	public void display()
	{
		if (!mInit) setupEditor();

		//POP3RespondController controller = (POP3RespondController) getController();
		//POP3RespondStep step = (POP3RespondStep) controller.getAttribute(POP3RespondController.STEP_INSTANCE_KEY);
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
