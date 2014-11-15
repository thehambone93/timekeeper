
package weshampson.timekeeper.signout;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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
import weshampson.timekeeper.settings.SettingsManager;
import weshampson.timekeeper.tech.Tech;
import weshampson.timekeeper.tech.TechException;
import weshampson.timekeeper.tech.TechManager;
import weshampson.timekeeper.tech.TechNotFoundException;

/**
 *
 * @author  Wes Hampson
 * @version 0.3.0 (Nov 13, 2014)
 * @since   0.2.0 (Jul 29, 2014)
 */
public class SignoutManager {
    protected static final String XMLTAG_ROOT = "signoutData";
    protected static final String XMLATTR_FILE_VERSION = "version";
    protected static final String XMLTAG_SIGNOUT_ROOT = "signout";
    protected static final String XMLATTR_SIGNOUT_ID = "id";
    protected static final String XMLTAG_TECH_ID = "techID";
    protected static final String XMLTAG_SCHEDULED_SIGNOUT_DATE = "scheduledSignoutDate";
    protected static final String XMLTAG_TIME_SIGNED_OUT = "timeSignedOut";
    protected static final String XMLTAG_SIGNOUT_REASON = "signoutReason";
    protected static final String XMLTAG_ADMIN_TECH_ID = "adminTechID";
    protected static final String XMLTAG_IS_ADMIN_APPROVED = "isAdminApproved";
    protected static final String XMLTAG_TIME_ADMIN_APPROVED = "timeAdminApproved";
    protected static final List<Signout> SIGNOUT_LIST = new ArrayList<>();
    public static synchronized void addSignout(Signout signout) throws SignoutException {
        for (Signout existingSignout : SIGNOUT_LIST) {
            if (existingSignout.getSignoutID() == signout.getSignoutID()) {
                throw new SignoutException("duplicate signout found!");
            }
        }
        SIGNOUT_LIST.add(signout);
        Collections.sort(SIGNOUT_LIST, new SignoutComparator());
    }
    public static synchronized int generateSignoutID() {
        int newID = 0;
        for (Signout s : SIGNOUT_LIST) {
            if (s.getSignoutID() > newID) {
                newID = s.getSignoutID();
            }
        }
        newID++;
        return(newID);
    }
    public static synchronized List<Signout> getSignoutsByScheduledDate(Date date) {
        List<Signout> signouts = new ArrayList<>();
        GregorianCalendar calendar1 =  new GregorianCalendar();
        GregorianCalendar calendar2 = new GregorianCalendar();
        calendar1.setTime(date);
        for (Signout s : SIGNOUT_LIST) {
            calendar2.setTime(s.getScheduledSignoutDate());
            if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)) {
                signouts.add(s);
            }
        }
        return(signouts);
    }
    public static synchronized Signout getSignoutByID(int signoutID) throws SignoutException {
        for (Signout s : SIGNOUT_LIST) {
            if (s.getSignoutID()== signoutID) {
                return(s);
            }
        }
        throw new SignoutException("signout not found for ID: " + signoutID);
    }
    public static synchronized List<Signout> getSignoutsByTechID(int techID) {
        List<Signout> signouts = new ArrayList<>();
        for (Signout s : SIGNOUT_LIST) {
            if (s.getTechID() == techID) {
                signouts.add(s);
            }
        }
        return(signouts);
    }
    public static synchronized int getSignoutsThisWeekCount() {
        int count = 0;
        Calendar now = Calendar.getInstance();
        for (Signout s : SIGNOUT_LIST) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(s.getScheduledSignoutDate());
            if (cal.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                && cal.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR)) {
                count++;
            }
        }
        return(count);
    }
    public static synchronized int getSignoutsTodayCount() {
        int count = 0;
        Calendar now = Calendar.getInstance();
        for (Signout s : SIGNOUT_LIST) {
            try {
                Tech t = TechManager.getTechByID(s.getTechID());
                Calendar cal = Calendar.getInstance();
                cal.setTime(s.getScheduledSignoutDate());
                if (cal.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
                        && t.isSignedOut()) {
                    count++;
                }
            } catch (TechNotFoundException ex) {
                Logger.log(Level.WARNING, "Tech not found for ID: " + s.getTechID());
            }
        }
        return(count);
    }
    public static synchronized List<Signout> getSignoutList() {
        return(SIGNOUT_LIST);
    }
    public static synchronized DefaultTableModel getSignoutTableModel(TableFilter filter) {
        DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] {"ID", "Scheduled signout date", "Tech name", "Time signed out", "Signout reason"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return(false);
            }
        };
        
        boolean adminApprovalEnabled = Boolean.parseBoolean(SettingsManager.get(SettingsManager.PROPERTY_ADMIN_APPROVAL_ENABLED));
        if (adminApprovalEnabled) {
            model.addColumn("Admin approved");
        }
        List<Signout> signoutsToAdd = generateFilteredSignoutList(filter);
        for (Signout s : signoutsToAdd) {
            try {
                List<String> rowData = new ArrayList<>();
                rowData.add(Integer.toString(s.getSignoutID()));
                rowData.add(new SimpleDateFormat("EEEE, MMMM dd, yyyy").format(s.getScheduledSignoutDate()));
                rowData.add(TechManager.getTechByID(s.getTechID()).getName());
                rowData.add(new SimpleDateFormat("hh:mm:ss a (EEEE, MMMM dd, yyyy)").format(s.getTimeSignedOut()));
                rowData.add(s.getSignoutReason());
                if (adminApprovalEnabled && s.isAdminApproved()) {
                    rowData.add(TechManager.getTechByID(s.getAdminTechID()).getName());
                }
                model.addRow(rowData.toArray());
            } catch (TechException ex) {
                ex.printStackTrace();
            }
        }
        return(model);
    }
    public static synchronized void loadSignoutData(File xMLFile) throws DocumentException, IOException {
        if (!xMLFile.exists()) {
            Logger.log(Level.WARNING, "Signout data file not found!");
            Logger.log(Level.INFO, "Creating new signout data file...");
            xMLFile.getParentFile().mkdirs();
            xMLFile.createNewFile();
            Logger.log(Level.INFO, "Signout data file successfully created at " + xMLFile.getAbsolutePath());
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
            Logger.log(Level.ERROR, "Invalid signout data file!");
            JOptionPane.showMessageDialog(null, "Invalid tech data file!", "Error Launching " + Main.APPLICATION_TITLE, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        if (!root.attributeValue(XMLATTR_FILE_VERSION).equalsIgnoreCase(Main.APPLICATION_VERSION)) {
            Logger.log(Level.WARNING, "Signout data file version does not match program version!");
        }
        for (Iterator i = root.elementIterator(XMLTAG_SIGNOUT_ROOT); i.hasNext();) {
            Element signoutElement = (Element)i.next();
            Signout signout = new Signout(signoutElement);
            try {
                addSignout(signout);
            } catch (SignoutException ex) {
                Logger.log(Level.ERROR, ex, null);
            }
        }
        Logger.log(weshampson.commonutils.logging.Level.INFO, "Signouts loaded from file: " + xMLFile.getAbsolutePath());
    }
    public static synchronized void removeSignout(int signoutID) throws SignoutException {
        for (Signout s : SIGNOUT_LIST) {
            if (s.getSignoutID() == signoutID) {
                SIGNOUT_LIST.remove(s);
                return;
            }
        }
        throw new SignoutException("signout not found for ID: " + signoutID);
    }
    public static synchronized void removeAllSignoutsByTechID(int id) {
        for (Iterator<Signout> i = SIGNOUT_LIST.iterator(); i.hasNext();) {
            Signout s = i.next();
            if (id == s.getTechID()) {
                i.remove();
            }
        }
    }
    public static synchronized void saveSignouts(File xMLFile) throws IOException {
        Document xMLDocument = DocumentHelper.createDocument();
        Element root = xMLDocument.addElement(XMLTAG_ROOT).addAttribute(XMLATTR_FILE_VERSION, Main.APPLICATION_VERSION);
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setIndentSize(4);
        XMLWriter xMLWriter = new XMLWriter(new FileWriter(xMLFile), outputFormat);
        for (Signout s : SIGNOUT_LIST) {
            Document signoutXMLDocument = s.getXMLData();
            root.add(signoutXMLDocument.getRootElement());
        }
        xMLWriter.write(xMLDocument);
        xMLWriter.close();
        Logger.log(Level.INFO, "Signouts saved to file: " + xMLFile.getAbsolutePath());
    }
    public static synchronized void updateSignout(Signout s) throws SignoutException {
        for (int i = 0; i < SIGNOUT_LIST.size(); i++) {
            Signout existingSignout = SIGNOUT_LIST.get(i);
            if (existingSignout.getSignoutID() == s.getSignoutID()) {
                SIGNOUT_LIST.set(i, s);
                return;
            }
        }
        throw new SignoutException("signout not found for ID: " + s.getSignoutID());
    }
    private static List<Signout> generateFilteredSignoutList(TableFilter filter) {
        List<Signout> list = new ArrayList<>();
        GregorianCalendar now = new GregorianCalendar();
        GregorianCalendar signoutCal = new GregorianCalendar();
        for (Signout s : SIGNOUT_LIST) {
            signoutCal.setTime(s.getScheduledSignoutDate());
            if (filter == TableFilter.ALL_FUTURE_SIGNOUTS) {
                if (signoutCal.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && signoutCal.get(Calendar.DAY_OF_YEAR) >= now.get(Calendar.DAY_OF_YEAR)) {
                    list.add(s);
                } else if (signoutCal.get(Calendar.YEAR) > now.get(Calendar.YEAR)) {
                    list.add(s);
                }
            } else if (filter == TableFilter.ALL_PAST_SIGNOUTS) {
                if (signoutCal.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && signoutCal.get(Calendar.DAY_OF_YEAR) < now.get(Calendar.DAY_OF_YEAR)) {
                    list.add(s);
                } else if (signoutCal.get(Calendar.YEAR) < now.get(Calendar.YEAR)) {
                    list.add(s);
                }
            } else if (filter == TableFilter.ALL_SIGNOUTS) {
                list.add(s);
            } else if (filter == TableFilter.LAST_WEEK) {
                now.add(Calendar.DATE, -7);
                if (signoutCal.getWeekYear() == now.get(Calendar.YEAR)
                        && signoutCal.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR)) {
                    list.add(s);
                }
                now.setTime(new Date());
            } else if (filter == TableFilter.NEXT_WEEK) {
                now.add(Calendar.DATE, 7);
                if (signoutCal.getWeekYear() == now.get(Calendar.YEAR)
                        && signoutCal.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR)) {
                    list.add(s);
                }
                now.setTime(new Date());
            } else if (filter == TableFilter.REMAINING_THIS_WEEK) {
                if (signoutCal.getWeekYear() == now.get(Calendar.YEAR)
                        && signoutCal.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR)
                        && signoutCal.get(Calendar.DAY_OF_YEAR) >= now.get(Calendar.DAY_OF_YEAR)) {
                    list.add(s);
                }
            } else if (filter == TableFilter.THIS_WEEK) {
                if (signoutCal.getWeekYear() == now.get(Calendar.YEAR)
                        && signoutCal.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR)) {
                    list.add(s);
                }
            } else if(filter == TableFilter.TODAY) {
                if (signoutCal.getWeekYear() == now.get(Calendar.YEAR)
                        && signoutCal.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR)
                        && signoutCal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) {
                    list.add(s);
                }
            }
        }
        return(list);
    }
    public static enum TableFilter {
        ALL_SIGNOUTS("All signouts"),
        ALL_PAST_SIGNOUTS("All past signouts"),
        ALL_FUTURE_SIGNOUTS("All future signouts"),
        TODAY("Today"),
        THIS_WEEK("This week"),
        LAST_WEEK("Last week"),
        NEXT_WEEK("Next week"),
        REMAINING_THIS_WEEK("Remaining this week");
        
        private final String filterText;
        private TableFilter(String filterText) {
            this.filterText = filterText;
        }
        public String getFilterText() {
            return(filterText);
        }
        public static TableFilter getFilterByString(String s) throws SignoutException {
            for (TableFilter f : TableFilter.values()) {
                if (f.getFilterText().equals(s)) {
                    return(f);
                }
            }
            throw new SignoutException("Table filter not found for string: " + s);
        }
    }
}