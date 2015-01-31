
package weshampson.timekeeper;

import java.awt.Frame;
import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import weshampson.commonutils.ansi.ANSILogger;
import weshampson.commonutils.exception.UncaughtExceptionHandler;
import weshampson.commonutils.io.DocumentOutputStream;
import weshampson.commonutils.jar.JarProperties;
import weshampson.commonutils.logging.Level;
import weshampson.commonutils.logging.Logger;
import weshampson.commonutils.updater.Updater;
import weshampson.commonutils.updater.UpdaterSettingsManager;
import weshampson.timekeeper.gui.MainWindow;

/**
 * This class houses the program's entry point. It also performs any necessary
 * operations prior to loading the GUI, such as setting the program's look and
 * feel.
 * 
 * @author  Wes Hampson
 * @version 1.0.0 (Jan 30, 2015)
 * @since   0.1.0 (Jul 16, 2014)
 */
public class Main {
    public static final String APPLICATION_AUTHOR = "Wes Hampson";
    public static final String APPLICATION_AUTHOR_EMAIL = "WesleyHampson@gmail.com";
    public static final String APPLICATION_TITLE = JarProperties.getApplicationTitle();
    public static final String APPLICATION_VERSION = JarProperties.getApplicationVersion();
    public static final int BUILD_NUMBER = JarProperties.getBuildNumber();
    public static final Date BUILD_DATE = JarProperties.getBuildDate();
    public static final long PROGRAM_START_TIME_NANOS = System.nanoTime();
    private static final int EXIT_SUCCESS = 0;
    private static Updater updater;
    private static boolean isDebugModeEnabled;
    private static boolean resetUpdaterVersionString;
    /**
     * The program's entry point.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        initLogger();
        initUncaughtExceptionHandler();
        initLookAndFeel();
        initDefaultUpdaterSettings();
        JarProperties.setSourceClass(Main.class);
         Logger.log(Level.INFO, "---------- " + new SimpleDateFormat("EEEE, MMMM dd, yyyy").format(new Date()) + " ----------", false, true);
        Logger.log(Level.INFO, "Launching " + APPLICATION_TITLE + "...");
        Logger.log(Level.INFO, "Version: " + APPLICATION_VERSION + " build " + BUILD_NUMBER);
        Logger.log(Level.INFO, "Build date: " + new SimpleDateFormat("MMM. dd, yyyy").format(BUILD_DATE));
        Logger.log(Level.INFO, "Built by: " + APPLICATION_AUTHOR);
        int exitCode = parseArgs(args);
        if (exitCode != EXIT_SUCCESS) {
            System.exit(exitCode);
        }
        MainWindow mw = new MainWindow();
        initUpdater(mw);
        mw.setExtendedState(JFrame.MAXIMIZED_BOTH);
        if (Boolean.parseBoolean(UpdaterSettingsManager.get(UpdaterSettingsManager.PROPERTY_CHECK_ON_STARTUP)) == true) {
            mw.checkForUpdates(false);
        }
        mw.setVisible(true);
    }
    public static Updater getUpdater() {
        return(updater);
    }
    public static boolean isDebugModeEnabled() {
        return(isDebugModeEnabled);
    }
    public static boolean resetUpdaterVersionString() {
        return(resetUpdaterVersionString);
    }
    private static void initLogger() {
        ANSILogger aNSILogger = new ANSILogger();
        aNSILogger.setColorEnabled(true);
        JTextPane logTextPane = new JTextPane();
        DocumentOutputStream logStream = new DocumentOutputStream(logTextPane);
        aNSILogger.setDocumentErr(logStream);
        aNSILogger.setDocumentOut(logStream);
        aNSILogger.setLoggingToDocumentEnabled(true);
        Logger.setLogger(aNSILogger);
    }
    private static void initUncaughtExceptionHandler() {
        UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler();
        uncaughtExceptionHandler.showDialog(true);
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }
    private static void initUpdater(Frame parent) {
        updater = new Updater(parent, true);
    }
    private static void initDefaultUpdaterSettings() {
        HashMap<String, String> defaultUpdaterSettings = new HashMap<>();
        defaultUpdaterSettings.put(UpdaterSettingsManager.PROPERTY_PROGRAM_NAME, APPLICATION_TITLE);
        defaultUpdaterSettings.put(UpdaterSettingsManager.PROPERTY_VERSION_STRING, APPLICATION_VERSION);
        defaultUpdaterSettings.put(UpdaterSettingsManager.PROPERTY_UPDATE_URL, "http://76.245.204.132/update.php");
        defaultUpdaterSettings.put(UpdaterSettingsManager.PROPERTY_BUILD_STATE, "stable");
        defaultUpdaterSettings.put(UpdaterSettingsManager.PROPERTY_CHECK_ON_STARTUP, "true");
        UpdaterSettingsManager.defineDefaultSettings(defaultUpdaterSettings);
    }
    /** Sets the GUI theme */
    private static void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            throw new RuntimeException(ex);
        }
    }
    private static int parseArgs(String[] args) {
        if (args.length == 0) {
            return(EXIT_SUCCESS);
        }
        int argIndex;
        for (argIndex = 0; argIndex < args.length; argIndex++) {
            switchStatement: switch (args[argIndex]) {
                case "--debug-mode":
                    isDebugModeEnabled = true;
                    Logger.log(Level.WARNING, "Debug mode enabled.");
                    break switchStatement;
                case "--reset-updater-version-string":
                    Logger.log(Level.INFO, "Setting updater version string to \"" + UpdaterSettingsManager.getDefault(UpdaterSettingsManager.PROPERTY_VERSION_STRING) + "\"...");
                    resetUpdaterVersionString = true;
            }
        }
        return(EXIT_SUCCESS);
    }
    public static File getProgramLocation() {
        try {
            File programLocation = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            return(programLocation);
        } catch (URISyntaxException ex) {
            // Should never be thrown of program is running in a normal environment
            throw new RuntimeException(ex);
        }
    }
    public static String getProgramUpTime() {
        long elapsed = System.nanoTime() - PROGRAM_START_TIME_NANOS;
        long millis = elapsed / 1000000;
        int seconds = (int)((millis / 1000) % 60);
        int minutes = (int)((millis / (1000 * 60)) % 60);
        int hours = (int)((millis / (1000 * 60 * 60)) % 24);
        int days = (int)(( millis / (1000 * 60 * 60 * 24)) % 7);
        StringBuilder sb = new StringBuilder();
        sb.append(days);
        if (days == 1) {
            sb.append(" day, ");
        } else {
            sb.append(" days, ");
        }
        sb.append(hours);
        if (hours == 1) {
            sb.append(" hour, ");
        } else {
            sb.append(" hours, ");
        }
        sb.append(minutes);
        if (minutes == 1) {
            sb.append(" minute, ");
        } else {
            sb.append(" minutes, ");
        }
        sb.append(seconds);
        if (seconds == 1) {
            sb.append(" second");
        } else {
            sb.append(" seconds");
        }
        return(sb.toString());
    }
}