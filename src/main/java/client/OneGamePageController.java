package client;

import blackjack.Card;
import blackjack.GameEngine;
import blackjack.Hand;
import blackjack.House;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class OneGamePageController {

    @FXML
    private Label lblPlayer1;

    @FXML
    private Label lblPoints;

    @FXML
    private Label lblDealer;

    @FXML
    private Button btnHit;

    @FXML
    private Button btnStand;

    @FXML
    private HBox hboxPlayer;

    @FXML
    private HBox hboxDealer;

    private GameEngine engine = new GameEngine();


    @FXML
    void backToMenu() {
        MainWindow.app.setScene("/client/InitialPage.fxml");
    }

    @FXML
    public void initialize() {
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
        engine.hitJugador();
        showHand(engine.getJugador(), hboxPlayer);

        int total = engine.getJugador().TotalSum();

        if (total == 21) {
            lblPoints.setText("Blackjack!");
            showHand(engine.getCasa(), hboxDealer);
            String result = GameEngine.EvalHand(engine.getJugador(), engine.getCasa());
            lblPlayer1.setText("Player 1 " + result + " with 21 points");
            lblDealer.setText("Dealer has " + engine.getCasa().TotalSum() + " points");
            btnHit.setDisable(true);
            btnStand.setDisable(true);
            return;
        }

        lblPoints.setText("" + engine.getJugador().TotalSum());
        if (!engine.getJugador().IsPlaying) {
            btnHit.setDisable(true);
            btnStand.setDisable(true);

            onStand();
        }
    }

    @FXML
    void onStand() {
        lblPoints.setText("");
        btnHit.setDisable(true);
        btnStand.setDisable(true);

        House dealer = engine.getCasa();

        while (dealer.IsPlaying && dealer.Plays()) {
            dealer.addCard(engine.getDeck().dealCard());
        }

        if (dealer.TotalSum() == 21) {
            showHand(dealer, hboxDealer);
            lblDealer.setText("Blackjack!");
            String result = GameEngine.EvalHand(engine.getJugador(), dealer);
            lblPlayer1.setText("Player 1 " + result + " with " + engine.getJugador().TotalSum() + " points");
            return;
        }

        showHand(dealer, hboxDealer);



        String result = GameEngine.EvalHand(engine.getJugador(), engine.getCasa());
        lblPlayer1.setText("Player 1 " + result + " with " + engine.getJugador().TotalSum() + " points");
        lblDealer.setText("Dealer has " + engine.getCasa().TotalSum() + " points");
    }

    @FXML
    void onReplay() {
        startPlaying();
    }

    private void startPlaying() {

        engine = new GameEngine();
        engine.excecuteGame(1);

        showHand(engine.getJugador(), hboxPlayer);
        hiddenDealer(engine.getCasa(), hboxDealer);

        lblPlayer1.setText("Player 1");
        lblDealer.setText("Dealer");
        lblPoints.setText("" + engine.getJugador().TotalSum());

        if (engine.gameShouldEndImmediately()) {
            showHand(engine.getCasa(), hboxDealer);
            String result = GameEngine.EvalHand(engine.getJugador(), engine.getCasa());

            if (engine.getJugador().IsBlackjack()) {
                lblPlayer1.setText("Blackjack!");
            } else {
                lblPlayer1.setText("Player 1 " + result + " with " + engine.getJugador().TotalSum() + " points");
            }

            if (engine.getCasa().IsBlackjack()) {
                lblDealer.setText("Blackjack!");
            } else {
                lblDealer.setText("Dealer has " + engine.getCasa().TotalSum() + " points");
            }

            btnHit.setDisable(true);
            btnStand.setDisable(true);
            return;
        }

        btnHit.setDisable(false);
        btnStand.setDisable(false);
    }
}