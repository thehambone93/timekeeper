
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

    /**
     * The program's entry point.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        lookAndFeel();
        MainWindow mw = new MainWindow();
        mw.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mw.setVisible(true);
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