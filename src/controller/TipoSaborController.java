package controller;

import dao.sabor.ITipoSaborDao;
import enums.EnTipoSabor;
import model.TipoSabor;
import view.AtualizarTipoSaborView;

import javax.swing.*;
import java.util.List;

public class TipoSaborController {
    private final ITipoSaborDao dao;
    private final SaborController saborController = new SaborController();
    private AtualizarTipoSaborView view;

    public TipoSaborController(ITipoSaborDao dao) {
        this.dao = dao;
    }

    public TipoSaborController(ITipoSaborDao dao, AtualizarTipoSaborView view) {
        this.dao = dao;
        this.view = view;
    }

    public void atualizarPreco(EnTipoSabor nomeTipoSabor, double novoPreco) {
        TipoSabor tipoExistente = dao.buscarPorTipo(nomeTipoSabor.toString());
        if(tipoExistente == null) {
            view.exibirMensagemErro("Tipo não encontrado!");
            return;
        }

        dao.atualizar(new TipoSabor(nomeTipoSabor, novoPreco));
        atualizarPrecoSabores(nomeTipoSabor.toString(), novoPreco);
        view.exibirMensagemSucesso(
                "Sucesso ao atualizar preço",
                "Preço atualizado com sucesso!");
    }

    public TipoSabor buscarPorTipo(String tipo) {
        return dao.buscarPorTipo(tipo);
    }

    public List<TipoSabor> carregarTipoSabores() {
        return dao.listar();
    }

    private void atualizarPrecoSabores(String tipo, Double preco) {
        saborController.atualizarPrecoSabores(tipo, preco);
    }
}