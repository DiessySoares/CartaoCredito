/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entidades.CartaoCredito;
import entidades.ItemFatura;
import entidades.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import servicos.Database;
import servicos.Dialog;

public class CompraController {

    private Database db = new Database();
    private ArrayList<Usuario> usuarios;
    private CartaoCredito cartao = new CartaoCredito();

    private ItemFatura item = new ItemFatura();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Usuario> comUsuario = new ChoiceBox<>();

    @FXML
    private ChoiceBox<CartaoCredito> comCartoes;

    @FXML
    private TextField comDescricaoItem;

    @FXML
    private TextField comValor;

    @FXML
    private Spinner<Integer> comVezes;

    @FXML
    private Button comBtnComprar;

    @FXML
    private void getCards(ActionEvent event) {
        if (comUsuario.getValue().getCartaoFisicoAtivo() != null && comUsuario.getValue().getCartaoFisicoAtivo().getStatus() == CartaoCredito.CartaoStatus.AVAILABLE) {
            comCartoes.getItems().add(comUsuario.getValue().getCartaoFisicoAtivo());
        }
        if (comUsuario.getValue().getCartaoVirtualAtivo() != null && comUsuario.getValue().getCartaoVirtualAtivo().getStatus() == CartaoCredito.CartaoStatus.AVAILABLE) {
            comCartoes.getItems().add(comUsuario.getValue().getCartaoVirtualAtivo());
        }
    }

    @FXML
    void initialize() throws IOException {
        assert comUsuario != null : "fx:id=\"comUsuario\" was not injected: check your FXML file 'Compra.fxml'.";
        assert comDescricaoItem != null : "fx:id=\"comDescricaoItem\" was not injected: check your FXML file 'Compra.fxml'.";
        assert comValor != null : "fx:id=\"comValor\" was not injected: check your FXML file 'Compra.fxml'.";
        assert comVezes != null : "fx:id=\"comVezes\" was not injected: check your FXML file 'Compra.fxml'.";
        assert comBtnComprar != null : "fx:id=\"comComprar\" was not injected: check your FXML file 'Compra.fxml'.";

        comBtnComprar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                cartao = comCartoes.getValue();

                if (cartao != null && cartao.getStatus() == CartaoCredito.CartaoStatus.AVAILABLE) {

                    // EFETUAR COMPRA
                    item.setDataCompra(new Date());
                    item.setDescricao(comDescricaoItem.getText());

                    if (comVezes.getValue() > 1) {
                        item.setVezes(comVezes.getValue());
                        item.setParcelado(true);
                        item.setParcela(Float.parseFloat(comValor.getText()) / comVezes.getValue());
                    } else {
                        item.setVezes(1);
                        item.setParcelado(false);
                        item.setParcela(0);
                    }

                    item.setValor(Float.parseFloat(comValor.getText()));

                    if (cartao.isVirtual()) {
                        resolveSymbolicLink();
                    }

                    if (cartao.inserirItem(item)) {

                        comUsuario.getValue().pushEditedCartao(cartao);
                        try {
                            db.post(comUsuario.getValue());
                            Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "Item comprado!");
                            ((Button) t.getTarget()).getScene().getWindow().hide();
                        } catch (IOException ex) {
                            Dialog.Dialog(Alert.AlertType.ERROR, "Erro", "Erro ao salvar");
                        }
                    }
                } else {
                    Dialog.Dialog(Alert.AlertType.ERROR, "Erro", "Não foi possivel user esse cartão");
                }
            }
        });

        usuarios = db.get();

        if (usuarios != null) {
            comUsuario.getItems().setAll(usuarios);
        }
    }

    public void resolveSymbolicLink() {   
        cartao = comUsuario.getValue().getCartaoFisicoAtivo();
    }
}
