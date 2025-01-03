package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuView extends JFrame{
    private JPanel tela;
    private JButton clienteButton;
    private JButton atualizarPreçosButton;
    private JButton pedidoButton;
    private JButton saboresPizzaButton;
    private JButton sairButton;
    private JLabel title;

    public MenuView() {
        setContentPane(tela);
        setTitle(title.toString());
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        clienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new ClienteView();
            }
        });
        pedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new PedidosView();
            }
        });
        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    System.exit(0);
            }
        });
        atualizarPreçosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new AtualizarTipoSaborView();

            }
        });
        saboresPizzaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new CadastrarSaborView();
            }
        });
    }
}
