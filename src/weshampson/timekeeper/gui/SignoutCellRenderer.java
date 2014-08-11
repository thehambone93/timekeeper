
package weshampson.timekeeper.gui;

import java.awt.Color;
import java.awt.Component;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import weshampson.timekeeper.signout.Signout;
import weshampson.timekeeper.signout.SignoutException;
import weshampson.timekeeper.signout.SignoutManager;

/**
 *
 * @author  Wes Hampson
 * @version 0.2.0 (Aug 6, 2014)
 * @since   0.2.0 (Aug 4, 2014)
 */
public class SignoutCellRenderer implements TableCellRenderer {
    private final DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        try {
            JLabel label = (JLabel)defaultTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int signoutID = Integer.parseInt((String)table.getValueAt(row, 0));
            Signout s = SignoutManager.getSignoutByID(signoutID);
            GregorianCalendar scheduledSignoutDate = new GregorianCalendar();
            scheduledSignoutDate.setTime(s.getScheduledSignoutDate());
            GregorianCalendar timeSigedOut = new GregorianCalendar();
            timeSigedOut.setTime(s.getTimeSignedOut());
            if (scheduledSignoutDate.get(Calendar.YEAR) == timeSigedOut.get(Calendar.YEAR) && scheduledSignoutDate.get(Calendar.DAY_OF_YEAR) == timeSigedOut.get(Calendar.DAY_OF_YEAR)) {
                if (timeSigedOut.get(Calendar.HOUR_OF_DAY) >= 12) {
                    label.setForeground(Color.RED);
                    label.setToolTipText("Signed out after 12:00 PM.");
                }
            } else {
                label.setForeground(Color.BLACK);
            }
            return(label);
        } catch (SignoutException ex) {
           throw new RuntimeException(ex);
        }
    }
}