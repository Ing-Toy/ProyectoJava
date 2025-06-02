/*
Esta página es el menú inicial con el que se puede elegir el tipo de juego
 */

package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

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
        MainWindow.app.setScene("/client/PlaceBetsScreen.fxml");
    }

    @FXML
    void enterGameOnline(ActionEvent event) {
        PlayerSession.multiplayermode =true;
        PlayerSession.ConectarMultijugador();
        if (!PlayerSession.conectado){
            System.out.println("No se pudo conectar a multijugador.");
            MainWindow.app.setScene("/client/InitialPage.fxml");
        }
        System.out.println("Entrando a modo multijugador");

        try {
            String[] comando = PlayerSession.recibircomando();
            if (comando[0].equalsIgnoreCase("setAsiento")){
                PlayerSession.PlayerSeat = Integer.parseInt(comando[1]);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        MainWindow.app.setScene("/client/PlaceBetsScreen.fxml");
    }

    @FXML
    void quitGame(ActionEvent event) {
        System.exit(0);
    }

    public void initialize() {

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
