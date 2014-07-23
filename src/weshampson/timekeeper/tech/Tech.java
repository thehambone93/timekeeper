
package weshampson.timekeeper.tech;

import java.util.Date;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import static weshampson.timekeeper.tech.TechManager.*;
import weshampson.timekeeper.xml.XMLWritable;

/**
 * This class holds the login and signout data for workers (techs).
 * 
 * @author  Wes Hampson
 * @version 0.1.0 (Jul 23, 2014)
 * @since   0.1.0 (Jul 17, 2014)
 */
public class Tech implements XMLWritable, Comparable<Tech> {
    private final int techID;
    private String techName;
    private Date techCreationDate;
    private Date techLastLoginDate;
    private Date techLastSignoutDate;
    private Date techNextSignoutDate;
    private boolean techIsLoggedIn;
    private boolean techIsSignedOut;
    private int techLoginCount;
    private int techSignoutCount;
//    private ArrayList<Signout> techSignouts;
    
    /**
     * Creates a new {@code Tech} object.
     * 
     * @param techID the tech's ID number
     * @param techName the tech's name
     */
    public Tech(int techID, String techName) {
        this.techID = techID;
        this.techName = techName;
        this.techCreationDate = new Date();
    }

    /**
     * Creates a new {@code Tech} object from an XML element. This constructor
     * is used when creating a {@code Tech} object from existing data.
     * 
     * @param xmlElement the element containing the tech's data
     */
    public Tech(Element xmlElement) {
        this.techID = Integer.parseInt(xmlElement.attributeValue(XML_TECH_ID));
        for (Iterator i = xmlElement.elementIterator(); i.hasNext();) {
            Element e = (Element)i.next();
            String elementName = e.getName();
            String elementText = e.getText();
            if (elementText.isEmpty()) {
                continue;
            }
            switch (elementName) {
                case XML_TECH_CREATION_DATE:
                    this.techCreationDate = new Date(Long.parseLong(elementText));
                    break;
                case XML_TECH_IS_LOGGED_IN:
                    this.techIsLoggedIn = Boolean.parseBoolean(elementText);
                    break;
                case XML_TECH_IS_SIGNED_OUT:
                    this.techIsSignedOut = Boolean.parseBoolean(elementText);
                    break;
                case XML_TECH_LAST_LOGIN_DATE:
                    this.techLastLoginDate = new Date(Long.parseLong(elementText));
                    break;
                case XML_TECH_LAST_SIGNOUT_DATE:
                    this.techLastSignoutDate = new Date(Long.parseLong(elementText));
                    break;
                case XML_TECH_LOGIN_COUNT:
                    this.techLoginCount = Integer.parseInt(elementText);
                    break;
                case XML_TECH_NAME:
                    this.techName = elementText;
                    break;
                case XML_TECH_NEXT_SIGNOUT_DATE:
                    this.techNextSignoutDate = new Date(Long.parseLong(elementText));
                    break;
                case XML_TECH_SIGNOUT_COUNT:
                    Integer.parseInt(elementText);
                    break;
                default:
                    System.err.println("unrecognized XML element - " + elementName);
                    break;
            }
        }
    }

    /**
     * Marks this tech as "logged in." When this method is called, the following
     * operations are performed:<br>
     * &nbsp;1.) A boolean is set to {@code true} to show that the tech is
     * logged in<br>
     * &nbsp;2.) The tech's last login date is set to the current date<br>
     * &nbsp;3.) The tech's login count is incremented by 1<br>
     * &nbsp;4.) The tech is no longer marked as "signed out" if he/she was
     * previously signed out
     */
    public void logIn() {
        techIsLoggedIn = true;
        techIsSignedOut = false;
        techLastLoginDate = new Date();
        techLoginCount++;
    }

    /**
     * Marks this tech as "logged out."
     */
    public void logOut() {
        techIsLoggedIn = false;
    }

    /**
     * Marks this tech as "signed out."
     */
    public void signOut() {
        techIsSignedOut = true;         // Incomplete; needs to handle dates
    }

    /**
     * Gets this tech's last login date.
     * 
     * @return last login date
     */
    public Date getLastLoginDate() {
        return(techLastLoginDate);
    }

    /**
     * Gets this tech's last signout date.
     * 
     * @return last signout date
     */
    public Date getLastSignoutDate() {
        return(techLastSignoutDate);
    }

    /**
     * Gets this tech's login count.
     * 
     * @return  login count
     */
    public int getLoginCount() {
        return(techLoginCount);
    }

    /**
     * Gets this tech's next signout date.
     * 
     * @return next signout date
     */
    public Date getNextSignoutDate() {
        return(techNextSignoutDate);
    }

    /**
     * Gets this tech's creation date.
     * 
     * @return creation date
     */
    public Date getCreationDate() {
        return(techCreationDate);
    }

    /**
     * Gets this tech's ID number.
     * 
     * @return ID number
     */
    public int getID() {
        return(techID);
    }

    /**
     * Gets this tech's name.
     * 
     * @return tech's name
     */
    public String getName() {
        return(techName);
    }

    /**
     * Returns a boolean value denoting whether or not this tech is currently
     * logged in.
     * 
     * @return tech logged in boolean
     */
    public boolean isLoggedIn() {
        return(techIsLoggedIn);
    }

    /**
     * Returns a boolean value denoting whether or not this tech is currently
     * signed out.
     * 
     * @return tech signed out boolean
     */
    public boolean isSignedOut() {
        return(techIsSignedOut);
    }

    /**
     * Sets this tech's name.
     * 
     * @param techName the tech's name
     */
    public void setName(String techName) {
        this.techName = techName;
    }
    @Override
    public Document getXMLData() {
        Document doc = DocumentHelper.createDocument();
        Element techElement = doc.addElement(XML_TECH_ROOT).addAttribute(XML_TECH_ID, Integer.toString(techID));
        Element techCreationDateElement = techElement.addElement(XML_TECH_CREATION_DATE);
        Element techIsLoggedInElement = techElement.addElement(XML_TECH_IS_LOGGED_IN);
        Element techIsSignedOutElement = techElement.addElement(XML_TECH_IS_SIGNED_OUT);
        Element techLastLoginDateElement = techElement.addElement(XML_TECH_LAST_LOGIN_DATE);
        Element techLastSignoutDateElement = techElement.addElement(XML_TECH_LAST_SIGNOUT_DATE);
        Element techLoginCountElement = techElement.addElement(XML_TECH_LOGIN_COUNT);
        Element techNameElement = techElement.addElement(XML_TECH_NAME);
        Element techNextSignoutDateElement = techElement.addElement(XML_TECH_NEXT_SIGNOUT_DATE);
        Element techSignoutCountElement = techElement.addElement(XML_TECH_SIGNOUT_COUNT);
        if (techCreationDate != null) {
            techCreationDateElement.addText(Long.toString(techCreationDate.getTime()));
        }
        techIsLoggedInElement.addText(Boolean.toString(techIsLoggedIn));
        techIsSignedOutElement.addText(Boolean.toString(techIsSignedOut));
        if (techLastLoginDate != null) {
            techLastLoginDateElement.addText(Long.toString(techLastLoginDate.getTime()));
        }
        if (techLastSignoutDate != null) {
            techLastSignoutDateElement.addText(Long.toString(techLastSignoutDate.getTime()));
        }
        techLoginCountElement.addText(Integer.toString(techLoginCount));
        if (techName != null) {
            techNameElement.addText(techName);
        }
        if (techNextSignoutDate != null) {
            techNextSignoutDateElement.addText(Long.toString(techNextSignoutDate.getTime()));
        }
        techSignoutCountElement.addText(Integer.toString(techSignoutCount));
        return(doc);
    }
    @Override
    public int compareTo(Tech t) {
        return(t.getName().compareTo(techName));
    }
}