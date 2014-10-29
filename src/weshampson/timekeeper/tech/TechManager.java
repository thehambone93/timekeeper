
package weshampson.timekeeper.tech;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import weshampson.commonutils.logging.Level;
import weshampson.commonutils.logging.Logger;
import weshampson.timekeeper.settings.SettingsManager;

/**
 * This class maintains a master list of {@code Tech} objects throughout the
 * course of the running program.
 * 
 * @author  Wes Hampson
 * @version 0.3.0 (Oct 29, 2014)
 * @since   0.1.0 (Jul 17, 2014)
 */
public class TechManager {
    public static final int SORTBY_FIRST_NAME = 0;
    public static final int SORTBY_LAST_NAME = 1;
    public static final int SORTBY_LAST_LOG_IN = 2;
    protected static final String XML_ROOT = "techData";
    protected static final String XML_TECH_ROOT = "tech";
    protected static final String XML_TECH_ID = "id";
    protected static final String XML_TECH_NAME = "name";
    protected static final String XML_TECH_CREATION_DATE = "creationDate";
    protected static final String XML_TECH_LAST_LOGIN_DATE = "lastLoginDate";
    protected static final String XML_TECH_LAST_SIGNOUT_DATE = "lastSignoutDate";
    protected static final String XML_TECH_NEXT_SIGNOUT_DATE = "nextSignoutDate";
    protected static final String XML_TECH_IS_LOGGED_IN = "isLoggedIn";
    protected static final String XML_TECH_IS_SIGNED_OUT = "isSignedOut";
    protected static final String XML_TECH_LOGIN_COUNT = "loginCount";
    protected static final String XML_TECH_SIGNOUT_COUNT = "signoutCount";
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
            return(t1.getLastLoginDate().compareTo(t2.getLastLoginDate()));
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
                throw new TechException("duplicate tech found for ID: " + tech.getID());
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
     * @throws TechException thrown if the {@code Tech} cannot be found given the
     * specified ID
     */
    public static synchronized Tech getTechByID(int techID) throws TechException {
        for (Tech tech : TECH_LIST) {
            if (tech.getID() == techID) {
                return(tech);
            }
        }
        throw new TechException("tech not found for ID:  " + techID);
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
        List<Tech> techsInList = new ArrayList<Tech>();
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
        List<Tech> techsOutList = new ArrayList<Tech>();
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
            Logger.log(Level.INFO, "Tech data file successfully created at " + xMLFile.getAbsolutePath());
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
        if (!root.getName().equals(XML_ROOT)) {
            // Change this exception
            throw new RuntimeException("wrong XML file!");
        }
        for (Iterator i = root.elementIterator(XML_TECH_ROOT); i.hasNext();) {
            Element techElement = (Element)i.next();
            Tech tech = new Tech(techElement);
            addTech(tech);
        }
        Logger.log(Level.INFO, "Loaded tech data from file: " + xMLFile.getAbsolutePath());
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
        Element root = xMLDocument.addElement(XML_ROOT);
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setIndentSize(4);
        XMLWriter xMLWriter = new XMLWriter(new FileWriter(xMLFile), outputFormat);
        for (Tech tech : TECH_LIST) {
            Document techXMLDocument = tech.getXMLData();
            root.add(techXMLDocument.getRootElement());
        }
        xMLWriter.write(xMLDocument);
        xMLWriter.close();
        Logger.log(Level.INFO, "Tech data saved to file: " + xMLFile.getAbsolutePath());
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

    /**
     * Updates a {@code Tech} object in the master list.
     * 
     * @param tech {@code Tech} object to be updated
     * @throws TechException thrown if the specified {@code Tech} object
     * cannot be found
     */
    public static synchronized void updateTech(Tech tech) throws TechException {
        for (int i = 0; i < TECH_LIST.size(); i++) {
            Tech existingTech = TECH_LIST.get(i);
            if (existingTech.getID() == tech.getID()) {
                TECH_LIST.set(i, tech);
                return;
            }
        }
        Collections.sort(TECH_LIST, new TechComparator());
        throw new TechException("tech not found for ID:  " + tech.getID());
    }
}