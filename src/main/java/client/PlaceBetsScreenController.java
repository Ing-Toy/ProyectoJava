package client;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class PlaceBetsScreenController {
    private boolean multijugador;
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
    private Label lblMoneyLeft;

    @FXML
    public void initialize(){
        choosecomp(cantidadcomputadoras);
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

    @FXML
    void onGO(ActionEvent event){
        if (validbet()){
            PlayerSession.chips -= cantidadapuesta;
            PlayerSession.bet = cantidadapuesta;
            MainWindow.app.setScene("/client/LocalMultiplayer.fxml");
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
