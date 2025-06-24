package view;

import controller.ClienteController;
import controller.ItemPedidoController;
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

    private PedidosController pedidosController;
    private ItemPedidoController itemPedidoController;
    private ClienteController clienteController;

    private DefaultTableModel tableModel;


    private Cliente cliente = null;
    private Long idPedido;
    Pedido pedido = null;

    public PedidoView(Long idPedido) {
        this.idPedido = idPedido;
        clienteController = new ClienteController();
        pedidosController = new PedidosController();
        itemPedidoController = new ItemPedidoController();
        this.cliente = clienteController.buscarClientePorIdPedido(Long.valueOf(idPedido));
        pedido = pedidosController.retornarPedidoPeloId(Long.valueOf(idPedido));
        statusPedido.setText(pedido.getStatus().toString());
        this.inicializarTela();
        renderizarItensNaTabela();

    }

    private void renderizarItensNaTabela() {
        List<Pizza> itensPedido = pedidosController.carregarItensPedido(Long.valueOf(this.idPedido));
        if(itensPedido.isEmpty()) {
            this.tableModel.setRowCount(0);
            response.setText("Não possui pedidos.");
            lblPrecoTotal.setText("R$ 0,00");
            return;
        }

        lblPrecoTotal.setText(String.format("%.2f", getPrecoTotal(itensPedido)));
        int contador = 0;
        for (Pizza pizza : itensPedido) {
            String valorTamanhoFormatado = String.format("%.2fcm²", pizza.getTamanho());

            String valorPrecoFormatado = String.format("R$%.2f", pizza.calculaPreco());
            this.tableModel.setRowCount(contador);
            tableModel.addRow(new Object[]{
                    pizza.getId(),
                    pizza.getForma().toString(),
                    valorTamanhoFormatado,
                    pizza.getNomeSabores(),
                    valorPrecoFormatado
            });
            contador++;
        }

    }

    private void inicializarTela() {
        setContentPane(tela);
        setTitle("Pedido");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Forma", "Tamanho", "Sabores", "Preço"}
        );
        tableItensPedido.setModel(tableModel);

        if(!pedido.getStatus().toString().equals(EnStatusPedido.ABERTO.toString())) {
            btnEditar.setEnabled(false);
            adicionarButton.setEnabled(false);
            deletarButton.setEnabled(false);
        }

        inicializarListeners();

        pack();
        setVisible(true);
    }

    private void inicializarListeners() {

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new PedidosView(idPedido);
            }
        });

        deletarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarItemPedido();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int row = tableItensPedido.getSelectedRow();

                if(row < 0) {
                    JOptionPane.showMessageDialog(tela,
                            "Nenhum Item selecionado para edição!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                setVisible(false);
                Long idItem = (Long) tableItensPedido.getValueAt(row, 0);
                new ItensPedidoFormView(idPedido, idItem);
            }
        });

        adicionarButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(cliente == null) {
                    JOptionPane.showMessageDialog(tela,
                            "Nenhum Cliente encontrado para adicionar pedido!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                setVisible(false);
                new ItensPedidoFormView(idPedido);


            }
        });
    }

    private Double getPrecoTotal(List<Pizza> itens){
        Double precoTotal = 0.0;
        for(Pizza pizza : itens){
            precoTotal += pizza.calculaPreco();
        }
        return precoTotal;
    }

    private void deletarItemPedido() {
        int selectedRow = tableItensPedido.getSelectedRow();

        if(selectedRow == -1){
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um pedido para alterar!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Long idItemPedido = (Long) tableModel.getValueAt(selectedRow, 0);

        this.itemPedidoController.deletarItemPedido(this.idPedido, idItemPedido);
        JOptionPane.showMessageDialog(tela,
                "Pedido excluido com sucesso",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        this.renderizarItensNaTabela();
    }
}
