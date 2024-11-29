package controller;

import model.Cliente;
import model.Pedido;
import view.ClienteView;
import view.PedidoView;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PedidoController {
    private DefaultTableModel tableModel;
    private PedidoView pedidoView;
    private List<Pedido> pedidos = new ArrayList<Pedido>();


}
