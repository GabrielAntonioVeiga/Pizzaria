package view;

import controller.PedidosController;
import enums.EnStatusPedido;
import model.Pedido;
import renderer.ButtonEditor;
import renderer.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PedidosView extends JFrame {

    private JPanel tela;
    private JTextField tfTelefoneCliente; 
    private JTable tablePedidos;
    private JButton adicionarPedidoButton;
    private JButton voltarButton;
    private JButton buscarButton;
    private JComboBox<EnStatusPedido> cbStatus;
    private JButton alterarStatusDoPedidoButton;
    private JButton deletarPedidoButton;
    private DefaultTableModel tableModel;

    private PedidosController controller;

    public PedidosView() {
        inicializarTela();
    }

    public void setController(PedidosController controller) {
        this.controller = controller;
    }

    private void inicializarTela() {
        setContentPane(tela);
        setTitle("Gerenciamento de Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID Pedido", "Cliente", "Status", "Preço Total", "Ações"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        tablePedidos.setModel(tableModel);
        tablePedidos.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("Ver Detalhes"));
        tablePedidos.getColumnModel().getColumn(4).setCellEditor(
                new ButtonEditor(new JCheckBox(), () -> controller.verDetalhesPedido())
        );

        cbStatus.setModel(new DefaultComboBoxModel<>(EnStatusPedido.values()));

        adicionarListeners();
        pack();
        setLocationRelativeTo(null);
    }

    private void adicionarListeners() {
        buscarButton.addActionListener(e -> controller.buscarPedidosPorCliente());
        adicionarPedidoButton.addActionListener(e -> controller.criarNovoPedido());
        deletarPedidoButton.addActionListener(e -> controller.deletarPedido());
        alterarStatusDoPedidoButton.addActionListener(e -> controller.alterarStatusPedido());
        voltarButton.addActionListener(e -> controller.voltarParaMenu());
    }


    public void renderizarItensNaTabela(List<Pedido> pedidos) {
        tableModel.setRowCount(0);

        if (pedidos == null || pedidos.isEmpty()) {
            return;
        }

        for (Pedido pedido : pedidos) {
            String valorTotalFormatado = String.format("R$%.2f", pedido.getPrecoTotal());
            tableModel.addRow(new Object[]{
                    pedido.getId(),
                    pedido.getCliente().getNome(),
                    pedido.getStatus(),
                    valorTotalFormatado,
                    "Ver Detalhes" 
            });
        }
    }

    public void exibirMensagem(String titulo, String mensagem, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }


    public Long getSelectedPedidoId() {
        int selectedRow = tablePedidos.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return (Long) tableModel.getValueAt(selectedRow, 0);
    }

    public String getTelefoneBusca() {
        return tfTelefoneCliente.getText();
    }

    public EnStatusPedido getStatusSelecionado() {
        return (EnStatusPedido) cbStatus.getSelectedItem();
    }
}