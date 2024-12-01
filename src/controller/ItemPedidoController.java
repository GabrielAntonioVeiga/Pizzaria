package controller;

import dados.BancoDados;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class ItemPedidoController {
    private final BancoDados banco = BancoDados.getInstancia();


    public void editarItemPedido(Cliente cliente, Pizza pizza, int idItem) {

        Pizza pizzaBanco = retornarItemPedido(cliente, idItem);

        pizzaBanco.setForma(pizza.getForma());
        pizzaBanco.setSabores(pizza.getSabores());
    }

    public Pizza retornarItemPedido(Cliente cliente, int idItem) {
        Cliente clienteEncontrado = retornarClientePorTelefone(cliente.getTelefone());

       Pizza itemBuscado = clienteEncontrado.getPedido().getItens().stream().filter(itemPedido -> itemPedido.getId() == idItem)
               .findFirst()
               .orElse(null);

       return itemBuscado;
    }

    public void adicionarItemPedido(Cliente cliente, Pizza pizza) {

       Cliente clienteEncontrado = retornarClientePorTelefone(cliente.getTelefone());


        Pedido pedido = clienteEncontrado.getPedido();
        if (pedido == null) {
            pedido = criarPedidoCliente();
            clienteEncontrado.setPedido(pedido);
        }

        pedido.getItens().add(pizza);

        clienteEncontrado.getPedido();
    }

    public Cliente retornarClientePorTelefone(String telefone) {
     return banco.getClientes().stream()
                .filter(clienteBanco -> clienteBanco.getTelefone().equals(telefone))
                .findFirst()
                .orElse(null);
    }

    public Pedido criarPedidoCliente() {
        return new Pedido(new ArrayList<>());
    }


}
