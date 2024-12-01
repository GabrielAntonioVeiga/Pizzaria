package view;

import controller.ClienteController;
import controller.ItemPedidoController;
import controller.SaborController;
import dados.BancoDados;
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
    Cliente cliente = null;
    boolean ehEdicao = false;
    ItemPedidoController itemPedidoController = new ItemPedidoController();
    SaborController saborController = new SaborController();
    ClienteController clienteController = null;


    private BancoDados banco = BancoDados.getInstancia();

    public ItensPedidoFormView(Cliente cliente, ClienteController clienteController) {
        this.clienteController = clienteController;
       this.inicializarTela();
    }
    public ItensPedidoFormView(Pizza pizza) {
        this.pizza = pizza;
        ehEdicao = true;
        this.inicializarTela();
        this.setarDadosPizza();
    }

    private void inicializarTela() {
        setContentPane(tela);
        String actionMode = ehEdicao ? "Editar" : "Criar";
        setTitle(actionMode + " Item no Pedido");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);


        cbForma.setModel(new DefaultComboBoxModel<>(getFormas()));
        cbSabor1.setModel(new DefaultComboBoxModel<>(getSabores()));
        cbSabor2.setModel(new DefaultComboBoxModel<>(getSabores()));

        setVisible(true);

        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarOperacao();
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
      List<SaborPizza> sabores = this.saborController.carregarSabores();
      return sabores.toArray(new SaborPizza[0]);
    }

    private Forma[] getFormas() {
        Quadrado quadrado = new Quadrado();
        Triangulo triangulo = new Triangulo();
        Circulo circulo = new Circulo();
        return new Forma[]{quadrado, triangulo, circulo};
    }

    private void finalizarOperacao() {
        try {
            Forma formaEscolhida = this.getFormaEscolhida();
            List<SaborPizza> SaboresEscolhidos = getSaboresEscolhidos();
            Pizza novaPizza = new Pizza(formaEscolhida, SaboresEscolhidos);
            List<Pizza> itens = List.of(novaPizza);
            if(ehEdicao)
                itemPedidoController.editarItemPedido(novaPizza);
            else
                clienteController.adicionarPedido(cliente, new Pedido(itens));

            JOptionPane.showMessageDialog(
                    tela,
                    "Item salvo no pedido com sucesso!",
                    "Sucesso ao salvar",
                    JOptionPane.INFORMATION_MESSAGE
            );
            setVisible(false);
            new PedidoView();

        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(
                    tela,
                    "Houve um erro ao salvar este pedido!",
                    "Erro ao salvar",
                    JOptionPane.ERROR_MESSAGE
            );
        }


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
                    "A dimensão deve ser um número válido!",
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
}
