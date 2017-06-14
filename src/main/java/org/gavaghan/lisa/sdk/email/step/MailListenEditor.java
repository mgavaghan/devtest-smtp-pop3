package org.gavaghan.lisa.sdk.email.step;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.itko.lisa.editor.CustomEditor;

/**
 * Base implementation of mail server listen editors.
 * 
 * @author mike@gavaghan.org
 */
public abstract class MailListenEditor extends CustomEditor
{
	/** Initialized flag. */
	private boolean mInit = false;

	/** TextField for listen port. */
	private JTextField mListenPort = new JTextField();

	protected JTextField getListenPort()
	{
		return mListenPort;
	}
	
	@Override
	public String isEditorValid()
	{
		if (mListenPort == null) return "Please provide a listen port.";
		return null;
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

		// add listen port to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Listen Port: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mListenPort, gbc);

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
