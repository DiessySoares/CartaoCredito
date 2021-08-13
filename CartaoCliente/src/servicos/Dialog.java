package servicos;

import javafx.scene.control.Alert;

public  class Dialog {
    public static void Dialog(Alert.AlertType type, String title, String text) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}
