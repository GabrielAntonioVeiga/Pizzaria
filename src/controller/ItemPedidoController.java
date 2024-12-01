package controller;

import dados.BancoDados;
import enums.NomeTipoSabor;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemPedidoController {
    private final BancoDados banco = BancoDados.getInstancia();
    private final PedidoController pedidoController = new PedidoController();


    public void editarItemPedido(int idPedido, Pizza pizza, int idItem) {
        Pizza pizzaBanco = retornarItemPedido(idPedido, idItem);

        pizzaBanco.setForma(pizza.getForma());
        pizzaBanco.setSabores(pizza.getSabores());
    }

    public Pizza retornarItemPedido(int idPedido, int idItem) {
        Pedido pedido = pedidoController.retornarPedidoPeloId(idPedido);
       Pizza itemBuscado = pedido.getItens().stream().filter(itemPedido -> itemPedido.getId() == idItem)
               .findFirst()
               .orElse(null);

       return itemBuscado;
    }

    public void adicionarItemPedido(int idPedido, Pizza pizza) {
       Pedido pedido = pedidoController.retornarPedidoPeloId(idPedido);
       pedido.getItens().add(pizza);
    }

    public Cliente retornarClientePorTelefone(String telefone) {
     return banco.getClientes().stream()
                .filter(clienteBanco -> clienteBanco.getTelefone().equals(telefone))
                .findFirst()
                .orElse(null);
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
        Pedido pedido = new Pedido(itens, idCliente);

        List<Pedido> pedidos = Arrays.asList(pedido);
        cliente.setPedidos(pedidos);

        return pedido.getId();
    }


}
