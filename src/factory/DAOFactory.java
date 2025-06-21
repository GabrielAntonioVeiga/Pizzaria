package factory;

import dao.cliente.ClienteDao;
import dao.cliente.IClienteDao;
import dao.pedido.PedidoDao;
import dao.sabor.ISaborDao;
import dao.sabor.ITipoSaborDao;
import dao.sabor.SaborDao;
import dao.sabor.TipoSaborDao;
import model.Pedido;

public class DAOFactory {

    public static ClienteDao getClienteDao() {
        return new ClienteDao();
    }

    public static PedidoDao getPedidoDao() {
        return new PedidoDao();
    }

    public static ISaborDao getSaborDao(){
        return new SaborDao();
    }

    public static ITipoSaborDao getTipoSaborDao(){
        return new TipoSaborDao();
    }
}
