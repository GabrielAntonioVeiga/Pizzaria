package view;

import enums.TipoSabor;
import model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ItensPedidoFormView extends JFrame {
    private JComboBox<Forma> cbForma;
    private JTextField tfDimensao;
    private JCheckBox cbxEhArea;
    private JComboBox<SaborPizza> cbSabor2;
    private JButton btnConfirmar;
    private JPanel tela;
    private JComboBox<SaborPizza> cbSabor1;
    private JCheckBox cbxDesativarSegundoSabor;

    Pizza pizza = null;

    public ItensPedidoFormView() {
       this.inicializarTela();
    }
    public ItensPedidoFormView(Pizza pizza) {
        this.pizza = pizza;
        this.inicializarTela();
        this.setarDadosPizza();
    }

    private void inicializarTela() {
        setContentPane(tela);
        setTitle("Criar Itens Pedido");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        cbForma.setModel(new DefaultComboBoxModel<>(getFormas()));
        cbSabor1.setModel(new DefaultComboBoxModel<>(getSabores()));
        cbSabor2.setModel(new DefaultComboBoxModel<>(getSabores()));

        setVisible(true);

        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarPedido();
            }
        });


        cbxDesativarSegundoSabor.addItemListener(e -> {

            boolean selecionado = cbxDesativarSegundoSabor.isSelected();
            cbSabor2.setEnabled(!selecionado);
        });

    }

    private void setarDadosPizza() {
        cbForma.setSelectedItem(pizza.getForma());
        setarSaboresPizza();
        tfDimensao.setText(pizza.getTamanho().toString());
        cbxEhArea.setSelected(true);
    }

    private void setarSaboresPizza() {
        List<SaborPizza> sabores = pizza.getSabores();
        List<JComboBox<SaborPizza>> cbSabores = List.of(cbSabor1, cbSabor2);
        List<JCheckBox> cbxDesativarSabores = new ArrayList<>(Arrays.asList(null, cbxDesativarSegundoSabor));

        IntStream.range(0, cbSabores.size())
                .forEach(i -> {
                    JComboBox<SaborPizza> cbSabor = cbSabores.get(i);

                    boolean hasSabor = i <= sabores.size() - 1;

                    if(hasSabor) {
                        SaborPizza saborPizzaSelecionada = sabores.get(i);
                        SaborPizza saborCorrespondenteNaCombobox = Arrays.stream(getSabores())
                                .filter(saborPizza -> saborPizza.equals(saborPizzaSelecionada))
                                .findFirst()
                                .orElse(null);
                        cbSabor.setSelectedItem(saborCorrespondenteNaCombobox);
                    }
                    else cbxDesativarSabores.get(i).setSelected(true);
                });
    }

    private SaborPizza[] getSabores() {
        SaborPizza sabor1 = new SaborPizza("Pepperoni", TipoSabor.ESPECIAL);
        SaborPizza sabor2 = new SaborPizza("Calabresa", TipoSabor.SIMPLES);
        SaborPizza sabor3 = new SaborPizza("Portuguesa", TipoSabor.PREMIUM);
        return new SaborPizza[]{sabor1, sabor2, sabor3};
    }

    private Forma[] getFormas() {
        Quadrado quadrado = new Quadrado();
        Triangulo triangulo = new Triangulo();
        Circulo circulo = new Circulo();
        return new Forma[]{quadrado, triangulo, circulo};
    }


    private void salvarPedido() {

        Forma formaEscolhida = this.getFormaEscolhida();
        List<SaborPizza> SaboresEscolhidos = getSaboresEscolhidos();
        this.pizza = new Pizza(formaEscolhida, SaboresEscolhidos);
        System.out.println(this.pizza.toString());

    }

    private List<SaborPizza> getSaboresEscolhidos() {
        if(cbxDesativarSegundoSabor.isSelected()) {
            return List.of((SaborPizza) cbSabor1.getSelectedItem());
        }

        return  List.of((SaborPizza) cbSabor1.getSelectedItem(), (SaborPizza) cbSabor2.getSelectedItem());
    }
    private Forma getFormaEscolhida() {
        Forma formaEscolhida = (Forma) cbForma.getSelectedItem();

        double dimensao = 0;
        try {
            dimensao = Double.parseDouble(tfDimensao.getText());

            if (cbxEhArea.isSelected()) {
                dimensao = formaEscolhida.calcularDimensao(dimensao);
            }

            boolean deveMostrarErroDeArea = this.cbxEhArea.isSelected();
            formaEscolhida.validarDimensao(dimensao, deveMostrarErroDeArea);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    tela,
                    "A dimensão deve ser um número válido!", // Mensagem do erro
                    "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    tela,
                    e.getMessage(),
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        formaEscolhida.setDimensao(dimensao);

        return formaEscolhida;
    }



    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(ItensPedidoFormView::new);
    }


}
