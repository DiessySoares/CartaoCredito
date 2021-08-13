package cartaoempresa;

import controller.HomeController;
import entidades.CartaoCredito;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CartaoEmpresa extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        HomeController.root = FXMLLoader.load(getClass().getResource("/telas/Home.fxml"));
        primaryStage.setScene(new Scene(HomeController.root));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Home");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
