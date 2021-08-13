/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entidades.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import servicos.Database;
import servicos.Dialog;
import servicos.Digest;

public class UserEditController {

    private Database db = new Database();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField edtTelefone;

    @FXML
    private TextField edtRenda;

    @FXML
    private TextField edtNovaSenha;

    @FXML
    private TextField edtLog;

    @FXML
    private TextField edtNumero;

    @FXML
    private TextField edtBairro;

    @FXML
    private TextField edtCidade;

    @FXML
    private TextField edtEstado;

    @FXML
    private TextField edtPais;

    @FXML
    private Button edtBtnCancelar;

    @FXML
    private Button edtBtnSalvar;

    public boolean checkValue() {
        return edtTelefone.getText() != null && !edtTelefone.getText().isEmpty()
                && edtRenda.getText() != null && !edtRenda.getText().isEmpty()
                && edtLog.getText() != null && !edtLog.getText().isEmpty()
                && edtNumero.getText() != null && !edtNumero.getText().isEmpty()
                && edtBairro.getText() != null && !edtBairro.getText().isEmpty()
                && edtCidade.getText() != null && !edtCidade.getText().isEmpty()
                && edtEstado.getText() != null && !edtEstado.getText().isEmpty()
                && edtPais.getText() != null && !edtPais.getText().isEmpty();
    }

    public boolean newPassword() {
        return edtNovaSenha.getText() != null && !edtNovaSenha.getText().isEmpty();
    }

    @FXML
    void initialize() {
        assert edtTelefone != null : "fx:id=\"edtTelefone\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert edtRenda != null : "fx:id=\"edtRenda\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert edtNovaSenha != null : "fx:id=\"edtNovaSenha\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert edtLog != null : "fx:id=\"edtLog\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert edtNumero != null : "fx:id=\"edtNumero\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert edtBairro != null : "fx:id=\"edtBairro\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert edtCidade != null : "fx:id=\"edtCidade\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert edtEstado != null : "fx:id=\"edtEstado\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert edtPais != null : "fx:id=\"edtPais\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert edtBtnCancelar != null : "fx:id=\"edtBtnCancelar\" was not injected: check your FXML file 'UserEdit.fxml'.";
        assert edtBtnSalvar != null : "fx:id=\"edtBtnSalvar\" was not injected: check your FXML file 'UserEdit.fxml'.";

        // init
        edtTelefone.setText(LoginController.loggedIn.getTelefone());
        edtRenda.setText("" + LoginController.loggedIn.getRendaMensal());

        edtLog.setText(LoginController.loggedIn.getEndereco().getLogradouro());
        edtNumero.setText("" + LoginController.loggedIn.getEndereco().getNumero());
        edtBairro.setText(LoginController.loggedIn.getEndereco().getBairro());
        edtCidade.setText(LoginController.loggedIn.getEndereco().getCidade());
        edtEstado.setText(LoginController.loggedIn.getEndereco().getEstado());
        edtPais.setText(LoginController.loggedIn.getEndereco().getPais());

        edtBtnCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ((Button) t.getTarget()).getScene().getWindow().hide();
            }
        });

        edtBtnSalvar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                // Editar user

                if (checkValue()) {

                    if (newPassword()) {
                        LoginController.loggedIn.setSenha(Digest.stringToMD5(edtNovaSenha.getText()));
                    }

                    LoginController.loggedIn.setTelefone(edtTelefone.getText());
                    LoginController.loggedIn.setRendaMensal(Float.parseFloat(edtRenda.getText()));

                    // Endereco
                    LoginController.loggedIn.getEndereco().setLogradouro(edtLog.getText());
                    LoginController.loggedIn.getEndereco().setBairro(edtBairro.getText());
                    LoginController.loggedIn.getEndereco().setCidade(edtCidade.getText());
                    LoginController.loggedIn.getEndereco().setEstado(edtEstado.getText());
                    LoginController.loggedIn.getEndereco().setPais(edtPais.getText());
                    LoginController.loggedIn.getEndereco().setEstado(edtEstado.getText());
                    LoginController.loggedIn.getEndereco().setNumero(Integer.parseInt(edtNumero.getText()));

                    try {
                        db.post(LoginController.loggedIn);
                        Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "Dados editados!");
                        ((Button) t.getTarget()).getScene().getWindow().hide();
                    } catch (IOException ex) {
                        Dialog.Dialog(Alert.AlertType.ERROR, "Erro", "Não foi possivel salvar o usuario");
                    }
                } else {
                    Dialog.Dialog(Alert.AlertType.WARNING, "Aviso", "Campos obragatorios estão faltando!");
                }
            }
        });
    }
}
