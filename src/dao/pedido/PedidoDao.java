package dao.pedido;

import enums.EnStatusPedido;
import factory.ConnectionFactory;
import model.Cliente;
import model.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.List;

public class PedidoDao implements IPedidoDao {

    private final Connection conn;

    public PedidoDao() {
        try {
            this.conn = ConnectionFactory.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_cliente, status, preco_total) VALUES (?, ?, ?)";
        try {
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, pedido.getCliente().getId());
            stmt.setString(2, pedido.getStatus().name());
            stmt.setDouble(3, 0.0); 

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Falha ao inserir pedido, nenhuma linha afetada.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setId(rs.getLong(1)); 
                } else {
                    conn.rollback();
                    throw new SQLException("Falha ao obter o ID do pedido gerado.");
                }
            }

            conn.commit();
            return pedido;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
         
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
       
            }
        }
    }

    @Override
    public void alterarPrecoPedido(Long id, Double preco) {
        String sql = "UPDATE pedido SET preco_total = ? WHERE id = ?";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) { 
            stmt.setDouble(1, preco);
            stmt.setLong(2, id); 
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remover(Long id) {
        String sql = "DELETE FROM pedido WHERE id = ?";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET id_cliente = ?, status = ?, preco_total = ? WHERE id = ?";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setLong(1, pedido.getCliente().getId());
            stmt.setString(2, pedido.getStatus().name());
            stmt.setDouble(3, pedido.getPrecoTotal());
            stmt.setLong(4, pedido.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Pedido> listar() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.id AS pedido_id, p.status, p.preco_total, " +
                "c.id AS cliente_id, c.nome, c.sobrenome, c.telefone " +
                "FROM pedido p JOIN cliente c ON p.id_cliente = c.id";

        try (PreparedStatement stmt = this.conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(rs.getLong("cliente_id"), rs.getString("nome"), rs.getString("sobrenome"), rs.getString("telefone"));
                Pedido pedido = new Pedido(
                        rs.getLong("pedido_id"), cliente, 
                        EnStatusPedido.valueOf(rs.getString("status")),
                        rs.getDouble("preco_total"));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pedidos;
    }

    @Override
    public List<Pedido> listarPorCliente(Cliente cliente) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.id AS pedido_id, p.status, p.preco_total, " +
                "c.id AS cliente_id, c.nome, c.sobrenome, c.telefone " +
                "FROM pedido p JOIN cliente c ON p.id_cliente = c.id WHERE p.id_cliente = ?";

        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setLong(1, cliente.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getLong("pedido_id"),
                        cliente,
                        EnStatusPedido.valueOf(rs.getString("status")), 
                        rs.getDouble("preco_total"));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pedidos;
    }

    @Override
    public Pedido listarPorId(Long id) {
        Pedido pedido = null;
        String sql = "SELECT p.id AS pedido_id, p.status, p.preco_total, " +
                "c.id AS cliente_id, c.nome, c.sobrenome, c.telefone " +
                "FROM pedido p JOIN cliente c ON p.id_cliente = c.id WHERE p.id = ?";

        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { 
                Cliente cliente = new Cliente(rs.getLong("cliente_id"), rs.getString("nome"), rs.getString("sobrenome"), rs.getString("telefone"));
                pedido = new Pedido(
                        rs.getLong("pedido_id"), 
                        cliente, 
                        EnStatusPedido.valueOf(rs.getString("status")),
                        rs.getDouble("preco_total"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pedido;
    }

    @Override
    public void alterarStatusPedido(Long id, EnStatusPedido status) {
        String sql = "UPDATE pedido SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}