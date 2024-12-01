package model;

    public abstract class Forma {
        public abstract double calcularArea();

        public abstract double calcularDimensao(double area);

        public abstract void setDimensao(double dimensao);

        public abstract void validarDimensao(double valor, boolean mostrarErroDeArea);

        public abstract String getNomeForma();

        @Override
        public String toString() {
            return getNomeForma();
        }
    }






