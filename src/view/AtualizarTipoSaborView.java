package view;

import controller.ItemPedidoController;
import controller.SaborController;
import controller.TipoSaborController;
import dados.BancoDados;
import enums.NomeTipoSabor;
import model.Forma;
import model.TipoSabor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class AtualizarTipoSaborView extends JFrame {
    private JComboBox<TipoSabor> cbTipoSabor;
    private JTextField tfPreco;
    private JPanel tela;
    private JButton btnConfirmar;
    private SaborController saborController = new SaborController();
    private TipoSaborController tipoSaborController = new TipoSaborController();

    public AtualizarTipoSaborView() {
        setContentPane(tela);
        setTitle("Atualizar preço do cm²");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        List<TipoSabor> tipoSabores = saborController.carregarTipoSabores();
        cbTipoSabor.setModel(new DefaultComboBoxModel<>(tipoSabores.toArray(new TipoSabor[0])));


        setVisible(true);

        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarPreco();
            }
        });
    }

    public void atualizarPreco() {
        try {
            double novoPreco = Double.parseDouble(tfPreco.getText());
            TipoSabor tipoSaborEscolhido = (TipoSabor) cbTipoSabor.getSelectedItem();

            tipoSaborController.atualizarPreco(tipoSaborEscolhido.getNome(), novoPreco);
            JOptionPane.showMessageDialog(
                    tela,
                    "Preço atualizado com sucesso!",
                    "Sucesso ao atualizar preço",
                    JOptionPane.INFORMATION_MESSAGE
            );

        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    tela,
                    "O preço deve ser um número válido!",
                    "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
