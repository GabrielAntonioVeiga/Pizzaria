package controller;

import dao.sabor.ITipoSaborDao;
import enums.EnTipoSabor;
import model.TipoSabor;

import javax.swing.*;
import java.util.List;

public class TipoSaborController {
    private final ITipoSaborDao dao;
    private final SaborController saborController = new SaborController();

    public TipoSaborController(ITipoSaborDao dao) {
        this.dao = dao;
    }

    public void atualizarPreco(EnTipoSabor nomeTipoSabor, double novoPreco) {
        TipoSabor tipoExistente = dao.buscarPorTipo(nomeTipoSabor.toString());
        if(tipoExistente == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Tipo não encontrado!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        dao.atualizar(new TipoSabor(nomeTipoSabor, novoPreco));
        atualizarPrecoSabores(nomeTipoSabor.toString(), novoPreco);
        JOptionPane.showMessageDialog(
                null,
                "Preço atualizado com sucesso!",
                "Sucesso ao atualizar preço",
                JOptionPane.INFORMATION_MESSAGE
        );
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