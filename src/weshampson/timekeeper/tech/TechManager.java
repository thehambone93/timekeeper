
package weshampson.timekeeper.tech;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
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

/**
 * This class maintains a master list of {@code Tech} objects throughout the
 * course of the running program.
 * 
 * @author  Wes Hampson
 * @version 0.1.0 (Jul 23, 2014)
 * @since   0.1.0 (Jul 17, 2014)
 */
public class TechManager {
    public static final File TECH_DATA_XML_FILE = new File("techData.xml");
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
    private static final List<Tech> TECHLIST = new ArrayList<>();

    /**
     * Adds a {@code Tech} object to the master list.
     * 
     * @param tech the {@code Tech} to be added
     * @throws TechException thrown if the tech already exists in the list
     * (determined by a duplicate ID number)
     */
    public static void addTech(Tech tech) throws TechException {
        for (Tech existingTech : TECHLIST) {
            if (existingTech.getID() == tech.getID()) {
                throw new TechException("duplicate tech found for ID: " + tech.getID());
            }
        }
        TECHLIST.add(tech);
        Collections.sort(TECHLIST, Collections.reverseOrder());
    }

    /**
     * Returns a {@code Tech} object given a specific ID.
     * 
     * @param techID the tech's ID
     * @return {@code Tech} containing the specified ID
     * @throws TechException thrown if the {@code Tech} cannot be found given the
     * specified ID
     */
    public static Tech getTechByID(int techID) throws TechException {
        for (Tech tech : TECHLIST) {
            if (tech.getID() == techID) {
                return(tech);
            }
        }
        throw new TechException("tech not found for ID:  " + techID);
    }

    /**
     * Returns the master list of {@code Tech} objects in the form of a
     * {@code List<Tech>}.
     * 
     * @return the master list
     */
    public static List<Tech> getTechList() {
        return(TECHLIST);
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
    public static DefaultListModel<Tech> getTechsInListModel() {
        DefaultListModel<Tech> model = new DefaultListModel<>();
        for (Tech tech : TECHLIST) {
            if (tech.isLoggedIn()) {
                model.addElement(tech);
            }
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
    public static DefaultListModel<Tech> getTechsOutListModel() {
        DefaultListModel<Tech> model = new DefaultListModel<>();
        for (Tech tech : TECHLIST) {
            if (!tech.isLoggedIn()) {
                model.addElement(tech);
            }
        }
        return(model);
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
        Collections.sort(TECHLIST, Collections.reverseOrder());
        System.out.println("Loaded tech data from file: " + xMLFile.getAbsolutePath());
    }

    /**
     * Removes a the specified {@code Tech} object from the master list.
     * 
     * @param tech the {@code Tech} to be removed
     * @throws TechException thrown if the {@code Tech} cannot be found
     */
    public static void removeTech(Tech tech) throws TechException {
        for (Tech existingTech : TECHLIST) {
            if (existingTech.getID() == tech.getID()) {
                TECHLIST.remove(existingTech);
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
        for (Tech tech : TECHLIST) {
            Document techXMLDocument = tech.getXMLData();
            root.add(techXMLDocument.getRootElement());
        }
        xMLWriter.write(xMLDocument);
        xMLWriter.close();
        System.out.println("Tech data saved to file: " + xMLFile.getAbsolutePath());
    }

    /**
     * Returns a boolean value reflecting whether or not a {@code Tech} object
     * containing the specified ID exists in the master list.
     * 
     * @param techID the tech's ID
     * @return {@code true} if the specified tech exists in the master list,
     * otherwise {@code false}
     */
    public static boolean techExists(int techID) {
        for (Tech existingTech : TECHLIST) {
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
    public static void updateTech(Tech tech) throws TechException {
        for (int i = 0; i < TECHLIST.size(); i++) {
            Tech existingTech = TECHLIST.get(i);
            if (existingTech.getID() == tech.getID()) {
                TECHLIST.set(i, tech);
                return;
            }
        }
        Collections.sort(TECHLIST, Collections.reverseOrder());
        throw new TechException("tech not found for ID:  " + tech.getID());
    }
}