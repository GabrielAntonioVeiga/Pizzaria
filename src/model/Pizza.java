package model;

import java.util.List;
import java.util.UUID;

public class Pizza {
    private Forma forma;
    private List<SaborPizza> saborPizza;
    private static int idCounter = 0;
    private final int id;

    public Pizza(Forma forma, List<SaborPizza> saborPizza) {
        this.forma = forma;
        this.saborPizza = saborPizza;
        this.id = ++idCounter;
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

    public Double getPreco() {
        Double preco = 0.0;
        int numSabores = this.saborPizza.size();

        switch (numSabores) {
            case 1:
                double precoSabor = this.getSabores().get(0).getTipoSabor().getPrecoCm2();
                preco = precoSabor * this.getForma().calcularArea();
                break;
            case 2:
                for (SaborPizza sabor : this.saborPizza) {
                    precoSabor = sabor.getTipoSabor().getPrecoCm2();
                    double areaSabor = this.getForma().calcularArea();
                    preco += (areaSabor * precoSabor)/2;
                }
                break;
            default:
                break;
        }
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
}
