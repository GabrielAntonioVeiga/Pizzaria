package dao.cliente;

import factory.ConnectionFactory;
import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao implements IClienteDao {

    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, sobrenome, telefone) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remover(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, sobrenome = ?, telefone = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setLong(4, cliente.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente(rs.getLong("id"), rs.getString("nome"), rs.getString("sobrenome"), rs.getString("telefone"));
                lista.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Cliente listarPorId(Long id) {
        Cliente cliente = null;
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cliente = new Cliente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("sobrenome"),
                        rs.getString("telefone")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cliente;
    }

    @Override
    public Cliente listarPorTelefone(String telefone) {
        Cliente cliente = null;
        String sql = "SELECT * FROM cliente WHERE telefone = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, telefone);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cliente = new Cliente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("sobrenome"),
                        rs.getString("telefone")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cliente;
    }

}
