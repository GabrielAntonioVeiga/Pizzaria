package dao.sabor; // Ou o pacote correto, ex: dao.tiposabor

import enums.EnTipoSabor;
import factory.ConnectionFactory;
import model.TipoSabor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoSaborDao implements ITipoSaborDao {
    private final Connection con;

    public TipoSaborDao() {
        try {
            this.con = ConnectionFactory.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados.", e);
        }
    }

    @Override
    public void atualizar(TipoSabor tipoSabor) {
        String sql = "UPDATE tipo_sabor SET preco_cm2 = ? WHERE tipo = ?";
        // MUDANÇA: Usando try-with-resources
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDouble(1, tipoSabor.getPrecoCm2());
            stmt.setString(2, tipoSabor.getNome().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar tipo de sabor.", e);
        }
    }

    @Override
    public TipoSabor buscarPorTipo(String tipo) {
        String sql = "SELECT tipo, preco_cm2 FROM tipo_sabor WHERE tipo = ?";
        // MUDANÇA: Usando try-with-resources
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, tipo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TipoSabor(
                            EnTipoSabor.valueOf(rs.getString("tipo")),
                            rs.getDouble("preco_cm2")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar tipo de sabor.", e);
        }
        return null;
    }

    @Override
    public List<TipoSabor> listar() {
        String sql = "SELECT tipo, preco_cm2 FROM tipo_sabor";
        List<TipoSabor> tipos = new ArrayList<>();
        // MUDANÇA: Usando try-with-resources
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TipoSabor tipo = new TipoSabor(
                        EnTipoSabor.valueOf(rs.getString("tipo")),
                        rs.getDouble("preco_cm2")
                );
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tipos de sabores.", e);
        }
        return tipos;
    }
}