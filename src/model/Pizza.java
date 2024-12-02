package model;

import java.util.List;

public class Pizza {
    private Forma forma;
    private List<SaborPizza> saborPizza;
    private static int idCounter = 0;
    private final int id;
    private double preco;

    public Pizza(Forma forma, List<SaborPizza> saborPizza) {
        this.forma = forma;
        this.saborPizza = saborPizza;
        this.id = ++idCounter;
        this.preco = calculaPreco();
    }

    public int getId() {
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
}
