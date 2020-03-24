package org.gavaghan.lisa.sdk.email.step.smtp.test;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.itko.lisa.editor.CustomEditor;

/**
 * FIXME use scroll panes
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPDATAEditor extends CustomEditor
{
   /** Initialized flag. */
   private boolean mInit = false;

   /** Command. */
   private JTextArea mHeaders = new JTextArea(10, 10);

   /** Command. */
   private JTextArea mBody = new JTextArea();

   @Override
   public String isEditorValid()
   {
      if (mHeaders.getText().trim().length() == 0) return "Please specify the headers";
      if (mBody.getText().trim().length() == 0) return "Please specify the body";
      return null;
   }

   public JTextArea getHeaders()
   {
      return mHeaders;
   }

   public JTextArea getBody()
   {
      return mBody;
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

		step.setHeaders(getHeaders().getText());
      step.setBody(getBody().getText());
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

		getHeaders().setText(step.getHeaders());
      getBody().setText(step.getBody());
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

      // add headers label
      mHeaders.setMinimumSize(new Dimension(10,30));
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 1;
      gbc.weightx = 1;
      gbc.weighty = 1;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      mainPanel.add(new JLabel("Headers: "), gbc);

      // add TEXT AREA to main panel
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.gridwidth = 1;
      gbc.weightx = 1;
      gbc.weighty = 1;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.BOTH;
      mainPanel.add(mHeaders, gbc);

      // add body label
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.gridwidth = 1;
      gbc.weightx = 1;
      gbc.weighty = 1;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.BOTH;
      mainPanel.add(new JLabel("Body: "), gbc);

      // add TEXT AREA to main panel
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbc.gridwidth = 1;
      gbc.weightx = 1;
      gbc.weighty = 1;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.BOTH;
      mainPanel.add(mBody, gbc);

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
