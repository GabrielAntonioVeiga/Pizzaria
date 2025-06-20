package controller;

import dados.BancoDados;
import dao.cliente.ClienteDao;
import dao.cliente.IClienteDao;
import factory.DAOFactory;
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
    private ClienteDao clienteDao = DAOFactory.getClienteDao();
    private ClienteView view;

    public ClienteController(ClienteView view) {
        this.view = view;
    }

    public List<Cliente> buscarClientes() {
        return clienteDao.listar();
    }

    public void adicionarCliente() {

        String nome = view.getNome();
        String sobrenome = view.getSobrenome();
        String telefone = view.getTelefone();


        String telefoneFormatado = telefone.replaceAll("[^\\d]", "");

        if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
            view.exibirMensagemErro("Por favor, insira todos os valores do cliente!");
            return;
        }

        if (!(telefoneFormatado.length() >= 10 && telefoneFormatado.length() <= 15)) {
            view.exibirMensagemErro("Por favor, insira um telefone válido!");
            return;
        }

        boolean telefoneExistente = buscarClientes().stream()
                .anyMatch(c -> c.getTelefone().equals(telefoneFormatado));

        if (telefoneExistente) {
            view.exibirMensagemErro("Já existe um cliente com esse telefone!");
            return;
        }

        Cliente cliente = new Cliente(nome, sobrenome, telefone);
        clienteDao.salvar(cliente);

        view.exibirMensagemSucesso("Cliente cadastrado com sucesso!");
        view.limparCampos();
        view.adicionarNaTabela(cliente);
    }

    public void removerCliente(Long id) {
        Cliente cliente = buscarClientePorId(id);
        if (cliente != null) {
            pedidosController.deletarPedidoPorCliente(id);
            clienteDao.remover(id);
            view.removerNaTabela(id);
            view.exibirMensagemSucesso("Cliente removido com sucesso!");
        } else {
            view.exibirMensagemErro("Cliente não encontrado");
        }
    }

    public void editarCliente(Long idSelecionado, String nome, String sobrenome, String telefone) {
        if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
            view.exibirMensagemErro("Todos os campos devem ser preenchidos.");
            return;
        }

        String telefoneFormatado = telefone.replaceAll("[^\\d]", "");

        if (!(telefoneFormatado.length() >= 10 && telefoneFormatado.length() <= 15)) {
            view.exibirMensagemErro("Telefone inválido.");
            return;
        }

        Cliente cliente = buscarClientePorId(idSelecionado);
        if (cliente != null) {
            cliente.setNome(nome);
            cliente.setSobrenome(sobrenome);
            cliente.setTelefone(telefoneFormatado);
            clienteDao.atualizar(cliente);
            view.exibirMensagemSucesso("Cliente editado com sucesso!");
        } else {
            view.exibirMensagemErro("Cliente não encontrado.");
        }
    }

    public Cliente buscarClientePorTelefone(String telefone) {
        return clienteDao.listarPorTelefone(telefone);
    }

    public Cliente buscarClientePorId(Long id) {
        return clienteDao.listarPorId(id);
    }


    public Cliente buscarClientePorIdPedido(int idPedido) {

        Cliente clienteEncontrado = this.banco.getPedidos().stream()
                .filter(pedido -> pedido.getId() == idPedido)
                .findFirst()
                .orElse(null)
                .getCliente();

        return clienteEncontrado;
    }

    public int criarPedidoCliente(Long idCliente) {

        Cliente cliente = buscarClientePorId(idCliente);
        List<Pizza> itens = new ArrayList<>();
        Pedido pedido = new Pedido(itens, cliente);
        banco.getPedidos().add(pedido);
        List<Pedido> pedidos = new ArrayList<>(Arrays.asList(pedido));
        cliente.setPedidos(pedidos);

        return pedido.getId();
    }

}