package model;

import enums.NomeTipoSabor;

public class TipoSabor {
    private NomeTipoSabor nomeTipoSabor;
    private double precoCm2;

    public TipoSabor(NomeTipoSabor nomeTipoSabor, double precoCm2) {
        this.nomeTipoSabor = nomeTipoSabor;
        this.precoCm2 = precoCm2;
    }

    public NomeTipoSabor getNomeTipoSabor() {
        return nomeTipoSabor;
    }

    public double getPrecoCm2() {
        return precoCm2;
    }

    public void setNomeTipoSabor(NomeTipoSabor nomeTipoSabor) {
        this.nomeTipoSabor = nomeTipoSabor;
    }

    public void setPrecoCm2(double precoCm2) {}
}
