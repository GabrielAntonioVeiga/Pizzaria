package view;

import controller.ClienteController;
import controller.PedidoController;
import controller.SaborController;
import dados.BancoDados;
import enums.StatusPedido;
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
    private JComboBox<StatusPedido> cbStatus;
    private JButton voltarButton;
    private JButton deletarButton;
    private JLabel lblPrecoTotal;
    private ClienteController clienteController;
    private PedidoController pedidoController;
    private SaborController saborController;
    private DefaultTableModel tableModel;

    private final BancoDados bd = BancoDados.getInstancia();
    private Cliente cliente = null;

    public PedidoView() {
        this.inicializarTela();
    }

    public PedidoView(Cliente cliente) {
        this.cliente = cliente;
        this.inicializarTela();
        this.procurarItensPedido(this.cliente);
        clienteField.setText(cliente.getTelefone());
        cbStatus.setEnabled(true);
        cbStatus.setSelectedItem(cliente.getPedido().getStatus());
    }

    private void renderizarItensNaTabela(List<Pizza> itensPedido) {
        int contador = 0;
        for (Pizza pizza : itensPedido) {
            String valorTamanhoFormatado = String.format("%.2fcm²", pizza.getTamanho());
            String valorPrecoFormatado = String.format("R$%.2f", pizza.getPreco());
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
        setTitle("Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cbStatus.setModel(new DefaultComboBoxModel<>(StatusPedido.values()));

        this.tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Forma", "Tamanho", "Sabores", "Preço"}
        );
        tableItensPedido.setModel(tableModel);

        clienteController = new ClienteController(tableModel);
        pedidoController = new PedidoController();
        saborController = new SaborController();

        pack();
        setVisible(true);

        inicializarListeners();
    }

    private void inicializarListeners() {

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new MenuView();
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
                } else {
                    setVisible(false);
                    int id = Integer.parseInt(tableItensPedido.getValueAt(row, 0).toString());
                    new ItensPedidoFormView(cliente, id);
                }


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
                new ItensPedidoFormView(cliente);


            }
        });

        procurarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cliente = clienteController.buscarClientePorTelefone(clienteField.getText());

                procurarItensPedido(cliente);

            }
        });

        cbStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // O item selecionado
                StatusPedido statusSelecionado = (StatusPedido) cbStatus.getSelectedItem();
                pedidoController.alterarStatusPedido(cliente.getPedido(), statusSelecionado);
                Pedido pedido = cliente.getPedido();
            }
        });
    }

    private void procurarItensPedido(Cliente cliente) {
        if(cliente == null) {
            response.setText("Cliente com o número " + clienteField.getText() + " não encontrado.");
            return;
        }

        List<Pizza> itensPedido = pedidoController.carregarItensPedido(cliente);
        if(itensPedido.isEmpty()) {
            response.setText("Não possui pedidos.");
            return;
        }

        lblPrecoTotal.setText(String.format("%.2f", getPrecoTotal(itensPedido)));
        renderizarItensNaTabela(itensPedido);

    }

    private Double getPrecoTotal(List<Pizza> itens){
        Double precoTotal = 0.0;
        for(Pizza pizza : itens){
            precoTotal += pizza.getPreco();
        }
        return precoTotal;
    }
}
