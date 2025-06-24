package model;

import enums.EnStatusPedido;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private EnStatusPedido status = EnStatusPedido.ABERTO;
    private List<Pizza> itens = new ArrayList<>();
    private Double precoTotal=0.0;
    private Long id;
    private Cliente cliente;

    public Pedido(Long id, Cliente cliente, EnStatusPedido status, Double precoTotal) {
        this.id = id;
        this.cliente = cliente;
        this.status = status;
        this.precoTotal = precoTotal;
    }

    public Pedido(List<Pizza> itens, Cliente cliente) {
        this.itens = itens;
        this.cliente = cliente;
    }



    public List<Pizza> getItens() {
        return itens;
    }

    public void setPrecoTotal(Double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public Double calculaPrecoTotal() {
        Double precoTotal = 0.0;
        for(Pizza pizza : itens){
            precoTotal += pizza.getPreco();
        }
        return precoTotal;
    }

    public Double getPrecoTotal() {
        return precoTotal;
    }

    public void setItens(List<Pizza> itens) {
        this.itens = itens;
    }

    public EnStatusPedido getStatus() {
        return status;
    }

    public void setStatus(EnStatusPedido status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
