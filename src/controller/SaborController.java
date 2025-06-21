package controller;

import dados.BancoDados;
import dao.sabor.ISaborDao;
import dao.sabor.SaborDao;
import factory.DAOFactory;
import model.SaborPizza;

import javax.swing.*;
import java.util.List;

public class SaborController {
    private final ISaborDao saborDao = DAOFactory.getSaborDao();

    public SaborController() {
    }

    public List<SaborPizza> carregarSabores() {
        return saborDao.listar();
    }

    public void adicionarOuEditar(SaborPizza sabor, String nomeAtual, boolean ehEdicao, int selectedRow) {
        if (sabor.getNome().isEmpty()) {
            JOptionPane.showMessageDialog(null, "O campo de sabor não pode estar vazio!");
            return;
        }

        if(ehEdicao) {
            if(selectedRow == -1){
                JOptionPane.showMessageDialog(
                        null,
                        "Selecione um sabor para alterar!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Deseja editar a pizza de sabor: " + sabor.getNome() + "?",
                    "Confirmar Edição", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION)
                return;

            atualizarSabor(sabor, nomeAtual);
        } else {
            SaborPizza saborExistente = saborDao.buscarPorNome(sabor.getNome());
            if(saborExistente != null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Um sabor com o nome" + saborExistente.getNome() + " já existe!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            adicionarSabor(sabor);
        }
    }

    public void adicionarSabor(SaborPizza novoSabor) {
        saborDao.salvar(novoSabor);
        JOptionPane.showMessageDialog(null,
                "Sabor salvo com sucesso!",
                "Sucesso!", JOptionPane.PLAIN_MESSAGE);
    }

    public void atualizarSabor(SaborPizza novoSabor, String nomeAtual) {
       SaborPizza sabor = saborDao.buscarPorNome(nomeAtual);
       sabor.setNome(novoSabor.getNome());
       sabor.setTipoSabor(novoSabor.getTipoSabor());
       saborDao.atualizar(sabor);
    JOptionPane.showMessageDialog(null,
            "Sabor atualizado com sucesso!",
            "Sucesso!", JOptionPane.PLAIN_MESSAGE);
    }

    public void deletarSabor(String nome) {
        saborDao.removerPorNome(nome);
    }


}
