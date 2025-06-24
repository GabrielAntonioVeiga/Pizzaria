package controller;

import dao.sabor.ISaborDao;
import factory.DAOFactory;
import model.SaborPizza;

import javax.swing.*;
import java.util.List;

public class SaborController {
    private final ISaborDao dao = DAOFactory.getSaborDao();

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
            SaborPizza saborExistente = dao.buscarPorNome(sabor.getNome());
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
        dao.salvar(novoSabor);
        JOptionPane.showMessageDialog(null,
                "Sabor salvo com sucesso!",
                "Sucesso!", JOptionPane.PLAIN_MESSAGE);
    }

    public void atualizarSabor(SaborPizza novoSabor, String nomeAtual) {
       SaborPizza sabor = dao.buscarPorNome(nomeAtual);
       sabor.setNome(novoSabor.getNome());
       sabor.setTipoSabor(novoSabor.getTipoSabor());
       dao.atualizar(sabor);
    JOptionPane.showMessageDialog(null,
            "Sabor atualizado com sucesso!",
            "Sucesso!", JOptionPane.PLAIN_MESSAGE);
    }

    public void deletarSabor(String nome) {
        dao.removerPorNome(nome);
    }

    public void atualizarPrecoSabores(String tipo, Double preco) {
        dao.atualizarPrecoSabores(tipo, preco);
    }
}
