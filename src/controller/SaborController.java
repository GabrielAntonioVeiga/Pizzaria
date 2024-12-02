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

    public void adicionarSabor(String nomeSabor, NomeTipoSabor tipoSaborEnum, double precoPorCm2) {
        TipoSabor tipoSabor = new TipoSabor(tipoSaborEnum, precoPorCm2);
        SaborPizza novoSabor = new SaborPizza(nomeSabor, tipoSabor);
        BancoDados.getInstancia().getSabores().add(novoSabor);
    }


}
