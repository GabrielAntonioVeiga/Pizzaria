package controller;

import dao.sabor.ISaborDao;
import dao.sabor.ITipoSaborDao;
import factory.DAOFactory;
import model.TipoSabor;
import view.AtualizarTipoSaborView;
import view.MenuView;

import javax.swing.*;
import java.util.List;

public class TipoSaborController {

    private AtualizarTipoSaborView view;
    private ITipoSaborDao tipoSaborDao = DAOFactory.getTipoSaborDao();
    private ISaborDao saborDao = DAOFactory.getSaborDao();

    public TipoSaborController() {}

    public void iniciar() {
        this.view = new AtualizarTipoSaborView();
        this.view.setController(this);

        List<TipoSabor> tipos = tipoSaborDao.listar();
        this.view.popularTiposDeSabor(tipos);

        this.view.setVisible(true);
    }

    public void atualizarPreco() {
        TipoSabor tipoSaborSelecionado = view.getTipoSaborSelecionado();
        if (tipoSaborSelecionado == null) {
            view.exibirMensagem("Por favor, selecione um tipo de sabor.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double novoPreco = view.getNovoPreco();
            if (novoPreco <= 0) {
                view.exibirMensagem("O preço deve ser um número positivo.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TipoSabor tipoParaAtualizar = new TipoSabor(tipoSaborSelecionado.getNome(), novoPreco);

            tipoSaborDao.atualizar(tipoParaAtualizar);

            saborDao.atualizarPrecoSabores(tipoParaAtualizar.getNome().toString(), novoPreco);

            view.exibirMensagem("Preço atualizado com sucesso!", JOptionPane.INFORMATION_MESSAGE);

            view.popularTiposDeSabor(tipoSaborDao.listar());

        } catch (NumberFormatException e) {
            view.exibirMensagem("O preço informado é inválido. Use ponto (.) como separador decimal.", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            view.exibirMensagem("Ocorreu um erro ao atualizar o preço: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    public void voltarParaMenu() {
        view.dispose();
        new MenuController().iniciar();
    }
}