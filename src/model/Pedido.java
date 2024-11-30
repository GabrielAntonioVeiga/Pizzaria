package model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private List<Pizza> itens;

    public Pedido(List<Pizza> itens) {
        this.itens = itens;
    }

    public List<Pizza> getItens() {
        return itens;
    }

    public void setItens(ArrayList<Pizza> itens) {
        this.itens = itens;
    }
}
