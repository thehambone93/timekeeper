/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package weshampson.timekeeper.activity;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import weshampson.commonutils.logging.Level;
import weshampson.commonutils.logging.Logger;
import weshampson.timekeeper.settings.SettingsManager;
import weshampson.timekeeper.tech.Tech;

/**
 *
 * @author  Wes Hampson
 * @version 0.3.0 (Nov 6, 2014)
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
    private ActivityLogger() {
        try {
            createNewLogFile();
        } catch (IOException ex) {
            Logger.log(Level.ERROR, ex, "Failed to create new activity log file: " + ex.toString());
        }
    }
    private void createNewLogFile() throws IOException {
        Date now = new Date();
        activityLogDir = SettingsManager.get(SettingsManager.PROPERTY_ACTIVITY_LOG_DIR);
        currentLogFile = new File(activityLogDir + "/" + new SimpleDateFormat("yyyy-MM_MMMM").format(now) + "/Week" + new SimpleDateFormat("W").format(now) + ".log");
        if (!currentLogFile.exists()) {
            currentLogFile.getParentFile().mkdirs();
            currentLogFile.createNewFile();
            Logger.log(Level.INFO, "Created new activity log file: " + currentLogFile.getAbsolutePath());
        }
        logWriter = new PrintWriter(currentLogFile);
    }
    private void log(Action action, Tech tech, String description) {
        Calendar now = Calendar.getInstance();
        if ((now.get(Calendar.WEEK_OF_YEAR) != globalCalendar.get(Calendar.WEEK_OF_YEAR) || now.get(Calendar.MONTH) != globalCalendar.get(Calendar.MONTH)) || !activityLogDir.equalsIgnoreCase(SettingsManager.get(SettingsManager.PROPERTY_ACTIVITY_LOG_DIR))) {
            try {
                globalCalendar = now;
                createNewLogFile();
            } catch (IOException ex) {
                Logger.log(Level.ERROR, ex, "Failed to create new activity log file: " + ex.toString());
            }
        }
        logWriter.println("\"" + tech.getID() + "\",\"" + tech.getName() + "\",\"" + new Date() + "\",\"" + action.actionString + "\",\"" + description + "\"");
        logWriter.flush();
    }
    public static enum Action {
        AUTO_LOG_OUT("*Auto log out"),
        LOG_IN("Log in"),
        LOG_OUT("Log out"),
        SIGNOUT_ADD("Add signout"),
        SIGNOUT_EXECUTE("Signout"),
        SIGNOUT_REMOVE("Remove signout");
        
        private final String actionString;
        private Action(String actionString) {
            this.actionString = actionString;
        }
    }
}
