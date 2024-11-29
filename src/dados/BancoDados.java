package dados;

import model.Cliente;

import java.util.ArrayList;

public class BancoDados {
    public ArrayList<Cliente> clientes;

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }
}
