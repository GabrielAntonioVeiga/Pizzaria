package renderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Function;

public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer(String text) {
        setOpaque(true);
        setText(text);

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null) return this;
        setText(value.toString());  // You can set the button text dynamically here if needed
        return this;
    }
}

