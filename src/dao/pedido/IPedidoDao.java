package dao.pedido;

import enums.EnStatusPedido;
import model.Cliente;
import model.Pedido;

import java.util.List;

public interface IPedidoDao {
    void salvar (Pedido pedido);
    void remover (Long id);
    void atualizar (Pedido pedido);
    List<Pedido> listar();
    List<Pedido> listarPorCliente(Cliente cliente);
    Pedido listarPorId(Long id);
    void alterarPrecoPedido(Long id, Double preco);
    void alterarStatusPedido(Long id, EnStatusPedido status);
}
