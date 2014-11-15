
package weshampson.timekeeper.tech;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import weshampson.commonutils.logging.Level;
import weshampson.commonutils.logging.Logger;
import static weshampson.timekeeper.tech.TechManager.*;
import weshampson.timekeeper.xml.XMLWritable;

/**
 * This class holds the login and signout data for workers (techs).
 * 
 * @author  Wes Hampson
 * @version 0.3.0 (Nov 13, 2014)
 * @since   0.1.0 (Jul 17, 2014)
 */
public class Tech implements XMLWritable {
    private final int techID;
    private String techName;
    private Date techCreationDate;
    private Date techLastLoginDate;
    private Date techLastSignoutDate;
    private boolean techIsLoggedIn;
    private boolean techIsSignedOut;
    private int techLoginCount;
    private int techSignoutCount;
    
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
        this.techID = Integer.parseInt(xmlElement.attributeValue(XMLATTR_TECH_ID));
        for (Iterator i = xmlElement.elementIterator(); i.hasNext();) {
            Element e = (Element)i.next();
            String elementName = e.getName();
            String elementText = e.getText();
            if (elementText.isEmpty()) {
                continue;
            }
            switch (elementName) {
                case XMLTAG_TECH_CREATION_DATE:
                    this.techCreationDate = new Date(Long.parseLong(elementText));
                    break;
                case XMLTAG_TECH_IS_LOGGED_IN:
                    this.techIsLoggedIn = Boolean.parseBoolean(elementText);
                    break;
                case XMLTAG_TECH_IS_SIGNED_OUT:
                    this.techIsSignedOut = Boolean.parseBoolean(elementText);
                    break;
                case XMLTAG_TECH_LAST_LOGIN_DATE:
                    this.techLastLoginDate = new Date(Long.parseLong(elementText));
                    break;
                case XMLTAG_TECH_LAST_SIGNOUT_DATE:
                    this.techLastSignoutDate = new Date(Long.parseLong(elementText));
                    break;
                case XMLTAG_TECH_LOGIN_COUNT:
                    this.techLoginCount = Integer.parseInt(elementText);
                    break;
                case XMLTAG_TECH_NAME:
                    this.techName = elementText;
                    break;
                case XMLTAG_TECH_SIGNOUT_COUNT:
                    this.techSignoutCount = Integer.parseInt(elementText);
                    break;
                default:
                    Logger.log(Level.WARNING, "Tech data: unrecognized XML element - " + elementName);;
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
        techIsSignedOut = true;
        techLastSignoutDate = new Date();
        techSignoutCount++;
    }
    
    /**
     * Unmarks this tech as "signed out."
     */
    public void resetSignoutStatus() {
        techIsSignedOut = false;
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
        Element techElement = doc.addElement(XMLTAG_TECH_ROOT).addAttribute(XMLATTR_TECH_ID, Integer.toString(techID));
        Element techCreationDateElement = techElement.addElement(XMLTAG_TECH_CREATION_DATE);
        Element techIsLoggedInElement = techElement.addElement(XMLTAG_TECH_IS_LOGGED_IN);
        Element techIsSignedOutElement = techElement.addElement(XMLTAG_TECH_IS_SIGNED_OUT);
        Element techLastLoginDateElement = techElement.addElement(XMLTAG_TECH_LAST_LOGIN_DATE);
        Element techLastSignoutDateElement = techElement.addElement(XMLTAG_TECH_LAST_SIGNOUT_DATE);
        Element techLoginCountElement = techElement.addElement(XMLTAG_TECH_LOGIN_COUNT);
        Element techNameElement = techElement.addElement(XMLTAG_TECH_NAME);
        Element techSignoutCountElement = techElement.addElement(XMLTAG_TECH_SIGNOUT_COUNT);
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
        techSignoutCountElement.addText(Integer.toString(techSignoutCount));
        return(doc);
    }
}
class TechComparator implements Comparator<Tech> {
    @Override
    public int compare(Tech o1, Tech o2) {
        return(o1.getName().compareTo(o2.getName()));
    }
}