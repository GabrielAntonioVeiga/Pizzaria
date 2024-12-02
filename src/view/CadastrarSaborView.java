package view;

import dados.BancoDados;
import enums.NomeTipoSabor;
import model.SaborPizza;
import model.TipoSabor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JComboBox<NomeTipoSabor> TipoPizzaBox;
    private JLabel BntSaborPizza;
    private JTextField saborPizza;
    private JButton ConfirmarButton;
    private JLabel precoPizza;
    private JTextField precoCm2;  // Campo para preço por cm²
    private JButton voltaMenuButton;
    private List<SaborPizza> sabores = BancoDados.getInstancia().getSabores();

    public static void main(String[] args) {
        CadastrarSaborView cadastrarSaborView = new CadastrarSaborView();
    }

    public CadastrarSaborView() {
        setContentPane(CadastraPizza);
        setTitle("Pedidos");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Tipo", "Sabor", "Preço por cm²"}
        );
        PizzasCadastradas.setModel(tableModel);


        TipoPizzaBox.setModel(new DefaultComboBoxModel<>(NomeTipoSabor.values()));


        ConfirmarButton.addActionListener(e -> {
            String tipo = TipoPizzaBox.getSelectedItem().toString();
            String sabor = saborPizza.getText();
            String precoCm2Str = this.precoCm2.getText().trim();

            if (precoCm2Str.isEmpty()) {
                JOptionPane.showMessageDialog(null, "O campo de preço não pode estar vazio!");
                return;
            }

            if (!validaPreco(precoCm2Str)) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um número válido para o preço por cm²!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }


            double precoCm2Convertido = Double.parseDouble(precoCm2Str);

            if (sabor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "O campo de sabor não pode estar vazio!");
                return;
            }


            tableModel.addRow(new Object[]{tipo, sabor, precoCm2Convertido});

            TipoSabor tipoSabor = new TipoSabor((NomeTipoSabor)TipoPizzaBox.getSelectedItem(), precoCm2Convertido);
            SaborPizza novoSabor = new SaborPizza(sabor, tipoSabor);
            sabores.add(novoSabor);


            saborPizza.setText("");
            precoCm2.setText("");
        });


        deletarButton.addActionListener(e -> {
            int selectedRow = PizzasCadastradas.getSelectedRow();

            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(null, "Selecione uma pizza válida para deletar!");
                return;
            }

            tableModel.removeRow(selectedRow);
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
            String precoAtual = PizzasCadastradas.getValueAt(selectedRow, 2).toString();


            saborPizza.setText(saborAtual);
            precoCm2.setText(precoAtual);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja editar a pizza de sabor: " + saborAtual + "?",
                    "Confirmar Edição", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String novoSabor = saborPizza.getText().trim();
                String novoPreco = precoCm2.getText().trim();


                if (novoSabor.isEmpty() || novoPreco.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Os campos de sabor e preço não podem estar vazios!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                NomeTipoSabor tipoSelecionado = (NomeTipoSabor) TipoPizzaBox.getSelectedItem();
                if (tipoSelecionado == null || !TipoSaborValido(tipoSelecionado)) {
                        JOptionPane.showMessageDialog(this,
                    "O tipo digitado é inválido: Digite SIMPLES, ESPECIAL OU PREMIUM",
                    "Erro", JOptionPane.ERROR_MESSAGE);
                    
                    return;
                }


                DefaultTableModel model1 = (DefaultTableModel) PizzasCadastradas.getModel();
                model1.setValueAt(novoSabor, selectedRow, 1);
                model1.setValueAt(novoPreco, selectedRow, 2);


                double precoCm2Convertido = Double.parseDouble(this.precoCm2.getText());
                sabores.get(selectedRow).setNome(novoSabor);
                sabores.get(selectedRow).setTipoSabor((NomeTipoSabor)TipoPizzaBox.getSelectedItem(), precoCm2Convertido);

                JOptionPane.showMessageDialog(this,
                        "Pizza editada com sucesso!",
                        "Edição", JOptionPane.PLAIN_MESSAGE);


                saborPizza.setText("");
                precoCm2.setText("");
            }
        });


        voltaMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new MenuView();
            }
        });
    }

    private boolean TipoSaborValido(NomeTipoSabor tipoSabor) {
        String ValidaSabor = tipoSabor.toString().toUpperCase();
        for (NomeTipoSabor tipo : NomeTipoSabor.values()) {
            if (tipoSabor == tipo) {
                return true;
            }
        }
        return false;
    }

    private boolean validaPreco(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }}