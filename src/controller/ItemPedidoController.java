package controller;

import dao.pizza.IPizzaDao;
import factory.DAOFactory;
import model.*;
import view.PedidosView;

public class ItemPedidoController {
    private IPizzaDao pizzaDao = DAOFactory.getPizzaDao();
    private PedidosView pedidosView;
    private final PedidosController pedidosController = new PedidosController();

    public ItemPedidoController() {

    }


    public void editarItemPedido(Long idPedido, Pizza pizza, Long idItem) {
        Pizza pizzaBanco = retornarItemPedido(idPedido, idItem);

        pizzaBanco.setForma(pizza.getForma());
        pizzaBanco.setSabores(pizza.getSabores());

        double novoPreco = pizza.calculaPreco();
        pizzaBanco.setPreco(novoPreco);
    }

    public Pizza retornarItemPedido(Long idPedido, Long idItem) {
        Pedido pedido = pedidosController.retornarPedidoPeloId(idPedido);

        return pedido.getItens().stream().filter(itemPedido -> itemPedido.getId() == idItem)
               .findFirst()
               .orElse(null);
    }

    public void adicionarItemPedido(Long idPedido, Pizza pizza) {
        Pedido pedido = pedidosController.retornarPedidoPeloId(idPedido);
        pizza.setId_pedido(pedido.getId());
        pizzaDao.salvar(pizza);
        pedido.getItens().add(pizza);

        double precoTotal = pedido.calculaPrecoTotal();
        pedidosController.alterarPrecoPedido(idPedido, precoTotal);
        pedido.setPrecoTotal(precoTotal);
    }

    public void deletarItemPedido(Long idPedido, Long idItem) {
        Pedido pedido = pedidosController.retornarPedidoPeloId(idPedido);
        Pizza itemPedido = retornarItemPedido(idPedido, idItem);
        pizzaDao.deletar(itemPedido.getId());
        pedido.getItens().remove(itemPedido);

        double precoTotal = pedido.calculaPrecoTotal();
        pedidosController.alterarPrecoPedido(idPedido, precoTotal);
        pedido.setPrecoTotal(precoTotal);
    }


}
