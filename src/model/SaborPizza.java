package model;
import java.util.Objects;

import enums.TipoSabor;

public class SaborPizza {
    private String nome;
    private TipoSabor tipoSabor;

    public SaborPizza(String nome, TipoSabor tipoSabor) {
        this.tipoSabor = tipoSabor;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SaborPizza that = (SaborPizza) obj;
        return Objects.equals(nome, that.nome) && Objects.equals(tipoSabor, that.tipoSabor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, tipoSabor);
    }

}
