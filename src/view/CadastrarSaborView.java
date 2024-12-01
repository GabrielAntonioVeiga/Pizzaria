package view;

import dados.BancoDados;
import model.SaborPizza;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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
    private JLabel precoPizza;
    private JTextField textField1;
    private List<SaborPizza> sabores = new BancoDados().getSabores();

    public static void main(String[] args) {
        CadastrarSaborView cadastrarSaborView = new CadastrarSaborView();
    }

    public CadastrarSaborView() {
        setContentPane(CadastraPizza);
        setTitle("Pedidos");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        TipoPizzaBox.addItem("Simples");
        TipoPizzaBox.addItem("Especial");
        TipoPizzaBox.addItem("Premium");


        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Tipo", "Sabor", "Preço"}
        );

        PizzasCadastradas.setModel(model);

        ConfirmarButton.addActionListener(e -> {
            String tipo = (String) TipoPizzaBox.getSelectedItem();
            String sabor = saborPizza.getText();
            String preco = precoPizza.getText();

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
            saborPizza.setText("");
        });


        deletarButton.addActionListener(e -> {
            int selectedRow = PizzasCadastradas.getSelectedRow();

            if (selectedRow <= 0) {
                JOptionPane.showMessageDialog(null, "Selecione uma pizza válida para deletar!");
                return;
            }

            model.removeRow(selectedRow);
            JOptionPane.showMessageDialog(null, "Pizza deletada com sucesso!");
        });


        editarButton.addActionListener(e -> {
            int selectedRow = PizzasCadastradas.getSelectedRow();


            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this,
                        "Nenhuma pizza selecionada para edição!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String tipoPizza = PizzasCadastradas.getValueAt(selectedRow, 0).toString();
            String saborAtual = PizzasCadastradas.getValueAt(selectedRow, 1).toString();

            saborPizza.setText(saborAtual);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja editar a pizza de sabor: " + saborAtual + "?",
                    "Confirmar Edição", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String novoSabor = saborPizza.getText().trim();

                if (novoSabor.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "O campo de sabor não pode estar vazio!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DefaultTableModel model1 = (DefaultTableModel) PizzasCadastradas.getModel();
                model1.setValueAt(novoSabor, selectedRow, 1);

                sabores.get(selectedRow).setNome(novoSabor);


                JOptionPane.showMessageDialog(this,
                        new StringBuilder("Pizza ").append(novoSabor).append(" editada com sucesso!"),
                        "Edição", JOptionPane.PLAIN_MESSAGE);


                saborPizza.setText("");
            }
        });

        //pack();
        //setVisible(true);

    }
}