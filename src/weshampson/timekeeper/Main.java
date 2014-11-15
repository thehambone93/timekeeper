
package weshampson.timekeeper;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.dom4j.DocumentException;
import weshampson.commonutils.exception.UncaughtExceptionHandler;
import weshampson.commonutils.io.DocumentOutputStream;
import weshampson.commonutils.jar.JarProperties;
import weshampson.commonutils.logging.ANSILogger;
import weshampson.commonutils.logging.Level;
import weshampson.commonutils.logging.Logger;
import weshampson.commonutils.updater.Updater;
import weshampson.timekeeper.gui.MainWindow;

/**
 * This class houses the program's entry point. It also performs any necessary
 * operations prior to loading the GUI, such as setting the program's look and
 * feel.
 * 
 * @author  Wes Hampson
 * @version 0.3.0 (Nov 6, 2014)
 * @since   0.1.0 (Jul 16, 2014)
 */
public class Main {
    public static final String APPLICATION_TITLE = JarProperties.getApplicationTitle();
    public static final String APPLICATION_VERSION = JarProperties.getApplicationVersion();
    public static final int BUILD_NUMBER = JarProperties.getBuildNumber();
    public static final Date BUILD_DATE = JarProperties.getBuildDate();
    public static final long PROGRAM_START_TIME_NANOS = System.nanoTime();
    private static final int EXIT_SUCCESS = 0;
    private static Updater updater;
    private static boolean isDebugModeEnabled;
    /**
     * The program's entry point.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        initLogger();
        initUncaughtExceptionHandler();
        initLookAndFeel();
        JarProperties.setSourceClass(Main.class);
        Logger.log(Level.INFO, "Launching " + APPLICATION_TITLE);
        Logger.log(Level.INFO, "Version: " + APPLICATION_VERSION + " build " + BUILD_NUMBER);
        Logger.log(Level.INFO, "Build date: " + new SimpleDateFormat("MMM. dd, yyyy").format(BUILD_DATE));
        Logger.log(Level.INFO, "Built by: Wes Hampson");
        int exitCode = parseArgs(args);
        if (exitCode != EXIT_SUCCESS) {
            System.exit(exitCode);
        }
        MainWindow mw = new MainWindow();
        initUpdater(mw);
        mw.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mw.checkForUpdates();
        mw.setVisible(true);
    }
    public static Updater getUpdater() {
        return(updater);
    }
    public static boolean isDebugModeEnabled() {
        return(isDebugModeEnabled);
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
        try {
            updater = new Updater(parent, true, new File("updaterConfig.xml"));
        } catch (IOException | DocumentException ex) {
            throw new RuntimeException(ex);
        }
        
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
        if (args[0].equals("--debug")) {
            isDebugModeEnabled = true;
            Logger.log(Level.WARNING, "Debug mode enabled.");
        }
        return(EXIT_SUCCESS);
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