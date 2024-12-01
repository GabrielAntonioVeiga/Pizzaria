package controller;

import dados.BancoDados;
import model.Cliente;
import model.Pedido;
import view.ClienteView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteController {
    private DefaultTableModel tableModel;
    private List<Cliente> clientes;
    BancoDados banco = BancoDados.getInstancia();

    public ClienteController(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
        this.clientes = banco.getClientes();
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

    private void tfPesquisarClientePorTelefone(String telefone) {
        if (!telefone.isEmpty()) {
            filtrarTabela(telefone);
        }
    }

    private List<Cliente> filtrarTabela(String telefone) {
        return  clientes.stream().filter(cliente -> cliente.getTelefone().contains(telefone)).collect(Collectors.toList());
    }

    public Cliente buscarClientePorTelefone(String telefone) {
        for (Cliente cliente : clientes) {
            if (cliente.getTelefone().equals(telefone)) {
                return cliente;
            }
        }
        return null;
    }

    public void adicionarPedido(Cliente cliente, Pedido pedido) {
        cliente.setPedido(pedido);
    }
}
