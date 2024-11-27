package controller;

import model.Cliente;

import javax.swing.table.DefaultTableModel;

public class ClienteController {
    private DefaultTableModel tableModel;

    public ClienteController(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public void adicionarCliente(String nome, String sobrenome, String telefone) {
        Cliente cliente = new Cliente(nome, sobrenome, telefone);
        tableModel.addRow(new Object[]{cliente.getNome(), cliente.getSobrenome(), cliente.getTelefone()});
    }

    public void removerCliente(int rowIndex) {
        if (rowIndex >= 0) {
            tableModel.removeRow(rowIndex);
        }
    }
}
