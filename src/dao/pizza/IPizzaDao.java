package dao.pizza;

import model.Pizza;

import java.util.List;

public interface IPizzaDao {
    public void salvar(Pizza pizza);
    public void deletar(Long id);
    public void atualizar(Pizza pizza);
    public Pizza listarPorId(int id);
    public List<Pizza> listarPorPedido(Long idPedido);
}
