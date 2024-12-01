package controller;

import dados.BancoDados;
import enums.NomeTipoSabor;
import model.Pizza;
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
}