/*
Esta página es el menú inicial con el que se puede elegir el tipo de juego
 */

package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class InitialPageController {

    @FXML
    private Button btnMinus;

    @FXML
    private Button btnPlus;

    @FXML
    private Label lblBet;

    @FXML
    private Label lblChips;

    @FXML
    void enterLocalOptions(ActionEvent event) {
        MainWindow.app.setScene("/client/LocalOptionsPage.fxml");
    }

    @FXML
    void enterGameOnline(ActionEvent event) {
        MainWindow.app.setScene("/client/OnlineGameView.fxml");
    }

    @FXML
    void quitGame(ActionEvent event) {
        System.exit(0);
    }

    public void initialize() {
        lblChips.setText("Chips: " + PlayerSession.chips);
        lblBet.setText("Bet: " + PlayerSession.bet);
    }

    @FXML
    void onMinus(ActionEvent event) {
        if (PlayerSession.bet > 50) {
            PlayerSession.bet -= 50;
            lblBet.setText("Bet: " + PlayerSession.bet);
        }
    }

    @FXML
    void onPlus(ActionEvent event) {
        if (PlayerSession.chips > PlayerSession.bet) {
            PlayerSession.bet += 50;
            lblBet.setText("Bet: " + PlayerSession.bet);
        }
    }
}
