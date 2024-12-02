package dados;

import enums.NomeTipoSabor;
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
    public List<SaborPizza> sabores = new ArrayList<>();
    public List<TipoSabor> tiposSabores = new ArrayList<>(
            Arrays.asList(
                    new TipoSabor(NomeTipoSabor.SIMPLES, 1),
                    new TipoSabor(NomeTipoSabor.ESPECIAL, 3),
                    new TipoSabor(NomeTipoSabor.PREMIUM, 5)
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

    public List<SaborPizza> getSabores() {
            SaborPizza sabor1 = new SaborPizza("Pepperoni", tiposSabores.get(0));
            SaborPizza sabor2 = new SaborPizza("Calabresa", tiposSabores.get(1));
            SaborPizza sabor3 = new SaborPizza("Portuguesa", tiposSabores.get(2));

            sabores.clear();
            sabores.add(sabor1);
            sabores.add(sabor2);
            sabores.add(sabor3);
            List<SaborPizza> saborPizzaList;

        return sabores;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setClientes(List<Cliente> novosClientes) {
        clientes = novosClientes;
    }

}
