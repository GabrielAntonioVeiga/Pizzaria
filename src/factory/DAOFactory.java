package factory;

import dao.cliente.ClienteDao;
import dao.cliente.IClienteDao;
import dao.pedido.IPedidoDao;
import dao.pedido.PedidoDao;
import dao.pizza.IPizzaDao;
import dao.pizza.PizzaDao;
import dao.sabor.ISaborDao;
import dao.sabor.ITipoSaborDao;
import dao.sabor.SaborDao;
import dao.sabor.TipoSaborDao;
import model.Pedido;

public class DAOFactory {

    public static IClienteDao getClienteDao() {
        return new ClienteDao();
    }

    public static IPedidoDao getPedidoDao() {return new PedidoDao();}

    public static ISaborDao getSaborDao(){
        return new SaborDao();
    }

    public static ITipoSaborDao getTipoSaborDao(){
        return new TipoSaborDao();
    }

    public static IPizzaDao getPizzaDao(){ return new PizzaDao(); }
}
