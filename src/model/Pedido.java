package model;

import enums.EnStatusPedido;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private EnStatusPedido status; 
    private List<Pizza> itens = new ArrayList<>();
    private Long id;
    private Cliente cliente;

    private Double precoTotal;
    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.status = EnStatusPedido.ABERTO; 
    }

    public Pedido(Long id, Cliente cliente, EnStatusPedido status,  Double precoTotal) {
        this.id = id;
        this.cliente = cliente;
        this.status = status;
        this.precoTotal = precoTotal;
    }

    public Double getPrecoTotal() {
        if (this.itens != null && !this.itens.isEmpty()) {
            return this.itens.stream()
                    .mapToDouble(Pizza::getPreco)
                    .sum();
        }

        return this.precoTotal;
    }

    public List<Pizza> getItens() {
        return itens;
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