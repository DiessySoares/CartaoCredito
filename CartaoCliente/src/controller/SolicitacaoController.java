package controller;

import entidades.Endereco;
import entidades.Usuario;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import servicos.Database;
import servicos.Dialog;
import servicos.Digest;

public class SolicitacaoController {

    private Database db = new Database();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField solNome;

    @FXML
    private TextField solCPF;

    @FXML
    private TextField solSenhaAplicativo;

    @FXML
    private TextField solSenhaCartao;

    @FXML
    private TextField solTelefone;

    @FXML
    private TextField solRenda;

    @FXML
    private TextField solLorgradouro;

    @FXML
    private TextField solNumero;

    @FXML
    private TextField solBairro;

    @FXML
    private TextField solCidade;

    @FXML
    private TextField solEstado;

    @FXML
    private TextField solPais;

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        Parent cadastro = FXMLLoader.load(getClass().getResource("/telas/Login.fxml"));
        Scene cadastroScene = new Scene(cadastro);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(cadastroScene);
        window.show();
    }

    @FXML
    void confirmar(ActionEvent event) throws IOException {
        // TODO Verificar campos ----------------------

        if (checkValues()) {
            Usuario user = new Usuario();
            user.setCPF(solCPF.getText());

            if (db.checkUserSave(solCPF.getText())) {
                Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Um usuario com esse cpf já existe!");
                return;
            }

            user.setNome(solNome.getText());
            user.setSenha(Digest.stringToMD5(solSenhaAplicativo.getText()));
            user.setSenhaCartao(solSenhaCartao.getText());
            user.setTelefone(solTelefone.getText());
            user.setRendaMensal(Integer.parseInt(solRenda.getText()));

            Endereco edr = new Endereco();
            edr.setBairro(solBairro.getText());
            edr.setCidade(solCidade.getText());
            edr.setEstado(solEstado.getText());
            edr.setNumero(Integer.parseInt(solNumero.getText()));
            edr.setLogradouro(solLorgradouro.getText());
            edr.setPais(solPais.getText());

            user.setEndereco(edr);
            user.setStatus(Usuario.ContaStatus.PENDENTE);

            db.post(user);

            Dialog.Dialog(Alert.AlertType.INFORMATION, "Registrado", "Sua conta foi enviada para analise!");

            Parent cadastro = FXMLLoader.load(getClass().getResource("/telas/Login.fxml"));
            Scene cadastroScene = new Scene(cadastro);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(cadastroScene);
            window.show();
        } else {
            Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Faltam informações!");
        }
    }

    @FXML
    void initialize() {
        assert solNome != null : "fx:id=\"solNome\" was not injected: check your FXML file 'Solicitacao.fxml'.";
        assert solCPF != null : "fx:id=\"solCPF\" was not injected: check your FXML file 'Solicitacao.fxml'.";
        assert solSenhaAplicativo != null : "fx:id=\"solSenhaAplicativo\" was not injected: check your FXML file 'Solicitacao.fxml'.";
        assert solSenhaCartao != null : "fx:id=\"solSenhaCartao\" was not injected: check your FXML file 'Solicitacao.fxml'.";
        assert solTelefone != null : "fx:id=\"solTelefone\" was not injected: check your FXML file 'Solicitacao.fxml'.";
        assert solRenda != null : "fx:id=\"solRenda\" was not injected: check your FXML file 'Solicitacao.fxml'.";
        assert solLorgradouro != null : "fx:id=\"solLorgradouro\" was not injected: check your FXML file 'Solicitacao.fxml'.";
        assert solNumero != null : "fx:id=\"solNumero\" was not injected: check your FXML file 'Solicitacao.fxml'.";
        assert solBairro != null : "fx:id=\"solBairro\" was not injected: check your FXML file 'Solicitacao.fxml'.";
        assert solCidade != null : "fx:id=\"solCidade\" was not injected: check your FXML file 'Solicitacao.fxml'.";
        assert solEstado != null : "fx:id=\"solEstado\" was not injected: check your FXML file 'Solicitacao.fxml'.";
        assert solPais != null : "fx:id=\"solPais\" was not injected: check your FXML file 'Solicitacao.fxml'.";

        typeDef();
    }

    public boolean checkValues() {
        return solNome.getText() != null && !solNome.getText().isEmpty()
                && solCPF.getText() != null && !solCPF.getText().isEmpty()
                && solSenhaAplicativo.getText() != null && !solSenhaAplicativo.getText().isEmpty()
                && solSenhaCartao.getText() != null && !solSenhaCartao.getText().isEmpty()
                && solTelefone.getText() != null && !solTelefone.getText().isEmpty()
                && solRenda.getText() != null && !solRenda.getText().isEmpty()
                && solLorgradouro.getText() != null && !solLorgradouro.getText().isEmpty()
                && solNumero.getText() != null && !solNumero.getText().isEmpty()
                && solBairro.getText() != null && !solBairro.getText().isEmpty()
                && solCidade.getText() != null && !solCidade.getText().isEmpty()
                && solEstado.getText() != null && !solEstado.getText().isEmpty()
                && solPais.getText() != null && !solPais.getText().isEmpty();
    }

    public void typeDef() {
        solTelefone.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    solTelefone.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        solRenda.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    solRenda.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        solNumero.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    solNumero.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        solSenhaCartao.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Apenas numeros
                if (!newValue.matches("\\d*")) {
                    solSenhaCartao.setText(newValue.replaceAll("[^\\d]", ""));
                }
                // 4 numeros
                if (solSenhaCartao.getText().length() > 4) {
                    String s = solSenhaCartao.getText().substring(0, 4);
                    solSenhaCartao.setText(s);
                }
            }
        });

    }

}
