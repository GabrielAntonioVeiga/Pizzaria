package model;

import enums.EnForma;

public class Quadrado extends Forma {
        private double lado;

        public Quadrado() {}

        public Quadrado(double lado) {
            this.lado = lado;
        }


        @Override
        public void setDimensao(double lado) {
            this.lado = lado;
        }

        public double getLado() {
            return lado;
        }

        @Override
        public double calcularDimensao(double area) {
            if (area < 0) {

                throw new IllegalArgumentException("A área não pode ser negativa");
            }
            return Math.sqrt(area);
        }

        @Override
        public double calcularArea() {
            return lado * lado;
        }

        @Override
        public void validarDimensao(double valor, boolean mostrarErroDeArea) {

            String mensagemErro = "Dimensão inválida: Os Lados deve estar entre 10 e 40.";

            if(mostrarErroDeArea)
                mensagemErro = "Dimensão inválida: A área deve estar entre 100cm² e 1600cm²";


            if (valor < 10 || valor > 40) {
                throw new IllegalArgumentException(mensagemErro);
            }
        }


        @Override
        public String getNomeForma() {
            return EnForma.QUADRADO.getNome();
        }
}
