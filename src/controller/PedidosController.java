package controller;

import dao.pedido.IPedidoDao;
import dao.pizza.IPizzaDao;
import enums.EnStatusPedido;
import factory.DAOFactory;
import model.Cliente;
import model.Pedido;
import model.Pizza;
import view.ItensPedidoFormView;
import view.PedidosView;

import java.util.List;

public class PedidosController {
    private IPedidoDao pedidoDao = DAOFactory.getPedidoDao();
    private IPizzaDao pizzaDao = DAOFactory.getPizzaDao();
    private ClienteController clienteController = new ClienteController();
    private PedidosView view;

    public PedidosController(PedidosView view) {
        this.view = view;
    }

    public PedidosController() {}


    public List<Pedido> carregarPedidos() {
        return pedidoDao.listar();
    }

    public List<Pedido> carregarPedidosPorCliente(Cliente cliente) {
        return pedidoDao.listarPorCliente(cliente);
    }


    public List<Pizza> carregarItensPedido(Long idPedido) {
        return pizzaDao.listarPorPedido(idPedido);
    }

    public void alterarStatusPedido(Long idPedido, EnStatusPedido novoStatus) {
        Pedido pedido = retornarPedidoPeloId(idPedido);

        view.setStatusPedidoField(pedido.getStatus().toString());

        pedidoDao.alterarStatusPedido(idPedido, novoStatus);
        pedido.setStatus(novoStatus);
        view.renderizarItensNaTabela(carregarPedidos());
    }

    public void alterarPrecoPedido(Long idPedido, double preco) {
        pedidoDao.alterarPrecoPedido(idPedido, preco);
    }

    public Pedido retornarPedidoPeloId(Long idPedido) {
        Pedido pedido = pedidoDao.listarPorId(idPedido);
        pedido.setItens(pizzaDao.listarPorPedido(idPedido));
        return pedido;
    }

    public void adicionarPedido(String telefone) {
        Cliente cliente = clienteController.buscarClientePorTelefone(telefone);
        if (cliente == null) {
            view.exibirMensagemErro("É necessário buscar um cliente para adicionar um pedido!");
            view.limparTabela();
            return;
        }
        Long idPedido = clienteController.criarPedidoCliente(cliente.getId());
        new ItensPedidoFormView(idPedido);
    }

    public void deletarPedido(Long idPedido) {
        pedidoDao.remover(idPedido);
        view.exibirMensagemSucesso("Sucesso", "Pedido removido com sucesso");
        view.renderizarItensNaTabela(carregarPedidos());
    }

    public void buscarPedidosClientePorNumero(String numero) {
        Cliente clienteEncontrado = clienteController.buscarClientePorTelefone(numero);

        if(clienteEncontrado == null) {
            view.exibirMensagemErro("Cliente com o número digitado não encontrado!");
            return;
        }

        List<Pedido> pedidos = carregarPedidosPorCliente(clienteEncontrado);
        view.renderizarItensNaTabela(pedidos);

        view.exibirMensagemSucesso("Busca","Cliente encontrado!");
        view.habilitarAlterarStatusPedido();

        if(pedidos.isEmpty()) {
            view.exibirMensagemInfo("Cliente sem pedidos.","O cliente encontrado não possui pedidos, adicione um pedido!");
        }

    }

}
