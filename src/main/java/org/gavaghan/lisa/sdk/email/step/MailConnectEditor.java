package org.gavaghan.lisa.sdk.email.step;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.itko.lisa.editor.CustomEditor;

/**
 * Base implementation of mail server connect editors.
 * 
 * @author mike@gavaghan.org
 */
public abstract class MailConnectEditor extends CustomEditor
{
	/** Initialized flag. */
	private boolean mInit = false;

	/** Server address. */
	protected JTextField mServerAddress = new JTextField();
	
	/** SSL flag. */
	private JCheckBox mSSL = new JCheckBox("Use SSL");
	
	protected abstract String getLabel();

	public JTextField getServerAddress()
	{
		return mServerAddress;
	}

	public JCheckBox getSSL()
	{
		return mSSL;
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
		mainPanel.add(new JLabel(getLabel()), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mServerAddress, gbc);

		// add SSL checkbox
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mSSL, gbc);

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
