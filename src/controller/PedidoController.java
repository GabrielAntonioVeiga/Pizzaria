package controller;

import dados.BancoDados;
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

    private final BancoDados banco = BancoDados.getInstancia();

    public PedidoController() {
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


}
