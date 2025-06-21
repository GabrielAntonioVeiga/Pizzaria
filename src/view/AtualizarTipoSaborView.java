package view;

import controller.SaborController;
import controller.TipoSaborController;
import factory.DAOFactory;
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
    private TipoSaborController tipoSaborController = new TipoSaborController(DAOFactory.getTipoSaborDao());

    public AtualizarTipoSaborView() {
        setContentPane(tela);
        setTitle("Atualizar preço do cm²");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        List<TipoSabor> tipoSabores = tipoSaborController.carregarTipoSabores();
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

            assert tipoSaborEscolhido != null;
            tipoSaborController.atualizarPreco(tipoSaborEscolhido.getNome(), novoPreco);
            setVisible(false);
            new MenuView();

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
