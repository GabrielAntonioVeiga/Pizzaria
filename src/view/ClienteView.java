package view;

import controller.ClienteController;
import dados.BancoDados;
import enums.NomeTipoSabor;
import model.SaborPizza;
import model.TipoSabor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;

public class ClienteView extends JFrame {
    private JPanel panelCliente;
    private JTextField tfSobrenome;
    private JTextField tfTelefone;
    private JPanel panelCadastroCliente;
    private JButton btnCriar;
    private JTextField tfNome;
    private JButton btnDeletar;
    private JTable tabelaCliente;
    private JButton btnEditar;
    private JButton btnCarregar;
    private JTextField tfFiltro;
    private JButton btnIrParaPedido;
    private final BancoDados bd = BancoDados.getInstancia();

    private ClienteController clienteController;

    public ClienteView() {
        setContentPane(panelCliente);
        setTitle("Cadastrar Cliente");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Nome", "Sobrenome", "Telefone"}
        );
        tabelaCliente.setModel(tableModel);

        clienteController = new ClienteController(tableModel);

        btnCriar.addActionListener(this::btnAddActionPerformed);
        btnDeletar.addActionListener(this::btnDeleteActionPerformed);
        btnEditar.addActionListener(this::btnEditarActionPerformed);
        btnCarregar.addActionListener(this::btnCarregarActionPerformed);
        btnIrParaPedido.addActionListener(this::btnTrocarPaginaActionPerformed);
        clienteController.carregarClientes();

        pack();
        setVisible(true);

        tfFiltro.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tfPesquisarCliente();
            }
        });

        tfFiltro.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tfFiltro.getText().equalsIgnoreCase("Pesquisar")) {
                    tfFiltro.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tfFiltro.getText().isEmpty()) {
                    tfFiltro.setText("Pesquisar");
                }
            }

        });

        panelCliente.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!tabelaCliente.getBounds().contains(e.getPoint())) {
                        tabelaCliente.clearSelection();
            }
        }
        });

    }

    private void tfPesquisarCliente() {
        String textoFiltro = tfFiltro.getText().trim();
        if (!textoFiltro.isEmpty()) {
            filtrarTabela(textoFiltro);
        } else {
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabelaCliente.getModel());
            tabelaCliente.setRowSorter(sorter);
            sorter.setRowFilter(null);
        }
    }

    private void filtrarTabela(String textoFiltro) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabelaCliente.getModel());
        tabelaCliente.setRowSorter(sorter);

        RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + textoFiltro);
        sorter.setRowFilter(rowFilter);
    }

    private void btnAddActionPerformed(ActionEvent e) {
        String nome = tfNome.getText();
        String sobrenome = tfSobrenome.getText();
        String telefone = tfTelefone.getText();

        if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, insira todos os valores do cliente!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            clienteController.adicionarCliente(nome, sobrenome, telefone);
            tfNome.setText("");
            tfSobrenome.setText("");
            tfTelefone.setText("");
        }
    }

    private void btnTrocarPaginaActionPerformed(ActionEvent e) {
        //Tela2 tela2 = new Tela2();
        this.dispose();
        new PedidosView();
        //tela2.setVisible(true);
    }

    private void btnDeleteActionPerformed(ActionEvent e) {
        int row = tabelaCliente.getSelectedRow();

        if(row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum cliente selecionado para deleção!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            clienteController.removerCliente(row);
        }



    }

    private void btnEditarActionPerformed(ActionEvent e) {
        int row = tabelaCliente.getSelectedRow();

        if(row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum cliente selecionado para edição!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            String nome = tabelaCliente.getValueAt(row, 0).toString();
            String sobrenome = tabelaCliente.getValueAt(row, 1).toString();
            String telefone = tabelaCliente.getValueAt(row, 2).toString();

            clienteController.editarCliente(row, nome, sobrenome, telefone);
            JOptionPane.showMessageDialog(this,
                    new StringBuilder("Cliente ").append(nome).append(" editado com sucesso!"),
                    "Edição", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void btnCarregarActionPerformed(ActionEvent e) {
        clienteController.carregarClientes();
    }

    //public static void main(String[] args) {
       // new ClienteView();
    //}
}
