package controller;

import dados.BancoDados;
import model.SaborPizza;

import java.util.List;

public class SaborController {
    private final BancoDados banco = BancoDados.getInstancia();
    private final TipoSaborController tipoSaborController = new TipoSaborController()  ;

    public List<SaborPizza> carregarSabores() {
        return banco.getSabores();
    }

    public void adicionarSabor(SaborPizza novoSabor) {
        BancoDados.getInstancia().getSabores().add(novoSabor);
    }

    public void atualizarSabor(SaborPizza novoSabor, String nomeAtual) {
       SaborPizza sabor = carregarSaborPeloNome(nomeAtual);
       sabor.setNome(novoSabor.getNome());
       sabor.setTipoSabor(novoSabor.getTipoSabor());
    }

    public SaborPizza carregarSaborPeloNome(String nome) {
        return this.carregarSabores().stream()
                .filter(sabor -> sabor.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }

    public void deletarSabor(String nome) {
        SaborPizza sabor = carregarSaborPeloNome(nome);
        List<SaborPizza> sabores = carregarSabores();
        sabores.remove(sabor);
    }


}
