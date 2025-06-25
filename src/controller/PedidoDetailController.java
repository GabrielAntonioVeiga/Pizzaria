package controller;

import dao.pedido.IPedidoDao;
import dao.pizza.IPizzaDao;
import enums.EnStatusPedido;
import factory.DAOFactory;
import model.Pedido;
import model.Pizza;
import view.PedidoView;

import javax.swing.*;
import java.util.List;

public class PedidoDetailController {

    private  PedidoView view;
    private final Long idPedido;

    private final IPedidoDao pedidoDao = DAOFactory.getPedidoDao();
    private final IPizzaDao pizzaDao = DAOFactory.getPizzaDao();

    public PedidoDetailController(Long idPedido) {
        this.idPedido = idPedido;
    }

    public void iniciar() {
        this.view = new PedidoView();
        this.view.setController(this);

        carregarDadosIniciais();
        this.view.setVisible(true);
    }
    private void carregarDadosIniciais() {
        Pedido pedido = pedidoDao.listarPorId(idPedido);
        List<Pizza> itens = pizzaDao.listarPorPedido(idPedido);
        pedido.setItens(itens);

        if (itens.isEmpty()) {
            view.mostrarMensagemVazio("Não há itens neste pedido.");
            view.setPrecoTotal("R$ 0,00");
        } else {
            view.renderizarItensNaTabela(itens);
            view.setPrecoTotal(String.format("R$%.2f", pedido.getPrecoTotal()));
        }

        view.setStatus(pedido.getStatus().toString());

        boolean podeEditar = pedido.getStatus() == EnStatusPedido.ABERTO;
        view.setBotoesEdicaoEnabled(podeEditar);
    }


    public void adicionarItem() {
        view.dispose();
        new ItemPedidoController(idPedido, null).iniciar();
    }

    public void editarItem() {
        Long idItemSelecionado = view.getIdItemSelecionado();
        if (idItemSelecionado == null) {
            view.exibirMensagem("Erro", "Nenhum Item selecionado para edição!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        view.dispose();
        new ItemPedidoController(idPedido, idItemSelecionado).iniciar();
    }
    public void deletarItem() {
        Long idItemSelecionado = view.getIdItemSelecionado();
        if (idItemSelecionado == null) {
            view.exibirMensagem("Erro", "Selecione um item para excluir!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pizzaDao.removerDoPedido(idPedido, idItemSelecionado);
        view.exibirMensagem("Sucesso", "Item excluído com sucesso!", JOptionPane.INFORMATION_MESSAGE);

        atualizarPrecoTotalPedido();

        carregarDadosIniciais();
    }
    public void voltar() {
        view.dispose();
        new PedidosController().iniciar();
    }
    private void atualizarPrecoTotalPedido() {
        List<Pizza> itensAtuais = pizzaDao.listarPorPedido(idPedido);
        Pedido pedidoTemporario = new Pedido(null);
        pedidoTemporario.setItens(itensAtuais);
        double novoPrecoTotal = pedidoTemporario.getPrecoTotal();
        pedidoDao.alterarPrecoPedido(idPedido, novoPrecoTotal);
    }
}