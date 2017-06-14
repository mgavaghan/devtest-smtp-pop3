package org.gavaghan.lisa.sdk.email.step;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.itko.lisa.editor.CustomEditor;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class MailClientIteratorEditor extends CustomEditor
{
	/** Initialized flag. */
	private boolean mInit = false;

	/** Server address. */
	protected JTextField mTestCommands = new JTextField();

	@Override
	public String isEditorValid()
	{
		if (mTestCommands.getText().trim().length() == 0) return "Please specify test commands";
		return null;
	}

	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		MailClientIteratorController controller = (MailClientIteratorController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		MailClientIteratorStep step = (MailClientIteratorStep) controller.getAttribute(MailClientIteratorController.STEP_KEY);

		step.setCommands(mTestCommands.getText());
	}

	/**
	 * Render the GUI.
	 */
	@Override
	public void display()
	{
		setupEditor();

		MailClientIteratorController controller = (MailClientIteratorController) getController();
		MailClientIteratorStep step = (MailClientIteratorStep) controller.getAttribute(MailClientIteratorController.STEP_KEY);

		mTestCommands.setText(step.getCommands());
	}

	/**
	 * 
	 */
	protected void setupEditor()
	{
		if (mInit)  return;
		
		mInit = true;

		GridBagConstraints gbc;

		// build the main editor panel
		JPanel mainPanel = new JPanel(new GridBagLayout());

		// add server address to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Test Commands"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mTestCommands, gbc);

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
