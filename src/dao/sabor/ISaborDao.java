package dao.sabor;

import model.Cliente;
import model.SaborPizza;

import java.sql.SQLException;
import java.util.List;

public interface ISaborDao {
    void salvar (SaborPizza sabor);
    void removerPorNome (String nome);
    void atualizar (SaborPizza sabor);
    SaborPizza buscarPorNome(String nome);
    List<SaborPizza> listar();
    void atualizarPrecoSabores(String tipo, Double preco);
}
