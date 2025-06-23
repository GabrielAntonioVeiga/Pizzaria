package controller;

import dao.pedido.IPedidoDao;
import dao.pizza.IPizzaDao;
import enums.EnStatusPedido;
import factory.DAOFactory;
import model.Cliente;
import model.Pedido;
import model.Pizza;
import view.PedidosView;

import java.util.List;

public class PedidosController {
    private IPedidoDao pedidoDao = DAOFactory.getPedidoDao();
    private IPizzaDao pizzaDao = DAOFactory.getPizzaDao();
    private PedidosView view;

    public PedidosController(PedidosView view) {
        this.view = view;
    }

    public PedidosController() {}


    public List<Pedido> carregarPedidos() {
        return pedidoDao.listar();
    }

    public List<Pedido> carregarPedidosPorCliente(Cliente cliente) {
        return pedidoDao.listarPorCliente(cliente);
    }


    public List<Pizza> carregarItensPedido(Long idPedido) {
        return pizzaDao.listarPorPedido(idPedido);
    }

    public void alterarStatusPedido(Long idPedido, EnStatusPedido novoStatus) {
        Pedido pedido = retornarPedidoPeloId(idPedido);
        pedido.setStatus(novoStatus);
    }

    public void alterarPrecoPedido(Long idPedido, double preco) {
        pedidoDao.alterarPrecoPedido(idPedido, preco);
    }

    public Pedido retornarPedidoPeloId(Long idPedido) {
        Pedido pedido = pedidoDao.listarPorId(idPedido);
        pedido.setItens(pizzaDao.listarPorPedido(idPedido));
        return pedido;
    }


    public void deletarPedido(Long idPedido) {
        pedidoDao.remover(idPedido);
    }




}
