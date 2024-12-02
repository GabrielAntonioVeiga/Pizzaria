package view;

import controller.ClienteController;
import controller.ItemPedidoController;
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
import java.util.Arrays;
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
    private JButton deletarPedidoButton;
    private DefaultTableModel tableModel;
    private final PedidosController pedidosController = new PedidosController();
    private final ClienteController clienteController = new ClienteController();
    private final ItemPedidoController itemPedidoController = new ItemPedidoController();
    private List<Pedido> pedidos;
    private Cliente cliente;

    public PedidosView() {
        this.inicializarTela();
        cbStatus.setEnabled(false);
        alterarStatusDoPedidoButton.setEnabled(false);
    }

    public PedidosView(int idPedido) {
        Cliente clienteTelaAnterior = clienteController.buscarClientePorIdPedido(idPedido);
        textField1.setText(clienteTelaAnterior.getTelefone());
        List<Pedido> pedidosCliente = pedidosController.carregarPedidosPorCliente(clienteTelaAnterior);
        cbStatus.setEnabled(false);
        alterarStatusDoPedidoButton.setEnabled(false);
        this.inicializarTela();
        renderizarItensNaTabela(pedidosCliente);

    }

    private void inicializarTela() {
        setContentPane(tela);
        setTitle("Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID Pedido", "Cliente", "Status", "Preco Total", "Detalhes"}
        );

        tablePedidos.setModel(tableModel);
        tablePedidos.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("Ver"));
        tablePedidos.getColumnModel().getColumn(4).setCellEditor(
                new ButtonEditor(new JCheckBox(), () -> detalhesPedido())  // Pass the method as a Runnable
        );

        renderizarItensNaTabela();



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

                Pedido pedido = pedidosController.retornarPedidoPeloId(idPedido);

                cbStatus.setSelectedItem(pedido.getStatus().toString());

                String status = cbStatus.getSelectedItem().toString();

                StatusPedido novoStatus = StatusPedido.valueOf(status);

                pedidosController.alterarStatusPedido(pedido.getId(), novoStatus);

                renderizarItensNaTabela();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new MenuView();
            }
        });

        deletarPedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarPedido();
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numeroCliente = textField1.getText();

                if(Objects.equals(numeroCliente, "")){
                    renderizarItensNaTabela();
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
                    limparTabela();
                    return;
                }

                List<Pedido> pedidosCliente = pedidosController.carregarPedidosPorCliente(cliente);
                renderizarItensNaTabela(pedidosCliente);

                if(pedidosCliente.isEmpty()){
                    JOptionPane.showMessageDialog(
                            tela,
                            "O cliente encontrado não possui pedidos, adicione um pedido!",
                            "Cliente sem pedidos.",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }

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
                    limparTabela();
                    return;
                }
                int idPedido = clienteController.criarPedidoCliente(cliente.getId());
                setVisible(false);
                new ItensPedidoFormView(idPedido);
            }
        });

    }

    private void limparTabela() {
        this.pedidos = new ArrayList<>();

        for(int i =0; i < tableModel.getRowCount(); i++){
            tableModel.removeRow(i);
        }
    }

    private void renderizarItensNaTabela() {
        this.pedidos = pedidosController.carregarPedidos();
        this.renderizarItens();
    }

    private void renderizarItensNaTabela(List<Pedido> pedidos) {
        this.pedidos = pedidos;
        this.renderizarItens();
    }

    private void renderizarItens() {
        int contador = 0;

        if(pedidos.isEmpty()) {
            this.limparTabela();
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

    public void detalhesPedido() {
        int row = tablePedidos.getSelectedRow();
        int idPedido = Integer.parseInt(tablePedidos.getValueAt(row, 0).toString());
        setVisible(false);
        new PedidoView(idPedido);
    }

    private void deletarPedido() {
        int selectedRow = tablePedidos.getSelectedRow();

        if(selectedRow == -1){
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um pedido para alterar!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int idPedido = (int)tableModel.getValueAt(selectedRow, 0);
        this.pedidosController.deletarPedido(idPedido);
        JOptionPane.showMessageDialog(tela,
                "Pedido excluido com sucesso",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        this.renderizarItensNaTabela();
    }
}
