package controller;

import dados.BancoDados;
import model.Cliente;
import model.Pedido;
import model.Pizza;
import view.ClienteView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteController {
    private List<Cliente> clientes;
    private final BancoDados banco = BancoDados.getInstancia();

    public ClienteController() {
        this.clientes = banco.getClientes();
    }

    public List<Cliente> buscarClientes() {
        return this.clientes;
    }

    public void adicionarCliente(String nome, String sobrenome, String telefone) {
        Cliente cliente = new Cliente(nome, sobrenome, telefone);
        clientes.add(cliente);
    }

    public void removerCliente(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < clientes.size()) {
            clientes.remove(rowIndex);
        }
    }

    public void editarCliente(int rowIndex, String nome, String sobrenome, String telefone) {
        if (rowIndex >= 0 && rowIndex < clientes.size()) {
            Cliente cliente = clientes.get(rowIndex);
            cliente.setNome(nome);
            cliente.setSobrenome(sobrenome);
            cliente.setTelefone(telefone);

        }
    }
    private List<Cliente> filtrarTabela(String telefone) {
        return  clientes.stream().filter(cliente -> cliente.getTelefone().contains(telefone)).collect(Collectors.toList());
    }

    public Cliente buscarClientePorTelefone(String telefone) {
        Cliente clienteEncontrado = this.clientes.stream()
                .filter(clienteBanco -> clienteBanco.getTelefone().equals(telefone))
                .findFirst()
                .orElse(null);

        return clienteEncontrado;
    }

    public Cliente buscarClientePorId(int id) {
        Cliente clienteEncontrado = this.clientes.stream()
                .filter(clienteBanco -> clienteBanco.getId() == id)
                .findFirst()
                .orElse(null);

        return clienteEncontrado;
    }


    public Cliente buscarClientePorIdPedido(int idPedido) {

        Cliente clienteEncontrado = this.banco.getPedidos().stream()
                .filter(pedido -> pedido.getId() == idPedido)
                .findFirst()
                .orElse(null)
                .getCliente();

        return clienteEncontrado;
    }

    public Cliente retornarClientePorId(int id) {
        return banco.getClientes().stream()
                .filter(clienteBanco -> clienteBanco.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public int criarPedidoCliente(int idCliente) {

        Cliente cliente = retornarClientePorId(idCliente);
        List<Pizza> itens = new ArrayList<>();
        Pedido pedido = new Pedido(itens, cliente);
        banco.getPedidos().add(pedido);
        List<Pedido> pedidos = Arrays.asList(pedido);
        cliente.setPedidos(pedidos);

        return pedido.getId();
    }

}