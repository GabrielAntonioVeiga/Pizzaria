package dao.pizza;

import model.Pizza;

import java.util.List;

public interface IPizzaDao {
    public void salvar(Pizza pizza);
    public void deletar(Long id);
    public void atualizar(Pizza pizza);

    Pizza listarPorId(long id);

    public List<Pizza> listarPorPedido(Long idPedido);
    
    public void removerDoPedido(Long idPedido, Long idPizza);
}
