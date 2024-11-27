package controller;

import model.Cliente;
import view.ClienteView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class ClienteController {
    private DefaultTableModel tableModel;
    private ClienteView clienteView;
    private List<Cliente> clientes;

    public ClienteController(DefaultTableModel tableModel, ClienteView clienteView) {
        this.tableModel = tableModel;
        this.clientes = new ArrayList<>();
    }

    public void carregarClientes() {
        tableModel.setRowCount(0);

        for (Cliente cliente : clientes) {
            tableModel.addRow(new Object[]{
                    cliente.getNome(),
                    cliente.getSobrenome(),
                    cliente.getTelefone()
            });
        }
    }

    public void adicionarCliente(String nome, String sobrenome, String telefone) {
        Cliente cliente = new Cliente(nome, sobrenome, telefone);
        clientes.add(cliente);
        tableModel.addRow(new Object[]{cliente.getNome(), cliente.getSobrenome(), cliente.getTelefone()});

    }

    public void removerCliente(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < clientes.size()) {
            clientes.remove(rowIndex);
            tableModel.removeRow(rowIndex);
        }
    }

    public void editarCliente(int rowIndex, String nome, String sobrenome, String telefone) {
        if (rowIndex >= 0 && rowIndex < clientes.size()) {
            Cliente cliente = clientes.get(rowIndex);
            cliente.setNome(nome);
            cliente.setSobrenome(sobrenome);
            cliente.setTelefone(telefone);

            tableModel.setValueAt(nome, rowIndex, 0);
            tableModel.setValueAt(sobrenome, rowIndex, 1);
            tableModel.setValueAt(telefone, rowIndex, 2);
        }
    }
}
