package controller;

import dados.BancoDados;
import enums.NomeTipoSabor;
import model.SaborPizza;
import model.TipoSabor;

public class TipoSaborController {
    private final BancoDados banco = BancoDados.getInstancia();

    public void atualizarPreco(NomeTipoSabor nomeTipoSabor, double novoPreco) {

        TipoSabor tipoSaborEncontrado = banco.getTiposSabores().stream()
                .filter(tipo -> tipo.getNome().equals(nomeTipoSabor))
                .findFirst()
                .orElse(null);

        tipoSaborEncontrado.setPrecoCm2(novoPreco);
    }
    public void adicionarSabor(String nomeSabor, NomeTipoSabor tipoSaborEnum, double precoPorCm2) {
        TipoSabor tipoSabor = new TipoSabor(tipoSaborEnum, precoPorCm2);
        SaborPizza novoSabor = new SaborPizza(nomeSabor, tipoSabor);
        BancoDados.getInstancia().getSabores().add(novoSabor);
    }
}