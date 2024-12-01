package view;

import controller.ClienteController;
import controller.PedidosController;
import model.Cliente;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PedidosView extends JFrame{
    private JPanel tela;
    private JTextField textField1;
    private JTable tablePedidos;
    private JButton adicionarPedidoButton;
    private JButton voltarButton;
    private JButton buscarButton;
    private DefaultTableModel tableModel;
    private ClienteController clienteController;
    private PedidosController pedidosController;
    private List<Pedido> pedidos;
    private Cliente cliente;

    public PedidosView() {
        this.inicializarTela();
        this.pedidosController = new PedidosController();
        this.clienteController = new ClienteController();
        this.pedidos = pedidosController.carregarPedidos();
    }

    private void inicializarTela() {
        setContentPane(tela);
        setVisible(true);

        this.tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID Pedido", "Cliente", "Status", "Preco Total", ""}
        );

        tablePedidos.setModel(tableModel);

        renderizarItensNaTabela(pedidos);
        this.inicializarListeners();
    }

    private void inicializarListeners(){
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numeroCliente = textField1.getText();
                cliente = clienteController.buscarClientePorTelefone(numeroCliente);

                if(cliente == null){
                    JOptionPane.showMessageDialog(
                            tela,
                            "Cliente com o número digitado não encontrado!",
                            "Erro ao busar",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                List<Pedido> pedidosCliente = pedidosController.carregarPedidosPorCliente(cliente);

                if(pedidosCliente.isEmpty()){
                    JOptionPane.showMessageDialog(
                            tela,
                            "O cliente enontrado não possui pedidos, adicione um pedido!",
                            "Cliente sem pedidos.",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }

                renderizarItensNaTabela(pedidosCliente);
            }
        });

        adicionarPedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void renderizarItensNaTabela(List<Pedido> pedidos) {
        int contador = 0;
        for (Pedido pedido : pedidos) {
            this.tableModel.setRowCount(contador);
            tableModel.addRow(new Object[]{
                    pedido.getId(),
                    getClientePedido(pedido.getIdCliente()),
                    pedido.getStatus(),
                    pedido.getPrecoTotal(),
                    new JButton("Ver mais")
            });
            contador++;
        }

    }

    private Cliente getClientePedido(int idCliente) {
        return clienteController.buscarClientePorId(idCliente);
    }
}
