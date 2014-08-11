
package weshampson.timekeeper.gui;

import java.io.File;
import javax.swing.JFileChooser;
import weshampson.timekeeper.settings.SettingsManager;
import static weshampson.timekeeper.settings.SettingsManager.*;

/**
 *
 * @author  Wes Hampson
 * @version 0.2.0 (Aug 4, 2014)
 * @since   0.2.0 (Jul 30, 2014)
 */
public class SettingsDialog extends javax.swing.JDialog {

    /** Creates new form SettingsDialog */
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadCurrentSettings();
    }
    private void loadCurrentSettings() {
        settingsTechDataFileTextField.setText(SettingsManager.get(PROPERTY_TECH_DATA_FILE));
        settingsSignoutDataFileTextField.setText(SettingsManager.get(PROPERTY_SIGNOUT_DATA_FILE));
        settingsAdminApprovalCheckbox.setSelected(Boolean.parseBoolean(SettingsManager.get(PROPERTY_ADMIN_APPROVAL_ENABLED)));
    }
    private void loadDefaultSettings() {
        settingsTechDataFileTextField.setText(SettingsManager.getDefault(PROPERTY_TECH_DATA_FILE));
        settingsSignoutDataFileTextField.setText(SettingsManager.getDefault(PROPERTY_SIGNOUT_DATA_FILE));
        settingsAdminApprovalCheckbox.setSelected(Boolean.parseBoolean(SettingsManager.getDefault(PROPERTY_ADMIN_APPROVAL_ENABLED)));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        settingsDataFilesPanel = new javax.swing.JPanel();
        settingsTechDataFileLabel = new javax.swing.JLabel();
        settingsTechDataFileTextField = new javax.swing.JTextField();
        settingsTechDataFileBrowseButton = new javax.swing.JButton();
        settingsSignoutDataFileLabel = new javax.swing.JLabel();
        settingsSignoutDataFileTextField = new javax.swing.JTextField();
        settingsSignoutDataFileBrowseButton = new javax.swing.JButton();
        settingsSignoutsPanel = new javax.swing.JPanel();
        settingsAdminApprovalCheckbox = new javax.swing.JCheckBox();
        settingsSetAdminPasswordButton = new javax.swing.JButton();
        settingsRestoreDefaultsButton = new javax.swing.JButton();
        settingsOKButton = new javax.swing.JButton();
        settingsCancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Settings");

        settingsDataFilesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Data files"));

        settingsTechDataFileLabel.setText("Tech data file:");

        settingsTechDataFileBrowseButton.setText("...");
        settingsTechDataFileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsTechDataFileBrowseButtonActionPerformed(evt);
            }
        });

        settingsSignoutDataFileLabel.setText("Signout data file:");

        settingsSignoutDataFileBrowseButton.setText("...");
        settingsSignoutDataFileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsSignoutDataFileBrowseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout settingsDataFilesPanelLayout = new javax.swing.GroupLayout(settingsDataFilesPanel);
        settingsDataFilesPanel.setLayout(settingsDataFilesPanelLayout);
        settingsDataFilesPanelLayout.setHorizontalGroup(
            settingsDataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsDataFilesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsDataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(settingsSignoutDataFileLabel)
                    .addComponent(settingsTechDataFileLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsDataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(settingsSignoutDataFileTextField)
                    .addComponent(settingsTechDataFileTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsDataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(settingsSignoutDataFileBrowseButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsTechDataFileBrowseButton, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        settingsDataFilesPanelLayout.setVerticalGroup(
            settingsDataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsDataFilesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsDataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(settingsTechDataFileLabel)
                    .addComponent(settingsTechDataFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsTechDataFileBrowseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsDataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(settingsSignoutDataFileLabel)
                    .addComponent(settingsSignoutDataFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsSignoutDataFileBrowseButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsSignoutsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Admin Approval"));

        settingsAdminApprovalCheckbox.setText("Enable Admin Approval");

        settingsSetAdminPasswordButton.setText("Set Admin Password");
        settingsSetAdminPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsSetAdminPasswordButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout settingsSignoutsPanelLayout = new javax.swing.GroupLayout(settingsSignoutsPanel);
        settingsSignoutsPanel.setLayout(settingsSignoutsPanelLayout);
        settingsSignoutsPanelLayout.setHorizontalGroup(
            settingsSignoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsSignoutsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsSignoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(settingsAdminApprovalCheckbox)
                    .addComponent(settingsSetAdminPasswordButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        settingsSignoutsPanelLayout.setVerticalGroup(
            settingsSignoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsSignoutsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsAdminApprovalCheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsSetAdminPasswordButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsRestoreDefaultsButton.setText("Restore Defaults");
        settingsRestoreDefaultsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsRestoreDefaultsButtonActionPerformed(evt);
            }
        });

        settingsOKButton.setText("OK");
        settingsOKButton.setMaximumSize(new java.awt.Dimension(65, 23));
        settingsOKButton.setMinimumSize(new java.awt.Dimension(65, 23));
        settingsOKButton.setPreferredSize(new java.awt.Dimension(65, 23));
        settingsOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsOKButtonActionPerformed(evt);
            }
        });

        settingsCancelButton.setText("Cancel");
        settingsCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsCancelButtonActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Something Else"));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(settingsDataFilesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsRestoreDefaultsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                .addComponent(settingsOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsCancelButton)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(settingsSignoutsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(settingsDataFilesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(settingsSignoutsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(settingsOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsCancelButton)
                    .addComponent(settingsRestoreDefaultsButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void settingsTechDataFileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsTechDataFileBrowseButtonActionPerformed
        JFileChooser chooser = new JFileChooser(SettingsManager.getDefault(PROPERTY_TECH_DATA_FILE));
        chooser.setDialogTitle("Tech Data File");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = chooser.getSelectedFile();
        settingsTechDataFileTextField.setText(f.getAbsolutePath());
    }//GEN-LAST:event_settingsTechDataFileBrowseButtonActionPerformed

    private void settingsSignoutDataFileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsSignoutDataFileBrowseButtonActionPerformed
        JFileChooser chooser = new JFileChooser(SettingsManager.getDefault(PROPERTY_SIGNOUT_DATA_FILE));
        chooser.setDialogTitle("Signout Data File");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = chooser.getSelectedFile();
        settingsSignoutDataFileTextField.setText(f.getAbsolutePath());
    }//GEN-LAST:event_settingsSignoutDataFileBrowseButtonActionPerformed

    private void settingsSetAdminPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsSetAdminPasswordButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_settingsSetAdminPasswordButtonActionPerformed

    private void settingsRestoreDefaultsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsRestoreDefaultsButtonActionPerformed
        loadDefaultSettings();
    }//GEN-LAST:event_settingsRestoreDefaultsButtonActionPerformed

    private void settingsOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsOKButtonActionPerformed
        SettingsManager.set(PROPERTY_TECH_DATA_FILE, settingsTechDataFileTextField.getText());
        SettingsManager.set(PROPERTY_SIGNOUT_DATA_FILE, settingsSignoutDataFileTextField.getText());
        SettingsManager.set(PROPERTY_ADMIN_APPROVAL_ENABLED, Boolean.toString(settingsAdminApprovalCheckbox.isSelected()));
        dispose();
    }//GEN-LAST:event_settingsOKButtonActionPerformed

    private void settingsCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsCancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_settingsCancelButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox settingsAdminApprovalCheckbox;
    private javax.swing.JButton settingsCancelButton;
    private javax.swing.JPanel settingsDataFilesPanel;
    private javax.swing.JButton settingsOKButton;
    private javax.swing.JButton settingsRestoreDefaultsButton;
    private javax.swing.JButton settingsSetAdminPasswordButton;
    private javax.swing.JButton settingsSignoutDataFileBrowseButton;
    private javax.swing.JLabel settingsSignoutDataFileLabel;
    private javax.swing.JTextField settingsSignoutDataFileTextField;
    private javax.swing.JPanel settingsSignoutsPanel;
    private javax.swing.JButton settingsTechDataFileBrowseButton;
    private javax.swing.JLabel settingsTechDataFileLabel;
    private javax.swing.JTextField settingsTechDataFileTextField;
    // End of variables declaration//GEN-END:variables
}