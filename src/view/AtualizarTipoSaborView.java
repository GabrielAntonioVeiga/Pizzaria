package view;

import controller.TipoSaborController;
import model.TipoSabor;

import javax.swing.*;
import java.util.List;

public class AtualizarTipoSaborView extends JFrame {
    private JComboBox<TipoSabor> cbTipoSabor;
    private JTextField tfPreco;
    private JPanel tela;
    private JButton btnConfirmar;
    private JButton btnVoltar;

    private TipoSaborController controller;

    public AtualizarTipoSaborView() {
        setContentPane(tela);
        setTitle("Atualizar Preço do cm² por Tipo de Sabor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        adicionarListeners();

        pack();
        setSize(500, 250);
        setLocationRelativeTo(null);
    }

    public void setController(TipoSaborController controller) {
        this.controller = controller;
    }

    private void adicionarListeners() {
        btnConfirmar.addActionListener(e -> controller.atualizarPreco());
        btnVoltar.addActionListener(e -> controller.voltarParaMenu());

        cbTipoSabor.addActionListener(e -> {
            TipoSabor selecionado = getTipoSaborSelecionado();
            if (selecionado != null) {
                tfPreco.setText(String.format("%.4f", selecionado.getPrecoCm2()).replace(',', '.'));
            } else {
                tfPreco.setText("");
            }
        });
    }

   

    public void popularTiposDeSabor(List<TipoSabor> tipos) {
        DefaultComboBoxModel<TipoSabor> model = new DefaultComboBoxModel<>(tipos.toArray(new TipoSabor[0]));
        cbTipoSabor.setModel(model);
        tfPreco.setText(""); 
    }

    public void exibirMensagem(String mensagem, int tipo) {
        String titulo = "Aviso";
        if (tipo == JOptionPane.ERROR_MESSAGE) titulo = "Erro";
        if (tipo == JOptionPane.INFORMATION_MESSAGE) titulo = "Sucesso";
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }


    public TipoSabor getTipoSaborSelecionado() {
        return (TipoSabor) cbTipoSabor.getSelectedItem();
    }

    public double getNovoPreco() throws NumberFormatException {
        return Double.parseDouble(tfPreco.getText().replace(',', '.'));
    }
}