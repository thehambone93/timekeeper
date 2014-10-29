
package weshampson.timekeeper;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.Document;
import jline.console.ConsoleReader;
import org.dom4j.DocumentException;
import weshampson.commonutils.exception.UncaughtExceptionHandler;
import weshampson.commonutils.io.DocumentOutputStream;
import weshampson.commonutils.jar.JarProperties;
import weshampson.commonutils.logging.ANSILogger;
import weshampson.commonutils.logging.JLineLogger;
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
 * @version 0.3.0 (Oct 9, 2014)
 * @since   0.1.0 (Jul 16, 2014)
 */
public class Main {
    public static final String APPLICATION_TITLE = JarProperties.getApplicationTitle();
    public static final String APPLICATION_VERSION = JarProperties.getApplicationVersion();
    public static final int BUILD_NUMBER = JarProperties.getBuildNumber();
    public static final Date BUILD_DATE = JarProperties.getBuildDate();
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
    public static boolean isDebugModeEnabled() {
        return(isDebugModeEnabled);
    }
    public static Updater getUpdater() {
        return(updater);
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
}