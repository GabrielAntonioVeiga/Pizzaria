package view;

import controller.ClienteController;
import controller.ItemPedidoController;
import controller.PedidoController;
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


    int idItemSelecionado = 0;
    int idPedido = 0;

    boolean ehEdicao = false;
    ItemPedidoController itemPedidoController = new ItemPedidoController();
    SaborController saborController = new SaborController();
    PedidoController pedidoController = new PedidoController();


    private BancoDados banco = BancoDados.getInstancia();

    public ItensPedidoFormView(int idPedido) {
        this.idPedido = idPedido;
        this.inicializarTela();
    }

    public ItensPedidoFormView(int idPedido, int idItem) {
        ehEdicao = true;
        this.idPedido = idPedido;
        this.idItemSelecionado = idItem;
        this.inicializarTela();
        Pizza itemSelecionado = itemPedidoController.retornarItemPedido(idPedido, idItem);
        setarDadosPizza(itemSelecionado);
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

        cbxDesativarSegundoSabor.setSelected(true);
        cbSabor2.setSelectedItem(null);
        cbSabor2.setEnabled(false);
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
            if (!selecionado)
                cbSabor2.setSelectedItem(null);
            this.atualizarDisponibilidadeSabores();
        });

        cbSabor1.addActionListener(e -> atualizarDisponibilidadeSabores());
        cbSabor2.addActionListener(e -> atualizarDisponibilidadeSabores());

    }

    private void atualizarDisponibilidadeSabores() {
        SaborPizza[] todosSabores = getSabores();

        SaborPizza saborSelecionado1 = (SaborPizza) cbSabor1.getSelectedItem();
        SaborPizza saborSelecionado2 = (SaborPizza) cbSabor2.getSelectedItem();

        if(!cbSabor2.isEnabled()) {
            cbSabor1.setModel(new DefaultComboBoxModel<>(todosSabores));
            cbSabor1.setSelectedItem(saborSelecionado1);
            return;
        }

        DefaultComboBoxModel<SaborPizza> modeloSabor1 = new DefaultComboBoxModel<>();
        for (SaborPizza sabor : todosSabores) {
            if (!sabor.equals(saborSelecionado2)) {
                modeloSabor1.addElement(sabor);
            }
        }
        cbSabor1.setModel(modeloSabor1);
        cbSabor1.setSelectedItem(saborSelecionado1);


        DefaultComboBoxModel<SaborPizza> modeloSabor2 = new DefaultComboBoxModel<>();
        for (SaborPizza sabor : todosSabores) {
            if (!sabor.equals(saborSelecionado1)) {
                modeloSabor2.addElement(sabor);
            }
        }
        cbSabor2.setModel(modeloSabor2);
        cbSabor2.setSelectedItem(saborSelecionado2);
    }

    private void setarDadosPizza(Pizza pizza) {
        cbForma.setSelectedItem(pizza.getForma());
        setarSaboresPizza(pizza);
        tfDimensao.setText(pizza.getTamanho().toString());
        cbxEhArea.setSelected(true);
    }

    private void setarSaboresPizza(Pizza pizza) {
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
            String acaoAtualMensagem = "";
            String acaoConcluidaMensagem = "";
            if(ehEdicao) {
                itemPedidoController.editarItemPedido(idPedido, novaPizza, this.idItemSelecionado);
                acaoAtualMensagem = "salvar";
                acaoConcluidaMensagem = "salvo";
            }
            else {
                itemPedidoController.adicionarItemPedido(idPedido, novaPizza);
                acaoAtualMensagem = "editar";
                acaoConcluidaMensagem = "editado";
            }


            JOptionPane.showMessageDialog(
                    tela,
                    "Item " + acaoConcluidaMensagem +  " no pedido com sucesso!",
                    "Sucesso ao " + acaoAtualMensagem,
                    JOptionPane.INFORMATION_MESSAGE
            );
            setVisible(false);
            new PedidoView(idPedido);

        }
        catch (Exception e) {
            String mensagemDeErro = "Houve um erro ao salvar este pedido!";

            boolean mensagemDeErroFoiTratada = e instanceof NullPointerException || e instanceof IllegalArgumentException;

            if(mensagemDeErroFoiTratada) {
                mensagemDeErro = e.getMessage();
            }

            JOptionPane.showMessageDialog(
                    tela,
                    mensagemDeErro,
                    "Erro ao salvar",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    private List<SaborPizza> getSaboresEscolhidos() {

        if(cbSabor2.getSelectedItem() == null && !cbxDesativarSegundoSabor.isSelected())
            throw new NullPointerException("Você não selecionou o segundo sabor.");

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

        }    catch (Exception e) {
            if (e instanceof NumberFormatException) {
                throw new NumberFormatException("A dimensão deve ser um número válido!");
            }
            throw e;
        }


        formaEscolhida.setDimensao(dimensao);

        return formaEscolhida;
    }
}
