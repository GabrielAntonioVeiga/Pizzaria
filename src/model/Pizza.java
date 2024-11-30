package model;

import java.util.List;

public class Pizza {
    private Forma forma;
    private List<SaborPizza> saborPizza;

    public Pizza(Forma forma, List<SaborPizza> saborPizza) {
        this.forma = forma;
        this.saborPizza = saborPizza;
    }

    public String getForma() {
        return forma.getForma();
    }

    public Double getTamanho(){
        return this.forma.calcularArea();
    }

    public String getSabores(){
        String sabores = "";
        for(SaborPizza sabor: this.saborPizza){
            sabores = sabor.getNome() + " | ";
        }
        return sabores;
    }

    public void setForma(Forma forma) {
        this.forma = forma;
    }
}
