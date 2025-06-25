package dao.pizza;

import enums.EnForma;
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

            conn.commit(); 

        } catch (SQLException e) {
            try {
                conn.rollback(); 
            } catch (SQLException rollbackEx) {
                System.err.println("Erro ao reverter transação: " + rollbackEx.getMessage());
            }
            throw new RuntimeException("Erro ao salvar pizza: " + e.getMessage(), e);
        } finally {
            try {
                conn.setAutoCommit(true); 
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
        String sql = "UPDATE pizza SET forma = ?, raio = ?, lado = ? WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, pizza.getForma().getNomeForma());

            if(pizza.getForma() instanceof Circulo) {
                stmt.setDouble(2, pizza.getForma().getMedida());
                stmt.setNull(3, java.sql.Types.NUMERIC);
            } else {
                stmt.setNull(2, java.sql.Types.NUMERIC);
                stmt.setDouble(3, pizza.getForma().getMedida());
            }

            stmt.setLong(4, pizza.getId());
            stmt.executeUpdate();

            atualizarSaboresDaPizza(conn, pizza);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void atualizarSaboresDaPizza(Connection conn, Pizza pizza) throws SQLException {

        try (PreparedStatement stmtDelete = conn.prepareStatement(
                "DELETE FROM pizza_sabor WHERE id_pizza = ?")) {
            stmtDelete.setLong(1, pizza.getId());
            stmtDelete.executeUpdate();
        }

        try (PreparedStatement stmtInsert = conn.prepareStatement(
                "INSERT INTO pizza_sabor (id_pizza, id_sabor) VALUES (?, ?)")) {

            for (SaborPizza sabor : pizza.getSabores()) {
                stmtInsert.setLong(1, pizza.getId());
                stmtInsert.setLong(2, sabor.getId());
                stmtInsert.addBatch();
            }
            stmtInsert.executeBatch();
        }
    }

    @Override
    public Pizza listarPorId(Long id) {
        Pizza pizza = null;
        String sql = "SELECT " +
                "p.id AS pizza_id, p.id_pedido, p.forma, p.raio, p.lado, " +
                "s.id AS sabor_id, s.nome AS sabor_nome, s.tipo AS sabor_tipo, s.preco_cm2 AS sabor_preco_cm2 " +
                "FROM pizza p " +
                "LEFT JOIN pizza_sabor ps ON p.id = ps.id_pizza " +
                "LEFT JOIN sabor s ON ps.id_sabor = s.id " +
                "WHERE p.id = ?";

        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (pizza == null) {
                        String nomeForma = rs.getString("forma");
                        EnForma enForma = EnForma.findByNome(nomeForma);
                        Double raio = rs.getObject("raio") != null ? rs.getDouble("raio") : null;
                        Double lado = rs.getObject("lado") != null ? rs.getDouble("lado") : null;

                        Forma forma = null;
                        switch (enForma) {
                            case CIRCULO:
                                forma = new Circulo(raio != null ? raio : 0);
                                break;
                            case TRIANGULO:
                                forma = new Triangulo(lado != null ? lado : 0);
                                break;
                            case QUADRADO:
                                forma = new Quadrado(lado != null ? lado : 0);
                                break;
                        }

                        Long idPedido = rs.getLong("id_pedido");
                        pizza = new Pizza(id, forma, new ArrayList<>());
                        pizza.setId_pedido(idPedido);
                    }

                    Long saborId = rs.getObject("sabor_id") != null ? rs.getLong("sabor_id") : null;
                    if (saborId != null) {
                        SaborPizza saborPizza = new SaborPizza(
                                saborId,
                                rs.getString("sabor_nome"),
                                rs.getString("sabor_tipo"),
                                rs.getDouble("sabor_preco_cm2")
                        );
                        pizza.getSabores().add(saborPizza);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar pizza por ID: " + e.getMessage(), e);
        }

        if (pizza != null) {
            pizza.setPreco(pizza.calculaPreco());
        }

        return pizza;
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

    @Override
    public void removerDoPedido(Long idPedido, Long idPizza) {
        String sqlDeletePizzaSabor = "DELETE FROM pizza_sabor WHERE id_pizza = ?";
        String sqlDeletePizza = "DELETE FROM pizza WHERE id = ? AND id_pedido = ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtSabor = conn.prepareStatement(sqlDeletePizzaSabor)) {
                stmtSabor.setLong(1, idPizza);
                stmtSabor.executeUpdate();
            }

            try (PreparedStatement stmtPizza = conn.prepareStatement(sqlDeletePizza)) {
                stmtPizza.setLong(1, idPizza);
                stmtPizza.setLong(2, idPedido);

                int affectedRows = stmtPizza.executeUpdate();

                if (affectedRows == 0) {
                    conn.rollback();
                    throw new SQLException("Falha ao remover a pizza: Nenhuma pizza com id " + idPizza + " encontrada para o pedido " + idPedido);
                }
            }

            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Erro crítico ao tentar reverter a transação de remoção: " + rollbackEx.getMessage());
            }
            throw new RuntimeException("Erro ao remover item do pedido: " + e.getMessage(), e);

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erro ao restaurar o auto-commit: " + e.getMessage());
            }
        }
    }
}

