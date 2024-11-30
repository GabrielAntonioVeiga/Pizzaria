package model;

    public class Quadrado extends Forma {
        private final String formaPizza = "Quadrado";
        private double lado;

        public Quadrado(double lado) {
            if (validarDimensao(lado)) {
                this.lado = lado;
            } else {
                throw new IllegalArgumentException("Lado do quadrado deve ser entre 10 e 40 cm.");
            }
        }

        @Override
        public double calcularArea() {
            return lado * lado;
        }

        @Override
        public boolean validarDimensao(double valor) {
            return valor >= 10 && valor <= 40;
        }

        @Override
        public String getForma() {
            return formaPizza;
        }
}
