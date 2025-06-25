package dao.sabor;

import factory.ConnectionFactory;
import model.SaborPizza;
import model.TipoSabor; // Import necessário
import enums.EnTipoSabor; // Import necessário

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaborDao implements ISaborDao {

    private final Connection con;

    public SaborDao() {
        try {
            this.con = ConnectionFactory.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados.", e);
        }
    }

    @Override
    public SaborPizza salvar(SaborPizza sabor) {
        String sql = "INSERT INTO sabor (nome, tipo, preco_cm2) VALUES(?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, sabor.getNome());
            stmt.setString(2, sabor.getTipoSabor().getNome().toString());
            stmt.setDouble(3, sabor.getTipoSabor().getPrecoCm2());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    sabor.setId(rs.getLong(1));
                }
            }
            return sabor;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar sabor.", e);
        }
    }

    @Override
    public void removerPorNome(String nome) {
        String sql = "DELETE FROM sabor WHERE nome = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover sabor.", e);
        }
    }

    @Override
    public void atualizar(SaborPizza sabor) {
        String sql = "UPDATE sabor SET nome = ?, tipo = ?, preco_cm2 = ? WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sabor.getNome());
            stmt.setString(2, sabor.getTipoSabor().getNome().toString());
            stmt.setDouble(3, sabor.getTipoSabor().getPrecoCm2());
            stmt.setLong(4, sabor.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar sabor.", e);
        }
    }

    @Override
    public SaborPizza buscarPorNome(String nome) {
        String sql = "SELECT id, nome, tipo, preco_cm2 FROM sabor WHERE nome = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SaborPizza(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("tipo"),
                            rs.getDouble("preco_cm2")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar sabor por nome.", e);
        }
        return null;
    }

    @Override
    public List<SaborPizza> listar() {
        String sql = "SELECT id, nome, tipo, preco_cm2 FROM sabor ORDER BY nome";
        List<SaborPizza> sabores = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SaborPizza sabor = new SaborPizza(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getDouble("preco_cm2")
                );
                sabores.add(sabor);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar sabores.", e);
        }
        return sabores;
    }

    @Override
    public void atualizarPrecoSabores(String tipo, Double preco) {
        String sql = "UPDATE sabor SET preco_cm2 = ? WHERE tipo = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDouble(1, preco);
            stmt.setString(2, tipo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar preço dos sabores.", e);
        }
    }
}