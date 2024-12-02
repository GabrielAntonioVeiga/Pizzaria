package view;

import controller.SaborController;
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
    private JButton voltaMenuButton;

    private SaborController saborController = new SaborController();
    private List<SaborPizza> sabores;

    public CadastrarSaborView() {
        setContentPane(CadastraPizza);
        setTitle("Pedidos");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Tipo", "Sabor"}
        );
        PizzasCadastradas.setModel(tableModel);

        this.sabores = this.saborController.carregarSabores();

        TipoPizzaBox.setModel(new DefaultComboBoxModel<>(NomeTipoSabor.values()));


        ConfirmarButton.addActionListener(e -> {
            String tipo = TipoPizzaBox.getSelectedItem().toString();
            String sabor = saborPizza.getText();

            if (sabor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "O campo de sabor não pode estar vazio!");
                return;
            }


            tableModel.addRow(new Object[]{tipo, sabor});

            TipoSabor tipoSabor = this.saborController.carregarTipoSaborPeloNome((NomeTipoSabor)TipoPizzaBox.getSelectedItem());

            SaborPizza novoSabor = new SaborPizza(sabor, tipoSabor);
            sabores.add(novoSabor);


            saborPizza.setText("");
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


            saborPizza.setText(saborAtual);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja editar a pizza de sabor: " + saborAtual + "?",
                    "Confirmar Edição", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String novoSabor = saborPizza.getText().trim();


                if (novoSabor.isEmpty() ) {
                    JOptionPane.showMessageDialog(this,
                            "Os campo de sabor não pode estar vazio",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                NomeTipoSabor tipoSelecionado = (NomeTipoSabor) TipoPizzaBox.getSelectedItem();
                if (tipoSelecionado == null || !TipoSaborValido(tipoSelecionado)) {
                        JOptionPane.showMessageDialog(this,
                    "O tipo escolhido é inválido: Digite SIMPLES, ESPECIAL OU PREMIUM",
                    "Erro", JOptionPane.ERROR_MESSAGE);
                    
                    return;
                }


                DefaultTableModel model1 = (DefaultTableModel) PizzasCadastradas.getModel();
                model1.setValueAt(novoSabor, selectedRow, 1);


                sabores.get(selectedRow).setNome(novoSabor);

                JOptionPane.showMessageDialog(this,
                        "Pizza editada com sucesso!",
                        "Edição", JOptionPane.PLAIN_MESSAGE);


                saborPizza.setText("");
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

}