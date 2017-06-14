package org.gavaghan.lisa.sdk.email.tph;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itko.lisa.gui.WizardStep;
import com.itko.util.swing.TextMessage;

/**
 * Specify recording parameters.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class MailConnectionPanel extends WizardStep
{
	/** Our logger. */
	static private Log LOG = LogFactory.getLog(MailConnectionPanel.class);

	/** This defines the header message for this step. */
	private final TextMessage mHeader;

	/** The protocol handler to configure. */
	private final MailProtocolHandler mProtocolHandler;

	/**
	 * This attribute holds a reference to the component for specifying our
	 * listening port.
	 */
	private final JTextField mListenPort = new JTextField();

	/**
	 * This attribute holds a reference to the component for specifying our
	 * target host.
	 */
	private final JTextField mTargetHost = new JTextField();

	/**
	 * This attribute holds a reference to the component for specifying our
	 * target port.
	 */
	private final JTextField mTargetPort = new JTextField();

	/**
	 * This attribute holds a reference to the component for specifying whether
	 * SSL to the server will be used.
	 */
	private final JCheckBox mSSLtoServer = new JCheckBox("SSL to Server");

	/**
	 * This is the only constructor for the <code>SMTPConnectionPanel</code>
	 * class.
	 * 
	 * @param protocolHandler
	 *           the protocolHandler we belong to.
	 */
	MailConnectionPanel(MailProtocolHandler protocolHandler, String header)
	{
		mHeader = new TextMessage(header, true);
		mHeader.setThickerBorder(2);
		
		mProtocolHandler = protocolHandler;
		GridBagConstraints gbc;

		// build the main editor panel
		JPanel mainPanel = new JPanel(new GridBagLayout());

		// add listen port to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 3, 0, 0);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Listen Port: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 0, 3);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mListenPort, gbc);

		// add target host to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 3, 0, 0);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Target Host: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 0, 3);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mTargetHost, gbc);

		// add listen port to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 3, 0, 0);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Target Port: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 0, 3);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mTargetPort, gbc);

		// add SSL to server to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 3, 0, 0);
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mSSLtoServer, gbc);

		// add main panel to editor
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(mainPanel, gbc);
	}

	/**
	 * Populate the UI from settings.
	 */
	@Override
	public void activate()
	{
		String listenPort = mProtocolHandler.getListenPort();
		String targetPort = mProtocolHandler.getTargetPort();
		boolean ssl = mProtocolHandler.getSSLtoServer();

		mListenPort.setText(listenPort);
		mTargetHost.setText(mProtocolHandler.getTargetHost());
		mTargetPort.setText(targetPort);
		mSSLtoServer.setSelected(ssl);
	}

	@Override
	public Component getHeaderComponent()
	{
		return mHeader.getComponent();
	}

	/**
	 * Save settings to the protocol handler.
	 */
	@Override
	public boolean save()
	{
		LOG.debug("ENTER: MailConnectionPanel.save()");

		mProtocolHandler.setListenPort(mListenPort.getText());
		mProtocolHandler.setTargetHost(mTargetHost.getText());
		mProtocolHandler.setTargetPort(mTargetPort.getText());
		mProtocolHandler.setSSLtoServer(mSSLtoServer.isSelected());

		LOG.debug("EXIT: MailConnectionPanel.save()");
		return true;
	}
}
