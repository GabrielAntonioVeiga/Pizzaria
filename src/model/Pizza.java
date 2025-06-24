package model;

import java.util.List;

public class Pizza {
    private Long id;
    private Forma forma;
    private List<SaborPizza> saborPizza;
    private double preco;
    private Long id_pedido;

    public Pizza(Long id, Forma forma, List<SaborPizza> saborPizza) {
        this.id = id;
        this.forma = forma;
        this.saborPizza = saborPizza;
        this.preco = calculaPreco();
    }

    public Long getId() {
        return id;
    }

    public Forma getForma() {
        return forma;
    }

    public Double getTamanho(){
        return this.forma.calcularArea();
    }

    public String getNomeSabores(){
        String sabores = "";
        for (int i = 0; i < this.saborPizza.size(); i++) {
            SaborPizza sabor = this.saborPizza.get(i);

            boolean deveAdicionarTraco = this.saborPizza.size() > 1 && i < this.saborPizza.size() - 1;

            sabores += sabor.getNome();

            if (deveAdicionarTraco) {
                sabores += " | ";
            }
        }
        return sabores;
    }

    public Double calculaPreco() {
        int numSabores = this.saborPizza.size();
        double precoPizza = 0.0;
        double areaSabor = this.getForma().calcularArea();

        for (SaborPizza sabor : this.saborPizza) {
            double precoSabor = sabor.getTipoSabor().getPrecoCm2();
            precoPizza += precoSabor * (areaSabor/numSabores);
        }

        return precoPizza;
    }

    public double getPreco() {
        return preco;
    }

    public List<SaborPizza>  getSabores(){
        return saborPizza;
    }

    public void setForma(Forma forma) {
        this.forma = forma;
    }
    public void setSabores(List<SaborPizza> saborPizza) {
        this.saborPizza = saborPizza;
    }

    public String toString() {
        return "Pizza [Forma=" + getForma().toString() + ", Tamanho=" + getTamanho() + "cm², Sabores=" + getNomeSabores() + "]";
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Long id_pedido) {
        this.id_pedido = id_pedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pizza that = (Pizza) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
