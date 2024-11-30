package model;

public class Triangulo extends Forma {
    private final String formaPizza = "Triângulo";
    private double lado;

    public Triangulo(double lado) {
        if (validarDimensao(lado)) {
            this.lado = lado;
        } else {
            throw new IllegalArgumentException("Lado do triângulo deve ser entre 20 e 60 cm.");
        }
    }

    @Override
    public double calcularArea() {
        return (Math.pow(lado, 2) * Math.sqrt(3)) / 4;
    }

    @Override
    public boolean validarDimensao(double valor) {
        return valor >= 20 && valor <= 60;
    }

    @Override
    public String getForma() {
        return formaPizza;
    }
}
