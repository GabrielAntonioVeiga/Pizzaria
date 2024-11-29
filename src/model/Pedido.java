package model;

import java.util.ArrayList;

public class Pedido {
    private ArrayList<Pizza> itens = new ArrayList();

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<Pizza> getItens() {
        return itens;
    }

    public void setItens(ArrayList<Pizza> itens) {
        this.itens = itens;
    }
}
