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

    private final Connection conn;

    public ClienteDao() {
        try {
            // A conexão é criada uma vez e reutilizada.
            this.conn = ConnectionFactory.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados.", e);
        }
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, sobrenome, telefone) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getLong(1));
                }
            }
            // Retorna o objeto cliente com o ID preenchido.
            return cliente;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar cliente.", e);
        }
    }

    @Override
    public void remover(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover cliente.", e);
        }
    }

    @Override
    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, sobrenome = ?, telefone = ? WHERE id = ?";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setLong(4, cliente.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar cliente.", e);
        }
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id, nome, sobrenome, telefone FROM cliente ORDER BY nome";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente(rs.getLong("id"), rs.getString("nome"), rs.getString("sobrenome"), rs.getString("telefone"));
                lista.add(c);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar clientes.", e);
        }
        return lista;
    }

    @Override
    public Cliente listarPorId(Long id) {
        Cliente cliente = null;
        String sql = "SELECT id, nome, sobrenome, telefone FROM cliente WHERE id = ?";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente(rs.getLong("id"), rs.getString("nome"), rs.getString("sobrenome"), rs.getString("telefone"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar cliente por ID.", e);
        }
        return cliente;
    }

    @Override
    public Cliente listarPorTelefone(String telefone) {
        Cliente cliente = null;
        String sql = "SELECT id, nome, sobrenome, telefone FROM cliente WHERE telefone = ?";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setString(1, telefone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente(rs.getLong("id"), rs.getString("nome"), rs.getString("sobrenome"), rs.getString("telefone"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar cliente por telefone.", e);
        }
        return cliente;
    }

    @Override
    public Cliente listarPorPedido(Long idPedido) {
        Cliente cliente = null;
        String sql = "SELECT c.id, c.nome, c.sobrenome, c.telefone FROM cliente c JOIN pedido p ON p.id_cliente = c.id WHERE p.id = ?";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setLong(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente(rs.getLong("id"), rs.getString("nome"), rs.getString("sobrenome"), rs.getString("telefone"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar cliente por pedido.", e);
        }
        return cliente;
    }
}