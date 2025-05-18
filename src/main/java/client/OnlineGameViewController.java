package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OnlineGameViewController {

    @FXML
    private Button btnBackToMenu;

    @FXML
    private Button btnHit;

    @FXML
    private Button btnReplay;

    @FXML
    private Button btnStand;

    @FXML
    private HBox hboxBackToMenu;

    @FXML
    private HBox hboxControls;

    @FXML
    private HBox hboxDealer;

    @FXML
    private HBox hboxPlayer;

    @FXML
    private HBox hboxPlayer2;

    @FXML
    private HBox hboxPlayers;

    @FXML
    private HBox hboxSpacer;

    @FXML
    private Label lblDealer;

    @FXML
    private Label lblPlayer1;

    @FXML
    private Label lblPlayer2;

    @FXML
    private Label lblPoints;

    @FXML
    private Label lblPoints2;

    @FXML
    private Label spacer;

    @FXML
    private VBox vboxPlayers;

    @FXML
    void backToMenu(ActionEvent event) {
        MainWindow.app.setScene("/client/InitialPage.fxml");
    }

    @FXML
    void onHit(ActionEvent event) {

    }

    @FXML
    void onReplay(ActionEvent event) {

    }

    @FXML
    void onStand(ActionEvent event) {

    }
}