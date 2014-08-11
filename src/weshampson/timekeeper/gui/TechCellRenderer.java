
package weshampson.timekeeper.gui;

import java.awt.Component;
import java.text.SimpleDateFormat;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import weshampson.timekeeper.tech.Tech;

/**
 * This class creates a custom {@code ListCellRenderer} for
 * displaying {@link weshampson.timekeeper.tech.Tech} objects in a
 * {@link javax.swing.JList} depending on their current state.
 * 
 * @author  Wes Hampson
 * @version 0.2.0 (Aug 11, 2014)
 * @since   0.1.0 (Jul 22, 2014)
 */
public class TechCellRenderer implements ListCellRenderer<Tech> {
    private final DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();
    @Override
    public Component getListCellRendererComponent(JList<? extends Tech> list, Tech tech, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel)defaultListCellRenderer.getListCellRendererComponent(list, index, index, isSelected, cellHasFocus);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        label.setFont(list.getFont());
        if (tech.isLoggedIn()) {
            label.setText(tech.getName() + " (" + dateFormat.format(tech.getLastLoginDate()) + ")");
        } else {
            label.setText(tech.getName());
        }
        if (isSelected) {
            label.setBackground(list.getSelectionBackground());
        }
        return(label);
    }
}