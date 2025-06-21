package dao.sabor;

import enums.EnTipoSabor;
import factory.ConnectionFactory;
import model.SaborPizza;
import model.TipoSabor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoSaborDao implements ITipoSaborDao{
    private final Connection con;

    public TipoSaborDao() {
        try {
            this.con = ConnectionFactory.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(TipoSabor tipoSabor) {
        String sql = "UPDATE tipo_sabor SET preco_cm2 = ? WHERE tipo = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDouble(1, tipoSabor.getPrecoCm2());
            stmt.setString(2, tipoSabor.getNome().toString());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhum tipo de sabor foi atualizado.");
            }

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TipoSabor buscarPorTipo(String tipo) {
        String sql = "SELECT * FROM tipo_sabor WHERE tipo = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, tipo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new TipoSabor(
                        EnTipoSabor.valueOf(rs.getString("tipo")),
                        rs.getDouble("preco_cm2")
                );
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao tipo.", e);
        }

        return null;
    }

    @Override
    public List<TipoSabor> listar() {
        String sql = "SELECT * FROM tipo_sabor";
        List<TipoSabor> tipos = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TipoSabor sabor = new TipoSabor(
                        EnTipoSabor.valueOf(rs.getString("tipo")),
                        rs.getDouble("preco_cm2")
                );

                tipos.add(sabor);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tipos de sabores", e);
        }

        return tipos;
    }
}
