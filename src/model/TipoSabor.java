package model;

import enums.EnTipoSabor;

public class TipoSabor {
    private EnTipoSabor nomeTipoSabor;
    private double precoCm2;

    public TipoSabor(EnTipoSabor nomeTipoSabor, double precoCm2) {
        this.nomeTipoSabor = nomeTipoSabor;
        this.precoCm2 = precoCm2;
    }

    public EnTipoSabor getNome() {
        return nomeTipoSabor;
    }

    public double getPrecoCm2() {
        return precoCm2;
    }

    public void setNomeTipoSabor(EnTipoSabor nomeTipoSabor) {
        this.nomeTipoSabor = nomeTipoSabor;
    }

    public void setPrecoCm2(double precoCm2) {
        this.precoCm2 = precoCm2;
    }

    @Override
    public String toString() {
        return nomeTipoSabor.toString();
    }
}
