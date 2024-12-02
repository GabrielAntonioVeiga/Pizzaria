package view;

import controller.ClienteController;
import controller.ItemPedidoController;
import controller.PedidoController;
import controller.PedidosController;
import dados.BancoDados;
import enums.StatusPedido;
import model.Cliente;
import model.Pedido;
import renderer.ButtonEditor;
import renderer.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PedidosView extends JFrame{
    private JPanel tela;
    private JTextField textField1;
    private JTable tablePedidos;
    private JButton adicionarPedidoButton;
    private JButton voltarButton;
    private JButton buscarButton;
    private JComboBox cbStatus;
    private JButton alterarStatusDoPedidoButton;
    private DefaultTableModel tableModel;
    private ClienteController clienteController;
    private PedidosController pedidosController;
    private PedidoController pedidoController;
    private ItemPedidoController itemPedidoController;
    private List<Pedido> pedidos = BancoDados.getInstancia().getPedidos();
    private Cliente cliente;

    public PedidosView() {
        this.inicializarTela();
        this.pedidosController = new PedidosController();
        this.clienteController = new ClienteController();
        this.itemPedidoController = new ItemPedidoController();
        this.pedidoController = new PedidoController();
        this.pedidos = pedidosController.carregarPedidos();
        cbStatus.setEnabled(false);
        alterarStatusDoPedidoButton.setEnabled(false);
    }

    public PedidosView(int idPedido) {
        this.pedidosController = new PedidosController();
        this.clienteController = new ClienteController();
        this.itemPedidoController = new ItemPedidoController();
        this.pedidoController = new PedidoController();
        this.pedidos = pedidosController.carregarPedidos();
        Cliente clienteTelaAnterior = clienteController.buscarClientePorIdPedido(idPedido);
        textField1.setText(clienteTelaAnterior.getTelefone());
        List<Pedido> pedidosCliente = pedidosController.carregarPedidosPorCliente(clienteController.buscarClientePorIdPedido(idPedido));
        cbStatus.setEnabled(false);
        alterarStatusDoPedidoButton.setEnabled(false);
        this.inicializarTela();
        renderizarItensNaTabela(pedidosCliente);

    }

    private void inicializarTela() {
        setContentPane(tela);
        setTitle("Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID Pedido", "Cliente", "Status", "Preco Total", "Detalhes"}
        );

        tablePedidos.setModel(tableModel);
        tablePedidos.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("Ver"));
        tablePedidos.getColumnModel().getColumn(4).setCellEditor(
                new ButtonEditor(new JCheckBox(), () -> detalhesPedido())  // Pass the method as a Runnable
        );

        renderizarItensNaTabela(pedidos);



        if(!pedidos.isEmpty()){
            cbStatus.setEnabled(true);
            cbStatus.setModel(new DefaultComboBoxModel<>(StatusPedido.values()));
            alterarStatusDoPedidoButton.setEnabled(true);
        }

        pack();
        setVisible(true);
        this.inicializarListeners();
    }

    private void inicializarListeners(){

        alterarStatusDoPedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tablePedidos.getSelectedRow();

                if(row == -1){
                    JOptionPane.showMessageDialog(
                            tela,
                            "Selecione um pedido para alterar o status!",
                            "Erro ao busar",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                int idPedido = (int) tableModel.getValueAt(row, 0);

                Pedido pedido = pedidoController.retornarPedidoPeloId(idPedido);

                cbStatus.setSelectedItem(pedido.getStatus().toString());

                String status = cbStatus.getSelectedItem().toString();

                StatusPedido novoStatus = StatusPedido.valueOf(status);

                pedidoController.alterarStatusPedido(pedido.getId(), novoStatus);

                renderizarItensNaTabela(pedidos);
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new MenuView();
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numeroCliente = textField1.getText();

                if(Objects.equals(numeroCliente, "")){
                    renderizarItensNaTabela(pedidos);
                    return;
                }

                cliente = clienteController.buscarClientePorTelefone(numeroCliente);

                if(cliente == null){
                    JOptionPane.showMessageDialog(
                            tela,
                            "Cliente com o número digitado não encontrado!",
                            "Erro ao busar",
                            JOptionPane.ERROR_MESSAGE
                    );
                    renderizarItensNaTabela(pedidos);
                    return;
                }

                List<Pedido> pedidosCliente = pedidosController.carregarPedidosPorCliente(cliente);

                if(pedidosCliente.isEmpty()){
                    JOptionPane.showMessageDialog(
                            tela,
                            "O cliente encontrado não possui pedidos, adicione um pedido!",
                            "Cliente sem pedidos.",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    renderizarItensNaTabela(new ArrayList<>());
                    return;
                }

                renderizarItensNaTabela(pedidosCliente);
            }
        });

        adicionarPedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cliente = clienteController.buscarClientePorTelefone(textField1.getText());
                if(cliente == null){
                    JOptionPane.showMessageDialog(
                            tela,
                            "É necessário buscar um cliente para adicionar um pedido!",
                            "Cliente não selecionado",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    renderizarItensNaTabela(new ArrayList<>());
                    return;
                }
                int idPedido = clienteController.criarPedidoCliente(cliente.getId());
                setVisible(false);
                new ItensPedidoFormView(idPedido);
            }
        });

    }

    private void renderizarItensNaTabela(List<Pedido> pedidos) {
        int contador = 0;

        if(pedidos.isEmpty()) {
            for(int i =0; i<tableModel.getRowCount(); i++){
                tableModel.removeRow(i);
            }
            return;
        }

        for (Pedido pedido : pedidos) {
            this.tableModel.setRowCount(contador);
            String valorTotalFormatado = String.format("R$%.2f", pedido.getPrecoTotal());
            tableModel.addRow(new Object[]{
                    pedido.getId(),
                    pedido.getCliente().getNome(),
                    pedido.getStatus(),
                    valorTotalFormatado,
                    "Ver Detalhes",
            });
            contador++;
        }

    }

    private Cliente getClientePedido(int idCliente) {
        return clienteController.buscarClientePorId(idCliente);
    }

    public void detalhesPedido() {
        int row = tablePedidos.getSelectedRow();
        int idPedido = Integer.parseInt(tablePedidos.getValueAt(row, 0).toString());
        setVisible(false);
        new PedidoView(idPedido);
    }
}
