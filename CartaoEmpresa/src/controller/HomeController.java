package controller;

import entidades.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.stage.Stage;
import servicos.Database;
import javafx.scene.Parent;
import javafx.stage.Modality;

public class HomeController {
    // TELA
    public static Parent root;

    private Database db = new Database();
    public ArrayList<Usuario> usuarios;
    public static Usuario selectedUser = new Usuario();
    
    private static final double BLUR_AMOUNT = 10;
    private static final Effect frostEffect =
        new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 3);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    

    @FXML
    private ListView<Usuario> gerListaSolicita;

    @FXML
    private ListView<Usuario> gerListaUsuario;

    @FXML
    private TextField gerPesquisaCPF;
    
    @FXML
    void comprar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/Compra.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Compra");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    void pesquisar(ActionEvent event) throws IOException {
        // refresh
        if(gerPesquisaCPF.getText() != null && !gerPesquisaCPF.getText().isEmpty()) {
            gerListaUsuario.getItems().clear();
            for (Usuario usuario : usuarios) { 
                if(usuario.getCPF().contains(gerPesquisaCPF.getText())) {
                    gerListaUsuario.getItems().add(usuario);
                }
            }
        } else { 
            fillLists();
        }
    }

    @FXML
    void sair(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
    
    @FXML
    void reloadData(ActionEvent event) throws IOException {
        fillLists();
    }

    @FXML
    void initialize() throws IOException {
        assert gerListaSolicita != null : "fx:id=\"gerListaSolicita\" was not injected: check your FXML file 'Home.fxml'.";
        assert gerListaUsuario != null : "fx:id=\"gerListaUsuario\" was not injected: check your FXML file 'Home.fxml'.";
        assert gerPesquisaCPF != null : "fx:id=\"gerPesquisaCPF\" was not injected: check your FXML file 'Home.fxml'.";

        this.listListenInit();

        fillLists();
    }

    public void listListenInit() {
        gerListaSolicita.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Usuario>() {
            @Override
            public void changed(ObservableValue<? extends Usuario> observable, Usuario oldValue, Usuario newValue) {
                if (newValue != null) {
                    HomeController.selectedUser = newValue;
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/UserEdit.fxml"));
                        Parent root = loader.load();
                        HomeController.root.setEffect(frostEffect);
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.setTitle("Editar usuario");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();
                        HomeController.root.setEffect(null);
                    } catch (Exception e) {

                    }
                }
            }
        });

        gerListaUsuario.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Usuario>() {
            @Override
            public void changed(ObservableValue<? extends Usuario> observable, Usuario oldValue, Usuario newValue) {
                if (newValue != null) {
                    HomeController.selectedUser = newValue;
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/UserEdit.fxml"));
                        Parent root = loader.load();
                        HomeController.root.setEffect(frostEffect);
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.setTitle("Editar usuario");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();
                        HomeController.root.setEffect(null);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }
    
    
    public void fillLists() throws IOException {
        gerListaUsuario.getItems().clear();
        gerListaSolicita.getItems().clear();

        usuarios = db.get();
        
        if(usuarios != null) {
            for (Usuario usuario : usuarios) {
                if (usuario.getStatus() == Usuario.ContaStatus.PENDENTE) {
                    gerListaSolicita.getItems().add(usuario);
                }
                gerListaUsuario.getItems().add(usuario);
            }
        }
    }
}
