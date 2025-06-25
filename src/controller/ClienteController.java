package controller;

import dao.cliente.IClienteDao;
import factory.DAOFactory;
import model.Cliente;
import view.ClienteView;
import view.MenuView;

import javax.swing.*;
import java.util.List;

public class ClienteController {

    private ClienteView view;
    private IClienteDao clienteDao = DAOFactory.getClienteDao();

    public ClienteController() {}

    public void iniciar() {
        this.view = new ClienteView();
        this.view.setController(this);

        List<Cliente> clientes = clienteDao.listar();
        this.view.carregarClientes(clientes);

        this.view.setVisible(true);
    }

    public void adicionarCliente() {
        String nome = view.getNome();
        String sobrenome = view.getSobrenome();
        String telefone = view.getTelefone().replaceAll("[^\\d]", "");

        if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
            view.exibirMensagem("Por favor, preencha todos os campos.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (telefone.length() < 10) {
            view.exibirMensagem("Telefone inválido. Deve conter DDD + número.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (clienteDao.listarPorTelefone(telefone) != null) {
            view.exibirMensagem("Já existe um cliente com este telefone.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente novoCliente = new Cliente(nome, sobrenome, telefone);
        clienteDao.salvar(novoCliente);

        view.exibirMensagem("Cliente cadastrado com sucesso!", JOptionPane.INFORMATION_MESSAGE);
        view.limparCampos();
        view.carregarClientes(clienteDao.listar());
    }

    public void removerCliente() {
        Long id = view.getIdClienteSelecionado();
        if (id == null) {
            view.exibirMensagem("Nenhum cliente selecionado para exclusão.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Atenção: Excluir um cliente também removerá todos os seus pedidos.\nTem certeza que deseja continuar?",
                "Excluir Cliente",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            clienteDao.remover(id);
            view.exibirMensagem("Cliente removido com sucesso!", JOptionPane.INFORMATION_MESSAGE);
            view.carregarClientes(clienteDao.listar());
        }
    }

    public void carregarClienteParaEdicao() {
        Long id = view.getIdClienteSelecionado();
        if (id == null) {
            view.limparCampos();
            return;
        }
        Cliente cliente = clienteDao.listarPorId(id);
        if (cliente != null) {
            view.preencherCampos(cliente);
        }
    }

    public void salvarEdicaoCliente() {
        Long id = view.getIdClienteSelecionado();
        if (id == null) {
            view.exibirMensagem("Nenhum cliente selecionado para salvar a edição.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = view.getNome();
        String sobrenome = view.getSobrenome();
        String telefone = view.getTelefone().replaceAll("[^\\d]", "");

        if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
            view.exibirMensagem("Por favor, preencha todos os campos.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (telefone.length() < 10) {
            view.exibirMensagem("Telefone inválido. Deve conter DDD + número.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente clienteComMesmoTelefone = clienteDao.listarPorTelefone(telefone);
        if (clienteComMesmoTelefone != null && !clienteComMesmoTelefone.getId().equals(id)) {
            view.exibirMensagem("Este telefone já está cadastrado para outro cliente.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente clienteParaAtualizar = new Cliente(id, nome, sobrenome, telefone);
        clienteDao.atualizar(clienteParaAtualizar);

        view.exibirMensagem("Cliente atualizado com sucesso!", JOptionPane.INFORMATION_MESSAGE);
        view.limparCampos();
        view.carregarClientes(clienteDao.listar());
    }

    public void filtrarTabela() {
        String filtro = view.getTextoFiltro();
        view.aplicarFiltroNaTabela(filtro);
    }

    public void irParaPedidos() {
        Long id = view.getIdClienteSelecionado();
        if (id == null) {
            view.exibirMensagem("Selecione um cliente para ver os pedidos.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        view.dispose();
        new PedidosController().iniciarParaCliente(id);
    }

    public void voltarParaMenu() {
        view.dispose();
        new MenuController().iniciar();
    }
}