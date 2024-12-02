package controller;

import dados.BancoDados;
import enums.NomeTipoSabor;
import model.SaborPizza;
import model.TipoSabor;

import java.util.List;

public class TipoSaborController {
    private final BancoDados banco = BancoDados.getInstancia();

    public void atualizarPreco(NomeTipoSabor nomeTipoSabor, double novoPreco) {

        TipoSabor tipoSaborEncontrado = banco.getTiposSabores().stream()
                .filter(tipo -> tipo.getNome().equals(nomeTipoSabor))
                .findFirst()
                .orElse(null);

        tipoSaborEncontrado.setPrecoCm2(novoPreco);
    }

    public List<TipoSabor> carregarTipoSabores() {
        return banco.getTiposSabores();
    }

    public TipoSabor carregarTipoSaborPeloNome(NomeTipoSabor nome) {
        return carregarTipoSabores().stream()
                .filter(tipoSabor -> tipoSabor.getNome() == nome)
                .findFirst()
                .orElse(null);
    }
}