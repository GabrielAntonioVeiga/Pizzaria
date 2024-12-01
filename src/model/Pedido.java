package model;

import enums.StatusPedido;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private StatusPedido status = StatusPedido.ABERTO;
    private List<Pizza> itens;

    private static int idCounter = 0;
    private final int id;

    public Pedido(List<Pizza> itens) {
        this.itens = itens;
        this.id = ++idCounter;
    }

    public List<Pizza> getItens() {
        return itens;
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
