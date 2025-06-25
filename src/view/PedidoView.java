package view;

import controller.ClienteController;
import controller.ItemPedidoController;
import controller.PedidoDetailController;
import controller.PedidosController;
import enums.EnStatusPedido;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PedidoView extends JFrame {
    private JTable tableItensPedido;
    private JPanel tela;
    private JScrollPane itensPedidos;
    private JButton adicionarButton;
    private JLabel response;
    private JButton btnEditar;
    private JButton voltarButton;
    private JButton deletarButton;
    private JLabel lblPrecoTotal;
    private JLabel statusPedido;
    private DefaultTableModel tableModel;


    
    private PedidoDetailController controller;
    
    public PedidoView() {
        inicializarTela();
    }

    public void setController(PedidoDetailController controller) {
        this.controller = controller;
    }

    private void inicializarTela() {
        setContentPane(tela);
        setTitle("Detalhes do Pedido");
        setSize(800, 600);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Forma", "Tamanho", "Sabores", "Preço"}
        );
        tableItensPedido.setModel(tableModel);

        inicializarListeners();
        pack();
        setLocationRelativeTo(null); 
    }

    public void renderizarItensNaTabela(List<Pizza> itensPedido) {
        tableModel.setRowCount(0); 
        response.setText("");

        for (Pizza pizza : itensPedido) {
            String valorTamanhoFormatado = String.format("%.2fcm²", pizza.getTamanho());
            String valorPrecoFormatado = String.format("R$%.2f", pizza.getPreco());
            tableModel.addRow(new Object[]{
                    pizza.getId(),
                    pizza.getForma().toString(),
                    valorTamanhoFormatado,
                    pizza.getNomeSabores(),
                    valorPrecoFormatado
            });
        }
    }

   

    private void inicializarListeners() {
        adicionarButton.addActionListener(e -> controller.adicionarItem());
        btnEditar.addActionListener(e -> controller.editarItem());
        deletarButton.addActionListener(e -> controller.deletarItem());
        voltarButton.addActionListener(e -> controller.voltar());
    }
    
    public void setPrecoTotal(String preco) {
        lblPrecoTotal.setText(preco);
    }

    public void setStatus(String status) {
        statusPedido.setText(status);
    }

    public void setBotoesEdicaoEnabled(boolean enabled) {
        btnEditar.setEnabled(enabled);
        adicionarButton.setEnabled(enabled);
        deletarButton.setEnabled(enabled);
    }

    public void mostrarMensagemVazio(String mensagem) {
        tableModel.setRowCount(0);
        response.setText(mensagem);
    }

    public void exibirMensagem(String titulo, String mensagem, int tipo) {
        JOptionPane.showMessageDialog(this.tela, mensagem, titulo, tipo);
    }

    public Long getIdItemSelecionado() {
        int row = tableItensPedido.getSelectedRow();
        if (row < 0) {
            return null; 
        }
        return (Long) tableModel.getValueAt(row, 0);
    }
}
