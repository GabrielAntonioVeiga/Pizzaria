package controller;

import dao.cliente.IClienteDao;
import dao.pedido.IPedidoDao;
import factory.DAOFactory;
import model.Cliente;
import model.Pedido;
import enums.EnStatusPedido;
import view.MenuView;
import view.PedidosView;

import javax.swing.*;
import java.util.List;

public class PedidosController {

    private PedidosView view;
    private IPedidoDao pedidoDao = DAOFactory.getPedidoDao();
    private IClienteDao clienteDao = DAOFactory.getClienteDao();

    public PedidosController() {}

    public void iniciar() {
        this.view = new PedidosView();
        this.view.setController(this);
        this.view.renderizarItensNaTabela(carregarTodosPedidos());
        this.view.setVisible(true);
    }

    public void iniciarParaCliente(Long clienteId) {
        this.view = new PedidosView();
        this.view.setController(this);

        Cliente cliente = clienteDao.listarPorId(clienteId);
        if (cliente != null) {
            List<Pedido> pedidosDoCliente = pedidoDao.listarPorCliente(cliente);
            this.view.renderizarItensNaTabela(pedidosDoCliente);
             this.view.setTelefoneBusca(cliente.getTelefone()); 
        } else {
            this.view.renderizarItensNaTabela(List.of());
            this.view.exibirMensagem("Erro", "Cliente com ID " + clienteId + " não foi encontrado.", JOptionPane.ERROR_MESSAGE);
        }

        this.view.setVisible(true);
    }

    public void buscarPedidosPorCliente() {
        String telefone = view.getTelefoneBusca();
        if (telefone == null || telefone.trim().isEmpty()) {
            view.exibirMensagem("Aviso", "Por favor, digite um telefone para buscar.", javax.swing.JOptionPane.WARNING_MESSAGE);
            view.renderizarItensNaTabela(carregarTodosPedidos());
            return;
        }

        Cliente cliente = clienteDao.listarPorTelefone(telefone);

        if (cliente == null) {
            view.exibirMensagem("Não Encontrado", "Nenhum cliente encontrado com este telefone.", JOptionPane.WARNING_MESSAGE);
            view.renderizarItensNaTabela(List.of());
            return;
        }

        List<Pedido> pedidosDoCliente = pedidoDao.listarPorCliente(cliente);
        view.renderizarItensNaTabela(pedidosDoCliente);
    }

    public void deletarPedido() {
        Long idPedido = view.getSelectedPedidoId();
        if (idPedido == null) {
            view.exibirMensagem("Aviso", "Por favor, selecione um pedido para excluir.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Tem certeza que deseja excluir o pedido " + idPedido + "?",
                "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            pedidoDao.remover(idPedido);
            view.exibirMensagem("Sucesso", "Pedido removido com sucesso.", JOptionPane.INFORMATION_MESSAGE);
            view.renderizarItensNaTabela(carregarTodosPedidos());
        }
    }

    public void alterarStatusPedido() {
        Long idPedido = view.getSelectedPedidoId();
        EnStatusPedido novoStatus = view.getStatusSelecionado();

        if (idPedido == null || novoStatus == null) {
            view.exibirMensagem("Aviso", "Por favor, selecione um pedido e um status para alterar.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        pedidoDao.alterarStatusPedido(idPedido, novoStatus);
        view.renderizarItensNaTabela(carregarTodosPedidos());
    }

    public void verDetalhesPedido() {
        Long idPedido = view.getSelectedPedidoId();
        if (idPedido == null) return;

        view.dispose();
        new PedidoDetailController(idPedido).iniciar();
    }

    public void criarNovoPedido() {
        String telefone = view.getTelefoneBusca();
        if (telefone == null || telefone.trim().isEmpty()) {
            view.exibirMensagem("Aviso", "Busque por um cliente antes de criar um novo pedido.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cliente = clienteDao.listarPorTelefone(telefone);

        if (cliente == null) {
            view.exibirMensagem("Erro", "Cliente não encontrado. Verifique o telefone e busque novamente.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pedido pedidoParaSalvar = new Pedido(cliente);
        Pedido pedidoSalvo = pedidoDao.salvar(pedidoParaSalvar);

        if (pedidoSalvo != null && pedidoSalvo.getId() != null) {
            view.dispose();
            new ItemPedidoController(pedidoSalvo.getId(), null).iniciar();
        } else {
            view.exibirMensagem("Erro", "Falha ao criar o pedido no banco de dados.", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void voltarParaMenu() {
        view.dispose();
        new MenuController().iniciar();
    }

    private List<Pedido> carregarTodosPedidos() {
        return pedidoDao.listar();
    }
}