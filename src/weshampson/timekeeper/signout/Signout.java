
package weshampson.timekeeper.signout;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import static weshampson.timekeeper.signout.SignoutManager.*;
import weshampson.timekeeper.xml.XMLWritable;

/**
 *
 * @author  Wes Hampson
 * @version 0.2.0 (Aug 4, 2014)
 * @since   0.2.0 (Jul 29, 2014)
 */
public class Signout implements XMLWritable {
    private final int signoutID;
    private int techID;
    private int adminTechID;
    private boolean isAdminApproved;
    private Date scheduledSignoutDate;
    private Date timeSignedOut;
    private Date timeAdminApproved;
    private String signoutReason;
    public Signout(int techID, Date scheduledSignoutDate, String signoutReason) {
        this.signoutID = SignoutManager.generateSignoutID();
        this.techID = techID;
        this.scheduledSignoutDate = scheduledSignoutDate;
        this.signoutReason = signoutReason;
        this.timeSignedOut = new Date();
    }
    public Signout(Element xMLElement) {
        this.signoutID = Integer.parseInt(xMLElement.attributeValue(XMLATTR_SIGNOUT_ID));
        for (Iterator i = xMLElement.elementIterator(); i.hasNext();) {
            Element e = (Element)i.next();
            String elementName = e.getName();
            String elementText = e.getText();
            if (elementText.isEmpty()) {
                continue;
            }
            switch (elementName) {
                case XMLTAG_TECH_ID:
                    this.techID = Integer.parseInt(elementText);
                    break;
                case XMLTAG_SCHEDULED_SIGNOUT_DATE:
                    this.scheduledSignoutDate = new Date(Long.parseLong(elementText));
                    break;
                case XMLTAG_TIME_SIGNED_OUT:
                    this.timeSignedOut = new Date(Long.parseLong(elementText));
                    break;
                case XMLTAG_SIGNOUT_REASON:
                    this.signoutReason = elementText;
                    break;
                case XMLTAG_IS_ADMIN_APPROVED:
                    this.isAdminApproved = Boolean.parseBoolean(elementText);
                    break;
                case XMLTAG_ADMIN_TECH_ID:
                    this.adminTechID = Integer.parseInt(elementText);
                    break;
                case XMLTAG_TIME_ADMIN_APPROVED:
                    this.timeAdminApproved = new Date(Long.parseLong(elementText));
                    break;
                default:
                    System.err.println("unrecognized XML element - " + elementName);
                    break;
            }
        }
    }
    public void adminApprove(int adminTechID) {
        isAdminApproved = true;
        this.adminTechID = adminTechID;
        this.timeAdminApproved = new Date();
    }
    public int getAdminTechID() {
        return(adminTechID);
    }
    public Date getScheduledSignoutDate() {
        return(scheduledSignoutDate);
    }
    public int getSignoutID() {
        return(signoutID);
    }
    public String getSignoutReason() {
        return(signoutReason);
    }
    public int getTechID() {
        return(techID);
    }
    public Date getTimeAdminApproved() {
        return(timeAdminApproved);
    }
    public Date getTimeSignedOut() {
        return(timeSignedOut);
    }
    public boolean isAdminApproved() {
        return(isAdminApproved);
    }
    @Override
    public Document getXMLData() {
        Document doc = DocumentHelper.createDocument();
        Element signoutRoot = doc.addElement(XMLTAG_SIGNOUT_ROOT).addAttribute(XMLATTR_SIGNOUT_ID, Integer.toString(signoutID));
        signoutRoot.addElement(XMLTAG_TECH_ID).addText(Integer.toString(techID));
        signoutRoot.addElement(XMLTAG_SCHEDULED_SIGNOUT_DATE).addText(Long.toString(scheduledSignoutDate.getTime()));
        signoutRoot.addElement(XMLTAG_TIME_SIGNED_OUT).addText(Long.toString(timeSignedOut.getTime()));
        signoutRoot.addElement(XMLTAG_SIGNOUT_REASON).addText(signoutReason);
        signoutRoot.addElement(XMLTAG_IS_ADMIN_APPROVED).addText(Boolean.toString(isAdminApproved));
        Element adminTechIDElement = signoutRoot.addElement(XMLTAG_ADMIN_TECH_ID);
        Element adminTimeSignedOutElement = signoutRoot.addElement(XMLTAG_TIME_ADMIN_APPROVED);
        if (isAdminApproved) {
            adminTechIDElement.addText(Integer.toString(adminTechID));
            adminTimeSignedOutElement.addText(Long.toString(timeAdminApproved.getTime()));
        }
        return(doc);
    }
}
class SignoutComparator implements Comparator<Signout> {
    @Override
    public int compare(Signout o1, Signout o2) {
        return(Long.compare(o1.getScheduledSignoutDate().getTime(), o2.getScheduledSignoutDate().getTime()));
    }
}