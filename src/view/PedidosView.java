package view;

import javax.swing.*;

public class PedidosView extends JFrame{
    private JPanel panel1;
    private JTextField textField1;
    private JTable tablePedidos;
    private JButton adicionarPedidoButton;
    private JButton voltarButton;

    public PedidosView() {
        this.inicializatTela();
    }

    private void inicializatTela() {
        setVisible(true);
    }
}
