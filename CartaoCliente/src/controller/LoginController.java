package controller;

import entidades.Usuario;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import servicos.Database;
import servicos.Dialog;
import servicos.Digest;

public class LoginController {

    private Database db = new Database();
    public static Usuario loggedIn = null;
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField logCPF;

    @FXML
    private PasswordField logSenha;

    @FXML
    void cadastrar(ActionEvent event) throws IOException {
        Parent cadastro = FXMLLoader.load(getClass().getResource("/telas/Solicitacao.fxml"));
        Scene cadastroScene  = new Scene(cadastro);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(cadastroScene);
        window.setTitle("Solicitação");
        window.show();
    }

    @FXML
    void login(ActionEvent event) throws IOException, FileNotFoundException, ClassNotFoundException {
        LoginController.loggedIn = db.getByCpf(logCPF.getText());
        if(LoginController.loggedIn != null) { // ok
            if(Digest.stringMD5Compare(logSenha.getText(), LoginController.loggedIn.getSenha())) {
                switch(LoginController.loggedIn.getStatus()) {
                    
                    case ACEITA:
                       Parent cadastro = FXMLLoader.load(getClass().getResource("/telas/Home.fxml"));
                        Scene cadastroScene  = new Scene(cadastro);
                        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                        window.setScene(cadastroScene);
                        window.setTitle("Home");
                        window.show();
                        break;
                    case PENDENTE:
                        Dialog.Dialog(Alert.AlertType.INFORMATION, "Informação", "Sua solicitação esta em analise!");
                        break;

                    case REJEITADA:
                        Dialog.Dialog(Alert.AlertType.WARNING, "Aviso", "Sua solicitação foi rejeitada");
                        break;    
                }
                 
            } else {
                Dialog.Dialog(Alert.AlertType.WARNING, "Aviso", "senha incorreta!");
            }
        } else {
            Dialog.Dialog(Alert.AlertType.WARNING, "Aviso", "Usuario nao foi encontrado!");
        }
    }   

    @FXML
    void initialize() {
        assert logCPF != null : "fx:id=\"logCPF\" was not injected: check your FXML file 'login.fxml'.";
        assert logSenha != null : "fx:id=\"logSenha\" was not injected: check your FXML file 'login.fxml'.";

    }
}