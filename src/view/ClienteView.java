package view;

import controller.ClienteController;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;
import java.text.ParseException;
import java.util.List;

public class ClienteView extends JFrame {
    private JPanel panelCliente;
    private JTextField tfSobrenome;
    private JFormattedTextField tfTelefone;
    private JPanel panelCadastroCliente;
    private JButton btnCriar;
    private JTextField tfNome;
    private JButton btnDeletar;
    private JTable tabelaCliente;
    private JButton btnEditar;
    private JTextField tfFiltro;
    private JButton btnIrParaPedido;
    private JButton btnVoltar;
    private DefaultTableModel tableModel;

    private ClienteController clienteController;

    public ClienteView() {
        setContentPane(panelCliente);
        setTitle("Cadastrar Cliente");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        clienteController = new ClienteController(this);

        btnCriar.addActionListener(this::btnAddActionPerformed);
        btnDeletar.addActionListener(this::btnDeleteActionPerformed);
        btnEditar.addActionListener(this::btnEditarActionPerformed);
        btnIrParaPedido.addActionListener(this::btnTrocarPaginaActionPerformed);
        this.inicializarTabela();

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

        tfTelefone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    int caretPosition = tfTelefone.getCaretPosition();
                    String text = tfTelefone.getText();

                    if (caretPosition > 0) {
                        int deleteIndex = caretPosition - 1;

                        while (deleteIndex > 0 && !Character.isDigit(text.charAt(deleteIndex))) {
                            deleteIndex--;
                        }

                        if (deleteIndex >= 0 && Character.isDigit(text.charAt(deleteIndex))) {
                            StringBuilder newText = new StringBuilder(text);
                            newText.deleteCharAt(deleteIndex);

                            e.consume();
                            updateTextField(newText.toString(), deleteIndex);
                        }
                    }
                }
            }
        });

        tfTelefone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                formatarTelefone();
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltar();
            }
        });
    }

    public void voltar() {
        setVisible(false);
        new MenuView();
    }

    private void updateTextField(String rawText, int caretPosition) {
        try {
            rawText = rawText.replaceAll("[^\\d]", "");
            MaskFormatter phoneMask = new MaskFormatter("(##) #####-####");
            phoneMask.setPlaceholderCharacter('_');
            phoneMask.setValueContainsLiteralCharacters(false);

            String formattedTelefone = phoneMask.valueToString(rawText);

            tfTelefone.setText(formattedTelefone);
            tfTelefone.setCaretPosition(Math.min(caretPosition, formattedTelefone.length()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void formatarTelefone() {
        String telefone = tfTelefone.getText();

        try {
            telefone = telefone.replaceAll("[^\\d]", "");
            MaskFormatter phoneMask = new MaskFormatter("(##) #####-####");
            phoneMask.setPlaceholderCharacter('_');
            phoneMask.setValueContainsLiteralCharacters(false);

            String formattedTelefone = phoneMask.valueToString(telefone);

            if (!tfTelefone.getText().equals(formattedTelefone)) {
                tfTelefone.setText(formattedTelefone);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void inicializarTabela() {
        this.tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nome", "Sobrenome", "Telefone"}
        );
        tabelaCliente.setModel(tableModel);
        this.carregarClientes(clienteController.buscarClientes());
    }

    public void carregarClientes(List<Cliente> clientes) {
        this.tableModel.setRowCount(0);

        for (Cliente cliente : clientes) {
            tableModel.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getSobrenome(),
                    formatarTelefoneParaExibicao(cliente.getTelefone())
            });
        }
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

    public String getNome() {
        return tfNome.getText();
    }

    public String getSobrenome() {
        return tfSobrenome.getText();
    }

    public String getTelefone() {
        return tfTelefone.getText();
    }

    public void exibirMensagemErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void exibirMensagemSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limparCampos() {
        tfNome.setText("");
        tfSobrenome.setText("");
        tfTelefone.setText("");
    }

    private void btnAddActionPerformed(ActionEvent e) {
        clienteController.adicionarCliente();
        carregarClientes(clienteController.buscarClientes());
    }

    private void btnTrocarPaginaActionPerformed(ActionEvent e) {
        this.setVisible(false);
        new PedidosView();
    }


    private void btnDeleteActionPerformed(ActionEvent e) {
        int row = tabelaCliente.getSelectedRow();

        if(row < 0) {
            exibirMensagemErro("Nenhum cliente selecionado para deleção!");
            return;
        }

            Long id = (Long) tabelaCliente.getValueAt(row, 0);
            clienteController.removerCliente(id);
            carregarClientes(clienteController.buscarClientes());
    }

    private void btnEditarActionPerformed(ActionEvent e) {
        int row = tabelaCliente.getSelectedRow();
        if (row < 0) {
            exibirMensagemErro("Nenhum cliente selecionado para edição!");
            return;
        }

        Long id = (Long) tabelaCliente.getValueAt(row, 0);
        String nome = tabelaCliente.getValueAt(row, 1).toString();
        String sobrenome = tabelaCliente.getValueAt(row, 2).toString();
        String telefone = tabelaCliente.getValueAt(row, 3).toString();

        clienteController.editarCliente(id, nome, sobrenome, telefone);
        carregarClientes(clienteController.buscarClientes());
    }

    private String formatarTelefoneParaExibicao(String telefone) {
        try {
            telefone = telefone.replaceAll("[^\\d]", "");
            MaskFormatter phoneMask = new MaskFormatter("(##) #####-####");
            phoneMask.setValueContainsLiteralCharacters(false);
            return phoneMask.valueToString(telefone);
        } catch (ParseException e) {
            e.printStackTrace();
            return telefone;
        }
    }
}