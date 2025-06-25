import controller.MenuController;
import view.MenuView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // A única responsabilidade do main é iniciar o primeiro controller.
                new MenuController().iniciar();
            }
        });
    }

}