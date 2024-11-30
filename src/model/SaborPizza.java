package model;

import enums.TipoSabor;

public class SaborPizza {
    private String nome;
    private TipoSabor tipoSabor;

    public SaborPizza(String nome, int tipoSabor) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
