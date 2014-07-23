
package weshampson.timekeeper.xml;

import org.dom4j.Document;

/**
 * This interface is meant to be implemented in classes where data is to be
 * written to an XML file.
 * 
 * @author  Wes Hampson
 * @version 0.1.0 (Jul 23, 2014)
 * @since   0.1.0 (Jul 22, 2014)
 */
public interface XMLWritable {

    /**
     * Retrieves the data to be written in XML format. The way in which the data
     * is written must be defined by classes that implement {@code XMLWritable}.
     * 
     * @return Document containing XML data
     */
    public Document getXMLData();
}
