package view;

import controller.ItemPedidoController;
import model.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ItensPedidoFormView extends JFrame {

    private JComboBox<Forma> cbForma;
    private JTextField tfDimensao;
    private JCheckBox cbxEhArea;
    private JComboBox<SaborPizza> cbSabor2;
    private JButton btnConfirmar;
    private JPanel tela;
    private JComboBox<SaborPizza> cbSabor1;
    private JCheckBox cbxDesativarSegundoSabor;
    private JLabel lblValorLado;
    private JLabel lblPrecoPizza;

    private ItemPedidoController controller;
    private SaborPizza[] todosOsSabores; 

    private boolean isUpdatingSabores = false;

    private ActionListener sabor1Listener;
    private ActionListener sabor2Listener;
    public ItensPedidoFormView() {
        inicializarTela();
    }

    public void setController(ItemPedidoController controller) {
        this.controller = controller;
    }

    private void inicializarTela() {
        setContentPane(tela);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cbxDesativarSegundoSabor.setSelected(true);
        cbSabor2.setEnabled(false);

        adicionarListeners();
    }

    private void adicionarListeners() {
        btnConfirmar.addActionListener(e -> { if (controller != null) controller.confirmar(); });

        DocumentListener listenerDocumento = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { if (controller != null) controller.onFormChanged(); }
            public void removeUpdate(DocumentEvent e) { if (controller != null) controller.onFormChanged(); }
            public void changedUpdate(DocumentEvent e) { if (controller != null) controller.onFormChanged(); }
        };

        tfDimensao.getDocument().addDocumentListener(listenerDocumento);
        cbForma.addActionListener(e -> { if (controller != null) controller.onFormChanged(); });
        cbxEhArea.addActionListener(e -> { if (controller != null) controller.onFormChanged(); });

        cbxDesativarSegundoSabor.addActionListener(e -> {
            cbSabor2.setEnabled(!cbxDesativarSegundoSabor.isSelected());
            if (cbxDesativarSegundoSabor.isSelected()) {
                cbSabor2.setSelectedItem(null);
            }
            if (controller != null) controller.onFormChanged();
        });

        sabor1Listener = e -> { if (controller != null) controller.onFormChanged(); };
        sabor2Listener = e -> { if (controller != null) controller.onFormChanged(); };

        cbSabor1.addActionListener(sabor1Listener);
        cbSabor2.addActionListener(sabor2Listener);
    }

    public void setTitulo(String titulo) {
        setTitle(titulo);
    }

    public void popularSabores(SaborPizza[] sabores) {
        this.todosOsSabores = sabores;
        atualizarDisponibilidadeSabores(); 
    }

    public void popularFormas(Forma[] formas) {
        cbForma.setModel(new DefaultComboBoxModel<>(formas));
    }

    public void preencherFormulario(Pizza pizza) {
        for (int i = 0; i < cbForma.getItemCount(); i++) {
            if (cbForma.getItemAt(i).getClass().equals(pizza.getForma().getClass())) {
                cbForma.setSelectedIndex(i);
                break;
            }
        }

        List<SaborPizza> sabores = pizza.getSabores();
        if (!sabores.isEmpty()) {
            selecionarItemNoCombo(cbSabor1, sabores.get(0));
        }
        if (sabores.size() > 1) {
            cbxDesativarSegundoSabor.setSelected(false);
            cbSabor2.setEnabled(true);
            selecionarItemNoCombo(cbSabor2, sabores.get(1));
        }

        tfDimensao.setText(String.format("%.2f", pizza.getTamanho()).replace(',', '.'));
        cbxEhArea.setSelected(true);

        if (controller != null) controller.onFormChanged();
    }

    private void selecionarItemNoCombo(JComboBox<SaborPizza> comboBox, SaborPizza saborParaSelecionar) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(saborParaSelecionar)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    public void setPrecoEstimado(String preco) {
        lblPrecoPizza.setText(preco);
    }

    public void setDimensaoCalculada(String texto) {
        lblValorLado.setText(texto);
    }

    public void exibirMensagem(String titulo, String mensagem, int tipo) {
        JOptionPane.showMessageDialog(this.tela, mensagem, titulo, tipo);
    }


    public Forma getFormaEscolhida(boolean validar) {
        Forma forma = (Forma) cbForma.getSelectedItem();
        double dimensao = getDimensao();
        if (isArea()) {
            dimensao = forma.calcularDimensao(dimensao);
        }
        if (validar) {
            forma.validarDimensao(dimensao, isArea());
        }
        forma.setDimensao(dimensao);
        return forma;
    }

    public List<SaborPizza> getSaboresSelecionados(boolean validar) {
        List<SaborPizza> sabores = new ArrayList<>();
        if (cbSabor1.getSelectedItem() != null) {
            sabores.add((SaborPizza) cbSabor1.getSelectedItem());
        }
        if (!cbxDesativarSegundoSabor.isSelected() && cbSabor2.getSelectedItem() != null) {
            sabores.add((SaborPizza) cbSabor2.getSelectedItem());
        }

        if (validar) {
            if (sabores.isEmpty()) throw new IllegalArgumentException("Selecione pelo menos um sabor.");
            if (!cbxDesativarSegundoSabor.isSelected() && cbSabor2.getSelectedItem() == null) {
                throw new IllegalArgumentException("O segundo sabor está ativado mas não foi selecionado.");
            }
        }
        return sabores;
    }

    public double getDimensao() {
        try {
            return Double.parseDouble(tfDimensao.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean isArea() {
        return cbxEhArea.isSelected();
    }

    public void atualizarDisponibilidadeSabores() {
        cbSabor1.removeActionListener(sabor1Listener);
        cbSabor2.removeActionListener(sabor2Listener);

        try {
            SaborPizza selecionado1 = (SaborPizza) cbSabor1.getSelectedItem();
            SaborPizza selecionado2 = (SaborPizza) cbSabor2.getSelectedItem();

            if (todosOsSabores == null) return;

            DefaultComboBoxModel<SaborPizza> model1 = new DefaultComboBoxModel<>();
            DefaultComboBoxModel<SaborPizza> model2 = new DefaultComboBoxModel<>();

            for (SaborPizza s : todosOsSabores) {
                if (selecionado2 == null || !s.equals(selecionado2)) {
                    model1.addElement(s);
                }
                if (selecionado1 == null || !s.equals(selecionado1)) {
                    model2.addElement(s);
                }
            }

            cbSabor1.setModel(model1);
            cbSabor2.setModel(model2);

            cbSabor1.setSelectedItem(selecionado1);
            cbSabor2.setSelectedItem(selecionado2);

        } finally {
            cbSabor1.addActionListener(sabor1Listener);
            cbSabor2.addActionListener(sabor2Listener);
        }
    }

}