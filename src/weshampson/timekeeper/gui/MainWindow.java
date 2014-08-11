
package weshampson.timekeeper.gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.dom4j.DocumentException;
import weshampson.timekeeper.Main;
import weshampson.timekeeper.settings.SettingsManager;
import weshampson.timekeeper.signout.Signout;
import weshampson.timekeeper.signout.SignoutException;
import weshampson.timekeeper.signout.SignoutManager;
import weshampson.timekeeper.tech.Tech;
import weshampson.timekeeper.tech.TechException;
import weshampson.timekeeper.tech.TechManager;

/**
 * This class handles most of the user interaction with the program.
 * 
 * @author  Wes Hampson
 * @version 0.2.0 (Aug 11, 2014)
 * @since   0.1.0 (Jul 16, 2014)
 */
public class MainWindow extends javax.swing.JFrame {
    private Window mainWindow = this;
    private JPopupMenu techsLoggedInPopupMenu;
    private JPopupMenu techsLoggedOutPopupMenu;
    private JPopupMenu techsSignedOutPopupMenu;
    private Timer clock;
    private TimerTask clockTask;

    /** Creates new form MainWindow */
    public MainWindow() {
        initComponents();
        initShutdownHook();
        initMenuBar();
        initLists();
        initPopupMenus();
        updateSignoutTable();
        initFilters();
        updateLists();
        startClock();
        loadSettings();
        loadTechData();
        loadSignoutData();
        iDTextField.requestFocus();
    }
    private void initShutdownHook() {
        Thread shutdownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Shutting down...");
                stopClock();
                saveSettings();
                saveTechData();
                saveSignoutData();
            }
        });
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }
    private void initMenuBar() {
        if (Main.isDebugModeEnabled()) {
            debugMenu.setVisible(true);
        } else {
            debugMenu.setVisible(false);
        }
    }
    @SuppressWarnings("unchecked")
    private void initLists() {
        TechCellRenderer cellRenderer = new TechCellRenderer();
        techsLoggedInList.setCellRenderer(cellRenderer);
        techsLoggedOutList.setCellRenderer(cellRenderer);
        updateLists();
    }
    private void initPopupMenus() {
        techsLoggedInPopupMenu = new JPopupMenu();
        techsLoggedOutPopupMenu = new JPopupMenu();
        techsSignedOutPopupMenu = new JPopupMenu();
        JMenuItem techsLoggedInEditTechNameMenuItem = new JMenuItem("Edit Name");
        JMenuItem techsLoggedOutEditTechNameMenuItem = new JMenuItem("Edit Name");
        JMenuItem techsLoggedInRemoveTechMenuItem = new JMenuItem("Remove Tech");
        JMenuItem techsLoggedOutRemoveTechMenuItem = new JMenuItem("Remove Tech");
        JMenuItem techsSignedOutRemoveSignoutMenuItem = new JMenuItem("Remove Signout");
        techsLoggedInEditTechNameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tech t = (Tech)techsLoggedInList.getSelectedValue();
                updateTechNameWizard(t);
            }
        });
        techsLoggedOutEditTechNameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tech t = (Tech)techsLoggedOutList.getSelectedValue();
                updateTechNameWizard(t);
            }
        });
        techsLoggedInRemoveTechMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Tech t = (Tech)techsLoggedInList.getSelectedValue();
                    TechManager.removeTech(t);
                    updateLists();
                } catch (TechException ex) {
                    ex.printStackTrace();
                }
            }
        });
        techsLoggedOutRemoveTechMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Tech t = (Tech)techsLoggedOutList.getSelectedValue();
                    TechManager.removeTech(t);
                    updateLists();
                } catch (TechException ex) {
                    ex.printStackTrace();
                }
            }
        });
        techsSignedOutRemoveSignoutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int signoutID = Integer.parseInt((String)techsSignedOutTable.getModel().getValueAt(techsSignedOutTable.convertRowIndexToModel(techsSignedOutTable.getSelectedRow()), 0));
                    int techID = getTechIDNumberFromUser("Please enter your ID number.", "ID Number Required");
                    if (techID == -1) {
                        return;
                    }
                    Signout s = SignoutManager.getSignoutByID(signoutID);
                    if (techID != s.getTechID()) {
                        JOptionPane.showMessageDialog(mainWindow, "Invalid ID number!", "Invalid ID Number", JOptionPane.ERROR_MESSAGE);
                    } else {
                        SignoutManager.removeSignout(signoutID);
                        updateSignoutTable();
                    }
                } catch (SignoutException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        techsLoggedInPopupMenu.add(techsLoggedInEditTechNameMenuItem);
        techsLoggedInPopupMenu.add(techsLoggedInRemoveTechMenuItem);
        techsLoggedOutPopupMenu.add(techsLoggedOutEditTechNameMenuItem);
        techsLoggedOutPopupMenu.add(techsLoggedOutRemoveTechMenuItem);
        techsSignedOutPopupMenu.add(techsSignedOutRemoveSignoutMenuItem);
    }
    private void initFilters() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (SignoutManager.TableFilter filter : SignoutManager.TableFilter.values()) {
            model.addElement(filter.getFilterText());
        }
        signoutFilterComboBox.setModel(model);
    }
    private void updateSignoutTable() {
        try {
            SignoutManager.TableFilter filter = SignoutManager.TableFilter.getFilterByString((String)signoutFilterComboBox.getSelectedItem());
            techsSignedOutTable.setModel(SignoutManager.getSignoutTableModel(filter));
            techsSignedOutTable.getColumnModel().getColumn(0).setMinWidth(0);
            techsSignedOutTable.getColumnModel().getColumn(0).setMaxWidth(0);
            techsSignedOutTable.getColumnModel().getColumn(0).setWidth(0);
            techsSignedOutTable.getColumnModel().getColumn(0).setResizable(false);
            techsSignedOutTable.setDefaultRenderer(Object.class, new SignoutCellRenderer());
        } catch (SignoutException ex) {
            ex.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    private void updateLists() {
        techsLoggedInList.setModel(TechManager.getTechsInListModel());
        techsLoggedOutList.setModel(TechManager.getTechsOutListModel());
    }
    private void startClock() {
        clock = new Timer("Clock", true);
        clockTask = new TimerTask() {
            Date date = new Date();
            @Override
            public void run() {
                date.setTime(System.currentTimeMillis());
                clockDisplay.setText(new SimpleDateFormat("hh:mm:ss a").format(date));
                dateDisplay.setText(new SimpleDateFormat("EEE, MMM. dd, yyyy").format(date));
            }
        };
        clock.scheduleAtFixedRate(clockTask, 0, 1000);
    }
    private void loadSettings() {
        //TODO: checking for missing entries (null values)
        try {
            SettingsManager.loadSettings();
            updateSignoutTable();
        } catch (IOException | DocumentException ex) {
            //TODO: Log this
            ex.printStackTrace();
        }
    }
    private void loadTechData() {
        try {
            TechManager.loadTechs(new File(SettingsManager.get(SettingsManager.PROPERTY_TECH_DATA_FILE)));
            updateLists();
        } catch (DocumentException | IOException | TechException ex) {
            ex.printStackTrace();
        }
    }
    private void loadSignoutData() {
        try {
            SignoutManager.loadSignoutData(new File(SettingsManager.get(SettingsManager.PROPERTY_SIGNOUT_DATA_FILE)));
            updateSignoutTable();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void saveSettings() {
        try {
            SettingsManager.saveSettings();
        } catch (IOException ex) {
            //TODO: Log this
            ex.printStackTrace();
        }
    }
    private void saveTechData() {
        try {
            TechManager.saveTechs(new File(SettingsManager.get(SettingsManager.PROPERTY_TECH_DATA_FILE)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void saveSignoutData() {
        try {
            SignoutManager.saveSignouts(new File(SettingsManager.get(SettingsManager.PROPERTY_SIGNOUT_DATA_FILE)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void stopClock() {
        clock.cancel();
    }
    private int getTechIDNumberFromUser() {
        return(getTechIDNumberFromUser("Please enter an ID number:", "ID Number Required"));
    }
    private int getTechIDNumberFromUser(String customMessage, String customTitle) {
        int iDNumber = -1;
        do {
            String iDNumber_String = JOptionPane.showInputDialog(this, customMessage, customTitle, JOptionPane.PLAIN_MESSAGE);
            if (iDNumber_String == null) {
                return(-1);
            }
            try {
                iDNumber = Integer.parseInt(iDNumber_String);
                if (iDNumber < 1) {
                    // TODO: Log this
                    System.err.println("input cannot be be negative or 0");
                }
            } catch (NumberFormatException ex) {
                 // TODO: Log this
                System.err.println("Please enter an ID number.");
            }
        } while (iDNumber < 0);
        return(iDNumber);
    }
    
    /**
     * Launches a wizard that guides the user through creating a new
     * {@link weshampson.timekeeper.tech.Tech}. The wizard asks if the user
     * would like to create a tech given the specified ID number and, if so,
     * asks the user for a name. The wizard subsequently adds the tech to the
     * master list in the {@link weshampson.timekeeper.tech.TechManager} class
     * via the {@link weshampson.timekeeper.tech.TechManager#addTech(weshampson.timekeeper.tech.Tech)}
     * method.
     * 
     * @param techID the ID number to use for the new tech
     * @return the newly created Tech object, null if the user cancelled.
     */
    private Tech createNewTechWizard(int techID) {
        System.out.println("Launched Create New Tech Wizard.");
        int option = JOptionPane.showOptionDialog(this,
                techID  + " not found. Create " + techID + "?",
                "Create New Tech Wizard",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if (option != JOptionPane.YES_OPTION) {
            System.out.println("Create New Tech Wizard cancelled by user.");
            return(null);
        }
        String techName;
        do {
            techName = JOptionPane.showInputDialog(this,
                "Enter a name for " + techID + ":",
                "Create New Tech Wizard",
                JOptionPane.PLAIN_MESSAGE);
            if (techName == null) {
                System.out.println("Create New Tech Wizard cancelled by user.");
                return(null);
            }
        } while (techName.trim().isEmpty());
        Tech tech = new Tech(techID, techName.trim());
        try {
            TechManager.addTech(tech);
            System.out.println("Created new tech: " + tech.getID() + " (" + tech.getName() + ")");
            updateLists();
        } catch (TechException ex) {
            System.err.println(ex.getMessage());
        }
        return(tech);
    }
    private Tech updateTechNameWizard(Tech tech) {
        String techName;
        do {
            techName = (String)JOptionPane.showInputDialog(mainWindow,
                    "Enter a name for " + tech.getID() + ":",
                    "Edit Tech Name",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    tech.getName());
            if (techName == null) {
                return(null);
            }
        } while (techName.trim().isEmpty());
        tech.setName(techName);
        try {
            TechManager.updateTech(tech);
            updateLists();
        } catch (TechException ex) {
            ex.printStackTrace();
        }
        return(tech);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leftPanel = new javax.swing.JPanel();
        techsLoggedOutLabel = new javax.swing.JLabel();
        techsLoggedOutScrollPane = new javax.swing.JScrollPane();
        techsLoggedOutList = new javax.swing.JList();
        centerPanel = new javax.swing.JPanel();
        timeStampPanel = new javax.swing.JPanel();
        dateDisplay = new javax.swing.JLabel();
        clockDisplay = new javax.swing.JLabel();
        timeStampLabel = new javax.swing.JLabel();
        iDTextField = new javax.swing.JTextField();
        timeStampButton = new javax.swing.JButton();
        techsSignedOutPanel = new javax.swing.JPanel();
        techsSignedOutLabel = new javax.swing.JLabel();
        signoutTableFilterLabel = new javax.swing.JLabel();
        signoutFilterComboBox = new javax.swing.JComboBox();
        techsSignedOutScrollPane = new javax.swing.JScrollPane();
        techsSignedOutTable = new javax.swing.JTable();
        rightPanel = new javax.swing.JPanel();
        techsLoggedInLabel = new javax.swing.JLabel();
        techsLoggedInScrollPane = new javax.swing.JScrollPane();
        techsLoggedInList = new javax.swing.JList();
        bottomPanel = new javax.swing.JPanel();
        techsInCountTitle = new javax.swing.JLabel();
        techsInCount = new javax.swing.JLabel();
        techsOutCountTitle = new javax.swing.JLabel();
        techsOutCount = new javax.swing.JLabel();
        techsSignedOutCountTitle = new javax.swing.JLabel();
        techsSignedOutCount = new javax.swing.JLabel();
        showAllCountersLabel = new javax.swing.JLabel();
        signOutButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        optionsMenu = new javax.swing.JMenu();
        optionsCreateNewTechMenuItem = new javax.swing.JMenuItem();
        optionsMenuSeparator1 = new javax.swing.JPopupMenu.Separator();
        optionsSettingsMenuItem = new javax.swing.JMenuItem();
        optionsMenuSeparator2 = new javax.swing.JPopupMenu.Separator();
        optionsExitMenuItem = new javax.swing.JMenuItem();
        debugMenu = new javax.swing.JMenu();
        debugMenuLoadSettingsMenuItem = new javax.swing.JMenuItem();
        debugMenuSaveSettingsMenuItem = new javax.swing.JMenuItem();
        debugMenuSeparator1 = new javax.swing.JPopupMenu.Separator();
        debugLoadTechDataMenuItem = new javax.swing.JMenuItem();
        debugSaveTechDataMenuItem = new javax.swing.JMenuItem();
        debugMenuSeparator2 = new javax.swing.JPopupMenu.Separator();
        debugLoadSignoutDataMenuItem = new javax.swing.JMenuItem();
        debugSaveSignoutDataMenuItem = new javax.swing.JMenuItem();
        debugMenuSeparator3 = new javax.swing.JPopupMenu.Separator();
        debugStartClockMenuItem = new javax.swing.JMenuItem();
        debugStopClockMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Main.APPLICATION_TITLE + " " + Main.APPLICATION_VERSION);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        leftPanel.setPreferredSize(new java.awt.Dimension(300, 469));

        techsLoggedOutLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        techsLoggedOutLabel.setText("Techs logged out:");

        techsLoggedOutList.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        techsLoggedOutList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Wes Hampson" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        techsLoggedOutList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        techsLoggedOutList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                techsLoggedOutListMouseClicked(evt);
            }
        });
        techsLoggedOutScrollPane.setViewportView(techsLoggedOutList);

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(techsLoggedOutLabel)
                        .addGap(0, 137, Short.MAX_VALUE))
                    .addComponent(techsLoggedOutScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))
                .addContainerGap())
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(techsLoggedOutLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(techsLoggedOutScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(leftPanel, java.awt.BorderLayout.LINE_START);

        dateDisplay.setFont(new java.awt.Font("Tahoma", 0, 26)); // NOI18N
        dateDisplay.setForeground(new java.awt.Color(255, 0, 0));
        dateDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dateDisplay.setText("Mon, Jan. 20, 2014");

        clockDisplay.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        clockDisplay.setForeground(new java.awt.Color(255, 0, 0));
        clockDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clockDisplay.setText("12:00:00 AM");

        timeStampLabel.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        timeStampLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timeStampLabel.setText("Time Stamp Me:");

        iDTextField.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        iDTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iDTextFieldMouseClicked(evt);
            }
        });
        iDTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iDTextFieldActionPerformed(evt);
            }
        });

        timeStampButton.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        timeStampButton.setText("Time Stamp!");
        timeStampButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeStampButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout timeStampPanelLayout = new javax.swing.GroupLayout(timeStampPanel);
        timeStampPanel.setLayout(timeStampPanelLayout);
        timeStampPanelLayout.setHorizontalGroup(
            timeStampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, timeStampPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(timeStampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clockDisplay, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(timeStampLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iDTextField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeStampButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        timeStampPanelLayout.setVerticalGroup(
            timeStampPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, timeStampPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dateDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clockDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timeStampLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(iDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(timeStampButton)
                .addContainerGap())
        );

        techsSignedOutLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        techsSignedOutLabel.setText("Techs Signed Out:");

        signoutTableFilterLabel.setText("Signout table filter:");

        signoutFilterComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Remaining this week" }));
        signoutFilterComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signoutFilterComboBoxActionPerformed(evt);
            }
        });

        techsSignedOutTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Tues. Jan 21, 2014", "Wes Hampson", "No ride", "12:00:00 AM (Mon, Jan 20, 2014)", "Taylor Gudmundson"}
            },
            new String [] {
                "Absent date", "Tech name", "Sign out reason", "Sign out timestamp", "Admin approved"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        techsSignedOutTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        techsSignedOutTable.getTableHeader().setReorderingAllowed(false);
        techsSignedOutTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                techsSignedOutTableMouseClicked(evt);
            }
        });
        techsSignedOutScrollPane.setViewportView(techsSignedOutTable);

        javax.swing.GroupLayout techsSignedOutPanelLayout = new javax.swing.GroupLayout(techsSignedOutPanel);
        techsSignedOutPanel.setLayout(techsSignedOutPanelLayout);
        techsSignedOutPanelLayout.setHorizontalGroup(
            techsSignedOutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(techsSignedOutScrollPane)
            .addGroup(techsSignedOutPanelLayout.createSequentialGroup()
                .addComponent(techsSignedOutLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(signoutTableFilterLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(signoutFilterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        techsSignedOutPanelLayout.setVerticalGroup(
            techsSignedOutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(techsSignedOutPanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(techsSignedOutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(techsSignedOutLabel)
                    .addComponent(signoutFilterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(signoutTableFilterLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(techsSignedOutScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout centerPanelLayout = new javax.swing.GroupLayout(centerPanel);
        centerPanel.setLayout(centerPanelLayout);
        centerPanelLayout.setHorizontalGroup(
            centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(centerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(techsSignedOutPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, centerPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(timeStampPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        centerPanelLayout.setVerticalGroup(
            centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(centerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(timeStampPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(techsSignedOutPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        rightPanel.setPreferredSize(new java.awt.Dimension(300, 469));

        techsLoggedInLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        techsLoggedInLabel.setText("Techs logged in:");

        techsLoggedInList.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        techsLoggedInList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Wes Hampson (12:00:00 AM)" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        techsLoggedInList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        techsLoggedInList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                techsLoggedInListMouseClicked(evt);
            }
        });
        techsLoggedInScrollPane.setViewportView(techsLoggedInList);

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(techsLoggedInScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                    .addComponent(techsLoggedInLabel))
                .addContainerGap())
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(techsLoggedInLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(techsLoggedInScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(rightPanel, java.awt.BorderLayout.LINE_END);

        bottomPanel.setPreferredSize(new java.awt.Dimension(645, 85));

        techsInCountTitle.setText("Techs in:");

        techsInCount.setText("0");

        techsOutCountTitle.setText("Techs out:");

        techsOutCount.setText("0");

        techsSignedOutCountTitle.setText("Techs signed out:");

        techsSignedOutCount.setText("0");

        showAllCountersLabel.setForeground(new java.awt.Color(0, 0, 255));
        showAllCountersLabel.setText("<html><u>Show all counters...</u></html>");
        showAllCountersLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        signOutButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        signOutButton.setText("Sign Out...");
        signOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signOutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bottomPanelLayout = new javax.swing.GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bottomPanelLayout.createSequentialGroup()
                                .addComponent(techsSignedOutCountTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(techsOutCount)
                                    .addComponent(techsSignedOutCount)
                                    .addComponent(techsInCount)))
                            .addComponent(showAllCountersLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(signOutButton))
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(techsOutCountTitle)
                            .addComponent(techsInCountTitle))
                        .addGap(0, 1001, Short.MAX_VALUE)))
                .addContainerGap())
        );
        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(signOutButton))
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(techsInCountTitle)
                            .addComponent(techsInCount))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(techsOutCountTitle)
                            .addComponent(techsOutCount))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(techsSignedOutCountTitle)
                            .addComponent(techsSignedOutCount))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showAllCountersLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(bottomPanel, java.awt.BorderLayout.PAGE_END);

        optionsMenu.setText("Options");

        optionsCreateNewTechMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        optionsCreateNewTechMenuItem.setText("Create New Tech...");
        optionsCreateNewTechMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsCreateNewTechMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(optionsCreateNewTechMenuItem);
        optionsMenu.add(optionsMenuSeparator1);

        optionsSettingsMenuItem.setText("Settings...");
        optionsSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsSettingsMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(optionsSettingsMenuItem);
        optionsMenu.add(optionsMenuSeparator2);

        optionsExitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        optionsExitMenuItem.setText("Exit");
        optionsExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsExitMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(optionsExitMenuItem);

        menuBar.add(optionsMenu);

        debugMenu.setText("Debug");

        debugMenuLoadSettingsMenuItem.setText("Load Settings");
        debugMenuLoadSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugMenuLoadSettingsMenuItemActionPerformed(evt);
            }
        });
        debugMenu.add(debugMenuLoadSettingsMenuItem);

        debugMenuSaveSettingsMenuItem.setText("Save Settings");
        debugMenuSaveSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugMenuSaveSettingsMenuItemActionPerformed(evt);
            }
        });
        debugMenu.add(debugMenuSaveSettingsMenuItem);
        debugMenu.add(debugMenuSeparator1);

        debugLoadTechDataMenuItem.setText("Load Tech Data");
        debugLoadTechDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugLoadTechDataMenuItemActionPerformed(evt);
            }
        });
        debugMenu.add(debugLoadTechDataMenuItem);

        debugSaveTechDataMenuItem.setText("Save Tech Data");
        debugSaveTechDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugSaveTechDataMenuItemActionPerformed(evt);
            }
        });
        debugMenu.add(debugSaveTechDataMenuItem);
        debugMenu.add(debugMenuSeparator2);

        debugLoadSignoutDataMenuItem.setText("Load Signout Data");
        debugLoadSignoutDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugLoadSignoutDataMenuItemActionPerformed(evt);
            }
        });
        debugMenu.add(debugLoadSignoutDataMenuItem);

        debugSaveSignoutDataMenuItem.setText("Save Signout Data");
        debugSaveSignoutDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugSaveSignoutDataMenuItemActionPerformed(evt);
            }
        });
        debugMenu.add(debugSaveSignoutDataMenuItem);
        debugMenu.add(debugMenuSeparator3);

        debugStartClockMenuItem.setText("Start Clock");
        debugStartClockMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugStartClockMenuItemActionPerformed(evt);
            }
        });
        debugMenu.add(debugStartClockMenuItem);

        debugStopClockMenuItem.setText("Stop Clock");
        debugStopClockMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugStopClockMenuItemActionPerformed(evt);
            }
        });
        debugMenu.add(debugStopClockMenuItem);

        menuBar.add(debugMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void iDTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iDTextFieldActionPerformed
        timeStampButtonActionPerformed(evt);
    }//GEN-LAST:event_iDTextFieldActionPerformed

    private void timeStampButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeStampButtonActionPerformed
        String input = iDTextField.getText().trim();
        int iDNumber;
        Tech tech = null;
        boolean launchCreateNewTechWizard = false;
        if (input.isEmpty()) {
            // TODO: Log this
            System.err.println("Please enter an ID number.");
            JOptionPane.showMessageDialog(this, "Please enter an ID number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            iDTextField.setText("");
            iDTextField.requestFocus();
            return;
        }
        try {
            iDNumber = Integer.parseInt(input);
            if (iDNumber < 1) {
                System.err.println("input cannot be be negative or 0");
                JOptionPane.showMessageDialog(this, "ID number cannot be negative.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                iDTextField.setText("");
                iDTextField.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            // TODO: Log this
            System.err.println("Please enter an ID number.");
            JOptionPane.showMessageDialog(this, "Please enter an ID number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            iDTextField.setText("");
            iDTextField.requestFocus();
            return;
        }
        try {
            tech = TechManager.getTechByID(iDNumber);
        } catch (TechException ex) {
            // TODO: Log this
            System.err.println(ex.getMessage());
            launchCreateNewTechWizard = true;
        }
        if (launchCreateNewTechWizard) {
            tech = createNewTechWizard(iDNumber);
        }
        if (tech == null) {
            iDTextField.setText("");
            iDTextField.requestFocus();
            return;
        }
        if (!tech.isLoggedIn()) {
            tech.logIn();
            System.out.println("Logged in: " + tech.getID() + " (" + tech.getName() + ")");
        } else {
            tech.logOut();
            System.out.println("Logged out: " + tech.getID() + " (" + tech.getName() + ")");
        }
        updateLists();
        iDTextField.setText("");
        iDTextField.requestFocus();
    }//GEN-LAST:event_timeStampButtonActionPerformed

    private void optionsCreateNewTechMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsCreateNewTechMenuItemActionPerformed
        int iDNumber = getTechIDNumberFromUser("Please enter an ID number.", "Create New Tech Wizard");
        if (iDNumber == -1) {
            return;
        }
        if (TechManager.techExists(iDNumber)) {
            // TODO: Log this
            JOptionPane.showMessageDialog(this, "Tech already exists!", "Tech Exists", JOptionPane.ERROR_MESSAGE);
            return;
        }
        createNewTechWizard(iDNumber);
    }//GEN-LAST:event_optionsCreateNewTechMenuItemActionPerformed

    private void debugSaveTechDataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugSaveTechDataMenuItemActionPerformed
        saveTechData();
    }//GEN-LAST:event_debugSaveTechDataMenuItemActionPerformed

    private void debugLoadTechDataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugLoadTechDataMenuItemActionPerformed
        loadTechData();
    }//GEN-LAST:event_debugLoadTechDataMenuItemActionPerformed

    private void debugStartClockMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugStartClockMenuItemActionPerformed
        startClock();
        System.out.println("Started clock.");
    }//GEN-LAST:event_debugStartClockMenuItemActionPerformed

    private void debugStopClockMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugStopClockMenuItemActionPerformed
        stopClock();
        System.out.println("Stopped clock.");
    }//GEN-LAST:event_debugStopClockMenuItemActionPerformed

    private void optionsSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsSettingsMenuItemActionPerformed
        SettingsDialog sd = new SettingsDialog(this, true);
        sd.setLocationRelativeTo(this);
        sd.setVisible(true);
    }//GEN-LAST:event_optionsSettingsMenuItemActionPerformed

    private void optionsExitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsExitMenuItemActionPerformed
        dispose();
        System.exit(0); // Calls shutdown hook
    }//GEN-LAST:event_optionsExitMenuItemActionPerformed

    private void debugMenuSaveSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugMenuSaveSettingsMenuItemActionPerformed
        saveSettings();
    }//GEN-LAST:event_debugMenuSaveSettingsMenuItemActionPerformed

    private void debugMenuLoadSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugMenuLoadSettingsMenuItemActionPerformed
        loadSettings();
    }//GEN-LAST:event_debugMenuLoadSettingsMenuItemActionPerformed

    private void techsLoggedOutListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_techsLoggedOutListMouseClicked
        techsSignedOutTable.clearSelection();
        techsLoggedInList.clearSelection();
        if (evt.getButton() == MouseEvent.BUTTON3) {
            if (techsLoggedOutList.getModel().getSize() != 0) {
                techsLoggedOutList.setSelectedIndex(techsLoggedOutList.locationToIndex(evt.getPoint()));
                techsLoggedOutPopupMenu.show(techsLoggedOutList, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_techsLoggedOutListMouseClicked

    private void techsLoggedInListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_techsLoggedInListMouseClicked
        techsSignedOutTable.clearSelection();
        techsLoggedOutList.clearSelection();
        if (evt.getButton() == MouseEvent.BUTTON3) {
            if (techsLoggedInList.getModel().getSize() != 0) {
                techsLoggedInList.setSelectedIndex(techsLoggedInList.locationToIndex(evt.getPoint()));
                techsLoggedInPopupMenu.show(techsLoggedInList, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_techsLoggedInListMouseClicked

    private void signOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signOutButtonActionPerformed
        String input = iDTextField.getText().trim();
        int techID;
        if (!input.isEmpty()) {
            try {
                techID = Integer.parseInt(input);
                if (techID < 0) {
                    System.err.println("input cannot be be negative");
                    JOptionPane.showMessageDialog(this, "ID number cannot be negative.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    iDTextField.setText("");
                    iDTextField.requestFocus();
                    return;
                }
            } catch (NumberFormatException ex) {
                // TODO: Log this
                System.err.println("Please enter an ID number.");
                JOptionPane.showMessageDialog(this, "Please enter an ID number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                iDTextField.setText("");
                iDTextField.requestFocus();
                return;
            }
        } else {
            techID = getTechIDNumberFromUser();
            if (techID == -1) {
                return;
            }
        }
        try {
            TechManager.getTechByID(techID);
        } catch (TechException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Tech not found for ID: " + techID, "Tech Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SignoutDialog sd = new SignoutDialog(techID, this, true);
        sd.setLocationRelativeTo(this);
        sd.setVisible(true);
        updateSignoutTable();
    }//GEN-LAST:event_signOutButtonActionPerformed

    private void debugSaveSignoutDataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugSaveSignoutDataMenuItemActionPerformed
        saveSignoutData();
    }//GEN-LAST:event_debugSaveSignoutDataMenuItemActionPerformed

    private void debugLoadSignoutDataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugLoadSignoutDataMenuItemActionPerformed
        loadSignoutData();
    }//GEN-LAST:event_debugLoadSignoutDataMenuItemActionPerformed

    private void techsSignedOutTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_techsSignedOutTableMouseClicked
        techsLoggedOutList.clearSelection();
        techsLoggedInList.clearSelection();
        if (evt.getButton() == MouseEvent.BUTTON3) {
            int row = techsSignedOutTable.rowAtPoint(evt.getPoint());
            if ((row >= 0) && (row < techsSignedOutTable.getRowCount())) {
                techsSignedOutTable.setRowSelectionInterval(row, row);
            } else {
                techsSignedOutTable.clearSelection();
            }
            if (techsSignedOutTable.getSelectedRow() < 0) {
                return;
            }
            techsSignedOutPopupMenu.show(techsSignedOutTable, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_techsSignedOutTableMouseClicked

    private void signoutFilterComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signoutFilterComboBoxActionPerformed
        updateSignoutTable();
    }//GEN-LAST:event_signoutFilterComboBoxActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        techsSignedOutTable.clearSelection();
        techsLoggedOutList.clearSelection();
        techsLoggedInList.clearSelection();
    }//GEN-LAST:event_formMouseClicked

    private void iDTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iDTextFieldMouseClicked
        techsSignedOutTable.clearSelection();
        techsLoggedOutList.clearSelection();
        techsLoggedInList.clearSelection();
    }//GEN-LAST:event_iDTextFieldMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JLabel clockDisplay;
    private javax.swing.JLabel dateDisplay;
    private javax.swing.JMenuItem debugLoadSignoutDataMenuItem;
    private javax.swing.JMenuItem debugLoadTechDataMenuItem;
    private javax.swing.JMenu debugMenu;
    private javax.swing.JMenuItem debugMenuLoadSettingsMenuItem;
    private javax.swing.JMenuItem debugMenuSaveSettingsMenuItem;
    private javax.swing.JPopupMenu.Separator debugMenuSeparator1;
    private javax.swing.JPopupMenu.Separator debugMenuSeparator2;
    private javax.swing.JPopupMenu.Separator debugMenuSeparator3;
    private javax.swing.JMenuItem debugSaveSignoutDataMenuItem;
    private javax.swing.JMenuItem debugSaveTechDataMenuItem;
    private javax.swing.JMenuItem debugStartClockMenuItem;
    private javax.swing.JMenuItem debugStopClockMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JTextField iDTextField;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem optionsCreateNewTechMenuItem;
    private javax.swing.JMenuItem optionsExitMenuItem;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.JPopupMenu.Separator optionsMenuSeparator1;
    private javax.swing.JPopupMenu.Separator optionsMenuSeparator2;
    private javax.swing.JMenuItem optionsSettingsMenuItem;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JLabel showAllCountersLabel;
    private javax.swing.JButton signOutButton;
    private javax.swing.JComboBox signoutFilterComboBox;
    private javax.swing.JLabel signoutTableFilterLabel;
    private javax.swing.JLabel techsInCount;
    private javax.swing.JLabel techsInCountTitle;
    private javax.swing.JLabel techsLoggedInLabel;
    private javax.swing.JList techsLoggedInList;
    private javax.swing.JScrollPane techsLoggedInScrollPane;
    private javax.swing.JLabel techsLoggedOutLabel;
    private javax.swing.JList techsLoggedOutList;
    private javax.swing.JScrollPane techsLoggedOutScrollPane;
    private javax.swing.JLabel techsOutCount;
    private javax.swing.JLabel techsOutCountTitle;
    private javax.swing.JLabel techsSignedOutCount;
    private javax.swing.JLabel techsSignedOutCountTitle;
    private javax.swing.JLabel techsSignedOutLabel;
    private javax.swing.JPanel techsSignedOutPanel;
    private javax.swing.JScrollPane techsSignedOutScrollPane;
    private javax.swing.JTable techsSignedOutTable;
    private javax.swing.JButton timeStampButton;
    private javax.swing.JLabel timeStampLabel;
    private javax.swing.JPanel timeStampPanel;
    // End of variables declaration//GEN-END:variables
}