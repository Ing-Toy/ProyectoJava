package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LocalOptionsPageController {

    @FXML
    void enterGame1p(ActionEvent event) {
        MainWindow.app.setScene("/client/OneGamePage.fxml");
    }

    @FXML
    void enterGameComs(ActionEvent event) {
        MainWindow.app.setScene("/client/PlaceBetsScreen.fxml");
    }

    @FXML
    void backToMenu() {
        MainWindow.app.setScene("/client/InitialPage.fxml");
    }
}
