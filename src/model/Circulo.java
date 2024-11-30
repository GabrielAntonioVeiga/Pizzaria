package model;

class Circulo extends Forma {
    private final String formaPizza = "Círculo";
    private double raio;

    public Circulo(double raio) {
        if (validarDimensao(raio)) {
            this.raio = raio;
        } else {
            throw new IllegalArgumentException("Raio do círculo deve ser entre 7 e 23 cm.");
        }
    }

    @Override
    public double calcularArea() {
        return Math.PI * Math.pow(raio, 2);
    }

    @Override
    public boolean validarDimensao(double valor) {
        return valor >= 7 && valor <= 23;
    }

    @Override
    public String getForma() {
        return formaPizza;
    }
}
