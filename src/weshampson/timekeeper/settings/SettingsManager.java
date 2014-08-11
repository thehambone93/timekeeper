
package weshampson.timekeeper.settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import weshampson.timekeeper.xml.XMLWritable;

/**
 *
 * @author  Wes Hampson
 * @version 0.2.0 (Aug 11, 2014)
 * @since   0.2.0 (Jul 28, 2014)
 */
public class SettingsManager implements XMLWritable {
    public static final File SETTINGS_XML_FILE = new File("settings.xml");
    public static final String PROPERTY_ADMIN_APPROVAL_ENABLED = "adminApprovalEnabled";
    public static final String PROPERTY_TECH_DATA_FILE = "techDataFile";
    public static final String PROPERTY_SIGNOUT_DATA_FILE = "signoutDataFile";
    protected static final String XML_ROOT = "settings";
    private static final Map<String, String> DEFAULT_SETTINGS;
    static {
        DEFAULT_SETTINGS = new HashMap<>();
        DEFAULT_SETTINGS.put(PROPERTY_ADMIN_APPROVAL_ENABLED, "false");
        DEFAULT_SETTINGS.put(PROPERTY_SIGNOUT_DATA_FILE, new File("signoutData.xml").getAbsolutePath());
        DEFAULT_SETTINGS.put(PROPERTY_TECH_DATA_FILE, new File("techData.xml").getAbsolutePath());
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
        SAXReader sAXReader = new SAXReader();
        Document formattedDocument = sAXReader.read(SETTINGS_XML_FILE);
        OutputFormat outputFormat = OutputFormat.createCompactFormat();
        StringWriter stringWriter = new StringWriter();
        XMLWriter xMLWriter = new XMLWriter(stringWriter, outputFormat);
        xMLWriter.write(formattedDocument);
        Document unformattedDocument = DocumentHelper.parseText(stringWriter.toString());
        Element root = unformattedDocument.getRootElement();
        if (!root.getName().equals(XML_ROOT)) {
            // change this exception
            throw new RuntimeException("wrong XML file!");
        }
        for (Iterator i = root.elementIterator(); i.hasNext();) {
            Element element = (Element)i.next();
            SETTINGS.put(element.getName(), element.getText());
        }
        System.out.println("Loaded settings from file: " + SETTINGS_XML_FILE.getAbsolutePath());
    }
    public static void saveSettings() throws IOException {
        Document xmlDocument = new SettingsManager().getXMLData();
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setIndentSize(4);
        XMLWriter xMLWriter = new XMLWriter(new FileWriter(SETTINGS_XML_FILE), outputFormat);
        xMLWriter.write(xmlDocument);
        xMLWriter.close();
        System.out.println("Settings saved to file: " + SETTINGS_XML_FILE.getAbsolutePath());
    }
    @Override
    public Document getXMLData() {
        Document doc = DocumentHelper.createDocument();
        Element rootElement = doc.addElement(XML_ROOT);
        for (Map.Entry keyPair : SETTINGS.entrySet()) {
            rootElement.addElement((String)keyPair.getKey()).addText((String)keyPair.getValue());
        }
        return(doc);
    }
}