package model;

import enums.StatusPedido;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private StatusPedido status = StatusPedido.ABERTO;
    private List<Pizza> itens;
    private Double precoTotal;
    private static int idCounter = 0;
    private final int id;
    private int idCliente;

    public Pedido(List<Pizza> itens, int idCliente) {
        this.itens = itens;
        this.id = ++idCounter;
        this.idCliente = idCliente;
    }

    public List<Pizza> getItens() {
        return itens;
    }

    public void setPrecoTotal(Double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public void setItens(List<Pizza> itens) {
        this.itens = itens;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }
}
