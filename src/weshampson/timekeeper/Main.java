
package weshampson.timekeeper;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import weshampson.timekeeper.gui.MainWindow;

/**
 * This class houses the program's entry point. It also performs any necessary
 * operations prior to loading the GUI, such as setting the program's look and
 * feel.
 * 
 * @author  Wes Hampson
 * @version 0.1.0 (Jul 23, 2014)
 * @since   0.1.0 (Jul 16, 2014)
 */
public class Main {
    public static final String APPLICATION_TITLE = "TimeKeeper";
    public static final String APPLICATION_VERSION = "0.2.0 (development version)";
    private static final int EXIT_SUCCESS = 0;
    private static boolean isDebugModeEnabled;
    /**
     * The program's entry point.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        int exitCode = parseArgs(args);
        if (exitCode != EXIT_SUCCESS) {
            System.exit(exitCode);
        }
        lookAndFeel();
        MainWindow mw = new MainWindow();
        mw.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mw.setVisible(true);
    }
    public static boolean isDebugModeEnabled() {
        return(isDebugModeEnabled);
    }
    private static int parseArgs(String[] args) {
        if (args.length == 0) {
            return(EXIT_SUCCESS);
        }
        if (args[0].equals("--debug")) {
            isDebugModeEnabled = true;
            System.out.println("Debug mode enabled.");
        }
        return(EXIT_SUCCESS);
    }
    /** Sets the GUI theme */
    private static void lookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // Log this
            ex.printStackTrace();
        }
    }
}