
package weshampson.timekeeper.activity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import weshampson.commonutils.logging.Level;
import weshampson.commonutils.logging.Logger;
import weshampson.timekeeper.settings.SettingsManager;
import weshampson.timekeeper.tech.Tech;

/**
 *
 * @author  Wes Hampson
 * @version 0.3.0 (Nov 22, 2014)
 * @since   0.3.0 (Oct 30, 2014)
 */
public class ActivityLogger {
    public static final ActivityLogger currentLogger = new ActivityLogger();    
    public static Calendar globalCalendar = Calendar.getInstance();
    private File currentLogFile;
    private String activityLogDir;
    private PrintWriter logWriter;
    public static void logActivity(Action action, Tech tech, String description) {
        currentLogger.log(action, tech, description);
    }
    private void createNewLogFile() throws IOException {
        Date now = new Date();
        activityLogDir = SettingsManager.get(SettingsManager.PROPERTY_ACTIVITY_LOG_DIR);
        currentLogFile = new File(activityLogDir + "/" + new SimpleDateFormat("yyyy-MM_MMMM").format(now) + "/Week" + new SimpleDateFormat("W").format(now) + ".log");
        if (!currentLogFile.exists()) {
            currentLogFile.getParentFile().mkdirs();
            currentLogFile.createNewFile();
            logWriter = new PrintWriter(new FileWriter(currentLogFile, true));
            logWriter.println("id,name,date,action,description");
            Logger.log(Level.INFO, "Created new activity log file: " + currentLogFile.getCanonicalPath());
        } else {
            logWriter = new PrintWriter(new FileWriter(currentLogFile, true));
        }
    }
    private void log(Action action, Tech tech, String description) {
        boolean hasWritten = false;
        while (!hasWritten) {
            try {
                Calendar now = Calendar.getInstance();
                globalCalendar = now;
                createNewLogFile();
                logWriter.println("\"" + tech.getID() + "\",\"" + tech.getName() + "\",\"" + new Date() + "\",\"" + action.actionString + "\",\"" + description + "\"");
                logWriter.flush();
                Logger.log(Level.INFO, description);
                hasWritten = true;
            } catch (IOException ex) {
                Logger.log(Level.ERROR, ex, "Failed to write to activity log file - " + ex.toString());
                Object[] options = {"Abort", "Retry"};
                int option = JOptionPane.showOptionDialog(null, "<html><p style='width: 200px;'>Failed to writ to activity log file:<br>"
                        + "<br>"
                        + ex.toString(), "Error Logging Activity", -1, JOptionPane.ERROR_MESSAGE, null, options, options[1]);
                if (option != 1) {
                    break;
                }
            }
        }
    }
    public static enum Action {
        
        AUTO_LOG_OUT("*Auto log out"),
        LOG_IN("Log in"),
        LOG_OUT("Log out"),
        SIGNOUT_EXECUTE("Signout"),
        SIGNOUT_ENTRY_ADD("Add signout entry"),
        SIGNOUT_ENTRY_ADMIN_APPROVE("Admin approve signout entry"),
        SIGNOUT_ENTRY_REMOVE("Remove signout entry"),
        TECH_CREATE("Create tech"),
        TECH_MARK_ADMIN("Mark tech as admin"),
        TECH_REMOVE("Remove tech"),
        TECH_UNMARK_ADMIN("Unmark tech as admin");
        
        private final String actionString;
        private Action(String actionString) {
            this.actionString = actionString;
        }
    }
}
