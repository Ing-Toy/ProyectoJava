package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NamePageController {

    @FXML
    private TextField txtName;

    @FXML
    private Label lblError;

    @FXML
    void sendInfo(ActionEvent event) {
        String name = txtName.getText().trim();
        if (txtName.getText().isBlank() || txtName.getText().isEmpty()) {
            lblError.setText("Please put a valid name");
        } else {
            PlayerSession.playerName = name;
            MainWindow.app.setScene("/client/InitialPage.fxml");
        }
    }
}