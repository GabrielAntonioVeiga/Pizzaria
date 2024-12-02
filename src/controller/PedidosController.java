package controller;

import dados.BancoDados;
import enums.StatusPedido;
import model.Cliente;
import model.Pedido;
import model.Pizza;

import java.util.ArrayList;
import java.util.List;

public class PedidosController {
    private final BancoDados banco = BancoDados.getInstancia();


    public List<Pedido> carregarPedidos() {
        List<Pedido> pedidos = banco.getPedidos();
        return pedidos;
    }

    public List<Pedido> carregarPedidosPorCliente(Cliente cliente) {
        List<Pedido> pedidosCliente = new ArrayList<>();
        for(Pedido pedido : carregarPedidos()) {
            if(pedido.getCliente().getId() == cliente.getId()){
                pedidosCliente.add(pedido);
            }
        }
        return pedidosCliente;
    }


    public List<Pizza> carregarItensPedido(int idPedido) {
        Pedido pedido = retornarPedidoPeloId(idPedido);
        List<Pizza> itensPedido = new ArrayList<>();
        if(pedido == null)
            return itensPedido;

        return pedido.getItens();
    }

    public void alterarStatusPedido(int idPedido, StatusPedido novoStatus) {
        Pedido pedido = retornarPedidoPeloId(idPedido);
        pedido.setStatus(novoStatus);
    }

    public Pedido retornarPedidoPeloId(int idPedido) {
        return banco.getPedidos().stream()
                .filter(pedidoBanco -> pedidoBanco.getId() == idPedido)
                .findFirst()
                .orElse(null);
    }


    public void deletarPedido(int idPedido) {
        Pedido pedido = retornarPedidoPeloId(idPedido);
        List<Pedido> pedidos = carregarPedidos();
        pedido.getCliente().getPedidos().remove(pedido);
        pedidos.remove(pedido);
    }


}
