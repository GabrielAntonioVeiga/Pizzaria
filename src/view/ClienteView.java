package view;

import controller.ClienteController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClienteView extends JFrame {
    private JPanel panelCliente;
    private JTextField tfSobrenome;
    private JTextField tfTelefone;
    private JPanel panelCadastroCliente;
    private JButton btnCriar;
    private JTextField tfNome;
    private JButton btnDeletar;
    private JTable tabelaCliente;

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


        pack();
        setVisible(true);
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

    public static void main(String[] args) {
        new ClienteView();
    }
}
