package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class OnlineGameViewController {

    private ClientConnection connection;

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
    private HBox hboxCardsPlayer1;

    @FXML
    private HBox hboxCardsPlayer2;

    @FXML
    private HBox hboxCardsPlayer3;

    @FXML
    private HBox hboxCardsPlayer4;

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
    private VBox vboxPlayers;

    @FXML
    public void initialize() {
        try {
            connection = new ClientConnection("localhost", 12345);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection.send("JOIN_GAME");

        Thread listenerThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = connection.receive()) != null) {
                    final String serverMsg = msg;
                    javafx.application.Platform.runLater(() -> handleServerMessage(serverMsg));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    @FXML
    void backToMenu(ActionEvent event) {
        try {
            if (connection != null) {
                connection.send("EXIT");
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainWindow.app.setScene("/client/InitialPage.fxml");
    }

    @FXML
    void onHit(ActionEvent event) {
        connection.send("HIT");
    }

    @FXML
    void onStand(ActionEvent event) {
        connection.send("STAND");
    }

    @FXML
    void onReplay(ActionEvent event) {
        connection.send("REPLAY");
    }

    private void handleServerMessage(String msg) {
        if (msg.startsWith("CARD")) {
            String[] parts = msg.split(" "); // ["CARD", "P1", "AH"]
            String playerId = parts[1];
            String card = parts[2];

            Image img = card.equals("HIDDEN") ?
                    new Image(getClass().getResource("/images/HIDDEN.png").toExternalForm()) :
                    new Image(getClass().getResource("/images/" + card + ".png").toExternalForm());

            ImageView imgv = new ImageView(img);
            imgv.setFitWidth(80);
            imgv.setPreserveRatio(true);

            if (playerId.equals("P1")) {
                hboxCardsPlayer1.getChildren().add(imgv);
            } else if (playerId.equals("P2")) {
                hboxCardsPlayer2.getChildren().add(imgv);
            } else if (playerId.equals("P3")) {
                hboxCardsPlayer3.getChildren().add(imgv);
            } else if (playerId.equals("P4")) {
                hboxCardsPlayer4.getChildren().add(imgv);
            }
        } else if (msg.startsWith("NEW_ROUND")) {
            hboxCardsPlayer1.getChildren().clear();
            hboxCardsPlayer2.getChildren().clear();
            hboxCardsPlayer3.getChildren().clear();
            hboxCardsPlayer4.getChildren().clear();

            lblPointsPlayer1.setText("");
            lblPointsPlayer2.setText("");
            lblPointsPlayer3.setText("");
            lblPointsPlayer4.setText("");

            lblPlayer1.setText("");
            lblPlayer2.setText("");
            lblPlayer3.setText("");
            lblPlayer4.setText("");
            lblDealer.setText("");

            btnHit.setDisable(true);
            btnStand.setDisable(true);
        }
    }
}