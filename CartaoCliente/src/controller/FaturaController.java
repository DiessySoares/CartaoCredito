package controller;

import entidades.Fatura;
import entidades.ItemFatura;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import servicos.Database;
import servicos.Dialog;

public class FaturaController {
    
    private Database db = new Database();
    
    private ArrayList<Fatura> faturas;

    private Fatura selectedFatura;
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label fatFechamento;

    @FXML
    private Label fatVencimento;

    @FXML
    private ListView<Fatura> fatFaturas;
    
    @FXML
    private ListView<ItemFatura> fatItem;

    @FXML
    private Label fatValor;

    @FXML
    private TextField fatValorOferecido;
    
    @FXML
    private Label fatValorPago;

    @FXML
    private Label fatValorDiferenca;

    @FXML
    void pagar(ActionEvent event) throws IOException {
        if(fatValorOferecido.getText() != null && !fatValorOferecido.getText().isEmpty()) {
            float falorOferecido = Float.parseFloat(fatValorOferecido.getText());
            
            if(falorOferecido == selectedFatura.getValorPendente()) {
                selectedFatura.setValorPago(falorOferecido);
                
                LoginController.loggedIn.getCartaoFisico().pushFaturaEdit(selectedFatura);
                db.post(LoginController.loggedIn);
                
                Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "Fatura paga!");
                fatFaturas.getItems().clear();
                dataFetch();
            } else {
                Dialog.Dialog(Alert.AlertType.WARNING, "Aviso", "O valor foi inserido não correspende ao valor pendente!");
        }
        } else {
            Dialog.Dialog(Alert.AlertType.WARNING, "Aviso", "Nenhum valor foi inserido!");
        }
        
    }

    @FXML
    void initialize() {
        assert fatFechamento != null : "fx:id=\"fatFechamento\" was not injected: check your FXML file 'Fatura.fxml'.";
        assert fatVencimento != null : "fx:id=\"fatVencimento\" was not injected: check your FXML file 'Fatura.fxml'.";
        assert fatFaturas != null : "fx:id=\"fatItens\" was not injected: check your FXML file 'Fatura.fxml'.";
        assert fatValor != null : "fx:id=\"fatValor\" was not injected: check your FXML file 'Fatura.fxml'.";
        assert fatValorOferecido != null : "fx:id=\"fatValorOferecido\" was not injected: check your FXML file 'Fatura.fxml'.";
        
        fatFaturas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Fatura>() {
            @Override
            public void changed(ObservableValue<? extends Fatura> observable, Fatura oldValue, Fatura newValue) {
                if (newValue != null) {
                    selectedFatura = newValue;
                    fetch();
                }
            }
        });
        dataFetch();
    }
    
    private void dataFetch(){
        if(LoginController.loggedIn.getCartaoFisico() != null) {
            ArrayList<Fatura> faturas = new ArrayList<>(); 
            faturas = LoginController.loggedIn.getCartaoFisico().getFaturas();           
            if(faturas != null) {
                for (int i = 0; i < 36; i++) {
                    if(faturas.get(i).getValorFatura() > 0) {
                        fatFaturas.getItems().add(faturas.get(i));
                    }   
                }
            }
        }
    }
    
    private void fetch() {
        fatFechamento.setText("Data de fechamento: " + new SimpleDateFormat("dd-MM-yyyy").format(selectedFatura.getDataFechamento()));
        fatVencimento.setText("Data de vencimento: " + new SimpleDateFormat("dd-MM-yyyy").format(selectedFatura.getDataVencimento()));
        
        fatValor.setText("Valor :" + selectedFatura.getValorFatura());
        
        fatValorPago.setText("Valor pago: " + selectedFatura.getValorPago());
        fatValorDiferenca.setText("Valor pendente: " + selectedFatura.getValorPendente());
       
        fatItem.getItems().clear();
                
        if(selectedFatura.getItens() != null){
            fatItem.getItems().addAll(selectedFatura.getItens());
        }
        
    }
    
    
    
}
