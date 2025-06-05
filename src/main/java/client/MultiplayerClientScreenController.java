package client;

import blackjack.*;
import com.sun.javafx.image.IntPixelGetter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import static client.PlaceBetsScreenController.cantidadcomputadoras;
import static client.PlayerSession.*;

public class MultiplayerClientScreenController {
    public static Player asiento1;
    public static Player asiento2;
    public static Player asiento3;
    public static Player asiento4;
    private House Casahand;
    //Platform.runLater(()->{});

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
    private Button btnBackToMenu;

    @FXML
    private Button btnHit;

    @FXML
    private Button btnDouble;

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
    private Label playerturn;

    @FXML
    public void initialize() {
        asiento1 = new Player();
        asiento2 = new Player();
        asiento3 = new Player();
        asiento4 = new Player();
        Casahand = new House();
        PlayerSession.mandarcomando("changebet:"+ bet);
        OcultarCarteras();
        btnReplay.setDisable(true);
        spacer.setText("You're: Seat "+ PlayerSeat);
        lblChipsP(PlayerSeat).setText("Chips: "+(chips));
        btnHit.setDisable(true);
        btnDouble.setDisable(true);
        btnStand.setDisable(true);
        btnBackToMenu.setDisable(true);
        playerturn.setText("Setting up Game.");
        // Ejecutar sesion
        Thread ServerListener = new Thread(()->{
            while(true){
                try{
                    Thread.sleep(100);
                    String[] comando = PlayerSession.recibircomando();
                    switch (comando[0]) {
                        case "nombres":
                            ActualizarNombres(comando[1]);
                            break;
                        case "apuestas":
                            ActualizarApuestas(comando[1]);
                            break;
                        case "actualizar":
                            Card carta = new Card(comando[3]);
                            switch (comando[1]) {
                                case "0":
                                    Casahand.addCard(carta);
                                    if (Casahand.getHand().size() == 2){
                                        hiddenDealer(Casahand,hboxDealer);
                                    } else {
                                        showHand(Casahand, hboxDealer);
                                    }
                                    break;
                                case "1":
                                    asiento1.addCard(carta);
                                    showHand(asiento1, hboxCardsPlayer1);
                                    break;
                                case "2":
                                    asiento2.addCard(carta);
                                    showHand(asiento2, hboxCardsPlayer2);
                                    break;
                                case "3":
                                    asiento3.addCard(carta);
                                    showHand(asiento3, hboxCardsPlayer3);
                                    break;
                                case "4":
                                    asiento4.addCard(carta);
                                    showHand(asiento4, hboxCardsPlayer4);
                                    break;
                            }
                            if (comando[1].equalsIgnoreCase(Integer.toString(PlayerSeat))){
                                Platform.runLater(()->{
                                    lblPoints(PlayerSeat).setText(String.valueOf(EncontrarJugador(Integer.parseInt(comando[1])).TotalSum()));
                                });
                            }
                            break;
                        case "turno":
                            if (Integer.parseInt(comando[1]) == PlayerSeat) {
                                Platform.runLater(()->{
                                    playerturn.setText("Your Turn!");
                                    lblPoints(PlayerSeat).setText(String.valueOf(EncontrarJugador(Integer.parseInt(comando[1])).TotalSum()));
                                });
                                Platform.runLater(()->{
                                    btnHit.setDisable(false);
                                    btnDouble.setDisable(false);
                                    btnStand.setDisable(false);
                                });
                            } else {
                                if (comando[1].equalsIgnoreCase("0")) {
                                    Platform.runLater(()-> {
                                        playerturn.setText("Dealers turn.");
                                    });
                                } else {
                                    Platform.runLater(()->{
                                        playerturn.setText("Seat " + comando[1] + " turn.");
                                    });
                                }
                                Platform.runLater(()-> {
                                    btnHit.setDisable(true);
                                    btnDouble.setDisable(true);
                                    btnStand.setDisable(true);
                                });
                            }
                            break;
                        case "gameresults":
                            ProcesarResultados(comando[1]);
                            return;
                        case "instantloss":
                            Platform.runLater(()-> {
                                btnHit.setDisable(true);
                                btnDouble.setDisable(true);
                                btnStand.setDisable(true);
                                lblPoints(PlayerSeat).setText(""+EncontrarJugador(PlayerSeat).TotalSum());
                                playerturn.setText("You Lost!");
                            });
                            break;
                    }
                } catch (SocketException socket){
                    System.out.println("Player Disconnected.");
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            });
        ServerListener.start();
    }

    @FXML
    void onHit(ActionEvent event) {
        PlayerSession.mandarcomando("hit:");
        Platform.runLater(()-> {
            btnDouble.setDisable(true);
        });
    }

    @FXML
    void onStand(ActionEvent event) {
        PlayerSession.mandarcomando("stand:");
    }

    @FXML
    void onDouble(ActionEvent event) {
        PlayerSession.mandarcomando("double:");
    }

    @FXML
    void backToMenu(ActionEvent event) {
        PlayerSession.DesconectarMultijugador();
        multiplayermode = false;
        MainWindow.app.setScene("/client/InitialPage.fxml");
    }

    @FXML
    void onReplay(ActionEvent event) {
        asiento1.ClearHand();
        asiento2.ClearHand();
        asiento3.ClearHand();
        asiento4.ClearHand();
        Casahand.ClearHand();
        ClearHboxes();
        PlayerSession.mandarcomando("ready:false");
        MainWindow.app.setScene("/client/PlaceBetsScreen.fxml");
    }

    private void ProcesarResultados(String linea) {
        Platform.runLater(()-> {
                    btnHit.setDisable(true);
                    btnStand.setDisable(true);
                    btnDouble.setDisable(true);
                    btnReplay.setDisable(false);
                    btnBackToMenu.setDisable(false);
                });
        Map<Integer,String> FinalResultados = new HashMap<Integer,String>();
        String jugo = linea.substring(1,linea.length()-1);
        String[] results = jugo.split(",");
        for (String resultado: results){
            String[] AsientoResultado = resultado.split("-");
            int asiento = Integer.parseInt(AsientoResultado[0]);
            String resultadofinal = AsientoResultado[1];
            FinalResultados.put(asiento,resultadofinal);
        }

        for (Map.Entry<Integer, String> entrada:FinalResultados.entrySet()) {
            int asiento = entrada.getKey();
            String resultado = entrada.getValue();
            if (asiento == PlayerSeat) {
                if (resultado.contains("[Win]")) {
                    chips += bet * 2;
                } else if (resultado.contains("[Draw]")) {
                    chips += bet;
                }
            }
            if (resultado.contains("[Win]")) {
                Platform.runLater(()-> {
                    lblPlayer(asiento).setText(EncontrarJugador(asiento).name + " won!");
                });
            } else if (resultado.contains("[Draw]")) {
                Platform.runLater(()-> {
                    lblPlayer(asiento).setText(EncontrarJugador(asiento).name + " got a draw.");
                });
            } else {
                Platform.runLater(()-> {
                    lblPlayer(asiento).setText(EncontrarJugador(asiento).name + " lost!");
                });
            Platform.runLater(()-> {
                lblBetP(asiento).setVisible(false);
                showHand(Casahand,hboxDealer);
                lblDealer.setText("Dealer has "+Casahand.TotalSum()+"!");
            });
            }
        }

    }


    public void ActualizarNombres(String linea) {
        Map<Integer,String> FinalResultados = new HashMap<Integer,String>();
        String jugo = linea.substring(1,linea.length()-1);
        String[] results = jugo.split(",");
        for (String resultado: results){
            String[] AsientoResultado = resultado.split("-");
            int asiento = Integer.parseInt(AsientoResultado[0]);
            String resultadofinal = AsientoResultado[1];
            if (resultadofinal != null && !resultadofinal.trim().isEmpty()){ //Modificado para marcar Asientos libres
                FinalResultados.put(asiento,resultadofinal);
            } else {
                FinalResultados.put(asiento, "Empty Seat");
            }
        }
        Platform.runLater(()->{
            lblPlayer(PlayerSeat).setText(playerName);
        });
        for (Map.Entry<Integer, String> entrada:FinalResultados.entrySet()) {
            int asiento = entrada.getKey();
            String resultado = entrada.getValue();
            EncontrarJugador(asiento).name = resultado;
            Platform.runLater(() -> {
                lblPlayer(asiento).setText(resultado);
            });
        }

        for (int i=1;i<=4 ; i++){
            int actual  = i;
            if (FinalResultados.containsKey(i)){
                Platform.runLater(()-> {
                    lblPlayer(actual).setText(EncontrarJugador(actual).name);
                });
            } else{
                Platform.runLater(()-> {
                    lblPlayer(actual).setText("Empty Seat.");
                    lblPlayer(actual).setDisable(true);
                });
            }
        }
    }

    private void OcultarCarteras() {
        for (int numasiento = 1; numasiento<=4; numasiento++) {
            if (numasiento != PlayerSeat){
                lblChipsP(numasiento).setVisible(false);
            }
        }
    }

    private void ActualizarApuestas(String entrada) {
        Map<Integer,String> FinalResultados = new HashMap<Integer,String>();
        String jugo = entrada.substring(1,entrada.length()-1);
        String[] results = jugo.split(",");
        for (String resultado: results){
            String[] AsientoResultado = resultado.split("-");
            int asiento = Integer.parseInt(AsientoResultado[0]);
            String resultadofinal = AsientoResultado[1];
            FinalResultados.put(asiento,resultadofinal);
        }

        Platform.runLater(()->{
            lblBetP(PlayerSeat).setText("Bet: " + bet);
        });

        for (Map.Entry<Integer, String> entrada1:FinalResultados.entrySet()) {
            int asiento = entrada1.getKey();
            String resultado = entrada1.getValue();
            EncontrarJugador(asiento).bet = Integer.parseInt(resultado);
            if (EncontrarJugador(asiento).bet > 0){ //Modificado para marcar Asientos libres
                Platform.runLater(()-> {
                    lblBetP(asiento).setText("Bet:" + resultado);
                });
            } else {
                Platform.runLater(()-> {
                    lblChipsP(asiento).setDisable(true);
                    lblBetP(asiento).setDisable(true);
                });
            }
        }

        for (int i=1;i<=4 ; i++){
            int actual  = i;
            if (FinalResultados.containsKey(i)){
                Platform.runLater(()-> {
                    lblBetP(actual).setText("Bet:" + FinalResultados.get(actual));
                });
            } else{
                Platform.runLater(()-> {
                    lblBetP(actual).setDisable(true);
                });
            }
        }
    }


    private Player EncontrarJugador(int asiento) {
        switch (asiento) {
            case 1:
                return asiento1;
            case 2:
                return asiento2;
            case 3:
                return asiento3;
            case 4:
                return asiento4;
            default:
                return null;
        }
    }

    private void ClearHboxes(){
        for (int box = 0;box<=4;box++){
            int alv = box;
            Platform.runLater(()->{
                getHbox(alv).getChildren().clear();
            });
        }

    }

    private void showHand(Hand hand, HBox destination) {
        Platform.runLater(()->{
            destination.getChildren().clear();
        });
        for (Card carta : hand.getHand()) {
            Image img = new Image(getClass().getResource("/images/" + carta.Face() + ".png").toExternalForm());
            ImageView imgv = new ImageView(img);
            imgv.setFitWidth(80);
            imgv.setPreserveRatio(true);
            Platform.runLater(()->{
                destination.getChildren().add(imgv);
            });
        }
    }

    private void hiddenDealer(House house, HBox destination) {
        Platform.runLater(()-> {
            destination.getChildren().clear();
        });
        if (house.getHand().size() > 1) {
            Card visible = house.getHand().get(1);
            Image img = new Image(getClass().getResource("/images/" + visible.Face() + ".png").toExternalForm());
            ImageView imgv = new ImageView(img);
            imgv.setFitWidth(80);
            imgv.setPreserveRatio(true);
            Platform.runLater(()-> {
                destination.getChildren().add(imgv);
            });
        }

        Image hiddenImg = new Image(getClass().getResource("/images/HIDDEN.png").toExternalForm());
        ImageView hiddenCard = new ImageView(hiddenImg);
        hiddenCard.setFitWidth(80);
        hiddenCard.setPreserveRatio(true);
        Platform.runLater(()-> {
            destination.getChildren().add(hiddenCard);
        });
    }

    private HBox getHbox(int i) {
        switch (i) {
            case 0:
                return hboxDealer;
            case 1:
                return hboxCardsPlayer1;
            case 2:
                return hboxCardsPlayer2;
            case 3:
                return hboxCardsPlayer3;
            case 4:
                return hboxCardsPlayer4;
            default:
                return null;
        }
    }

    private Label lblPoints(int i) {
        switch (i) {
            case 1:
                return lblPointsPlayer1;
            case 2:
                return lblPointsPlayer2;
            case 3:
                return lblPointsPlayer3;
            case 4:
                return lblPointsPlayer4;
            default:
                return null;
        }
    }

    private Label lblPlayer(int i) {
        switch (i) {
            case 1:
                return lblPlayer1;
            case 2:
                return lblPlayer2;
            case 3:
                return lblPlayer3;
            case 4:
                return lblPlayer4;
            default:
                return null;
        }
    }

    private Label lblChipsP(int i) {
        switch (i) {
            case 1:
                return lblChipsP1;
            case 2:
                return lblChipsP2;
            case 3:
                return lblChipsP3;
            case 4:
                return lblChipsP4;
            default:
                return null;
        }
    }

    private Label lblBetP(int i) {
        switch (i) {
            case 1:
                return lblBetP1;
            case 2:
                return lblBetP2;
            case 3:
                return lblBetP3;
            case 4:
                return lblBetP4;
            default:
                return null;
        }
    }
}

