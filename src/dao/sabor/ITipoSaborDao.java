package dao.sabor;

import model.SaborPizza;
import model.TipoSabor;

import java.util.List;

public interface ITipoSaborDao {
    void atualizar (TipoSabor tipoSabor);
    TipoSabor buscarPorTipo(String tipo);
    List<TipoSabor> listar();
}
