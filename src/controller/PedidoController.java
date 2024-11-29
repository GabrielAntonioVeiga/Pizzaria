package controller;

import model.Cliente;
import model.Pedido;
import model.Pizza;
import view.ClienteView;
import view.PedidoView;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PedidoController {
    private DefaultTableModel tableModel;
    private PedidoView pedidoView;
    private List<Pizza> itensPedido = new ArrayList<>();

    public PedidoController(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public void carregarItensPedido(Cliente cliente) {
        Pedido pedido = cliente.getPedido();
        itensPedido = pedido.getItens();
        int contador = 0;
        for (Pizza pizza : itensPedido) {
            tableModel.setRowCount(contador);
            tableModel.addRow(new Object[]{
                    pizza.getForma(),
                    pizza.getTamanho(),
                    pizza.getSabores()
            });
        }
    }
}
