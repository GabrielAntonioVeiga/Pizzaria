package model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
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

    public void setItens(ArrayList<Pizza> itens) {
        this.itens = itens;
    }
}
