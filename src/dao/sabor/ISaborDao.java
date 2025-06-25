package dao.sabor;

import model.SaborPizza;
import java.util.List;

public interface ISaborDao {
    SaborPizza salvar(SaborPizza sabor);

    void removerPorNome(String nome);
    void atualizar(SaborPizza sabor);
    SaborPizza buscarPorNome(String nome);
    List<SaborPizza> listar();
    void atualizarPrecoSabores(String tipo, Double preco);
    int contarPizzasComSabor(Long saborId);
}