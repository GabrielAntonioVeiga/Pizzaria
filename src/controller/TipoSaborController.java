package controller;

import dados.BancoDados;
import enums.EnTipoSabor;
import model.TipoSabor;

import java.util.List;

public class TipoSaborController {
    private final BancoDados banco = BancoDados.getInstancia();

    public void atualizarPreco(EnTipoSabor nomeTipoSabor, double novoPreco) {

        TipoSabor tipoSaborEncontrado = banco.getTiposSabores().stream()
                .filter(tipo -> tipo.getNome().equals(nomeTipoSabor))
                .findFirst()
                .orElse(null);

        tipoSaborEncontrado.setPrecoCm2(novoPreco);
    }

    public List<TipoSabor> carregarTipoSabores() {
        return banco.getTiposSabores();
    }

    public TipoSabor carregarTipoSaborPeloNome(EnTipoSabor nome) {
        return carregarTipoSabores().stream()
                .filter(tipoSabor -> tipoSabor.getNome() == nome)
                .findFirst()
                .orElse(null);
    }
}