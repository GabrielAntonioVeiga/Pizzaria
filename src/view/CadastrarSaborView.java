package view;

import controller.SaborController;
import model.SaborPizza;
import model.TipoSabor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CadastrarSaborView extends JFrame {
    private JPanel CadastraPizza;
    private JButton deletarButton;
    private JButton editarButton;
    private JTable PizzasCadastradas;
    private JComboBox<TipoSabor> TipoPizzaBox;
    private JTextField saborPizza;
    private JButton ConfirmarButton;
    private JButton voltaMenuButton;
    private DefaultTableModel tableModel;

    private SaborController controller;

    public CadastrarSaborView() {
        setContentPane(CadastraPizza);
        setTitle("Gerenciamento de Sabores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Sabor", "Tipo"}) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        PizzasCadastradas.setModel(tableModel);

        adicionarListeners();
        pack();
        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    public void setController(SaborController controller) {
        this.controller = controller;
    }

    private void adicionarListeners() {
        ConfirmarButton.addActionListener(e -> controller.adicionarSabor());
        editarButton.addActionListener(e -> controller.salvarEdicaoSabor());
        deletarButton.addActionListener(e -> controller.deletarSabor());
        voltaMenuButton.addActionListener(e -> controller.voltarParaMenu());

        PizzasCadastradas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.carregarSaborParaEdicao();
            }
        });
    }


    public void renderizarItensNaTabela(List<SaborPizza> sabores) {
        tableModel.setRowCount(0);
        if (sabores == null) return;
        for (SaborPizza sabor : sabores) {
            tableModel.addRow(new Object[]{
                    sabor.getId(),
                    sabor.getNome(),
                    sabor.getTipoSabor().getNome().toString()
            });
        }
    }

    public void popularTiposDeSabor(List<TipoSabor> tipos) {
        DefaultComboBoxModel<TipoSabor> model = new DefaultComboBoxModel<>(tipos.toArray(new TipoSabor[0]));
        TipoPizzaBox.setModel(model);
        limparCampos();
    }

    public void exibirMensagem(String mensagem, int tipo) {
        String titulo = (tipo == JOptionPane.ERROR_MESSAGE) ? "Erro" : "Sucesso";
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }

    public void limparCampos() {
        saborPizza.setText("");
        TipoPizzaBox.setSelectedItem(null);
        PizzasCadastradas.clearSelection();
    }

    public void preencherCampos(SaborPizza sabor) {
        saborPizza.setText(sabor.getNome());
        for (int i = 0; i < TipoPizzaBox.getItemCount(); i++) {
            if (TipoPizzaBox.getItemAt(i).equals(sabor.getTipoSabor())) {
                TipoPizzaBox.setSelectedIndex(i);
                break;
            }
        }
    }


    public String getNomeSabor() {
        return saborPizza.getText().trim();
    }

    public TipoSabor getTipoSaborSelecionado() {
        return (TipoSabor) TipoPizzaBox.getSelectedItem();
    }

    public SaborPizza getSaborSelecionadoDaTabela() {
        int selectedRow = PizzasCadastradas.getSelectedRow();
        if (selectedRow < 0) return null;

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        String nome = (String) tableModel.getValueAt(selectedRow, 1);

        SaborPizza sabor = new SaborPizza(nome, null);
        sabor.setId(id);
        return sabor;
    }
}