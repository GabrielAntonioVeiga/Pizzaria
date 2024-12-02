package view;

import controller.ClienteController;
import controller.ItemPedidoController;
import controller.PedidosController;
import dados.BancoDados;
import model.Cliente;
import model.Pedido;

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
    private JButton verMaisBtn;
    private DefaultTableModel tableModel;
    private ClienteController clienteController;
    private PedidosController pedidosController;
    private ItemPedidoController itemPedidoController;
    private List<Pedido> pedidos = BancoDados.getInstancia().getPedidos();
    private Cliente cliente;

    public PedidosView() {
        this.inicializarTela();
        this.pedidosController = new PedidosController();
        this.clienteController = new ClienteController();
        this.itemPedidoController = new ItemPedidoController();
        this.pedidos = pedidosController.carregarPedidos();
    }

    public PedidosView(int idPedido) {
        this.inicializarTela();
        this.pedidosController = new PedidosController();
        this.clienteController = new ClienteController();
        this.itemPedidoController = new ItemPedidoController();
        this.pedidos = pedidosController.carregarPedidos();
        Cliente clienteTelaAnterior = clienteController.buscarClientePorIdPedido(idPedido);
        textField1.setText(clienteTelaAnterior.getTelefone());
        List<Pedido> pedidosCliente = pedidosController.carregarPedidosPorCliente(clienteController.buscarClientePorIdPedido(idPedido));
        renderizarItensNaTabela(pedidosCliente);
    }

    private void inicializarTela() {
        setContentPane(tela);
        setTitle("Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID Pedido", "Cliente", "Status", "Preco Total", ""}
        );

        tablePedidos.setModel(tableModel);

        renderizarItensNaTabela(pedidos);
        pack();
        setVisible(true);
        this.inicializarListeners();
    }

    private void inicializarListeners(){
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
                int idPedido = itemPedidoController.criarPedidoCliente(cliente.getId());
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
            verMaisBtn = new JButton("Ver mais");
            this.tableModel.setRowCount(contador);
            tableModel.addRow(new Object[]{
                    pedido.getId(),
                    pedido.getCliente().getNome(),
                    pedido.getStatus(),
                    pedido.getPrecoTotal(),
                    verMais(pedido.getId())
            });
            contador++;
        }

    }

    public Object verMais(int id){
        verMaisBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PedidoView(id);
            }
        });
        return null;
    }

    private Cliente getClientePedido(int idCliente) {
        return clienteController.buscarClientePorId(idCliente);
    }
}
