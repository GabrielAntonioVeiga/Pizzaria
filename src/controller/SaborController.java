package controller;

import dao.sabor.ISaborDao;
import factory.DAOFactory;
import model.SaborPizza;
import view.CadastrarSaborView;

import javax.swing.*;
import java.util.List;

public class SaborController {
    private ISaborDao dao = DAOFactory.getSaborDao();
    private CadastrarSaborView view;

    public SaborController(CadastrarSaborView view) {
        this.view = view;
    }

    public SaborController() {
    }

    public List<SaborPizza> carregarSabores() {
        return dao.listar();
    }

    public void adicionarOuEditar(SaborPizza sabor, String nomeAtual, boolean ehEdicao, int selectedRow) {
        if (sabor.getNome().isEmpty()) {
            JOptionPane.showMessageDialog(null, "O campo de sabor não pode estar vazio!");
            return;
        }

        if(ehEdicao) {
            if(selectedRow == -1){
                view.exibirMensagemErro("Selecione um sabor para alterar!");
                return;
            }

            int confirm = view.exibirYesNoMessage(
                    "Confirmar Edição",
                    "Deseja editar a pizza de sabor: " + sabor.getNome() + "?"
            );

            if (confirm != JOptionPane.YES_OPTION)
                return;

            atualizarSabor(sabor, nomeAtual);
        } else {
            SaborPizza saborExistente = dao.buscarPorNome(sabor.getNome());
            if(saborExistente != null) {
                view.exibirMensagemErro("Um sabor com o nome" + saborExistente.getNome() + " já existe!");
                return;
            }
            adicionarSabor(sabor);
        }
    }

    public void adicionarSabor(SaborPizza novoSabor) {
        dao.salvar(novoSabor);
        view.renderizarItensNaTabela();
        view.exibirMensagemSucesso("Sucesso!", "Sabor salvo com sucesso!");
    }

    public void atualizarSabor(SaborPizza novoSabor, String nomeAtual) {
       SaborPizza sabor = dao.buscarPorNome(nomeAtual);
       sabor.setNome(novoSabor.getNome());
       sabor.setTipoSabor(novoSabor.getTipoSabor());
       dao.atualizar(sabor);
       view.renderizarItensNaTabela();
       view.exibirMensagemSucesso("Sucesso!", "Sabor salvo com sucesso!");

    }

    public void deletarSabor(String nome) {
        dao.removerPorNome(nome);
        view.renderizarItensNaTabela();
        view.exibirMensagemSucesso("Sucesso!", "Sabor removido com sucesso!");
    }

    public void atualizarPrecoSabores(String tipo, Double preco) {
        dao.atualizarPrecoSabores(tipo, preco);
    }
}
