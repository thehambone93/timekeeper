
package weshampson.timekeeper.gui;

import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
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
 * @version 0.1.0 (Jul 23, 2014)
 * @since   0.1.0 (Jul 22, 2014)
 */
public class TechCellRenderer extends JLabel implements ListCellRenderer<Tech> {
    @Override
    public Component getListCellRendererComponent(JList<? extends Tech> list, Tech tech, int index, boolean isSelected, boolean cellHasFocus) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        setFont(new Font("Tahoma", Font.PLAIN, 18));
        if (tech.isLoggedIn()) {
            setText(tech.getName() + " (" + dateFormat.format(tech.getLastLoginDate()) + ")");
        } else {
            setText(tech.getName());
        }
        return(this);
    }
}
