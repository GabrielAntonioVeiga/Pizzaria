package controller;

import dados.BancoDados;
import enums.NomeTipoSabor;
import model.SaborPizza;
import model.TipoSabor;

import java.util.List;

public class SaborController {
    private final BancoDados banco = BancoDados.getInstancia();

    public List<SaborPizza> carregarSabores() {
        return banco.getSabores();
    }

    public List<TipoSabor> carregarTipoSabores() {
        return banco.getTiposSabores();
    }

}
