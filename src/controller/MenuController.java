package controller;

import view.MenuView;
public class MenuController {

    private MenuView view;

    public MenuController() {
    }


    public void iniciar() {
        this.view = new MenuView();
        this.view.setController(this); 
        this.view.setVisible(true);
    }

    public void abrirTelaClientes() {
        view.dispose();
        new ClienteController().iniciar(); 
    }

    public void abrirTelaPedidos() {
        view.dispose();
        new PedidosController().iniciar(); 
    }

    public void abrirTelaSabores() {
        view.dispose();
         new SaborController().iniciar(); 
    }

    public void abrirTelaAtualizarPrecos() {
        view.dispose();
         new TipoSaborController().iniciar(); 
    }

    public void sairDaAplicacao() {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(view,
                "Tem certeza que deseja sair?",
                "Sair da Aplicação",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}