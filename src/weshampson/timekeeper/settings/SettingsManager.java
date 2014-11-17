
package weshampson.timekeeper.settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import weshampson.timekeeper.tech.TechManager;
import weshampson.timekeeper.xml.XMLWritable;

/**
 *
 * @author  Wes Hampson
 * @version 0.3.0 (Nov 17, 2014)
 * @since   0.2.0 (Jul 28, 2014)
 */
public class SettingsManager implements XMLWritable {
    public static final File SETTINGS_XML_FILE = new File("./data/settings.xml");
    public static final String PROPERTY_ACTIVITY_LOG_DIR = "activityLogDir";
    public static final String PROPERTY_ADMIN_APPROVAL_ENABLED = "adminApprovalEnabled";
    public static final String PROPERTY_ADMIN_PASSWORD = "adminPassword";
    public static final String PROPERTY_AUTO_OUT_AT_MIDNIGHT = "autoOutAtMidnight";
    public static final String PROPERTY_LATE_SIGNOUT_TIME = "lateSignoutTime";
    public static final String PROPERTY_LATE_SIGNOUT_TIME_FORMAT = "lateSignoutTimeFormat";
    public static final String PROPERTY_SIGNOUT_DATA_FILE = "signoutDataFile";
    public static final String PROPERTY_SIGNOUT_FILTER_STATE = "signoutFilterState";
    public static final String PROPERTY_TECH_DATA_FILE = "techDataFile";
    public static final String PROPERTY_TECHS_LOGGED_IN_SORT_ID = "techsLoggedInSortID";
    public static final String PROPERTY_TECHS_LOGGED_OUT_SORT_ID = "techsLoggedOutSortID";
    protected static final String XMLTAG_ROOT = "settings";
    protected static final String XMLATTR_FILE_VERSION = "version";
    private static final Map<String, String> DEFAULT_SETTINGS;
    static {
        DEFAULT_SETTINGS = new HashMap<>();
        DEFAULT_SETTINGS.put(PROPERTY_ACTIVITY_LOG_DIR, new File("./data/activity").getPath());
        DEFAULT_SETTINGS.put(PROPERTY_ADMIN_APPROVAL_ENABLED, "false");
        DEFAULT_SETTINGS.put(PROPERTY_AUTO_OUT_AT_MIDNIGHT, "true");
        DEFAULT_SETTINGS.put(PROPERTY_SIGNOUT_DATA_FILE, new File("./data/signoutData.xml").getPath());
        DEFAULT_SETTINGS.put(PROPERTY_LATE_SIGNOUT_TIME, "12:00 PM");
        DEFAULT_SETTINGS.put(PROPERTY_LATE_SIGNOUT_TIME_FORMAT, "hh:mm a");
        DEFAULT_SETTINGS.put(PROPERTY_SIGNOUT_FILTER_STATE, "6");
        DEFAULT_SETTINGS.put(PROPERTY_TECH_DATA_FILE, new File("./data/techData.xml").getPath());
        DEFAULT_SETTINGS.put(PROPERTY_TECHS_LOGGED_IN_SORT_ID, Integer.toString(TechManager.SORTBY_LAST_LOG_IN));
        DEFAULT_SETTINGS.put(PROPERTY_TECHS_LOGGED_OUT_SORT_ID, Integer.toString(TechManager.SORTBY_FIRST_NAME));
    }
    private static final Map<String, String> SETTINGS = new HashMap<>(DEFAULT_SETTINGS);
    public static String get(String property) {
        return(SETTINGS.get(property));
    }
    public static String getDefault(String property) {
        return(DEFAULT_SETTINGS.get(property));
    }
    public static void set(String property, String value) {
        SETTINGS.put(property, value);
    }
    public static void loadSettings() throws IOException, DocumentException {
        if (!SETTINGS_XML_FILE.exists()) {
            Logger.log(Level.WARNING, "Settings file not found!");
            Logger.log(Level.INFO, "Creating new settings file...");
            SETTINGS_XML_FILE.getParentFile().mkdirs();
            SETTINGS_XML_FILE.createNewFile();
            Logger.log(Level.INFO, "Settings file successfully created at " + SETTINGS_XML_FILE.getAbsolutePath());
            return;
        }
        SAXReader sAXReader = new SAXReader();
        Document formattedDocument = sAXReader.read(SETTINGS_XML_FILE);
        OutputFormat outputFormat = OutputFormat.createCompactFormat();
        StringWriter stringWriter = new StringWriter();
        XMLWriter xMLWriter = new XMLWriter(stringWriter, outputFormat);
        xMLWriter.write(formattedDocument);
        Document unformattedDocument = DocumentHelper.parseText(stringWriter.toString());
        Element root = unformattedDocument.getRootElement();
        if (!root.getName().equals(XMLTAG_ROOT)) {
            Logger.log(Level.ERROR, "Invalid settings file!");
            JOptionPane.showMessageDialog(null, "Invalid settings file!", "Error Launching " + Main.APPLICATION_TITLE, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        if (!root.attributeValue(XMLATTR_FILE_VERSION).equalsIgnoreCase(Main.APPLICATION_VERSION)) {
            Logger.log(Level.WARNING, "Settings file version does not match program version!");
        }
        for (Iterator i = root.elementIterator(); i.hasNext();) {
            Element element = (Element)i.next();
            SETTINGS.put(element.getName(), element.getText());
        }
        Logger.log(Level.INFO, "Loaded settings from file: " + SETTINGS_XML_FILE.getAbsolutePath());
    }
    public static void saveSettings() throws IOException {
        Document xmlDocument = new SettingsManager().getXMLData();
        xmlDocument.getRootElement().addAttribute(XMLATTR_FILE_VERSION, Main.APPLICATION_VERSION);
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setIndentSize(4);
        XMLWriter xMLWriter = new XMLWriter(new FileWriter(SETTINGS_XML_FILE), outputFormat);
        xMLWriter.write(xmlDocument);
        xMLWriter.close();
        Logger.log(Level.INFO, "Settings saved to file: " + SETTINGS_XML_FILE.getAbsolutePath());
    }
    @Override
    public Document getXMLData() {
        Document doc = DocumentHelper.createDocument();
        Element rootElement = doc.addElement(XMLTAG_ROOT);
        for (Map.Entry keyPair : SETTINGS.entrySet()) {
            rootElement.addElement((String)keyPair.getKey()).addText((String)keyPair.getValue());
        }
        return(doc);
    }
}