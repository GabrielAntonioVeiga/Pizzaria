import view.ClienteView;
import view.MenuView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        MenuView menu = new MenuView();
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}