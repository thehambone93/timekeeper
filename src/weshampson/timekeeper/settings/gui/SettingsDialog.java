
package weshampson.timekeeper.settings.gui;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import weshampson.commonutils.logging.Level;
import weshampson.commonutils.logging.Logger;
import weshampson.commonutils.updater.UpdaterSettingsPanel;
import weshampson.timekeeper.fileops.FileOps;
import weshampson.timekeeper.admin.gui.AdminManagerDialog;
import weshampson.timekeeper.admin.gui.AdminPasswordDialog;
import weshampson.timekeeper.settings.SettingsManager;
import static weshampson.timekeeper.settings.SettingsManager.*;

/**
 *
 * @author  Wes Hampson
 * @version 0.3.0 (Nov 23, 2014)
 * @since   0.2.0 (Jul 30, 2014)
 */
public class SettingsDialog extends javax.swing.JDialog {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private final UpdaterSettingsPanel updaterSettingsPanel;
    private boolean passwordChanged;
    private String newAdminPasswordHash;
    private MessageDigest messageDigest;
    private String existingAdminPasswordHash;

    /** Creates new form SettingsDialog */
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initLateSignoutComboBoxes();
        loadCurrentSettings();
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            // Shouldn't happen
            Logger.log(Level.ERROR, ex, "Unable to find password hash algorithm - " + ex.toString());
        }
        changeAdminPasswordButton.setEnabled(adminApprovalEnabledCheckbox.isSelected());
        manageAdminsButton.setEnabled(adminApprovalEnabledCheckbox.isSelected());
        updaterSettingsPanel = new UpdaterSettingsPanel(new File(SettingsManager.get(PROPERTY_UPDATER_CONFIGURATION_DATA_FILE)));
        tabbedPane.addTab("Updater", updaterSettingsPanel);
        pack();
    }
    @SuppressWarnings("unchecked")
    private void initLateSignoutComboBoxes() {
        hourComboBox.removeAllItems();
        minuteComboBox.removeAllItems();
        periodComboBox.removeAllItems();
        for (int i = 0; i < 12; i++) {
            hourComboBox.addItem(String.format("%d", i + 1));
        }
        for (int i = 0; i < 60; i++) {
            minuteComboBox.addItem(String.format("%02d", i));
        }
        periodComboBox.addItem("AM");
        periodComboBox.addItem("PM");
    }
    private void loadCurrentSettings() {
        techDataFileTextField.setText(SettingsManager.get(PROPERTY_TECH_DATA_FILE));
        signoutDataFileTextField.setText(SettingsManager.get(PROPERTY_SIGNOUT_DATA_FILE));
        activityLogDirTextField.setText(SettingsManager.get(PROPERTY_ACTIVITY_LOG_DIR));
        updaterConfigFileTextField.setText(SettingsManager.get(PROPERTY_UPDATER_CONFIGURATION_DATA_FILE));
        adminApprovalEnabledCheckbox.setSelected(Boolean.parseBoolean(SettingsManager.get(PROPERTY_ADMIN_APPROVAL_ENABLED)));
        logTechsOutAtMidnightCheckbox.setSelected(Boolean.parseBoolean(SettingsManager.get(PROPERTY_AUTO_OUT_AT_MIDNIGHT)));
        existingAdminPasswordHash = SettingsManager.get(PROPERTY_ADMIN_PASSWORD);
        try {
            Date lateSignoutTime = new SimpleDateFormat(SettingsManager.get(PROPERTY_LATE_SIGNOUT_TIME_FORMAT)).parse(SettingsManager.get(PROPERTY_LATE_SIGNOUT_TIME));
            setLateSignoutTime(lateSignoutTime);
        } catch (ParseException ex) {
            Logger.log(Level.ERROR, ex, "Failed to parse late signout date - " + ex.toString());
        }
    }
    private void loadDefaultSettings() {
        techDataFileTextField.setText(SettingsManager.getDefault(PROPERTY_TECH_DATA_FILE));
        signoutDataFileTextField.setText(SettingsManager.getDefault(PROPERTY_SIGNOUT_DATA_FILE));
        activityLogDirTextField.setText(SettingsManager.getDefault(PROPERTY_ACTIVITY_LOG_DIR));
        updaterConfigFileTextField.setText(SettingsManager.getDefault(PROPERTY_UPDATER_CONFIGURATION_DATA_FILE));
        adminApprovalEnabledCheckbox.setSelected(Boolean.parseBoolean(SettingsManager.getDefault(PROPERTY_ADMIN_APPROVAL_ENABLED)));
        logTechsOutAtMidnightCheckbox.setSelected(Boolean.parseBoolean(SettingsManager.getDefault(PROPERTY_AUTO_OUT_AT_MIDNIGHT)));
        try {
            Date lateSignoutTime = new SimpleDateFormat(SettingsManager.getDefault(PROPERTY_LATE_SIGNOUT_TIME_FORMAT)).parse(SettingsManager.getDefault(PROPERTY_LATE_SIGNOUT_TIME));
            setLateSignoutTime(lateSignoutTime);
        } catch (ParseException ex) {
            Logger.log(Level.ERROR, ex, "Failed to parse late signout date - " + ex.toString());
        }
    }
    private void setLateSignoutTime(Date time) {
        hourComboBox.setSelectedItem(new SimpleDateFormat("h").format(time));
        minuteComboBox.setSelectedItem(new SimpleDateFormat("mm").format(time));
        periodComboBox.setSelectedItem(new SimpleDateFormat("a").format(time));
    }
    private String getLateSignoutTime() {
        return(hourComboBox.getSelectedItem() + ":" + minuteComboBox.getSelectedItem() + " " + periodComboBox.getSelectedItem());
    }
    private String bytesToHexString(byte[] b) {
        char[] hexChars = new char[b.length * 2];
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return(new String(hexChars));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setAdminPasswordDialog = new javax.swing.JDialog();
        setAdminPasswordNewPasswordField = new javax.swing.JPasswordField();
        setAdminPasswordNewPasswordLabel = new javax.swing.JLabel();
        setAdminPasswordConfirmPasswordField = new javax.swing.JPasswordField();
        setAdminPasswordConfirmPasswordLabel = new javax.swing.JLabel();
        setAdminPasswordCancelButton = new javax.swing.JButton();
        setAdminPasswordOKButton = new javax.swing.JButton();
        changeAdminPasswordDialog = new javax.swing.JDialog();
        changeAdminPasswordOldPasswordLabel = new javax.swing.JLabel();
        changeAdminPasswordOldPasswordField = new javax.swing.JPasswordField();
        changeAdminPasswordNewPasswordLabel = new javax.swing.JLabel();
        changeAdminPasswordNewPasswordField = new javax.swing.JPasswordField();
        changeAdminPasswordConfirmPasswordLabel = new javax.swing.JLabel();
        changeAdminPasswordConfirmPasswordField = new javax.swing.JPasswordField();
        changeAdminPasswordCancelButton = new javax.swing.JButton();
        changeAdminPasswordOKButton = new javax.swing.JButton();
        tabbedPane = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        dataFilesPanel = new javax.swing.JPanel();
        techDataFileLabel = new javax.swing.JLabel();
        techDataFileTextField = new javax.swing.JTextField();
        techDataFileBrowseButton = new javax.swing.JButton();
        signoutDataFileLabel = new javax.swing.JLabel();
        signoutDataFileTextField = new javax.swing.JTextField();
        signoutDataFileBrowseButton = new javax.swing.JButton();
        activityLogDirLabel = new javax.swing.JLabel();
        activityLogDirTextField = new javax.swing.JTextField();
        activityLogDirBrowseButton = new javax.swing.JButton();
        separator1 = new javax.swing.JSeparator();
        updaterConfigFileLabel = new javax.swing.JLabel();
        updaterConfigFileTextField = new javax.swing.JTextField();
        updaterConfigFileBrowseButton = new javax.swing.JButton();
        signoutsPanel = new javax.swing.JPanel();
        adminApprovalPanel = new javax.swing.JPanel();
        adminApprovalEnabledCheckbox = new javax.swing.JCheckBox();
        changeAdminPasswordButton = new javax.swing.JButton();
        manageAdminsButton = new javax.swing.JButton();
        lateSignoutsPanel = new javax.swing.JPanel();
        signoutsLateAfterLabel = new javax.swing.JLabel();
        hourComboBox = new javax.swing.JComboBox();
        hourSeparatorLabel = new javax.swing.JLabel();
        minuteComboBox = new javax.swing.JComboBox();
        periodComboBox = new javax.swing.JComboBox();
        logInsOutsPanel = new javax.swing.JPanel();
        logTechsOutAtMidnightCheckbox = new javax.swing.JCheckBox();
        restoreDefaultsButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        oKButton = new javax.swing.JButton();

        setAdminPasswordDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAdminPasswordDialog.setTitle("Set Admin Password");
        setAdminPasswordDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                setAdminPasswordDialogWindowClosing(evt);
            }
        });

        setAdminPasswordNewPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setAdminPasswordNewPasswordFieldActionPerformed(evt);
            }
        });

        setAdminPasswordNewPasswordLabel.setText("New password:");

        setAdminPasswordConfirmPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setAdminPasswordConfirmPasswordFieldActionPerformed(evt);
            }
        });

        setAdminPasswordConfirmPasswordLabel.setText("Confirm password:");

        setAdminPasswordCancelButton.setText("Cancel");
        setAdminPasswordCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setAdminPasswordCancelButtonActionPerformed(evt);
            }
        });

        setAdminPasswordOKButton.setText("OK");
        setAdminPasswordOKButton.setMaximumSize(new java.awt.Dimension(65, 23));
        setAdminPasswordOKButton.setMinimumSize(new java.awt.Dimension(65, 23));
        setAdminPasswordOKButton.setPreferredSize(new java.awt.Dimension(65, 23));
        setAdminPasswordOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setAdminPasswordOKButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout setAdminPasswordDialogLayout = new javax.swing.GroupLayout(setAdminPasswordDialog.getContentPane());
        setAdminPasswordDialog.getContentPane().setLayout(setAdminPasswordDialogLayout);
        setAdminPasswordDialogLayout.setHorizontalGroup(
            setAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(setAdminPasswordDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(setAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(setAdminPasswordDialogLayout.createSequentialGroup()
                        .addGroup(setAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(setAdminPasswordConfirmPasswordLabel)
                            .addComponent(setAdminPasswordNewPasswordLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(setAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(setAdminPasswordConfirmPasswordField)
                            .addComponent(setAdminPasswordNewPasswordField)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, setAdminPasswordDialogLayout.createSequentialGroup()
                        .addGap(0, 108, Short.MAX_VALUE)
                        .addComponent(setAdminPasswordCancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setAdminPasswordOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        setAdminPasswordDialogLayout.setVerticalGroup(
            setAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(setAdminPasswordDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(setAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(setAdminPasswordNewPasswordLabel)
                    .addComponent(setAdminPasswordNewPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(setAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(setAdminPasswordConfirmPasswordLabel)
                    .addComponent(setAdminPasswordConfirmPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(setAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(setAdminPasswordOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setAdminPasswordCancelButton))
                .addContainerGap())
        );

        changeAdminPasswordDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        changeAdminPasswordDialog.setTitle("Change Admin Password");
        changeAdminPasswordDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                changeAdminPasswordDialogWindowClosing(evt);
            }
        });

        changeAdminPasswordOldPasswordLabel.setText("Old password:");

        changeAdminPasswordOldPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeAdminPasswordOldPasswordFieldActionPerformed(evt);
            }
        });

        changeAdminPasswordNewPasswordLabel.setText("New password:");

        changeAdminPasswordNewPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeAdminPasswordNewPasswordFieldActionPerformed(evt);
            }
        });

        changeAdminPasswordConfirmPasswordLabel.setText("Confirm password:");

        changeAdminPasswordConfirmPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeAdminPasswordConfirmPasswordFieldActionPerformed(evt);
            }
        });

        changeAdminPasswordCancelButton.setText("Cancel");
        changeAdminPasswordCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeAdminPasswordCancelButtonActionPerformed(evt);
            }
        });

        changeAdminPasswordOKButton.setText("OK");
        changeAdminPasswordOKButton.setMaximumSize(new java.awt.Dimension(65, 23));
        changeAdminPasswordOKButton.setMinimumSize(new java.awt.Dimension(65, 23));
        changeAdminPasswordOKButton.setPreferredSize(new java.awt.Dimension(65, 23));
        changeAdminPasswordOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeAdminPasswordOKButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout changeAdminPasswordDialogLayout = new javax.swing.GroupLayout(changeAdminPasswordDialog.getContentPane());
        changeAdminPasswordDialog.getContentPane().setLayout(changeAdminPasswordDialogLayout);
        changeAdminPasswordDialogLayout.setHorizontalGroup(
            changeAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changeAdminPasswordDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(changeAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(changeAdminPasswordDialogLayout.createSequentialGroup()
                        .addGroup(changeAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(changeAdminPasswordConfirmPasswordLabel)
                            .addComponent(changeAdminPasswordNewPasswordLabel)
                            .addComponent(changeAdminPasswordOldPasswordLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(changeAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(changeAdminPasswordOldPasswordField)
                            .addComponent(changeAdminPasswordNewPasswordField)
                            .addComponent(changeAdminPasswordConfirmPasswordField)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, changeAdminPasswordDialogLayout.createSequentialGroup()
                        .addGap(0, 108, Short.MAX_VALUE)
                        .addComponent(changeAdminPasswordCancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(changeAdminPasswordOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        changeAdminPasswordDialogLayout.setVerticalGroup(
            changeAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changeAdminPasswordDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(changeAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeAdminPasswordOldPasswordLabel)
                    .addComponent(changeAdminPasswordOldPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(changeAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeAdminPasswordNewPasswordLabel)
                    .addComponent(changeAdminPasswordNewPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(changeAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeAdminPasswordConfirmPasswordLabel)
                    .addComponent(changeAdminPasswordConfirmPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(changeAdminPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeAdminPasswordOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeAdminPasswordCancelButton))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Settings");
        setResizable(false);

        dataFilesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Data files"));

        techDataFileLabel.setText("Tech data file:");

        techDataFileTextField.setMinimumSize(new java.awt.Dimension(254, 20));
        techDataFileTextField.setPreferredSize(new java.awt.Dimension(254, 20));

        techDataFileBrowseButton.setText("...");
        techDataFileBrowseButton.setToolTipText("Browse");
        techDataFileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                techDataFileBrowseButtonActionPerformed(evt);
            }
        });

        signoutDataFileLabel.setText("Signout data file:");

        signoutDataFileTextField.setMinimumSize(new java.awt.Dimension(254, 20));
        signoutDataFileTextField.setPreferredSize(new java.awt.Dimension(254, 20));

        signoutDataFileBrowseButton.setText("...");
        signoutDataFileBrowseButton.setToolTipText("Browse");
        signoutDataFileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signoutDataFileBrowseButtonActionPerformed(evt);
            }
        });

        activityLogDirLabel.setText("Activity log folder:");

        activityLogDirBrowseButton.setText("...");
        activityLogDirBrowseButton.setToolTipText("Browse");
        activityLogDirBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activityLogDirBrowseButtonActionPerformed(evt);
            }
        });

        updaterConfigFileLabel.setText("Updater config file:");

        updaterConfigFileBrowseButton.setText("...");
        updaterConfigFileBrowseButton.setToolTipText("Browse");
        updaterConfigFileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updaterConfigFileBrowseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dataFilesPanelLayout = new javax.swing.GroupLayout(dataFilesPanel);
        dataFilesPanel.setLayout(dataFilesPanelLayout);
        dataFilesPanelLayout.setHorizontalGroup(
            dataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataFilesPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(dataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dataFilesPanelLayout.createSequentialGroup()
                        .addComponent(updaterConfigFileLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updaterConfigFileTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updaterConfigFileBrowseButton))
                    .addGroup(dataFilesPanelLayout.createSequentialGroup()
                        .addGroup(dataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(activityLogDirLabel)
                            .addComponent(signoutDataFileLabel)
                            .addComponent(techDataFileLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(signoutDataFileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                            .addComponent(activityLogDirTextField)
                            .addComponent(techDataFileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(signoutDataFileBrowseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(activityLogDirBrowseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(techDataFileBrowseButton)))
                    .addComponent(separator1))
                .addContainerGap())
        );
        dataFilesPanelLayout.setVerticalGroup(
            dataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataFilesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(techDataFileLabel)
                    .addComponent(techDataFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(techDataFileBrowseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(signoutDataFileLabel)
                    .addComponent(signoutDataFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(signoutDataFileBrowseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(activityLogDirTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(activityLogDirLabel)
                    .addComponent(activityLogDirBrowseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(separator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updaterConfigFileLabel)
                    .addComponent(updaterConfigFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updaterConfigFileBrowseButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        signoutsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Signouts"));

        adminApprovalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Admin Approval"));

        adminApprovalEnabledCheckbox.setText("Admin approval enabled");
        adminApprovalEnabledCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminApprovalEnabledCheckboxActionPerformed(evt);
            }
        });

        changeAdminPasswordButton.setText("Change Admin Password...");
        changeAdminPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeAdminPasswordButtonActionPerformed(evt);
            }
        });

        manageAdminsButton.setText("Manage Admins...");
        manageAdminsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageAdminsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout adminApprovalPanelLayout = new javax.swing.GroupLayout(adminApprovalPanel);
        adminApprovalPanel.setLayout(adminApprovalPanelLayout);
        adminApprovalPanelLayout.setHorizontalGroup(
            adminApprovalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminApprovalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(adminApprovalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(manageAdminsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(adminApprovalEnabledCheckbox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(changeAdminPasswordButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        adminApprovalPanelLayout.setVerticalGroup(
            adminApprovalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminApprovalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adminApprovalEnabledCheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(changeAdminPasswordButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manageAdminsButton)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        lateSignoutsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Late Signouts"));

        signoutsLateAfterLabel.setText("Signouts late after:");

        hourComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "12" }));

        hourSeparatorLabel.setText(":");

        minuteComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00" }));

        periodComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PM" }));

        javax.swing.GroupLayout lateSignoutsPanelLayout = new javax.swing.GroupLayout(lateSignoutsPanel);
        lateSignoutsPanel.setLayout(lateSignoutsPanelLayout);
        lateSignoutsPanelLayout.setHorizontalGroup(
            lateSignoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lateSignoutsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lateSignoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lateSignoutsPanelLayout.createSequentialGroup()
                        .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hourSeparatorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(periodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(signoutsLateAfterLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lateSignoutsPanelLayout.setVerticalGroup(
            lateSignoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lateSignoutsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(signoutsLateAfterLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lateSignoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hourSeparatorLabel)
                    .addComponent(periodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout signoutsPanelLayout = new javax.swing.GroupLayout(signoutsPanel);
        signoutsPanel.setLayout(signoutsPanelLayout);
        signoutsPanelLayout.setHorizontalGroup(
            signoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signoutsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adminApprovalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lateSignoutsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        signoutsPanelLayout.setVerticalGroup(
            signoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signoutsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(signoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(adminApprovalPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lateSignoutsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        logInsOutsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Log ins/outs"));

        logTechsOutAtMidnightCheckbox.setText("Log techs out at midnight.");

        javax.swing.GroupLayout logInsOutsPanelLayout = new javax.swing.GroupLayout(logInsOutsPanel);
        logInsOutsPanel.setLayout(logInsOutsPanelLayout);
        logInsOutsPanelLayout.setHorizontalGroup(
            logInsOutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logInsOutsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logTechsOutAtMidnightCheckbox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        logInsOutsPanelLayout.setVerticalGroup(
            logInsOutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logInsOutsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logTechsOutAtMidnightCheckbox)
                .addContainerGap(111, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataFilesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addComponent(signoutsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logInsOutsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dataFilesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(signoutsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logInsOutsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tabbedPane.addTab("General", generalPanel);

        restoreDefaultsButton.setText("Restore Defaults");
        restoreDefaultsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restoreDefaultsButtonActionPerformed(evt);
            }
        });

        applyButton.setText("Apply");
        applyButton.setMaximumSize(new java.awt.Dimension(65, 23));
        applyButton.setMinimumSize(new java.awt.Dimension(65, 23));
        applyButton.setPreferredSize(new java.awt.Dimension(65, 23));
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        oKButton.setText("OK");
        oKButton.setMaximumSize(new java.awt.Dimension(65, 23));
        oKButton.setMinimumSize(new java.awt.Dimension(65, 23));
        oKButton.setPreferredSize(new java.awt.Dimension(65, 23));
        oKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oKButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(restoreDefaultsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(applyButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cancelButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oKButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(tabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oKButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton)
                    .addComponent(restoreDefaultsButton)
                    .addComponent(applyButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void techDataFileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_techDataFileBrowseButtonActionPerformed
        JFileChooser chooser = new JFileChooser(SettingsManager.getDefault(PROPERTY_TECH_DATA_FILE));
        chooser.setDialogTitle("Tech Data File");
        chooser.setCurrentDirectory(new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile()));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = chooser.getSelectedFile();
        techDataFileTextField.setText(FileOps.getRelativePath(System.getProperty("user.dir"), f.getAbsolutePath()));
    }//GEN-LAST:event_techDataFileBrowseButtonActionPerformed

    private void signoutDataFileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signoutDataFileBrowseButtonActionPerformed
        JFileChooser chooser = new JFileChooser(SettingsManager.getDefault(PROPERTY_SIGNOUT_DATA_FILE));
        chooser.setDialogTitle("Signout Data File");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = chooser.getSelectedFile();
        signoutDataFileTextField.setText(FileOps.getRelativePath(System.getProperty("user.dir"), f.getAbsolutePath()));
    }//GEN-LAST:event_signoutDataFileBrowseButtonActionPerformed

    private void changeAdminPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeAdminPasswordButtonActionPerformed
        changeAdminPasswordDialog.pack();
        changeAdminPasswordDialog.setLocationRelativeTo(this);
        changeAdminPasswordDialog.setModal(true);
        changeAdminPasswordDialog.setVisible(true);
    }//GEN-LAST:event_changeAdminPasswordButtonActionPerformed

    private void restoreDefaultsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restoreDefaultsButtonActionPerformed
        loadDefaultSettings();
        updaterSettingsPanel.loadDefaultSettings();
        changeAdminPasswordButton.setEnabled(adminApprovalEnabledCheckbox.isSelected());
        manageAdminsButton.setEnabled(adminApprovalEnabledCheckbox.isSelected());
    }//GEN-LAST:event_restoreDefaultsButtonActionPerformed

    private void oKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oKButtonActionPerformed
        applyButton.doClick();
        dispose();
    }//GEN-LAST:event_oKButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void activityLogDirBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activityLogDirBrowseButtonActionPerformed
        JFileChooser chooser = new JFileChooser(SettingsManager.getDefault(PROPERTY_ACTIVITY_LOG_DIR));
        chooser.setDialogTitle("Activity Log Folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = chooser.getSelectedFile();
        activityLogDirTextField.setText(FileOps.getRelativePath(System.getProperty("user.dir"), f.getAbsolutePath()));
    }//GEN-LAST:event_activityLogDirBrowseButtonActionPerformed

    private void manageAdminsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageAdminsButtonActionPerformed
        String hash;
        if (passwordChanged || existingAdminPasswordHash == null || existingAdminPasswordHash.isEmpty()) {
            hash = newAdminPasswordHash;
        } else {
            hash = existingAdminPasswordHash;
        }
        AdminPasswordDialog adminPasswordDialog = new AdminPasswordDialog(this, true, hash);
        adminPasswordDialog.setLocationRelativeTo(this);
        adminPasswordDialog.setVisible(true);
        if (!adminPasswordDialog.isAccessGranted()) {
            return;
        }
        AdminManagerDialog adminManagerDialog = new AdminManagerDialog(this, true);
        adminManagerDialog.setLocationRelativeTo(this);
        adminManagerDialog.setVisible(true);
    }//GEN-LAST:event_manageAdminsButtonActionPerformed

    private void adminApprovalEnabledCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminApprovalEnabledCheckboxActionPerformed
        if (adminApprovalEnabledCheckbox.isSelected() && (newAdminPasswordHash == null || newAdminPasswordHash.isEmpty()) && (existingAdminPasswordHash == null || existingAdminPasswordHash.isEmpty())) {
            setAdminPasswordDialog.pack();
            setAdminPasswordDialog.setLocationRelativeTo(this);
            setAdminPasswordDialog.setModal(true);
            setAdminPasswordDialog.setVisible(true);
            if (newAdminPasswordHash == null || newAdminPasswordHash.isEmpty()) {
                adminApprovalEnabledCheckbox.setSelected(false);
                return;
            }
        }
        changeAdminPasswordButton.setEnabled(adminApprovalEnabledCheckbox.isSelected());
        manageAdminsButton.setEnabled(adminApprovalEnabledCheckbox.isSelected());
    }//GEN-LAST:event_adminApprovalEnabledCheckboxActionPerformed

    private void setAdminPasswordCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setAdminPasswordCancelButtonActionPerformed
        setAdminPasswordDialog.setModal(false);
        setAdminPasswordDialog.dispose();
    }//GEN-LAST:event_setAdminPasswordCancelButtonActionPerformed

    private void setAdminPasswordDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_setAdminPasswordDialogWindowClosing
        setAdminPasswordDialog.setModal(false);
        setAdminPasswordDialog.dispose();
    }//GEN-LAST:event_setAdminPasswordDialogWindowClosing

    private void setAdminPasswordOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setAdminPasswordOKButtonActionPerformed
        if (setAdminPasswordNewPasswordField.getPassword().length == 0 || setAdminPasswordConfirmPasswordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(setAdminPasswordDialog, "Password cannot be blank!", "Password Error", JOptionPane.ERROR_MESSAGE);
            setAdminPasswordNewPasswordField.requestFocus();
            return;
        }
        if (!Arrays.equals(setAdminPasswordNewPasswordField.getPassword(), setAdminPasswordConfirmPasswordField.getPassword())) {
            JOptionPane.showMessageDialog(setAdminPasswordDialog, "Passwords do not match!", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
            setAdminPasswordConfirmPasswordField.setText("");
            setAdminPasswordNewPasswordField.selectAll();
            setAdminPasswordNewPasswordField.requestFocus();
            return;
        }
        newAdminPasswordHash = bytesToHexString(messageDigest.digest(new String(setAdminPasswordNewPasswordField.getPassword()).getBytes()));
        JOptionPane.showMessageDialog(setAdminPasswordDialog, "Admin password has been set!", "Password Set", JOptionPane.INFORMATION_MESSAGE);
        passwordChanged = true;
        setAdminPasswordDialog.dispose();
    }//GEN-LAST:event_setAdminPasswordOKButtonActionPerformed

    private void changeAdminPasswordCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeAdminPasswordCancelButtonActionPerformed
        changeAdminPasswordDialog.setModal(false);
        changeAdminPasswordDialog.dispose();
    }//GEN-LAST:event_changeAdminPasswordCancelButtonActionPerformed

    private void changeAdminPasswordDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_changeAdminPasswordDialogWindowClosing
        changeAdminPasswordDialog.setModal(false);
        changeAdminPasswordDialog.dispose();
    }//GEN-LAST:event_changeAdminPasswordDialogWindowClosing

    private void changeAdminPasswordOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeAdminPasswordOKButtonActionPerformed
        boolean wrongOldPassword = false;
        if (changeAdminPasswordOldPasswordField.getPassword().length == 0 || changeAdminPasswordNewPasswordField.getPassword().length == 0 || changeAdminPasswordConfirmPasswordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(changeAdminPasswordDialog, "Password cannot be blank!", "Password Error", JOptionPane.ERROR_MESSAGE);
            changeAdminPasswordOldPasswordField.requestFocus();
            return;
        }
        if (newAdminPasswordHash == null || newAdminPasswordHash.isEmpty()) {
            byte[] typedOldPasswordHash = messageDigest.digest(new String(changeAdminPasswordOldPasswordField.getPassword()).getBytes());
            if (!existingAdminPasswordHash.equalsIgnoreCase(bytesToHexString(typedOldPasswordHash))) {
                wrongOldPassword = true;
            }
        } else {
            byte[] typedOldPasswordHash = messageDigest.digest(new String(changeAdminPasswordOldPasswordField.getPassword()).getBytes());
            if (newAdminPasswordHash.equalsIgnoreCase(bytesToHexString(typedOldPasswordHash))) {
                wrongOldPassword = true;
            }
        }
        if (wrongOldPassword) {
            JOptionPane.showMessageDialog(changeAdminPasswordDialog, "Old password is incorrect!", "Incorrect Old Password", JOptionPane.ERROR_MESSAGE);
            changeAdminPasswordConfirmPasswordField.setText("");
            changeAdminPasswordNewPasswordField.setText("");
            changeAdminPasswordOldPasswordField.selectAll();
            changeAdminPasswordOldPasswordField.requestFocus();
            return;
        }
        if (!Arrays.equals(changeAdminPasswordNewPasswordField.getPassword(), changeAdminPasswordConfirmPasswordField.getPassword())) {
            JOptionPane.showMessageDialog(changeAdminPasswordDialog, "Passwords do not match!", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
            changeAdminPasswordConfirmPasswordField.setText("");
            changeAdminPasswordNewPasswordField.selectAll();
            changeAdminPasswordNewPasswordField.requestFocus();
            return;
        }
        newAdminPasswordHash = bytesToHexString(messageDigest.digest(new String(changeAdminPasswordNewPasswordField.getPassword()).getBytes()));
        JOptionPane.showMessageDialog(changeAdminPasswordDialog, "Admin password has been set!", "Password Set", JOptionPane.INFORMATION_MESSAGE);
        passwordChanged = true;
        changeAdminPasswordDialog.dispose();
    }//GEN-LAST:event_changeAdminPasswordOKButtonActionPerformed

    private void changeAdminPasswordOldPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeAdminPasswordOldPasswordFieldActionPerformed
        changeAdminPasswordOKButton.doClick();
    }//GEN-LAST:event_changeAdminPasswordOldPasswordFieldActionPerformed

    private void changeAdminPasswordNewPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeAdminPasswordNewPasswordFieldActionPerformed
        changeAdminPasswordOKButton.doClick();
    }//GEN-LAST:event_changeAdminPasswordNewPasswordFieldActionPerformed

    private void changeAdminPasswordConfirmPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeAdminPasswordConfirmPasswordFieldActionPerformed
        changeAdminPasswordOKButton.doClick();
    }//GEN-LAST:event_changeAdminPasswordConfirmPasswordFieldActionPerformed

    private void setAdminPasswordNewPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setAdminPasswordNewPasswordFieldActionPerformed
        setAdminPasswordOKButton.doClick();
    }//GEN-LAST:event_setAdminPasswordNewPasswordFieldActionPerformed

    private void setAdminPasswordConfirmPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setAdminPasswordConfirmPasswordFieldActionPerformed
        setAdminPasswordOKButton.doClick();
    }//GEN-LAST:event_setAdminPasswordConfirmPasswordFieldActionPerformed

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        SettingsManager.set(PROPERTY_TECH_DATA_FILE, techDataFileTextField.getText());
        SettingsManager.set(PROPERTY_SIGNOUT_DATA_FILE, signoutDataFileTextField.getText());
        SettingsManager.set(PROPERTY_ACTIVITY_LOG_DIR, activityLogDirTextField.getText());
        SettingsManager.set(PROPERTY_UPDATER_CONFIGURATION_DATA_FILE, updaterConfigFileTextField.getText());
        SettingsManager.set(PROPERTY_ADMIN_APPROVAL_ENABLED, Boolean.toString(adminApprovalEnabledCheckbox.isSelected()));
        SettingsManager.set(PROPERTY_LATE_SIGNOUT_TIME, getLateSignoutTime());
        SettingsManager.set(PROPERTY_AUTO_OUT_AT_MIDNIGHT, Boolean.toString(logTechsOutAtMidnightCheckbox.isSelected()));
        if (passwordChanged) {
            SettingsManager.set(PROPERTY_ADMIN_PASSWORD, newAdminPasswordHash);
        } else {
            SettingsManager.set(PROPERTY_ADMIN_PASSWORD, existingAdminPasswordHash);
        }
        try {
            SettingsManager.saveSettings();
            updaterSettingsPanel.saveSettings();
        } catch (IOException ex) {
            Logger.log(Level.ERROR, ex, "failed to save settings - " + ex.toString());
            JOptionPane.showMessageDialog(this, "Failed to save settings:\n"
                    + "\n"
                    + ex.toString(), "Error Saving Settings", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_applyButtonActionPerformed

    private void updaterConfigFileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updaterConfigFileBrowseButtonActionPerformed
        JFileChooser chooser = new JFileChooser(SettingsManager.getDefault(PROPERTY_UPDATER_CONFIGURATION_DATA_FILE));
        chooser.setDialogTitle("Updater Configuration File");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = chooser.getSelectedFile();
        updaterConfigFileTextField.setText(FileOps.getRelativePath(System.getProperty("user.dir"), f.getAbsolutePath()));
    }//GEN-LAST:event_updaterConfigFileBrowseButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activityLogDirBrowseButton;
    private javax.swing.JLabel activityLogDirLabel;
    private javax.swing.JTextField activityLogDirTextField;
    private javax.swing.JCheckBox adminApprovalEnabledCheckbox;
    private javax.swing.JPanel adminApprovalPanel;
    private javax.swing.JButton applyButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton changeAdminPasswordButton;
    private javax.swing.JButton changeAdminPasswordCancelButton;
    private javax.swing.JPasswordField changeAdminPasswordConfirmPasswordField;
    private javax.swing.JLabel changeAdminPasswordConfirmPasswordLabel;
    private javax.swing.JDialog changeAdminPasswordDialog;
    private javax.swing.JPasswordField changeAdminPasswordNewPasswordField;
    private javax.swing.JLabel changeAdminPasswordNewPasswordLabel;
    private javax.swing.JButton changeAdminPasswordOKButton;
    private javax.swing.JPasswordField changeAdminPasswordOldPasswordField;
    private javax.swing.JLabel changeAdminPasswordOldPasswordLabel;
    private javax.swing.JPanel dataFilesPanel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JComboBox hourComboBox;
    private javax.swing.JLabel hourSeparatorLabel;
    private javax.swing.JPanel lateSignoutsPanel;
    private javax.swing.JPanel logInsOutsPanel;
    private javax.swing.JCheckBox logTechsOutAtMidnightCheckbox;
    private javax.swing.JButton manageAdminsButton;
    private javax.swing.JComboBox minuteComboBox;
    private javax.swing.JButton oKButton;
    private javax.swing.JComboBox periodComboBox;
    private javax.swing.JButton restoreDefaultsButton;
    private javax.swing.JSeparator separator1;
    private javax.swing.JButton setAdminPasswordCancelButton;
    private javax.swing.JPasswordField setAdminPasswordConfirmPasswordField;
    private javax.swing.JLabel setAdminPasswordConfirmPasswordLabel;
    private javax.swing.JDialog setAdminPasswordDialog;
    private javax.swing.JPasswordField setAdminPasswordNewPasswordField;
    private javax.swing.JLabel setAdminPasswordNewPasswordLabel;
    private javax.swing.JButton setAdminPasswordOKButton;
    private javax.swing.JButton signoutDataFileBrowseButton;
    private javax.swing.JLabel signoutDataFileLabel;
    private javax.swing.JTextField signoutDataFileTextField;
    private javax.swing.JLabel signoutsLateAfterLabel;
    private javax.swing.JPanel signoutsPanel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JButton techDataFileBrowseButton;
    private javax.swing.JLabel techDataFileLabel;
    private javax.swing.JTextField techDataFileTextField;
    private javax.swing.JButton updaterConfigFileBrowseButton;
    private javax.swing.JLabel updaterConfigFileLabel;
    private javax.swing.JTextField updaterConfigFileTextField;
    // End of variables declaration//GEN-END:variables
}