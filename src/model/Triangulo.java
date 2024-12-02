package model;

public class Triangulo extends Forma {
    private double lado;

    public Triangulo() {}

    public Triangulo(double lado) {
        this.lado = lado;
    }

    @Override
    public void setDimensao(double lado) {
        this.lado = lado;
    }

    @Override
    public double calcularDimensao(double area) {
        if (area < 0) {
            throw new IllegalArgumentException("A área não pode ser negativa");
        }
        return Math.sqrt((4 * area) / Math.sqrt(3));
    }

    @Override
    public double calcularArea() {
        return (Math.pow(lado, 2) * Math.sqrt(3)) / 4;
    }

    public void validarDimensao(double valor, boolean mostrarErroDeArea) {

        String mensagemErro = "Dimensão inválida: Deve estar entre 20 e 60.";

        if(mostrarErroDeArea)
            mensagemErro = "Dimensão inválida: A área deve estar 173,21cm² e 1558,85cm²";


        if (valor < 20 || valor > 60) {
            throw new IllegalArgumentException(mensagemErro);
        }
    }

    @Override
    public String getNomeForma() {
        return "Triangulo";
    }
}
