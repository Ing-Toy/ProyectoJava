package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class InitialPageController {

    @FXML
    void enterGame1p(ActionEvent event) {
        MainWindow.app.setScene("/client/OneGamePage.fxml");
    }

    @FXML
    void enterGame2p(ActionEvent event) {
        MainWindow.app.setScene("/client/TwoGamePage.fxml");
    }

    @FXML
    void enterGameOnline(ActionEvent event) {
        MainWindow.app.setScene("/client/OnlineGameView.fxml");
    }

    @FXML
    void quitGame(ActionEvent event) {
        System.exit(0);
    }

}
