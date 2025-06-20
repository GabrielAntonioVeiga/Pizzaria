package dao.cliente;

import model.Cliente;

import java.util.List;

public interface IClienteDao {
    void salvar (Cliente cliente);
    void remover (Long id);
    void atualizar (Cliente cliente);
    List<Cliente> listar();
    Cliente listarPorId(Long id);
    Cliente listarPorTelefone(String telefone);
}
