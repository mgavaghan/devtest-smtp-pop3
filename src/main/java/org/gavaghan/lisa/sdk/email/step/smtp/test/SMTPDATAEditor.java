package org.gavaghan.lisa.sdk.email.step.smtp.test;

import org.gavaghan.devtest.autostep.AutoEditor;

/**
 * FIXME use scroll panes
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SMTPDATAEditor extends AutoEditor<SMTPDATAStep>
{
   public SMTPDATAEditor()
   {
      super(SMTPDATAStep.class);
   }

   /**
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
   */
}
