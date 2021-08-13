package controller;

import entidades.CartaoCredito;
import entidades.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import servicos.Database;
import servicos.Dialog;

public class UserEditController {

    private Database db = new Database();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Usuario.ContaStatus> usuStatus = new ChoiceBox<>();

    @FXML
    private TextField usuNome;

    @FXML
    private TextField usuCPF;

    @FXML
    private TextField usuTelefone;

    @FXML
    private TextField usuRenda;

    @FXML
    private TextField usuLog;

    @FXML
    private TextField usuNumero;

    @FXML
    private TextField usuBairro;

    @FXML
    private TextField usuCidade;

    @FXML
    private TextField usuEstado;

    @FXML
    private TextField usuPais;

    @FXML
    private Button usuBtnSalvar;

    @FXML
    private Button usuBtnGernciarCartao;

    @FXML
    private Button usuBtnCancelar;

    @FXML
    void gerenciarCartao(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/Cartao.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    void initialize() {
        assert usuNome != null : "fx:id=\"usuNome\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert usuCPF != null : "fx:id=\"usuCPF\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert usuTelefone != null : "fx:id=\"usuTelefone\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert usuRenda != null : "fx:id=\"usuRenda\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert usuLog != null : "fx:id=\"usuLog\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert usuNumero != null : "fx:id=\"usuNumero\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert usuBairro != null : "fx:id=\"usuBairro\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert usuCidade != null : "fx:id=\"usuCidade\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert usuEstado != null : "fx:id=\"usuEstado\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert usuPais != null : "fx:id=\"usuPais\" was not injected: check your FXML file 'UserEdit.fxml'.";

        usuBtnCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (HomeController.selectedUser.getStatus() == Usuario.ContaStatus.REJEITADA) {
                    HomeController.root.setEffect(null);
                    ((Button) t.getTarget()).getScene().getWindow().hide();
                }
                else {
                    Dialog.Dialog(Alert.AlertType.CONFIRMATION, "Aviso", "As mudanças não serao salvas!");
                }
            }
        });

        usuBtnSalvar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                if (HomeController.selectedUser.getStatus() == Usuario.ContaStatus.PENDENTE) {
                    HomeController.selectedUser.setStatus(usuStatus.getValue());
                    if (HomeController.selectedUser.getCartaoFisicoAtivo() != null || HomeController.selectedUser.getStatus() == Usuario.ContaStatus.REJEITADA) {
                        try {
                            db.post(HomeController.selectedUser);
                            HomeController.root.setEffect(null);
                            ((Button) t.getTarget()).getScene().getWindow().hide();
                        } catch (IOException ex) {
                            Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Não foi possivel salvar o usuario!");
                        }
                    } else {
                        Dialog.Dialog(Alert.AlertType.WARNING, "Aviso", "O usuario necessita de um cartao!");
                    }
                } else {
                     try {
                        db.post(HomeController.selectedUser);
                        Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "O usuario foi salvo!");
                        HomeController.root.setEffect(null);
                        ((Button) t.getTarget()).getScene().getWindow().hide();
                    } catch (IOException ex) {
                        Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Não foi possivel salvar o usuario!");
                    }
                }
            }

        });

        // INIT --------------------------------------------------------------------------------------
        usuNome.setText(HomeController.selectedUser.getNome());
        usuCPF.setText(HomeController.selectedUser.getCPF());
        usuTelefone.setText(HomeController.selectedUser.getTelefone());
        usuRenda.setText(String.valueOf(HomeController.selectedUser.getRendaMensal()));

        usuLog.setText(HomeController.selectedUser.getEndereco().getLogradouro());
        usuNumero.setText(String.valueOf(HomeController.selectedUser.getEndereco().getNumero()));
        usuBairro.setText(HomeController.selectedUser.getEndereco().getBairro());
        usuCidade.setText(HomeController.selectedUser.getEndereco().getCidade());
        usuEstado.setText(HomeController.selectedUser.getEndereco().getEstado());
        usuPais.setText(HomeController.selectedUser.getEndereco().getPais());

        usuStatus.getItems().setAll(Usuario.ContaStatus.values());
        usuStatus.setValue(HomeController.selectedUser.getStatus());

        limitEditByUserType(HomeController.selectedUser.getStatus());
    }

    public void limitEditByUserType(Usuario.ContaStatus status) {
        switch (status) {
            case REJEITADA:
                usuBtnSalvar.setDisable(true);
                usuBtnGernciarCartao.setDisable(true);
            case ACEITA:
                usuStatus.setDisable(true);
            case PENDENTE:
                usuNome.setDisable(true);
                usuCPF.setDisable(true);
                usuTelefone.setDisable(true);
                usuRenda.setDisable(true);
                usuLog.setDisable(true);
                usuNumero.setDisable(true);
                usuBairro.setDisable(true);
                usuCidade.setDisable(true);
                usuEstado.setDisable(true);
                usuPais.setDisable(true);
        }

    }

}
