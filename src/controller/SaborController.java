package controller;

import dao.sabor.ISaborDao;
import dao.sabor.ITipoSaborDao;
import factory.DAOFactory;
import model.SaborPizza;
import model.TipoSabor;
import view.CadastrarSaborView;

import javax.swing.*;

public class SaborController {

    private CadastrarSaborView view;
    private ISaborDao saborDao = DAOFactory.getSaborDao();
    private ITipoSaborDao tipoSaborDao = DAOFactory.getTipoSaborDao(); 

    public SaborController() {}

    public void iniciar() {
        this.view = new CadastrarSaborView();
        this.view.setController(this);

        this.view.renderizarItensNaTabela(saborDao.listar());
        this.view.popularTiposDeSabor(tipoSaborDao.listar());

        this.view.setVisible(true);
    }


    public void adicionarSabor() {
        String nome = view.getNomeSabor();
        TipoSabor tipoSabor = view.getTipoSaborSelecionado();

        if (nome.isEmpty() || tipoSabor == null) {
            view.exibirMensagem("Por favor, preencha o nome e selecione um tipo.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (saborDao.buscarPorNome(nome) != null) {
            view.exibirMensagem("Um sabor com o nome '" + nome + "' já existe!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SaborPizza novoSabor = new SaborPizza(nome, tipoSabor);
        saborDao.salvar(novoSabor);

        view.exibirMensagem("Sabor salvo com sucesso!", JOptionPane.INFORMATION_MESSAGE);
        view.limparCampos();
        view.renderizarItensNaTabela(saborDao.listar());
    }

    public void carregarSaborParaEdicao() {
        SaborPizza saborSelecionado = view.getSaborSelecionadoDaTabela();
        if (saborSelecionado == null) {
            view.limparCampos();
            return;
        }
        view.preencherCampos(saborSelecionado);
    }

    public void salvarEdicaoSabor() {
        SaborPizza saborOriginal = view.getSaborSelecionadoDaTabela();
        if (saborOriginal == null) {
            view.exibirMensagem("Selecione um sabor na tabela para editar.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String novoNome = view.getNomeSabor();
        TipoSabor novoTipo = view.getTipoSaborSelecionado();

        if (novoNome.isEmpty() || novoTipo == null) {
            view.exibirMensagem("Todos os campos devem ser preenchidos.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SaborPizza saborComMesmoNome = saborDao.buscarPorNome(novoNome);
        if (saborComMesmoNome != null && !saborComMesmoNome.getId().equals(saborOriginal.getId())) {
            view.exibirMensagem("Já existe outro sabor com este nome.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        saborOriginal.setNome(novoNome);
        saborOriginal.setTipoSabor(novoTipo);
        saborDao.atualizar(saborOriginal);

        view.exibirMensagem("Sabor atualizado com sucesso!", JOptionPane.INFORMATION_MESSAGE);
        view.limparCampos();
        view.renderizarItensNaTabela(saborDao.listar());
    }

    public void deletarSabor() {
        SaborPizza saborSelecionado = view.getSaborSelecionadoDaTabela();
        if (saborSelecionado == null) {
            view.exibirMensagem("Selecione um sabor para deletar.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int pizzasUsando = saborDao.contarPizzasComSabor(saborSelecionado.getId());

        if (pizzasUsando > 0) {
            String mensagem = "Não é possível excluir o sabor '" + saborSelecionado.getNome() +
                    "', pois ele está sendo usado em " + pizzasUsando + " pizza(s) existente(s).";
            view.exibirMensagem(mensagem, JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Tem certeza que deseja excluir o sabor '" + saborSelecionado.getNome() + "'?", "Excluir Sabor", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            saborDao.removerPorNome(saborSelecionado.getNome());
            view.exibirMensagem("Sabor removido com sucesso!", JOptionPane.INFORMATION_MESSAGE);
            view.limparCampos();
            view.renderizarItensNaTabela(saborDao.listar());
        }
    }

    public void voltarParaMenu() {
        view.dispose();
        new MenuController().iniciar();
    }
}