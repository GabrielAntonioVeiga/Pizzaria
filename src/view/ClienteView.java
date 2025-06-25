package view;

import controller.ClienteController;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.List;

public class ClienteView extends JFrame {

    private JPanel panelCliente;
    private JTextField tfSobrenome;
    private JFormattedTextField tfTelefone;
    private JButton btnCriar;
    private JTextField tfNome;
    private JButton btnDeletar;
    private JTable tabelaCliente;
    private JButton btnEditar;
    private JTextField tfFiltro;
    private JButton btnIrParaPedido;
    private JButton btnVoltar;
    private DefaultTableModel tableModel;
    private JPanel panelCadastroCliente;
    private ClienteController controller;

    public ClienteView() {
        inicializarTela();
    }

    public void setController(ClienteController controller) {
        this.controller = controller;
    }

    private void inicializarTela() {
        setContentPane(panelCliente);
        setTitle("Gerenciamento de Clientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Nome", "Sobrenome", "Telefone"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCliente.setModel(tableModel);

        adicionarListeners();
        pack();
        setSize(700, 500);
        setLocationRelativeTo(null);
    }

    private void adicionarListeners() {
        btnCriar.addActionListener(e -> controller.adicionarCliente());
        btnDeletar.addActionListener(e -> controller.removerCliente());
        btnEditar.addActionListener(e -> controller.salvarEdicaoCliente());
        btnIrParaPedido.addActionListener(e -> controller.irParaPedidos());
        btnVoltar.addActionListener(e -> controller.voltarParaMenu());

        tabelaCliente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.carregarClienteParaEdicao();
            }
        });

        tfFiltro.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                controller.filtrarTabela();
            }
        });
    }

    public void carregarClientes(List<Cliente> clientes) {
        tableModel.setRowCount(0);
        for (Cliente cliente : clientes) {
            tableModel.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getSobrenome(),
                    formatarTelefoneParaExibicao(cliente.getTelefone())
            });
        }
    }

    public void exibirMensagem(String mensagem, int tipo) {
        String titulo = "";
        switch (tipo) {
            case JOptionPane.INFORMATION_MESSAGE:
                titulo = "Sucesso";
                break;
            case JOptionPane.WARNING_MESSAGE:
                titulo = "Aviso";
                break;
            case JOptionPane.ERROR_MESSAGE:
                titulo = "Erro";
                break;
        }
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }

    public void limparCampos() {
        tfNome.setText("");
        tfSobrenome.setText("");
        tfTelefone.setText("");
        tabelaCliente.clearSelection();
    }

    public void preencherCampos(Cliente cliente) {
        tfNome.setText(cliente.getNome());
        tfSobrenome.setText(cliente.getSobrenome());
        tfTelefone.setText(formatarTelefoneParaExibicao(cliente.getTelefone()));
    }

    public void aplicarFiltroNaTabela(String textoFiltro) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabelaCliente.getModel());
        tabelaCliente.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textoFiltro));
    }

    public String getNome() { return tfNome.getText().trim(); }
    public String getSobrenome() { return tfSobrenome.getText().trim(); }
    public String getTelefone() { return tfTelefone.getText(); }
    public String getTextoFiltro() { return tfFiltro.getText().trim(); }

    public Long getIdClienteSelecionado() {
        int row = tabelaCliente.getSelectedRow();
        if (row < 0) return null;
        return (Long) tableModel.getValueAt(row, 0);
    }

    private String formatarTelefoneParaExibicao(String telefone) {
        try {
            MaskFormatter phoneMask = new MaskFormatter("(##) #####-####");
            phoneMask.setValueContainsLiteralCharacters(false);
            return phoneMask.valueToString(telefone.replaceAll("[^\\d]", ""));
        } catch (ParseException e) {
            return telefone; 
        }
    }
}