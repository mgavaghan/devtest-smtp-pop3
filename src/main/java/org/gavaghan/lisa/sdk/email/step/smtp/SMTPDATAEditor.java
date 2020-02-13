package org.gavaghan.lisa.sdk.email.step.smtp;

import java.awt.Dimension;
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
public class SMTPDATAEditor extends CustomEditor
{
   /** Initialized flag. */
   private boolean mInit = false;

   /** Command. */
   private JTextField mCommand = new JTextField();

   @Override
   public String isEditorValid()
   {
      if (mCommand.getText().trim().length() == 0) return "Please specify a command";
      return null;
   }

   public JTextField getCommand()
   {
      return mCommand;
   }
   
	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		SMTPDATAController controller = (SMTPDATAController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		SMTPDATAStep step = (SMTPDATAStep) controller.getAttribute(SMTPDATAController.STEP_KEY);

		step.setCommand(getCommand().getText());
	}

	/**
	 * Render the GUI.
	 */
	@Override
	public void display()
	{
		setupEditor();

		SMTPDATAController controller = (SMTPDATAController) getController();
		SMTPDATAStep step = (SMTPDATAStep) controller.getAttribute(SMTPDATAController.STEP_KEY);

		getCommand().setText(step.getCommand());
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
      mainPanel.add(new JLabel("Command: "), gbc);

      // add TEXT FIELD to main panel
      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbc.gridwidth = 1;
      gbc.weightx = 1;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      mainPanel.add(mCommand, gbc);

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
