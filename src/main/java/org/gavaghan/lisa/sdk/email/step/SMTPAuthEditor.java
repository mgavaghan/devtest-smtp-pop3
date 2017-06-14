package org.gavaghan.lisa.sdk.email.step;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.itko.lisa.editor.CustomEditor;

/**
 * SMTP AUTH PLAIN Editor.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPAuthEditor extends CustomEditor
{
	/** Initialized flag. */
	private boolean mInit = false;

	/** Username and password. */
	private JTextField mUsername = new JTextField();
	private JTextField mPassword = new JTextField();

	@Override
	public String isEditorValid()
	{
		if (mUsername.getText().trim().length() == 0) return "Please specify a username";
		if (mPassword.getText().trim().length() == 0) return "Please specify a password";
		return null;
	}

	public JTextField getUsername()
	{
		return mUsername;
	}

	public JTextField getPassword()
	{
		return mPassword;
	}
	
	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		SMTPAuthPLAINController controller = (SMTPAuthPLAINController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		SMTPAuthPLAINStep step = (SMTPAuthPLAINStep) controller.getAttribute(SMTPAuthPLAINController.STEP_KEY);

		step.setUsername(getUsername().getText());
		step.setPassword(getPassword().getText());
	}

	/**
	 * Render the GUI.
	 */
	@Override
	public void display()
	{
		setupEditor();

		SMTPAuthPLAINController controller = (SMTPAuthPLAINController) getController();
		SMTPAuthPLAINStep step = (SMTPAuthPLAINStep) controller.getAttribute(SMTPAuthPLAINController.STEP_KEY);

		getUsername().setText(step.getUsername());
		getPassword().setText(step.getPassword());
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
		setMinimumSize(new Dimension(300,300));

		// add request label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Username: "), gbc);

		// add TEXT FIELD to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mUsername, gbc);

		// add request label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Password: "), gbc);

		// add TEXT FIELD to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mPassword, gbc);

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
