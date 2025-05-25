package client;

import blackjack.Card;
import blackjack.GameEngine;
import blackjack.Hand;
import blackjack.House;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import static client.PlayerSession.bet;

public class OneGamePageController {

    @FXML
    private Label lblBet;

    @FXML
    private Label lblChips;

    @FXML
    private Label lblPlayer1;

    @FXML
    private Label lblPoints;

    @FXML
    private Label lblDealer;

    @FXML
    private Button btnDouble;

    @FXML
    private Button btnHit;

    @FXML
    private Button btnStand;

    @FXML
    private HBox hboxPlayer;

    @FXML
    private HBox hboxDealer;

    @FXML
    private Button btnReplay;

    private GameEngine engine = new GameEngine();


    @FXML
    void backToMenu() {
        MainWindow.app.setScene("/client/InitialPage.fxml");
    }

    @FXML
    public void initialize() {
        PlaceBetsScreenController controlador = MainWindow.app.setSceneWithController("/client/PlaceBetsScreenController.fxml");
        startPlaying();
        lblPoints.setText("" + engine.getJugador().TotalSum());
    }

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

        Image hid = new Image(getClass().getResource("/images/HIDDEN.png").toExternalForm());
        ImageView hiddenCard = new ImageView(hid);
        hiddenCard.setFitWidth(80);
        hiddenCard.setPreserveRatio(true);
        destination.getChildren().add(hiddenCard);
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


    @FXML
    void onHit() {
        btnDouble.setDisable(true);
        engine.hitJugador();
        showHand(engine.getJugador(), hboxPlayer);

        int total = engine.getJugador().TotalSum();

        if (total == 21) { // Que tenga 21 no significa que gane inmediatamente
            showHand(engine.getCasa(), hboxDealer);
            onStand();
        }

        lblPoints.setText("" + engine.getJugador().TotalSum());
        if (!engine.getJugador().IsPlaying) {
            btnHit.setDisable(true);
            btnStand.setDisable(true);
            btnReplay.setDisable(false);

            onStand();
        }
    }

    @FXML
    void onStand() {
        lblPoints.setText("");
        btnHit.setDisable(true);
        btnStand.setDisable(true);
        btnDouble.setDisable(true);
        btnReplay.setDisable(false);

        House dealer = engine.getCasa();

        if (!engine.getJugador().IsPlaying){
            showHand(dealer, hboxDealer);
            String result = GameEngine.EvalHand(engine.getJugador(), engine.getCasa());
            lblPlayer1.setText(PlayerSession.playerName + " " + result + " with " + engine.getJugador().TotalSum() + " points");
            lblDealer.setText("Dealer has " + engine.getCasa().TotalSum() + " points");
            return;
        }

        while (dealer.IsPlaying && dealer.Plays()) {
            dealer.addCard(engine.getDeck().dealCard());
        }

        showHand(dealer, hboxDealer);

        String result = GameEngine.EvalHand(engine.getJugador(), engine.getCasa());
        if (result.contains("W")) {
            PlayerSession.chips += bet * 2;
        } else if (result.contains("D")) {
            PlayerSession.chips += bet;
        }

        lblPlayer1.setText(PlayerSession.playerName + " " + result + " with " + engine.getJugador().TotalSum() + " points");
        lblDealer.setText("Dealer has " + engine.getCasa().TotalSum() + " points");
    }

    @FXML
    void onReplay() {
        //Aquí se añade la pantalla para poner apuestas
        startPlaying();
    }

    @FXML
    void onDouble(ActionEvent event) {

        PlayerSession.chips -= bet;
        engine.hitJugador();
        showHand(engine.getJugador(), hboxPlayer);

        lblPoints.setText("" + engine.getJugador().TotalSum());
        if (!engine.getJugador().IsPlaying) {
            btnHit.setDisable(true);
            btnStand.setDisable(true);
            btnReplay.setDisable(false);

            doubleGame();
        }
        doubleGame();
    }

    private void startPlaying() {

        engine = new GameEngine();
        engine.excecuteGame(1);

        showHand(engine.getJugador(), hboxPlayer);
        hiddenDealer(engine.getCasa(), hboxDealer);

        lblPlayer1.setText(PlayerSession.playerName);
        lblDealer.setText("Dealer");
        lblPoints.setText("" + engine.getJugador().TotalSum());

        lblChips.setText("Chips: " + PlayerSession.chips);
        lblBet.setText("Bet: " + bet);

        if (engine.gameShouldEndImmediately()) {
            showHand(engine.getCasa(), hboxDealer);
            String result = GameEngine.EvalHand(engine.getJugador(), engine.getCasa());

            if (engine.getJugador().IsBlackjack()) {
                lblPlayer1.setText("Blackjack!");
                PlayerSession.chips += bet * 3;
            } else {
                lblPlayer1.setText(PlayerSession.playerName + " " + result + " with " + engine.getJugador().TotalSum() + " points");
            }

            if (engine.getCasa().IsBlackjack()) {
                lblDealer.setText("Blackjack!");
            } else {
                lblDealer.setText("Dealer has " + engine.getCasa().TotalSum() + " points");
            }

            btnHit.setDisable(true);
            btnStand.setDisable(true);
            btnDouble.setDisable(true);
            btnReplay.setDisable(false);

            return;
        }

        btnHit.setDisable(false);
        btnStand.setDisable(false);
        btnDouble.setDisable(false);
        btnReplay.setDisable(true);

        if (PlayerSession.chips < bet) {
            btnHit.setDisable(true);
            btnStand.setDisable(true);
            btnReplay.setDisable(true);
            btnDouble.setDisable(true);
            return;
        } else if (PlayerSession.chips < bet * 2) {
            btnDouble.setDisable(true);
        }

        PlayerSession.chips -= bet;

    }

    void doubleGame() {

        lblPoints.setText("");
        btnHit.setDisable(true);
        btnStand.setDisable(true);
        btnDouble.setDisable(true);
        btnReplay.setDisable(false);

        House dealer = engine.getCasa();

        while (dealer.IsPlaying && dealer.Plays()) {
            dealer.addCard(engine.getDeck().dealCard());
        }

        showHand(dealer, hboxDealer);



        String result = GameEngine.EvalHand(engine.getJugador(), engine.getCasa());
        if (result.contains("W")) {
            PlayerSession.chips += bet * 4;
        } else if (result.contains("D")) {
            PlayerSession.chips += bet * 2;
        }

        lblPlayer1.setText(PlayerSession.playerName + " " + result + " with " + engine.getJugador().TotalSum() + " points");
        lblDealer.setText("Dealer has " + engine.getCasa().TotalSum() + " points");
    }
}