package client;

import blackjack.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import client.PlayerSession;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlaceBetsScreenController {
    public static int cantidadcomputadoras = 0;
    private int cantidadapuesta;

    @FXML
    private Button btnFicha1; //no se usa

    @FXML
    private Button btnFicha10; //no se usa

    @FXML
    private Button btnFicha25; //no se usa

    @FXML
    private Button btnFicha50; //no se usa

    @FXML
    private Button btnFicha100; //no se usa

    @FXML
    private Button btnBack; //no se usa

    @FXML
    private Button btnStartGame; //no se usa

    @FXML
    private Button btnComp0;

    @FXML
    private Button btnComp1;

    @FXML
    private Button btnComp2;

    @FXML
    private Button btnComp3;

    @FXML
    private TextField txtFieldApuesta;

    @FXML
    private Label lblPrincipal;

    @FXML
    private Label lblComputers;

    @FXML
    private Label lblMoneyLeft;

    @FXML
    public void initialize(){
        if (!PlayerSession.multiplayermode){
            choosecomp(cantidadcomputadoras);
        } else{
            btnComp0.setVisible(false);
            btnComp1.setVisible(false);
            btnComp2.setVisible(false);
            btnComp3.setVisible(false);
            lblComputers.setVisible(false);
            btnStartGame.setText("Ready!");
        }
        txtFieldApuesta.setText(Integer.toString(PlayerSession.bet));
        stablishmoney();
    }

    @FXML
    void comp0(ActionEvent event){
        btnComp0.setDisable(true);
        btnComp1.setDisable(false);
        btnComp2.setDisable(false);
        btnComp3.setDisable(false);
        cantidadcomputadoras = 0;
    }

    @FXML
    void comp1(ActionEvent event){
        btnComp0.setDisable(false);
        btnComp1.setDisable(true);
        btnComp2.setDisable(false);
        btnComp3.setDisable(false);
        cantidadcomputadoras = 1;
    }

    @FXML
    void comp2(ActionEvent event){
        btnComp0.setDisable(false);
        btnComp1.setDisable(false);
        btnComp2.setDisable(true);
        btnComp3.setDisable(false);
        cantidadcomputadoras = 2;
    }

    @FXML
    void comp3(ActionEvent event){
        btnComp0.setDisable(false);
        btnComp1.setDisable(false);
        btnComp2.setDisable(false);
        btnComp3.setDisable(true);
        cantidadcomputadoras= 3;
    }

    private Player EncontrarJugador(int asiento) {
        switch (asiento) {
            case 1:
                return MultiplayerClientScreenController.asiento1;
            case 2:
                return MultiplayerClientScreenController.asiento2;
            case 3:
                return MultiplayerClientScreenController.asiento3;
            case 4:
                return MultiplayerClientScreenController.asiento4;
            default:
                return null;
        }
    }

    void AsignarNombres(String linea){
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
            EncontrarJugador(asiento).name = resultado;
        }
    }

    @FXML
    void onGO(ActionEvent event){
        if (validbet()){
            PlayerSession.chips -= cantidadapuesta;
            PlayerSession.bet = cantidadapuesta;
            if (PlayerSession.multiplayermode){

                PlayerSession.ready = true;
                PlayerSession.mandarcomando("ready:" + PlayerSession.ready);
                btnStartGame.setDisable(true);
                lblComputers.setText("Waiting for other players to be ready...");
                lblComputers.setVisible(true);

                new Thread(()->{
                    while (true) {
                        try{
                            String[] comando = PlayerSession.recibircomando();
                            if (comando[0].equalsIgnoreCase("empezar")){
                                Platform.runLater(()->{
                                    MainWindow.app.setScene("/client/MultiplayerClientScreen.fxml");
                                });
                                return;
                            } else {
                                Platform.runLater(()->{
                                    lblPrincipal.setText("Please try again later!");
                                });
                            }
                            Thread.sleep(120);
                        } catch (IOException e) {
                            Platform.runLater(()->{
                                lblPrincipal.setText("Network problem.");
                            });
                            throw new RuntimeException(e);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else{
                MainWindow.app.setScene("/client/LocalMultiplayer.fxml");
            }
        }
    }

    @FXML
    void sumarficha1(ActionEvent event){
        sumBet(1);
    }
    @FXML
    void sumarficha10(ActionEvent event){
        sumBet(10);
    }
    @FXML
    void sumarficha25(ActionEvent event){
        sumBet(25);
    }
    @FXML
    void sumarficha50(ActionEvent event){
        sumBet(50);
    }
    @FXML
    void sumarficha100(ActionEvent event){
        sumBet(100);
    }

    @FXML
    void backtomenu(ActionEvent event){
        if(PlayerSession.multiplayermode) {
            PlayerSession.DesconectarMultijugador();
        }
        MainWindow.app.setScene("/client/InitialPage.fxml");
    }

    private void sumBet(int cantidad){
        int cantidadfinal = cantidadapuesta + cantidad;
        txtFieldApuesta.setText(Integer.toString(cantidadfinal));
        cantidadapuesta = cantidadfinal;
    }

    private void stablishmoney(){
        lblMoneyLeft.setText("Chips left: "+PlayerSession.chips);
    }

    private void choosecomp(int numcomp){
        switch (numcomp){
            case 1:
                btnComp0.setDisable(false);
                btnComp1.setDisable(true);
                btnComp2.setDisable(false);
                btnComp3.setDisable(false);
                cantidadcomputadoras = 1;
                break;
            case 2:
                btnComp0.setDisable(false);
                btnComp1.setDisable(false);
                btnComp2.setDisable(true);
                btnComp3.setDisable(false);
                cantidadcomputadoras = 2;
                break;
            case 3:
                btnComp0.setDisable(false);
                btnComp1.setDisable(false);
                btnComp2.setDisable(false);
                btnComp3.setDisable(true);
                cantidadcomputadoras= 3;
                break;
            default:
                btnComp0.setDisable(true);
                btnComp1.setDisable(false);
                btnComp2.setDisable(false);
                btnComp3.setDisable(false);
                cantidadcomputadoras = 0;
                break;
        }
    }

    private boolean validbet(){ //Checa si es valida y si es, establece su cantidad desde un principio
        int tempbet;
        try{
            tempbet = Integer.parseInt(txtFieldApuesta.getText());
            if (tempbet <= 0){
                lblPrincipal.setText("Please insert a valid bet.");
                return false;
            } else if (tempbet > PlayerSession.chips){
                lblPrincipal.setText("Not enough chips!");
                return false;
            } else {
                this.cantidadapuesta = tempbet;
                return true;
            }
        } catch (Exception e){
            lblPrincipal.setText("Please put a number!");
            return false;
        }
    }



}
