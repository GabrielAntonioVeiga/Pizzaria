package controller;

import enums.StatusPedido;
import model.Cliente;
import model.Pedido;
import model.Pizza;
import view.ClienteView;
import view.PedidoView;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PedidoController {

    public PedidoController() {
    }


    public List<Pizza> carregarItensPedido(Cliente cliente) {
        Pedido pedido = cliente.getPedido();
        List<Pizza> itensPedido = new ArrayList<>();
        if(pedido == null)
            return itensPedido;

        return pedido.getItens();
    }

    public void alterarStatusPedido(Pedido pedido, StatusPedido novoStatus) {
        pedido.setStatus(novoStatus);
    }
}
