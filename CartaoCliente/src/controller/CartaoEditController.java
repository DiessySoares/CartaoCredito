package controller;

import entidades.CartaoCredito;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import servicos.Database;
import servicos.Dialog;
import servicos.Digest;

public class CartaoEditController {
    
    private Database db = new Database();
    
    private CartaoCredito cartao;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField edtSenhaCartao;

    @FXML
    private TextField edtDiaVencimento;

    @FXML
    private Button cancelar;

    @FXML
    private Button salvar;

    public boolean checkValue() {
        return edtDiaVencimento.getText() != null && !edtDiaVencimento.getText().isEmpty() && Integer.parseInt(edtDiaVencimento.getText()) > 0 && Integer.parseInt(edtDiaVencimento.getText()) < 29;
    }

    public boolean newPassword() {
        return edtSenhaCartao.getText() != null && !edtSenhaCartao.getText().isEmpty();
    }
    
    private Date getFechamento(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -7);
        return cal.getTime();
    }
    
    private Date trim(Date date) {
        date = addOneMonth(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }


    private static Date addOneMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    private Date getVencimento(Date date, int diaFaturaVencimento) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_MONTH, diaFaturaVencimento);
        return calendar.getTime();
    }
    
    public void reallocFaturas() throws IOException {
        if(cartao.getFaturas()  != null) {

            Date dateNow = new Date();
            Date dateFatura;

            // Ex: Editando o vencimento e fechamento de junho (apenas as proximas faturas mudam)
            dateNow = addOneMonth(dateNow);     //  ex: junho 

            dateNow = trim(dateNow);

            for (int i = 0; i < 36; i++) {
                dateFatura = trim(cartao.getFaturas().get(i).getDataVencimento());
                
                if(dateFatura.after(dateNow)){
                    
                    dateFatura = cartao.getFaturas().get(i).getDataVencimento();
                    dateFatura = getVencimento(dateFatura, Integer.parseInt(edtDiaVencimento.getText()));
                            
                    cartao.getFaturas().get(i).setDataVencimento(dateFatura);
                    
                    dateFatura = getFechamento(dateFatura);
                    
                    cartao.getFaturas().get(i).setDataFechamento(dateFatura);
                }
            }
            LoginController.loggedIn.pushEditedCartao(cartao);
            
            Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "Dia de vencimento alterado!");
        }
    }

    @FXML
    void initialize() {
        assert edtSenhaCartao != null : "fx:id=\"edtSenhaCartao\" was not injected: check your FXML file 'CartaoEdit.fxml'.";
        assert edtDiaVencimento != null : "fx:id=\"edtDiaVencimento\" was not injected: check your FXML file 'CartaoEdit.fxml'.";
        assert cancelar != null : "fx:id=\"cancelar\" was not injected: check your FXML file 'CartaoEdit.fxml'.";
        assert salvar != null : "fx:id=\"salvar\" was not injected: check your FXML file 'CartaoEdit.fxml'.";
        
        cartao = LoginController.loggedIn.getCartaoFisico();
        
        edtDiaVencimento.setText("" + cartao.getDiaVencimento());
        
        cancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ((Button) t.getTarget()).getScene().getWindow().hide();
            }
        });
        
        salvar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if(checkValue()) {
                    if(newPassword()) {
                        cartao.setSenha(Digest.stringToMD5(edtSenhaCartao.getText()));
                    }
                    cartao.setDiaVencimento(Integer.parseInt(edtDiaVencimento.getText()));
                    
                    LoginController.loggedIn.pushEditedCartao(cartao);
                    
                    try {
                        reallocFaturas();
                        db.post(LoginController.loggedIn);
                        Dialog.Dialog(Alert.AlertType.INFORMATION, "Tudo certo!", "As informações foram salvas");                        
                        ((Button) t.getTarget()).getScene().getWindow().hide();          
                    } catch (IOException ex) {
                        Dialog.Dialog(Alert.AlertType.ERROR, "Error", "Não foi possivel salvar as novas informações");
                    }
                } else {
                    Dialog.Dialog(Alert.AlertType.WARNING, "Aviso", "Os valores informados são invalidos!");
                }
                
            }
        });
    }
}
