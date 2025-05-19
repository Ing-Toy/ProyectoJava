package client;

import blackjack.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static client.PlayerSession.*;

public class LocalMultiplayerController {

    GameEngine engine = new GameEngine();
    private int numPlayers = 1;

    @FXML
    private Label lblBetP1;

    @FXML
    private Label lblBetP2;

    @FXML
    private Label lblBetP3;

    @FXML
    private Label lblBetP4;

    @FXML
    private Label lblChipsP1;

    @FXML
    private Label lblChipsP2;

    @FXML
    private Label lblChipsP3;

    @FXML
    private Label lblChipsP4;

    @FXML
    private Button btnAddPlayer;

    @FXML
    private Button btnRemovePlayer;

    @FXML
    private Button btnBackToMenu;

    @FXML
    private Button btnHit;

    @FXML
    private Button btnReplay;

    @FXML
    private Button btnStand;

    @FXML
    private HBox hboxCardsPlayer1;

    @FXML
    private HBox hboxCardsPlayer2;

    @FXML
    private HBox hboxCardsPlayer3;

    @FXML
    private HBox hboxCardsPlayer4;

    @FXML
    private HBox hboxDealer;

    @FXML
    private HBox hboxGambleButtons;

    @FXML
    private HBox hboxMenuButtons;

    @FXML
    private HBox hboxPlayers;

    @FXML
    private Label lblDealer;

    @FXML
    private Label lblPlayer1;

    @FXML
    private Label lblPlayer2;

    @FXML
    private Label lblPlayer3;

    @FXML
    private Label lblPlayer4;

    @FXML
    private Label lblPointsPlayer1;

    @FXML
    private Label lblPointsPlayer2;

    @FXML
    private Label lblPointsPlayer3;

    @FXML
    private Label lblPointsPlayer4;

    @FXML
    private Label spacer;

    @FXML
    private VBox vboxContainerPlayer1;

    @FXML
    private VBox vboxContainerPlayer2;

    @FXML
    private VBox vboxContainerPlayer3;

    @FXML
    private VBox vboxContainerPlayer4;

    @FXML
    public void initialize() {
        vboxContainerPlayer2.setVisible(false);
        vboxContainerPlayer3.setVisible(false);
        vboxContainerPlayer4.setVisible(false);
        startGame();
    }

    @FXML
    void onHit(ActionEvent event) {
        engine.hitJugador();
        showHand(engine.getJugador(), hboxCardsPlayer1);

        int total = engine.getJugador().TotalSum();

        if (total == 21) {
            PlayerSession.chips += bet * 2;
            lblPointsPlayer1.setText("Blackjack!");
            showHand(engine.getCasa(), hboxDealer);
            String result = GameEngine.EvalHand(engine.getJugador(), engine.getCasa());
            lblPlayer1.setText(PlayerSession.playerName + " " + result + " with 21 points");
            lblDealer.setText("Dealer has " + engine.getCasa().TotalSum() + " points");
            btnHit.setDisable(true);
            btnStand.setDisable(true);
            return;
        }

        lblPointsPlayer1.setText("" + total);
        if (!engine.getJugador().IsPlaying) {
            btnHit.setDisable(true);
            btnStand.setDisable(true);
            onStand(null);
        }
    }

    @FXML
    void onStand(ActionEvent event) {

        lblPointsPlayer1.setText("");
        btnHit.setDisable(true);
        btnStand.setDisable(true);

        for (int i = 0; i < engine.getComputadoras().size(); i++) {
            Computer cpu = engine.getComputadoras().get(i);
            while (cpu.IsPlaying && cpu.Plays()) {
                cpu.addCard(engine.getDeck().dealCard());
            }
            showHand(cpu, getHboxForCPU(i + 1));
            lblPoints(i + 1).setText("" + cpu.TotalSum());
        }

        House dealer = engine.getCasa();

        while (dealer.IsPlaying && dealer.Plays()) {
            dealer.addCard(engine.getDeck().dealCard());
        }

        showHand(dealer, hboxDealer);

        String result = GameEngine.EvalHand(engine.getJugador(), dealer);
        if (result.contains("W")) {
            PlayerSession.chips += bet * 2;
        } else if (result.contains("D")) {
            PlayerSession.chips += bet;
        }

        lblPlayer1.setText(PlayerSession.playerName + " " + result + " with " + engine.getJugador().TotalSum() + " points");
        lblDealer.setText("Dealer has " + dealer.TotalSum() + " points");

        for (int i = 0; i < engine.getComputadoras().size(); i++) {
            String resultCpu = GameEngine.EvalHand(engine.getComputadoras().get(i), engine.getCasa());

            if (resultCpu.contains("W")) {
                PlayerSession.chipsCpu.set(i, PlayerSession.chipsCpu.get(i) + bet * 2);
            } else if (resultCpu.contains("D")) {
                PlayerSession.chipsCpu.set(i, PlayerSession.chipsCpu.get(i) + bet);
            }

            String cpuResult = GameEngine.EvalHand(engine.getComputadoras().get(i), dealer);
            lblPlayer(i + 1).setText("CPU " + (i + 1) + " " + cpuResult + " with " + engine.getComputadoras().get(i).TotalSum() + " points");
        }


    }

    @FXML
    void backToMenu(ActionEvent event) {
        MainWindow.app.setScene("/client/InitialPage.fxml");
    }

    @FXML
    void onReplay(ActionEvent event) {
        startGame();
    }

    @FXML
    void onAddPlayer(ActionEvent event) {
        if (!vboxContainerPlayer2.isVisible()) {
            vboxContainerPlayer2.setVisible(true);

            numPlayers++;
        } else if (!vboxContainerPlayer3.isVisible()) {
            vboxContainerPlayer3.setVisible(true);

            numPlayers++;
        } else if (!vboxContainerPlayer4.isVisible()) {
            vboxContainerPlayer4.setVisible(true);

            numPlayers++;
        }
        startGame();
    }

    @FXML
    void onRemovePlayer(ActionEvent event) {
        if (vboxContainerPlayer4.isVisible()) {
            vboxContainerPlayer4.setVisible(false);

            numPlayers--;
        } else if (vboxContainerPlayer3.isVisible()) {
            vboxContainerPlayer3.setVisible(false);

            numPlayers--;
        } else if (vboxContainerPlayer2.isVisible()) {
            vboxContainerPlayer2.setVisible(false);

            numPlayers--;
        }

        startGame();
    }

    private void startGame() {

        btnHit.setDisable(false);
        btnStand.setDisable(false);
        engine = new GameEngine();
        engine.excecuteGame(numPlayers);
        showHand(engine.getJugador(), hboxCardsPlayer1);
        lblPlayer1.setText(PlayerSession.playerName);
        lblPointsPlayer1.setText("" + engine.getJugador().TotalSum());

        if (PlayerSession.chips < bet) {
            btnHit.setDisable(true);
            btnStand.setDisable(true);
        }

        lblChipsP1.setText("Chips: " + PlayerSession.chips);
        lblBetP1.setText("Bet: " + bet);

        if (PlayerSession.chips >= bet) {
            PlayerSession.chips -= bet;
        } else {
            btnHit.setDisable(true);
            btnStand.setDisable(true);
        }


        for (int i = 1; i < numPlayers; i++) {

            if (PlayerSession.chipsCpu.get(i - 1) >= bet) {
                PlayerSession.chipsCpu.set(i - 1, PlayerSession.chipsCpu.get(i - 1) - bet);
            }

            showHand(engine.getComputadoras().get(i - 1), getHboxForCPU(i));
            lblPoints(i).setText("");
            lblPlayer(i).setText("CPU " + (i));
            lblChipsP(i).setText("Chips: " + PlayerSession.chipsCpu.get(i - 1));
            lblBetP(i).setText("Bet: " + bet);
        }

        hiddenDealer(engine.getCasa(), hboxDealer);
        lblDealer.setText("Dealer");
    }

    private void showHand(Hand hand, HBox destination) {
        destination.getChildren().clear();
        for (Card carta : hand.getHand()) {
            Image img = new Image(getClass().getResource("/images/" + carta.Face() + ".png").toExternalForm());
            ImageView imgv = new ImageView(img);
            imgv.setFitWidth(80);
            imgv.setPreserveRatio(true);
            destination.getChildren().add(imgv);
        }
    }

    /*
    private void hiddenHand(Hand hand, HBox destination) {
        destination.getChildren().clear();
        Image hiddenImg = new Image(getClass().getResource("/images/HIDDEN.png").toExternalForm());
        for (int i = 0; i < hand.getHand().size(); i++) {
            ImageView imgv = new ImageView(hiddenImg);
            imgv.setFitWidth(80);
            imgv.setPreserveRatio(true);
            destination.getChildren().add(imgv);
        }
    }
     */

    private void hiddenDealer(House house, HBox destination) {
        destination.getChildren().clear();

        if (house.getHand().size() > 1) {
            Card visible = house.getHand().get(1);
            Image img = new Image(getClass().getResource("/images/" + visible.Face() + ".png").toExternalForm());
            ImageView imgv = new ImageView(img);
            imgv.setFitWidth(80);
            imgv.setPreserveRatio(true);
            destination.getChildren().add(imgv);
        }

        Image hiddenImg = new Image(getClass().getResource("/images/HIDDEN.png").toExternalForm());
        ImageView hiddenCard = new ImageView(hiddenImg);
        hiddenCard.setFitWidth(80);
        hiddenCard.setPreserveRatio(true);
        destination.getChildren().add(hiddenCard);
    }

    private HBox getHboxForCPU(int i) {
        switch (i) {
            case 1:
                return hboxCardsPlayer2;
            case 2:
                return hboxCardsPlayer3;
            case 3:
                return hboxCardsPlayer4;
            default:
                return null;
        }
    }

    private Label lblPoints(int i) {
        switch (i) {
            case 0:
                return lblPointsPlayer1;
            case 1:
                return lblPointsPlayer2;
            case 2:
                return lblPointsPlayer3;
            case 3:
                return lblPointsPlayer4;
            default:
                return null;
        }
    }

    private Label lblPlayer(int i) {
        switch (i) {
            case 0:
                return lblPlayer1;
            case 1:
                return lblPlayer2;
            case 2:
                return lblPlayer3;
            case 3:
                return lblPlayer4;
            default:
                return null;
        }
    }

    private Label lblChipsP(int i) {
        switch (i) {
            case 1:
                return lblChipsP2;
            case 2:
                return lblChipsP3;
            case 3:
                return lblChipsP4;
            default:
                return null;
        }
    }

    private Label lblBetP(int i) {
        switch (i) {
            case 1:
                return lblBetP2;
            case 2:
                return lblBetP3;
            case 3:
                return lblBetP4;
            default:
                return null;
        }
    }
}