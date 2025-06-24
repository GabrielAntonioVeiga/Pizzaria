package view;

import controller.SaborController;
import controller.TipoSaborController;
import enums.EnTipoSabor;
import factory.DAOFactory;
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
    private JComboBox<EnTipoSabor> TipoPizzaBox;
    private JLabel BntSaborPizza;
    private JTextField saborPizza;
    private JButton ConfirmarButton;
    private JButton voltaMenuButton;

    private DefaultTableModel tableModel;
    private SaborController saborController = new SaborController();
    private TipoSaborController tipoSaborController = new TipoSaborController(DAOFactory.getTipoSaborDao());

    public CadastrarSaborView() {
        setContentPane(CadastraPizza);
        setTitle("Pedidos");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        this.tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Tipo", "Sabor"}
        );
        PizzasCadastradas.setModel(tableModel);

        this.renderizarItensNaTabela();

        TipoPizzaBox.setModel(new DefaultComboBoxModel<>(EnTipoSabor.values()));


        ConfirmarButton.addActionListener(e -> {
          finalizarOperacao(false);
        });


        deletarButton.addActionListener(e -> {
          this.deletarSabor();
        });


        editarButton.addActionListener(e -> {
            this.finalizarOperacao(true);
        });


        voltaMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new MenuView();
            }
        });
    }


    private void finalizarOperacao(boolean ehEdicao) {
        String sabor = saborPizza.getText();
        EnTipoSabor nomeTipoSaborSelecionado = (EnTipoSabor) TipoPizzaBox.getSelectedItem();
        TipoSabor tipoSabor = this.tipoSaborController.buscarPorTipo(nomeTipoSaborSelecionado.toString());
        SaborPizza novoSabor = new SaborPizza(sabor, tipoSabor);
        String saborAtual = "";
        int selectedRow = 0;
        if(ehEdicao) {
            selectedRow = PizzasCadastradas.getSelectedRow();
            saborAtual = (String)tableModel.getValueAt(selectedRow, 1);
        }

        saborController.adicionarOuEditar(novoSabor, saborAtual, ehEdicao, selectedRow);

        this.renderizarItensNaTabela();
    }

    private void renderizarItensNaTabela() {
        List<SaborPizza> sabores = this.saborController.carregarSabores();

        int contador = 0;

        if(sabores.isEmpty()) {
            for(int i =0; i<tableModel.getRowCount(); i++){
                tableModel.removeRow(i);
            }
            return;
        }

        for (SaborPizza sabor : sabores) {
            this.tableModel.setRowCount(contador);
            tableModel.addRow(new Object[]{
                    sabor.getTipoSabor().toString(),
                    sabor.getNome(),
            });
            contador++;
        }

    }

    private void deletarSabor() {
        int selectedRow = PizzasCadastradas.getSelectedRow();

        if(selectedRow == -1){
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um pedido para alterar!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String saborAtual = (String)tableModel.getValueAt(selectedRow, 1);

        this.saborController.deletarSabor(saborAtual);

        JOptionPane.showMessageDialog(
                this,
                "Sabor excluído com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );

        this.renderizarItensNaTabela();
    }


}