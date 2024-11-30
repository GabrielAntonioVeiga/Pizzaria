package view;

import controller.ClienteController;
import controller.PedidoController;
import dados.BancoDados;
import enums.TipoSabor;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static enums.TipoSabor.PREMIUM;

public class PedidoView extends JFrame {
    private JTextField clienteField;
    private JLabel clienteLabel;
    private JTable tableItensPedido;
    private JPanel tela;
    private JScrollPane itensPedidos;
    private JButton adicionarButton;
    private JButton procurarButton;
    private JLabel response;
    private ClienteController clienteController;
    private PedidoController pedidoController;
    private BancoDados bd = new BancoDados();

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

        pack();
        setVisible(true);

        procurarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cliente cliente = clienteController.buscarClientePorTelefone(clienteField.getText());

                if(cliente == null) {
                    response.setText("Cliente com o número " + clienteField.getText() + " não encontrado.");
                    return;
                }

                SaborPizza calabresa = new SaborPizza("calabresa", 0);
                SaborPizza peperoni = new SaborPizza("Pepperoni", 1);
                List<SaborPizza> sabores = List.of(calabresa, peperoni);

                Pizza p1 = new Pizza(new Quadrado(20), sabores);
                Pizza p2 = new Pizza(new Triangulo(30), sabores);

                List<Pizza> itens = List.of(p1,p2);

                clienteController.adicionarPedido(cliente, new Pedido(itens));


                pedidoController.carregarItensPedido(cliente);
            }
        });
    }
}
