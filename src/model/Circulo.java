package model;

import enums.EnForma;

public class Circulo extends Forma {
    private double raio;

    public Circulo() { }

    @Override
    public void setDimensao(double raio) {
        this.raio = raio;
    }

    public double getRaio() {
        return raio;
    }

    @Override
    public double calcularDimensao(double area) {
        if (area < 0) {
            throw new IllegalArgumentException("A área não pode ser negativa");
        }
        return Math.sqrt(area / Math.PI);
    }

    @Override
    public double calcularArea() {
        return Math.PI * Math.pow(raio, 2);
    }

    public void validarDimensao(double valor, boolean mostrarErroDeArea) {

        String mensagemErro = "Dimensão inválida: Deve estar entre 7 e 23.";

        if(mostrarErroDeArea)
            mensagemErro = "Dimensão inválida: A área deve estar 153,94cm² e 1661,90cm²";

        if (valor < 7 || valor > 23) {
            throw new IllegalArgumentException(mensagemErro);
        }
    }

    @Override
    public String getNomeForma() {
        return EnForma.CIRCULO.getNome();
    }

    @Override
    public Double getMedida() {
        return this.raio;
    }
}
