package renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private boolean isPushed;
    private Runnable onClickAction;  // Store the action to be executed on click

    public ButtonEditor(JCheckBox checkBox, Runnable onClickAction) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);  // Permite que o botão seja desenhado corretamente
        this.onClickAction = onClickAction;  // Store the passed action

        // Adiciona o ActionListener ao botão
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();  // Para de editar a célula quando o botão for clicado
                if (onClickAction != null) {
                    onClickAction.run();  // Execute the passed action
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText((value == null) ? "Ver" : value.toString());  // Define o texto do botão
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return null;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
