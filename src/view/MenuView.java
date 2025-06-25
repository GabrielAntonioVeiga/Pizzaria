package view;

import controller.MenuController; 
import javax.swing.*;

public class MenuView extends JFrame {

    private JPanel tela;
    private JButton clienteButton;
    private JButton atualizarPrecosButton;
    private JButton pedidoButton;
    private JButton saboresPizzaButton;
    private JButton sairButton;


    private MenuController controller;

    public MenuView() {
        inicializarTela();
        adicionarListeners();
    }

    public void setController(MenuController controller) {
        this.controller = controller;
    }

    private void inicializarTela() {
        setContentPane(tela);
        setTitle("Pizzaria - Menu Principal");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
    }

    private void adicionarListeners() {
        clienteButton.addActionListener(e -> controller.abrirTelaClientes());
        pedidoButton.addActionListener(e -> controller.abrirTelaPedidos());
        saboresPizzaButton.addActionListener(e -> controller.abrirTelaSabores());
        atualizarPrecosButton.addActionListener(e -> controller.abrirTelaAtualizarPrecos());
        sairButton.addActionListener(e -> controller.sairDaAplicacao());
    }
}