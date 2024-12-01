package controller;

import dados.BancoDados;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class ItemPedidoController {
    private final BancoDados banco = BancoDados.getInstancia();


    public void editarItemPedido(Pizza pizza, int idItem) {
    }

//    public Pizza retornarItemPedido(int idItem, int idPedido, idItem) {
//
//    }

    public void adicionarItemPedido(Cliente cliente, Pizza pizza) {

       Cliente clienteEncontrado = banco.getClientes().stream()
                .filter(clienteBanco -> clienteBanco.getTelefone().equals(cliente.getTelefone()))
                .findFirst()
                .orElse(null);


        Pedido pedido = clienteEncontrado.getPedido();
        if (pedido == null) {
            pedido = criarPedidoCliente();
            clienteEncontrado.setPedido(pedido);
        }

        pedido.getItens().add(pizza);

        clienteEncontrado.getPedido();

    }

    public Pedido criarPedidoCliente() {
        return new Pedido(new ArrayList<>());
    }

}
