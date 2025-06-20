package dao.pedido;

import model.Cliente;

import java.util.List;

public interface IPedidoDao {
    void salvar (Cliente cliente);
    void remover (Long id);
    void atualizar (Cliente cliente);
    List<Cliente> listar();
}
