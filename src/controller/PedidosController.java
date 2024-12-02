package controller;

import dados.BancoDados;
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

}
