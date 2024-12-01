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

                List<SaborPizza> sabores = List.of(saborController.carregarSabores().get(1), saborController.carregarSabores().get(0));
                Pizza pizzaSelecionada = new Pizza(new Quadrado(20), sabores);

                ItensPedidoFormView tela2 = new ItensPedidoFormView(pizzaSelecionada);

                tela2.setVisible(true);

                dispose();
            }
        });

        procurarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cliente cliente = clienteController.buscarClientePorTelefone(clienteField.getText());

                if(cliente == null) {
                    response.setText("Cliente com o número " + clienteField.getText() + " não encontrado.");
                    return;
                }


                List<SaborPizza> sabores = List.of(saborController.carregarSabores().get(1), saborController.carregarSabores().get(0));

                Pizza p1 = new Pizza(new Quadrado(20), sabores);
                Pizza p2 = new Pizza(new Triangulo(30), sabores);

                List<Pizza> itens = List.of(p1,p2);

                clienteController.adicionarPedido(cliente, new Pedido(itens));


                pedidoController.carregarItensPedido(cliente);
            }
        });
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(PedidoView::new);
    }
}
