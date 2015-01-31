
package weshampson.timekeeper.tech.gui;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import weshampson.timekeeper.tech.Tech;
import weshampson.timekeeper.tech.TechManager;

/**
 * This class creates a custom {@code ListCellRenderer} for
 * displaying {@link weshampson.timekeeper.tech.Tech} objects in a
 * {@link javax.swing.JList} depending on their current state.
 * 
 * @author  Wes Hampson
 * @version 1.0.0 (Jan 30, 2015)
 * @since   0.1.0 (Jul 22, 2014)
 */
public class TechCellRenderer implements ListCellRenderer<Tech> {
    private final DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();
    private final SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm:ss a");
    private final SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE, MMM. dd, yyyy, hh:mm:ss a");
    @Override
    public Component getListCellRendererComponent(JList<? extends Tech> list, Tech tech, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel)defaultListCellRenderer.getListCellRendererComponent(list, index, index, isSelected, cellHasFocus);
        label.setFont(list.getFont());
        if (tech.isLoggedIn()) {
            label.setText(getLabelText(TechManager.getTechsInSortingID(), tech));
        } else {
            label.setText(getLabelText(TechManager.getTechsOutSortingID(), tech));
        }
        if (isSelected) {
            label.setBackground(list.getSelectionBackground());
        }
        return(label);
    }
    private String getLabelText(int sortID, Tech tech) {
        String labelText = "";
        switch (sortID) {
            case TechManager.SORTBY_FIRST_NAME:
                labelText = tech.getName();
                break;
            case TechManager.SORTBY_LAST_NAME:
                String firstName = "";
                String lastName = tech.getName();
                if (tech.getName().contains(" ")) {
                    firstName = tech.getName().substring(0, tech.getName().indexOf(' '));
                    lastName = tech.getName().substring(tech.getName().indexOf(' ') + 1) + ", ";
                }
                labelText = lastName + firstName;
                break;
            case TechManager.SORTBY_LAST_LOG_IN:
                Date lastLoginDate = tech.getLastLoginDate();
                if (lastLoginDate == null) {
                    labelText = tech.getName() + " (never logged in)";
                    break;
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(lastLoginDate);
                Calendar now = Calendar.getInstance();
                if (cal.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR)) {
                    labelText = tech.getName() + " (" + dateFormat2.format(tech.getLastLoginDate()) + ")";
                } else {
                    labelText = tech.getName() + " (" + dateFormat1.format(tech.getLastLoginDate()) + ")";
                }
                break;
            default:
                labelText = tech.getName();
                break;
        }
        return(labelText);
    }
}