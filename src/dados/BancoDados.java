package dados;

import enums.EnTipoSabor;
import model.Cliente;
import model.Pedido;
import model.SaborPizza;
import model.TipoSabor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BancoDados {

    private static BancoDados instancia;

    public static List<Cliente> clientes = new ArrayList<>();

    public List<TipoSabor> tiposSabores = new ArrayList<>(
            Arrays.asList(
                    new TipoSabor(EnTipoSabor.SIMPLES, 0.19),
                    new TipoSabor(EnTipoSabor.ESPECIAL, 0.25),
                    new TipoSabor(EnTipoSabor.PREMIUM, 0.30)
            )
    );

    public List<SaborPizza> sabores = new ArrayList<>(
            Arrays.asList(
                    new SaborPizza("Pepperoni", tiposSabores.get(0)),
                    new SaborPizza("Calabresa", tiposSabores.get(1)),
                    new SaborPizza("Portuguesa", tiposSabores.get(2))
                    )
    );
    public List<Pedido> pedidos = new ArrayList<>();


    private BancoDados() {
    }

    public static BancoDados getInstancia() {
        if (instancia == null) {
            instancia = new BancoDados();
        }
        return instancia;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<TipoSabor> getTiposSabores() {
        return tiposSabores;
    }

    public List<SaborPizza> getSabores() { return sabores; }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setClientes(List<Cliente> novosClientes) {
        clientes = novosClientes;
    }

}
