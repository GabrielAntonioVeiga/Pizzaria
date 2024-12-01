package view;

import controller.ClienteController;
import controller.PedidoController;
import controller.SaborController;
import dados.BancoDados;
import enums.NomeTipoSabor;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PedidoView extends JFrame {
    private JTextField clienteField;
    private JLabel clienteLabel;
    private JTable tableItensPedido;
    private JPanel tela;
    private JScrollPane itensPedidos;
    private JButton adicionarButton;
    private JButton procurarButton;
    private JLabel response;
    private JButton btnEditar;
    private ClienteController clienteController;
    private PedidoController pedidoController;
    private SaborController saborController;

    private final BancoDados bd = BancoDados.getInstancia();
    private Cliente cliente = null;

    public PedidoView() {
        setContentPane(tela);
        setTitle("Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Forma", "Tamanho", "Sabores"}
        );
        tableItensPedido.setModel(tableModel);

        clienteController = new ClienteController(tableModel);
        pedidoController = new PedidoController(tableModel);
        saborController = new SaborController();

        pack();
        setVisible(true);

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new ItensPedidoFormView(cliente, 1);

            }
        });

        adicionarButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                setVisible(false);
                new ItensPedidoFormView(cliente);


            }
        });

        procurarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cliente = clienteController.buscarClientePorTelefone(clienteField.getText());

                if(cliente == null) {
                    response.setText("Cliente com o número " + clienteField.getText() + " não encontrado.");
                    return;
                }

                pedidoController.carregarItensPedido(cliente);


            }
        });
    }
}
