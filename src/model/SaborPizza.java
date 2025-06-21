package model;
import enums.EnTipoSabor;

import java.util.Objects;

public class SaborPizza {
    private Long id;
    private String nome;
    private TipoSabor tipoSabor;

    public SaborPizza(String nome, TipoSabor tipoSabor) {
        this.tipoSabor = tipoSabor;
        this.nome = nome;
    }

    public SaborPizza(Long id, String nome, String tipo, Double precoCm2) {
        this.id = id;
        this.nome = nome;
        this.tipoSabor = new TipoSabor(EnTipoSabor.valueOf(tipo), precoCm2);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoSabor getTipoSabor() {
        return tipoSabor;
    }
    public void setTipoSabor(TipoSabor tipoSabor) {
        this.tipoSabor = tipoSabor;
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
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

}
