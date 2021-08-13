/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entidades.CartaoCredito;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import servicos.Database;
import servicos.Dialog;

public class CartaoController {

    private Database db = new Database();
    private CartaoCredito cartaoFisico;
    private CartaoCredito cartaoVirtual;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox carPaneFisico;

    @FXML
    private Label carNumero;

    @FXML
    private Label carCVV;

    @FXML
    private Label carDataValidade;

    @FXML
    private Label carLimite;

    @FXML
    private Label carDiaVencimentoFatura;

    @FXML
    private Label carCategoria;

    @FXML
    private Label carVariante;

    @FXML
    private Label carBandeira;

    @FXML
    private Label carStatus;

    @FXML
    private Pane carPaneVirtual;

    @FXML
    private Label carNumeroVirtual;

    @FXML
    private Label carCVVVirtual;

    @FXML
    private Label carDataValidadeVirtual;

    @FXML
    private Button carHandleFisico;

    @FXML
    private Button carHandleVirtual;

    @FXML
    private Button carCriarVirtual;

    @FXML
    private Label carStatusVirtual;

    @FXML
    void handleCartaoFisico(ActionEvent event) throws IOException {
        if (cartaoFisico.getStatus() == CartaoCredito.CartaoStatus.LOCK) {
            cartaoFisico.setStatus(CartaoCredito.CartaoStatus.AVAILABLE);
        } else {
            cartaoFisico.setStatus(CartaoCredito.CartaoStatus.LOCK);
        }
        LoginController.loggedIn.pushEditedCartao(cartaoFisico);
        db.post(LoginController.loggedIn);
        fetch();
  
        Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "Cartao editado!");
    }

    @FXML
    void handleCartaoVirtual(ActionEvent event) throws IOException {
        if (cartaoVirtual.getStatus() == CartaoCredito.CartaoStatus.LOCK) {
            cartaoVirtual.setStatus(CartaoCredito.CartaoStatus.AVAILABLE);
        } else {
            cartaoVirtual.setStatus(CartaoCredito.CartaoStatus.LOCK);
        }
        LoginController.loggedIn.pushEditedCartao(cartaoVirtual);
        db.post(LoginController.loggedIn);
        fetch();
  
        Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "Cartao editado!");
    }

    @FXML
    void criarCartaoVirtual(ActionEvent event) throws IOException {
        CartaoCredito newCartaoVirtual = new CartaoCredito();

        newCartaoVirtual.setVirtual(true);
        newCartaoVirtual.gerarCVV();
        newCartaoVirtual.gerarNumeroCartao();

        newCartaoVirtual.setBandeira(cartaoFisico.getBandeira());
        newCartaoVirtual.setCategoria(cartaoFisico.getCategoria());
        newCartaoVirtual.setVariante(cartaoFisico.getVariante());
        newCartaoVirtual.setStatus(CartaoCredito.CartaoStatus.AVAILABLE);

        newCartaoVirtual.setDataValidade(cartaoFisico.getDataValidade());
        LoginController.loggedIn.getCartoes().add(newCartaoVirtual);

        db.post(LoginController.loggedIn);
        Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "Cartão virtual criado!");
        fetch();
    }

    @FXML
    void editarCartao(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/CartaoEdit.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Editar cartão");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        fetch();
    }

    @FXML
    void initialize() {
        assert carPaneFisico != null : "fx:id=\"carPaneFisico\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carNumero != null : "fx:id=\"carNumero\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carCVV != null : "fx:id=\"carCVV\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carDataValidade != null : "fx:id=\"carDataValidade\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carLimite != null : "fx:id=\"carLimite\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carDiaVencimentoFatura != null : "fx:id=\"carDiaVencimentoFatura\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carCategoria != null : "fx:id=\"carCategoria\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carVariante != null : "fx:id=\"carVariante\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carBandeira != null : "fx:id=\"carBandeira\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carStatus != null : "fx:id=\"carStatus\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carPaneVirtual != null : "fx:id=\"carPaneVirtual\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carNumeroVirtual != null : "fx:id=\"carNumeroVirtual\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carCVVVirtual != null : "fx:id=\"carCVVVirtual\" was not injected: check your FXML file 'Cartao.fxml'.";
        assert carDataValidadeVirtual != null : "fx:id=\"carDataValidadeVirtual\" was not injected: check your FXML file 'Cartao.fxml'.";

        fetch();
    }

    public void fetch() {
        cartaoFisico = LoginController.loggedIn.getCartaoFisico();
        cartaoVirtual = LoginController.loggedIn.getCartaoVirtual();

        if (cartaoFisico != null) {
            carNumero.setText(cartaoFisico.getNumeroCartao());
            carCVV.setText("CVV:" + cartaoFisico.getCodigoSeguranca());
            carDataValidade.setText("Validade:" + new SimpleDateFormat("dd-MM-yyyy").format(cartaoFisico.getDataValidade()));
            carLimite.setText("Limite: " + cartaoFisico.getLimite());
            carDiaVencimentoFatura.setText("Dia de vencimento da fatura: " + cartaoFisico.getDiaVencimento());
            carCategoria.setText("Categoria: " + cartaoFisico.getCategoria().name());
            carVariante.setText("Variante: " + cartaoFisico.getVariante().name());
            carBandeira.setText("Bandeira: " + cartaoFisico.getBandeira().name());
            carStatus.setText("Status: " + cartaoFisico.getStatus().name());

            if (cartaoFisico.getStatus() == CartaoCredito.CartaoStatus.LOCK) {
                carHandleFisico.setText("Desbloquear");
            } else {
                carHandleFisico.setText("Bloquear");
            }

            if (cartaoVirtual != null) {
                carCriarVirtual.setDisable(true);
                carCriarVirtual.setOpacity(0);
                carHandleVirtual.setDisable(false);
                carPaneVirtual.setDisable(false);

                if (cartaoVirtual.getStatus() == CartaoCredito.CartaoStatus.LOCK) {
                    carHandleVirtual.setText("Desbloquear");
                } else {
                    carHandleVirtual.setText("Bloquear");
                }

                carNumeroVirtual.setText(cartaoVirtual.getNumeroCartao());
                carCVVVirtual.setText("CVV:" + cartaoVirtual.getCodigoSeguranca());
                carDataValidadeVirtual.setText("Validade:" + new SimpleDateFormat("dd-MM-yyyy").format(cartaoVirtual.getDataValidade()));
                carStatusVirtual.setText("Status: " + cartaoVirtual.getStatus().name());

            } else { // Nao tem cartao virtual
                carCriarVirtual.setDisable(false);
                carCriarVirtual.setOpacity(1);
            }
        } else {
            Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Não ha cartões registrado no seu usuario, entre em contato com o suporte!");
        }
    }

}
