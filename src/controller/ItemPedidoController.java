package controller;

import dados.BancoDados;
import model.*;

public class ItemPedidoController {
    private final BancoDados banco = BancoDados.getInstancia();
    private final PedidosController pedidosController = new PedidosController();


    public void editarItemPedido(int idPedido, Pizza pizza, int idItem) {
        Pizza pizzaBanco = retornarItemPedido(idPedido, idItem);

        pizzaBanco.setForma(pizza.getForma());
        pizzaBanco.setSabores(pizza.getSabores());

        double novoPreco = pizza.calculaPreco();
        pizzaBanco.setPreco(novoPreco);
    }

    public Pizza retornarItemPedido(int idPedido, int idItem) {
        Pedido pedido = pedidosController.retornarPedidoPeloId(idPedido);
       Pizza itemBuscado = pedido.getItens().stream().filter(itemPedido -> itemPedido.getId() == idItem)
               .findFirst()
               .orElse(null);

       return itemBuscado;
    }

    public void adicionarItemPedido(int idPedido, Pizza pizza) {
        Pedido pedido = pedidosController.retornarPedidoPeloId(idPedido);
        pedido.getItens().add(pizza);
        double precoTotal = pedido.calculaPrecoTotal();
        pedido.setPrecoTotal(precoTotal);
    }

    public void deletarItemPedido(int idPedido, int idItem) {
        Pedido pedido = pedidosController.retornarPedidoPeloId(idPedido);
        Pizza itemPedido = retornarItemPedido(idPedido, idItem);
        pedido.getItens().remove(itemPedido);

        double precoTotal = pedido.calculaPrecoTotal();
        pedido.setPrecoTotal(precoTotal);
    }


}
