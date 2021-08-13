package controller;

import entidades.CartaoCredito;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import servicos.Dialog;

public class CartaoController {

    private final int VALID_YEARS = 3;

    private CartaoCredito cartao;
    private boolean edit = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label carRenda;

    @FXML
    private TextField carLimite;

    @FXML
    private ChoiceBox<CartaoCredito.CartaoCategoria> carCategoria = new ChoiceBox<>();

    @FXML
    private ChoiceBox<CartaoCredito.CartaoBandeira> carBandeira = new ChoiceBox<>();

    @FXML
    private ChoiceBox<CartaoCredito.CartaoVariante> carVariante = new ChoiceBox<>();

    @FXML
    private ChoiceBox<CartaoCredito.CartaoStatus> carStatus = new ChoiceBox<>();

    @FXML
    private Label carInfoAcao;

    @FXML
    private Button crtBtnCancelar;

    @FXML
    private Button crtBtnSalvar;
    

    @FXML
    void salvar(ActionEvent event) {
        if (checkValues()) {
            if (edit) {
                cartao.setLimite(Float.parseFloat(carLimite.getText()));
                cartao.setStatus(carStatus.getValue());
                HomeController.selectedUser.pushEditedCartao(cartao);
                Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "Cartão foi editado!");

            } else {
                // Novo cartao -------------------------
                cartao = new CartaoCredito();

                cartao.gerarNumeroCartao();
                cartao.gerarCVV();
                cartao.setBandeira(carBandeira.getValue());
                cartao.setCategoria(carCategoria.getValue());
                cartao.setVariante(carVariante.getValue());
                cartao.setStatus(CartaoCredito.CartaoStatus.AVAILABLE);
                cartao.setDataValidade(addYears(new Date())); // add 3 years to current date
                cartao.setVirtual(false);
                cartao.setLimite(Float.parseFloat(carLimite.getText()));

            }
        } else {
            Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Existem campos obrigatorios vazios");
        }
    }
    
    // campos limite numero

    private Date addYears(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, VALID_YEARS);
        return cal.getTime();
    }

    private boolean checkValues() {
        return carLimite.getText() != null && !carLimite.getText().trim().isEmpty()
                && carCategoria.getValue() != null && carBandeira.getValue() != null 
                && carVariante.getValue() != null && carStatus.getValue() != null;
    }

    @FXML
    void initialize() {
        assert carRenda != null : "fx:id=\"carRenda\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carLimite != null : "fx:id=\"carLimite\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carCategoria != null : "fx:id=\"carCategoria\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carBandeira != null : "fx:id=\"carBandeira\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carVariante != null : "fx:id=\"carVariante\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carStatus != null : "fx:id=\"carStatus\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert crtBtnSalvar != null : "fx:id=\"crtBtnSalvar\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carInfoAcao != null : "fx:id=\"carInfoAcao\" was not injected: check your FXML file 'Cartao.fxml'.";

        
        /*carLimite.textProperty().addListener(new ChangeListener<String>() {
          @Override
          public void changed(ObservableValue<? extends String> observable, String oldValue, 
              String newValue) {
              if (!newValue.matches("\\d*")) {
                  carLimite.setText(newValue.replaceAll("[^\\d]", ""));
              }
          }
        });
        */
        
        crtBtnCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ((Button) t.getTarget()).getScene().getWindow().hide();
            }
        });

        crtBtnSalvar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (checkValues()) {
                    if (edit) {
                        cartao.setLimite(Float.parseFloat(carLimite.getText()));
                        cartao.setStatus(carStatus.getValue());
                        HomeController.selectedUser.pushEditedCartao(cartao);
                        Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "Cartão foi editado!");
                    } else {
                        // Novo cartao -------------------------
                        cartao = new CartaoCredito();

                        cartao.gerarNumeroCartao();
                        cartao.gerarCVV();
                        cartao.setBandeira(carBandeira.getValue());
                        cartao.setCategoria(carCategoria.getValue());
                        cartao.setVariante(carVariante.getValue());
                        cartao.setStatus(CartaoCredito.CartaoStatus.AVAILABLE);
                        cartao.setDiaVencimento(8);
                        cartao.setDataValidade(addYears(new Date())); // add 3 years to current date
                        cartao.setVirtual(false);
                        cartao.setLimite(Float.parseFloat(carLimite.getText()));
                        
                        if(HomeController.selectedUser.getCartoes() == null) {
                            HomeController.selectedUser.setCartoes(new ArrayList<>());
                        }
                       
                        HomeController.selectedUser.getCartoes().add(cartao);
                        Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "Cartão foi criado!");
                    }
                    ((Button) t.getTarget()).getScene().getWindow().hide();
                } else {
                    Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Existem campos obrigatorios vazios!");
                }
            }
        });

        carRenda.setText("Renda: R$" + String.valueOf(HomeController.selectedUser.getRendaMensal()));

        cartao = HomeController.selectedUser.getCartaoFisicoAtivo();
        edit = (cartao != null);

        carBandeira.getItems().setAll(CartaoCredito.CartaoBandeira.values());
        carCategoria.getItems().setAll(CartaoCredito.CartaoCategoria.values());
        carVariante.getItems().setAll(CartaoCredito.CartaoVariante.values());
        carStatus.getItems().setAll(CartaoCredito.CartaoStatus.values());

        if (edit) {
            carInfoAcao.setText("Editar Cartão");
            
            carLimite.setText(String.valueOf(cartao.getLimite()));

            carBandeira.setValue(this.cartao.getBandeira());
            carCategoria.setValue(this.cartao.getCategoria());
            carVariante.setValue(this.cartao.getVariante());
            carStatus.setValue(this.cartao.getStatus());

            carBandeira.setDisable(true);
            carCategoria.setDisable(true);
            carVariante.setDisable(true);
        } else {
            carInfoAcao.setText("Novo Cartão");

            carStatus.setValue(CartaoCredito.CartaoStatus.AVAILABLE);
            carStatus.setDisable(true);
        }

    }
}
