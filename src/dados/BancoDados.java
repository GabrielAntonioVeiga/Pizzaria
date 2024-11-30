package dados;

import model.Cliente;
import model.SaborPizza;

import java.util.ArrayList;
import java.util.List;

public class BancoDados {
    public static List<Cliente> clientes = new ArrayList<>();
    public static List<SaborPizza> sabores = new ArrayList<>();


    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<SaborPizza> getSabores() {
        return sabores;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
