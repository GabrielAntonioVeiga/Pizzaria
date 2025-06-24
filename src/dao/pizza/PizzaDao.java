package dao.pizza;

import enums.EnForma;
import enums.EnStatusPedido;
import factory.ConnectionFactory;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PizzaDao implements IPizzaDao {

    private final Connection conn;

    public PizzaDao(){
        try {
            this.conn = ConnectionFactory.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void salvar(Pizza pizza) {
        String sql = "INSERT INTO pizza (forma, raio, lado, id_pedido) VALUES (?, ?, ?, ?)";
        String sqlPizzaSabor = "INSERT INTO pizza_sabor (id_pizza, id_sabor) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, pizza.getForma().getNomeForma());

            if (pizza.getForma() instanceof Circulo) {
                Circulo circulo = (Circulo) pizza.getForma();
                stmt.setDouble(2, circulo.getRaio());
                stmt.setNull(3, java.sql.Types.NUMERIC);
            } else if (pizza.getForma() instanceof Triangulo) {
                Triangulo triangulo = (Triangulo) pizza.getForma();
                stmt.setNull(2, java.sql.Types.NUMERIC);
                stmt.setDouble(3, triangulo.getLado());
            } else if (pizza.getForma() instanceof Quadrado) {
                Quadrado quadrado = (Quadrado) pizza.getForma();
                stmt.setNull(2, java.sql.Types.NUMERIC);
                stmt.setDouble(3, quadrado.getLado());
            } else {
                throw new IllegalArgumentException("Tipo de forma desconhecido: " + pizza.getForma().getClass().getName());
            }

            stmt.setLong(4, pizza.getId_pedido());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Falha ao inserir pizza, nenhuma linha afetada.");
            }

            Long generatedPizzaId = null;
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                generatedPizzaId = rs.getLong(1);
                pizza.setId(generatedPizzaId);
            } else {
                conn.rollback();
                throw new SQLException("Falha ao obter o ID da pizza.");
            }

            if (Objects.nonNull(generatedPizzaId) && Objects.nonNull(pizza.getSabores()) && !pizza.getSabores().isEmpty()) {
                PreparedStatement stmtPizzaSabor = conn.prepareStatement(sqlPizzaSabor);
                for (SaborPizza sabor : pizza.getSabores()) {
                    if(sabor.getId() == null){
                        conn.rollback();
                        throw new SQLException("Sabor id is null");
                    }
                    stmtPizzaSabor.setLong(1, generatedPizzaId);
                    stmtPizzaSabor.setLong(2, sabor.getId());
                    stmtPizzaSabor.addBatch();
                }
                stmtPizzaSabor.executeBatch();
            }

            conn.commit(); // Commit transaction

        } catch (SQLException e) {
            try {
                conn.rollback(); // Rollback transaction on error
            } catch (SQLException rollbackEx) {
                System.err.println("Erro ao reverter transação: " + rollbackEx.getMessage());
            }
            throw new RuntimeException("Erro ao salvar pizza: " + e.getMessage(), e);
        } finally {
            try {
                conn.setAutoCommit(true); // Reset auto-commit
            } catch (SQLException autoCommitEx) {
                System.err.println("Erro ao restaurar auto-commit: " + autoCommitEx.getMessage());
            }
        }
    }

    @Override
    public void deletar(Long id) {
            String sql = "DELETE FROM pizza WHERE id = ?";
            try  {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void atualizar(Pizza pizza) {

    }

    @Override
    public Pizza listarPorId(int id) {
        return null;
    }

    @Override
    public List<Pizza> listarPorPedido(Long idPedido) {
        Map<Long, Pizza> pizzasMap = new HashMap<>();
        String sql = "SELECT " +
                "p.id AS pizza_id, p.id_pedido, p.forma, p.raio, p.lado, " +
                "s.id AS sabor_id, s.nome AS sabor_nome, s.tipo AS sabor_tipo, s.preco_cm2 AS sabor_preco_cm2 " +
                "FROM pizza p " +
                "JOIN pizza_sabor ps ON p.id = ps.id_pizza " +
                "JOIN sabor s ON ps.id_sabor = s.id " +
                "WHERE p.id_pedido = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setLong(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long pizzaId = rs.getLong("pizza_id");

                    Pizza pizza = pizzasMap.get(pizzaId);
                    if (pizza == null) {
                        String nomeForma = rs.getString("forma");
                        EnForma enForma = EnForma.findByNome(nomeForma);
                        Double raio = rs.getObject("raio") != null ? rs.getDouble("raio") : null;
                        Double lado = rs.getObject("lado") != null ? rs.getDouble("lado") : null;

                        Forma forma = null;
                        switch (enForma) {
                            case CIRCULO:
                                Circulo circulo = new Circulo();
                                if (raio != null) circulo.setDimensao(raio);
                                forma = circulo;
                                break;
                            case TRIANGULO:
                                Triangulo triangulo = new Triangulo();
                                if (lado != null) triangulo.setDimensao(lado);
                                forma = triangulo;
                                break;
                            case QUADRADO:
                                Quadrado quadrado = new Quadrado();
                                if (lado != null) quadrado.setDimensao(lado);
                                forma = quadrado;
                                break;
                            default:
                                throw new IllegalStateException("Forma desconhecida: " + nomeForma);
                        }
                        pizza = new Pizza(pizzaId, forma, new ArrayList<>());
                        pizzasMap.put(pizzaId, pizza);
                    }

                    Long saborId = rs.getLong("sabor_id");
                    String saborNome = rs.getString("sabor_nome");
                    String saborTipo = rs.getString("sabor_tipo");
                    Double saborPrecoCm2 = rs.getDouble("sabor_preco_cm2");

                    SaborPizza saborPizza = new SaborPizza(saborId, saborNome, saborTipo, saborPrecoCm2);

                    pizza.getSabores().add(saborPizza);
                    pizza.setPreco(pizza.calculaPreco());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ArrayList<>(pizzasMap.values());
    }
}

