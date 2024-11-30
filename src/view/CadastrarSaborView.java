package view;

import dados.BancoDados;
import model.SaborPizza;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CadastrarSaborView extends JFrame {
    private JPanel CadastraPizza;
    private JPanel PanelTitle;
    private JLabel CadastrarPizza;
    private JPanel PanelEdicao;
    private JButton deletarButton;
    private JButton editarButton;
    private JButton carregarButton;
    private JPanel LabelPizzas;
    private JTable PizzasCadastradas;
    private JPanel LabelCadastraPizza;
    private JLabel BntTipoPizza;
    private JComboBox<String> TipoPizzaBox;
    private JLabel BntSaborPizza;
    private JTextField saborPizza;
    private JButton ConfirmarButton;
    private JLabel TipoPizzaTable;
    private JLabel SaborPizzaTable;
    private List<SaborPizza> sabores = new BancoDados().getSabores();

    public CadastrarSaborView() {

        setContentPane(CadastraPizza);
        setTitle("Pedidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        TipoPizzaBox.addItem("Simples");
        TipoPizzaBox.addItem("Especial");
        TipoPizzaBox.addItem("Premium");

        // Configuração inicial da tabela
        DefaultTableModel model = new DefaultTableModel(new String[]{"Tipo", "Sabor"}, 0);
        PizzasCadastradas.setModel(model);

        // Adicionar cabeçalhos à tabela
        model.addRow(new Object[]{"Tipo da pizza", "Sabor da pizza"});

        // Botão Confirmar: Adicionar pizza
        ConfirmarButton.addActionListener(e -> {
            String tipo = (String) TipoPizzaBox.getSelectedItem();
            String sabor = saborPizza.getText();

            if (sabor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "O campo de sabor não pode estar vazio!");
                return;
            }

            model.addRow(new Object[]{tipo, sabor});
            int saborNum = -1;
            switch (tipo) {
                case "Simples":
                    saborNum = 0;
                    break;
                case "Especial":
                    saborNum = 1;
                    break;
                case "Premium":
                    saborNum = 2;
                    break;
            }

            SaborPizza novoSabor = new SaborPizza(sabor, saborNum);
            sabores.add(novoSabor);
            saborPizza.setText(""); // Limpar o campo de sabor
        });

        // Botão Deletar: Remover pizza
        deletarButton.addActionListener(e -> {
            int selectedRow = PizzasCadastradas.getSelectedRow();

            if (selectedRow <= 0) { // Evita excluir a linha inicial
                JOptionPane.showMessageDialog(null, "Selecione uma pizza válida para deletar!");
                return;
            }

            model.removeRow(selectedRow); // Remove a pizza da tabela
            JOptionPane.showMessageDialog(null, "Pizza deletada com sucesso!");
        });

        // Botão Editar: Atualizar sabor da pizza
        editarButton.addActionListener(e -> {

            int selectedRow = PizzasCadastradas.getSelectedRow();

            if (selectedRow <= 0) { // Evita edição da linha inicial
                JOptionPane.showMessageDialog(null, "Selecione uma pizza válida para editar!");
                return;
            }

            String novoSabor = saborPizza.getText().trim(); // Verifica o valor do campo
            if (novoSabor.isEmpty()) { // Garante que não está vazio
                JOptionPane.showMessageDialog(null, "O campo de sabor não pode estar vazio!");
                return;
            }

            // Atualiza o sabor na tabela
            model.setValueAt(novoSabor, selectedRow, 1);
            JOptionPane.showMessageDialog(null, "Pizza editada com sucesso!");
            saborPizza.setText("");
        });

        carregarButton.addActionListener(e -> {
            int selectedRow = PizzasCadastradas.getSelectedRow();

            if (selectedRow <= 0) { // Evita seleção da linha inicial
                JOptionPane.showMessageDialog(null, "Selecione uma pizza válida para carregar!");
                return;
            }

            String novoTipo = saborPizza.getText().trim(); // Lê o valor do campo
            String tipoLower = novoTipo.toLowerCase(); // Normaliza para comparação

            // Verificar se o tipo é válido
            if (tipoLower.equals("simples") || tipoLower.equals("especial") || tipoLower.equals("premium")) {
                // Capitaliza o tipo da pizza
                novoTipo = novoTipo.substring(0, 1).toUpperCase() + novoTipo.substring(1).toLowerCase();

                // Atualiza o tipo na tabela
                model.setValueAt(novoTipo, selectedRow, 0);
                JOptionPane.showMessageDialog(null, "Tipo da pizza atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Tipo inválido! Deve ser Simples, Especial ou Premium.");
            }
        });

        pack();
        setVisible(true);
    }
}