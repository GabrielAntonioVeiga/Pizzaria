package controller;

import dao.pedido.IPedidoDao;
import dao.pizza.IPizzaDao;
import dao.sabor.ISaborDao;
import factory.DAOFactory;
import model.*;
import view.ItensPedidoFormView;

import javax.swing.*;
import java.util.List;

public class ItemPedidoController {

    private final ItensPedidoFormView view;
    private final Long idPedido;
    private final Long idItemSelecionado;
    private final boolean ehEdicao;

    private IPizzaDao pizzaDao = DAOFactory.getPizzaDao();
    private IPedidoDao pedidoDao = DAOFactory.getPedidoDao();
    private ISaborDao saborDao = DAOFactory.getSaborDao();

    public ItemPedidoController(Long idPedido, Long idItemSelecionado) {
        this.idPedido = idPedido;
        this.idItemSelecionado = idItemSelecionado;
        this.ehEdicao = (idItemSelecionado != null);

        this.view = new ItensPedidoFormView();
        this.view.setController(this);
    }

    public void iniciar() {
        String titulo = ehEdicao ? "Editar Item no Pedido" : "Adicionar Item no Pedido";
        view.setTitulo(titulo);

        List<SaborPizza> todosSabores = saborDao.listar();
        view.popularSabores(todosSabores.toArray(new SaborPizza[0]));
        view.popularFormas(new Forma[]{new Circulo(), new Quadrado(), new Triangulo()});

        if (ehEdicao) {
            Pizza itemExistente = pizzaDao.listarPorId(idItemSelecionado);
            view.preencherFormulario(itemExistente);
        }

        view.setVisible(true);
    }

    public void onFormChanged() {
        try {
            Forma forma = view.getFormaEscolhida(false);
            double dimensao = view.getDimensao();
            boolean ehArea = view.isArea();

            if (ehArea && dimensao > 0) {
                double ladoCalculado = forma.calcularDimensao(dimensao);
                String nomeMedida = (forma instanceof Circulo) ? "raio" : "lado";
                view.setDimensaoCalculada(String.format("A medida do %s correspondente é: %.2f cm", nomeMedida, ladoCalculado));
            } else {
                view.setDimensaoCalculada("");
            }

            List<SaborPizza> sabores = view.getSaboresSelecionados(false);
            Forma formaParaCalculo = view.getFormaEscolhida(false);
            Pizza pizzaRascunho = new Pizza(null, formaParaCalculo, sabores);
            view.setPrecoEstimado(String.format("R$ %.2f", pizzaRascunho.getPreco()));

        } catch (Exception e) {
            view.setPrecoEstimado("Dados incompletos");
        }
        view.atualizarDisponibilidadeSabores();
    }

    public void confirmar() {
        try {
            Forma forma = view.getFormaEscolhida(true);
            List<SaborPizza> sabores = view.getSaboresSelecionados(true);
            Pizza novaPizza = new Pizza(null, forma, sabores);

            if (ehEdicao) {
                novaPizza.setId(idItemSelecionado);
                pizzaDao.atualizar(novaPizza);
                view.exibirMensagem("Sucesso", "Item atualizado com sucesso!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                novaPizza.setId_pedido(idPedido);
                pizzaDao.salvar(novaPizza);
                view.exibirMensagem("Sucesso", "Item salvo com sucesso!", JOptionPane.INFORMATION_MESSAGE);
            }

            atualizarPrecoTotalPedido();

            view.dispose();
            new PedidoDetailController(idPedido).iniciar();

        } catch (Exception e) {
            view.exibirMensagem("Erro de Validação", e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarPrecoTotalPedido() {
        List<Pizza> itensAtuais = pizzaDao.listarPorPedido(idPedido);

        Pedido pedidoTemporario = new Pedido(null); 
        pedidoTemporario.setItens(itensAtuais);
        double novoPrecoTotal = pedidoTemporario.getPrecoTotal();

        pedidoDao.alterarPrecoPedido(idPedido, novoPrecoTotal);
    }

}