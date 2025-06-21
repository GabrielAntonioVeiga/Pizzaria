package dao.sabor;

import factory.ConnectionFactory;
import model.SaborPizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaborDao implements ISaborDao{

    private final Connection con;

    public SaborDao(){
        try {
            this.con = ConnectionFactory.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void salvar(SaborPizza sabor) {
        String sql = "INSERT INTO sabor (nome, tipo, preco_cm2) VALUES(?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, sabor.getNome());
            stmt.setString(2, sabor.getTipoSabor().getNome().toString());
            stmt.setDouble(3, sabor.getTipoSabor().getPrecoCm2());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void removerPorNome(String nome) {
        String sql = "DELETE FROM sabor WHERE nome = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(SaborPizza sabor) {
        String sql = "UPDATE sabor SET nome = ?, tipo = ?, preco_cm2 = ? WHERE id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, sabor.getNome());
            stmt.setString(2, sabor.getTipoSabor().getNome().toString());
            stmt.setDouble(3, sabor.getTipoSabor().getPrecoCm2());
            stmt.setLong(4, sabor.getId());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhum sabor foi atualizado. ID não encontrado: " + sabor.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SaborPizza buscarPorNome(String nome) {
        String sql = "SELECT * FROM sabor WHERE nome = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new SaborPizza(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getDouble("preco_cm2")
                );
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar sabor por Nome", e);
        }

        return null;
    }


    @Override
    public List<SaborPizza> listar() {
        String sql = "SELECT * FROM sabor";
        List<SaborPizza> sabores = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SaborPizza sabor = new SaborPizza(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getDouble("preco_cm2")
                );

                sabores.add(sabor);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar sabores", e);
        }

        return sabores;
    }

}
