package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import servicos.Database;
import servicos.Dialog;

public class HomeController {
    
    private Database db = new Database();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label hmeBemVindoMsg;

    @FXML
    private Button sair;

    @FXML
    private Label hmeNome;

    @FXML
    private Label hmeCPF;
    
    @FXML
    private Label hmeRenda;

    @FXML
    private Label hmeLimite;
    
    @FXML
    private Button hmeBtnEditarDados;

    @FXML
    private Button hmeBtnVisualizarCartao;

    @FXML
    private Button hmeBtnVisualizarFaturas;
    
    @FXML
    void sair(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
    
    
    @FXML
    void VisualizarCartao(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/Cartao.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Cartão");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        
        try {
            fetch();
        } catch ( IOException | ClassNotFoundException e) {
            Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Não foi possivel obter os dados!");
        }
        
    }

    @FXML
    void editarDados(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/UserEdit.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Editar dados");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        try {
            fetch();
        } catch ( IOException | ClassNotFoundException e) {
            Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Não foi possivel obter os dados!");
        }
    }

    @FXML
    void visualizarFaturas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/Fatura.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Visualizar faturas");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        
        try {
            fetch();
        } catch ( IOException | ClassNotFoundException e) {
            Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Não foi possivel obter os dados!");
        }
        
    }
    
    

    @FXML
    void initialize(){
        assert hmeBemVindoMsg != null : "fx:id=\"hmeBemVindoMsg\" was not injected: check your FXML file 'Home.fxml'.";
        assert sair != null : "fx:id=\"sair\" was not injected: check your FXML file 'Home.fxml'.";
        assert hmeNome != null : "fx:id=\"hmeNome\" was not injected: check your FXML file 'Home.fxml'.";
        assert hmeCPF != null : "fx:id=\"hmeCPF\" was not injected: check your FXML file 'Home.fxml'.";
        assert hmeLimite != null : "fx:id=\"hmeLimite\" was not injected: check your FXML file 'Home.fxml'.";
        assert hmeBtnEditarDados != null : "fx:id=\"hmeBtnEditarDados\" was not injected: check your FXML file 'Home.fxml'.";
        assert hmeBtnVisualizarCartao != null : "fx:id=\"hmeBtnVisualizarCartao\" was not injected: check your FXML file 'Home.fxml'.";
        assert hmeBtnVisualizarFaturas != null : "fx:id=\"hmeBtnVisualizarFaturas\" was not injected: check your FXML file 'Home.fxml'.";
        assert hmeRenda != null : "fx:id=\"hmeRenda\" was not injected: check your FXML file 'Home.fxml'.";
        
        hmeBemVindoMsg.setText("Bem vindo, " + LoginController.loggedIn.getNome().replaceAll(" .*", ""));
        hmeNome.setText("Nome: " + LoginController.loggedIn.getNome());
        hmeCPF.setText("CPF: " +LoginController.loggedIn.getCPF());
        hmeRenda.setText("Renda: " + LoginController.loggedIn.getRendaMensal());
        
        try {
            fetch();
        } catch ( IOException | ClassNotFoundException e) {
            Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Não foi possivel obter os dados!");
        }
    }
    
    public void fetch() throws IOException, FileNotFoundException, ClassNotFoundException{
        LoginController.loggedIn = db.getByCpf(LoginController.loggedIn.getCPF());
        if(LoginController.loggedIn.getCartaoFisicoAtivo() != null) {
            hmeLimite.setText("Limite: " + String.valueOf(LoginController.loggedIn.getCartaoFisicoAtivo().getLimite()));
        } else {
            hmeLimite.setText("Limite: limite indisponivel" );
        }
    }
}
