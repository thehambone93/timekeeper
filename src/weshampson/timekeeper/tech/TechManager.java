
package weshampson.timekeeper.tech;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import weshampson.commonutils.logging.Level;
import weshampson.commonutils.logging.Logger;
import weshampson.timekeeper.Main;

/**
 * This class maintains a master list of {@code Tech} objects throughout the
 * course of the running program.
 * 
 * @author  Wes Hampson
 * @version 1.0.0 (Jan 30, 2015)
 * @since   0.1.0 (Jul 17, 2014)
 */
public class TechManager {
    public static final int SORTBY_FIRST_NAME = 0;
    public static final int SORTBY_LAST_NAME = 1;
    public static final int SORTBY_LAST_LOG_IN = 2;
    protected static final String XMLTAG_ROOT = "techData";
    protected static final String XMLATTR_FILE_VERSION = "version";
    protected static final String XMLTAG_TECH_ROOT = "tech";
    protected static final String XMLATTR_TECH_ID = "id";
    protected static final String XMLTAG_TECH_NAME = "name";
    protected static final String XMLTAG_TECH_CREATION_DATE = "creationDate";
    protected static final String XMLTAG_TECH_LAST_LOGIN_DATE = "lastLoginDate";
    protected static final String XMLTAG_TECH_LAST_SIGNOUT_DATE = "lastSignoutDate";
    protected static final String XMLTAG_TECH_IS_ADMIN = "isAdmin";
    protected static final String XMLTAG_TECH_IS_LOGGED_IN = "isLoggedIn";
    protected static final String XMLTAG_TECH_IS_SIGNED_OUT = "isSignedOut";
    protected static final String XMLTAG_TECH_LOGIN_COUNT = "loginCount";
    protected static final String XMLTAG_TECH_SIGNOUT_COUNT = "signoutCount";
    private static final List<Tech> TECH_LIST = new ArrayList<>();
    private static final Comparator FIRST_NAME_COMPARATOR = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            Tech t1 = (Tech)o1;
            Tech t2 = (Tech)o2;
            return(t1.getName().compareToIgnoreCase(t2.getName()));
        }
    };
    private static final Comparator LAST_NAME_COMPARATOR = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            Tech t1 = (Tech)o1;
            Tech t2 = (Tech)o2;
            String t1LastName = t1.getName();
            String t2LastName = t2.getName();
            if (t1.getName().contains(" ")) {
                t1LastName = t1.getName().substring(t1.getName().indexOf(' ') + 1);
            }
            if (t2.getName().contains(" ")) {
                t2LastName = t2.getName().substring(t2.getName().indexOf(' ') + 1);
            }
            return(t1LastName.compareToIgnoreCase(t2LastName));
        }
    };
    private static final Comparator LAST_LOG_IN_COMPARATOR = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            Tech t1 = (Tech)o1;
            Tech t2 = (Tech)o2;
            Date t1LastLoginDate = t1.getLastLoginDate();
            Date t2LastLoginDate = t2.getLastLoginDate();
            if (t1LastLoginDate == null && t2LastLoginDate == null) {
                return(0);
            }else if (t1LastLoginDate == null) {
                return(1);
            } else if (t2LastLoginDate == null) {
                return(-1);
            }
            return(t1LastLoginDate.compareTo(t2LastLoginDate));
        }
    };
    private static int techsInSortBy = SORTBY_FIRST_NAME;
    private static int techsOutSortBy = SORTBY_FIRST_NAME;

    /**
     * Adds a {@code Tech} object to the master list.
     * 
     * @param tech the {@code Tech} to be added
     * @throws TechException thrown if the tech already exists in the list
     * (determined by a duplicate ID number)
     */
    public static synchronized void addTech(Tech tech) throws TechException {
        for (Tech existingTech : TECH_LIST) {
            if (existingTech.getID() == tech.getID()) {
                Logger.log(Level.WARNING, "duplicate tech found for ID: " + tech.getID());
            }
        }
        TECH_LIST.add(tech);
        Collections.sort(TECH_LIST, new TechComparator());
    }

    /**
     * Returns a {@code Tech} object given a specific ID.
     * 
     * @param techID the tech's ID
     * @return {@code Tech} containing the specified ID
     * @throws TechNotFoundException thrown if the {@code Tech} cannot be found
     * given the specified ID
     */
    public static synchronized Tech getTechByID(int techID) throws TechNotFoundException {
        for (Tech tech : TECH_LIST) {
            if (tech.getID() == techID) {
                return(tech);
            }
        }
        throw new TechNotFoundException("tech not found for ID:  " + techID);
    }
    public static synchronized List<Tech> getTechList() {
        return(TECH_LIST);
    }
    public static synchronized int getTechsInCount() {
        int count = 0;
        for (Tech t : TECH_LIST) {
            if (t.isLoggedIn()) {
                count++;
            }
        }
        return(count);
    }

    /**
     * Creates a {@code DefaultListModel} containing all techs who are
     * currently logged in. This is intended for the "Techs logged in"
     * {@link javax.swing.JList} in the {@link weshampson.timekeeper.gui.MainWindow}
     * class.
     * 
     * @see weshampson.timekeeper.tech.TechManager#getTechsOutListModel()
     * @return the {@code DefaultListModel}
     */
    @SuppressWarnings("unchecked")
    public static synchronized DefaultListModel<Tech> getTechsInListModel() {
        DefaultListModel<Tech> model = new DefaultListModel<>();
        List<Tech> techsInList = new ArrayList<>();
        for (Tech tech : TECH_LIST) {
            if (tech.isLoggedIn()) {
                techsInList.add(tech);
            }
        }
        switch (techsInSortBy) {
            case SORTBY_FIRST_NAME:
                Collections.sort(techsInList, FIRST_NAME_COMPARATOR);
                break;
            case SORTBY_LAST_NAME:
                Collections.sort(techsInList, LAST_NAME_COMPARATOR);
                break;
            case SORTBY_LAST_LOG_IN:
                Collections.sort(techsInList, LAST_LOG_IN_COMPARATOR);
        }
        for (Tech tech : techsInList) {
            model.addElement(tech);
        }
        return(model);
    }
    public static synchronized int getTechsOutCount() {
        int count = 0;
        for (Tech t : TECH_LIST) {
            if (!t.isLoggedIn()) {
                count++;
            }
        }
        return(count);
    }

    /**
     * Creates a {@code DefaultListModel} containing all techs who are
     * currently logged out. This is intended for the "Techs logged out"
     * {@link javax.swing.JList} in the {@link weshampson.timekeeper.gui.MainWindow}
     * class.
     * 
     * @see weshampson.timekeeper.tech.TechManager#getTechsInListModel()
     * @return the {@code DefaultListModel}
     */
    @SuppressWarnings("unchecked")
    public static synchronized DefaultListModel<Tech> getTechsOutListModel() {
        DefaultListModel<Tech> model = new DefaultListModel<>();
        List<Tech> techsOutList = new ArrayList<>();
        for (Tech tech : TECH_LIST) {
            if (!tech.isLoggedIn()) {
                techsOutList.add(tech);
            }
        }
        switch (techsOutSortBy) {
            case SORTBY_FIRST_NAME:
                Collections.sort(techsOutList, FIRST_NAME_COMPARATOR);
                break;
            case SORTBY_LAST_NAME:
                Collections.sort(techsOutList, LAST_NAME_COMPARATOR);
                break;
            case SORTBY_LAST_LOG_IN:
                Collections.sort(techsOutList, LAST_LOG_IN_COMPARATOR);
        }
        for (Tech tech : techsOutList) {
            model.addElement(tech);
        }
        return(model);
    }
    public static int getTechsInSortingID() {
        return(techsInSortBy);
    }
    public static int getTechsOutSortingID() {
        return(techsOutSortBy);
    }

    /**
     * Loads tech data from a specified XML file. The loaded data is
     * subsequently cast to new {@code Tech} objects and added to the master
     * list.
     * 
     * @param xMLFile the XML file containing the tech data
     * @throws DocumentException thrown if the XML file is malformatted
     * @throws IOException thrown if there is a problem reading the XML file
     * @throws TechException thrown if a duplicate {@code Tech} is detected
     * @see weshampson.timekeeper.tech.TechManager#saveTechs(java.io.File) 
     */
    public static void loadTechs(File xMLFile) throws DocumentException, IOException, TechException {
        if (!xMLFile.exists()) {
            Logger.log(Level.WARNING, "Tech data file not found!");
            Logger.log(Level.INFO, "Creating new tech data file...");
            xMLFile.getParentFile().mkdirs();
            xMLFile.createNewFile();
            Logger.log(Level.INFO, "Tech data file successfully created at " + xMLFile.getCanonicalPath());
            return;
        }
        SAXReader sAXReader = new SAXReader();
        Document formattedDocument = sAXReader.read(xMLFile);
        OutputFormat outputFormat = OutputFormat.createCompactFormat();
        StringWriter stringWriter = new StringWriter();
        XMLWriter xMLWriter = new XMLWriter(stringWriter, outputFormat);
        xMLWriter.write(formattedDocument);
        Document unformattedDocument = DocumentHelper.parseText(stringWriter.toString());
        Element root = unformattedDocument.getRootElement();
        if (!root.getName().equals(XMLTAG_ROOT)) {
            Logger.log(Level.ERROR, "Invalid settings file!");
            JOptionPane.showMessageDialog(null, "Invalid tech data file!", "Error Launching " + Main.APPLICATION_TITLE, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        if (!root.attributeValue(XMLATTR_FILE_VERSION).equalsIgnoreCase(Main.APPLICATION_VERSION)) {
            Logger.log(Level.WARNING, "Tech data file version does not match program version!");
        }
        for (Iterator i = root.elementIterator(XMLTAG_TECH_ROOT); i.hasNext();) {
            Element techElement = (Element)i.next();
            Tech tech = new Tech(techElement);
            addTech(tech);
        }
        Logger.log(Level.INFO, "Loaded tech data from file: " + xMLFile.getCanonicalPath());
    }

    /**
     * Removes a the specified {@code Tech} object from the master list.
     * 
     * @param tech the {@code Tech} to be removed
     * @throws TechException thrown if the {@code Tech} cannot be found
     */
    public static synchronized void removeTech(Tech tech) throws TechException {
        for (Tech existingTech : TECH_LIST) {
            if (existingTech.getID() == tech.getID()) {
                TECH_LIST.remove(existingTech);
                return;
            }
        }
        throw new TechException("tech not found for ID:  " + tech.getID());
    }

    /**
     * Saves the data contained in the {@code Tech} objects in the master list
     * to the specified XML file.
     * 
     * @param xMLFile the file in which to save tech data
     * @throws IOException thrown if there is a problem writing to the file
     * @see weshampson.timekeeper.tech.TechManager#loadTechs(java.io.File) 
     * @see weshampson.timekeeper.xml.XMLWritable
     */
    public static void saveTechs(File xMLFile) throws IOException {
        Document xMLDocument = DocumentHelper.createDocument();
        Element root = xMLDocument.addElement(XMLTAG_ROOT).addAttribute(XMLATTR_FILE_VERSION, Main.APPLICATION_VERSION);
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setIndentSize(4);
        XMLWriter xMLWriter = new XMLWriter(new FileWriter(xMLFile), outputFormat);
        for (Tech tech : TECH_LIST) {
            Document techXMLDocument = tech.getXMLData();
            root.add(techXMLDocument.getRootElement());
        }
        xMLWriter.write(xMLDocument);
        xMLWriter.close();
        Logger.log(Level.INFO, "Tech data saved to file: " + xMLFile.getCanonicalPath());
    }
    public static void setTechsInSortingID(int sortBy) {
        techsInSortBy = sortBy;
        
        
    }
    public static void setTechsOutSortingID(int sortBy) {
        techsOutSortBy = sortBy;
    }

    /**
     * Returns a boolean value reflecting whether or not a {@code Tech} object
     * containing the specified ID exists in the master list.
     * 
     * @param techID the tech's ID
     * @return {@code true} if the specified tech exists in the master list,
     * otherwise {@code false}
     */
    public static synchronized boolean techExists(int techID) {
        for (Tech existingTech : TECH_LIST) {
            if (existingTech.getID() == techID) {
                return(true);
            }
        }
        return(false);
    }
}