package client;

import javafx.fxml.FXML;

import java.awt.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class PlaceBetsScreenController {
    private boolean multijugador;
    public static int cantidadcomputadoras = 0;
    private int cantidadapuesta;

    @FXML
    private Button ficha1;

    @FXML
    private Button ficha10;

    @FXML
    private Button ficha25;

    @FXML
    private Button ficha50;

    @FXML
    private Button ficha100;

    @FXML
    private Button Back;

    @FXML
    private Button startgame;

    @FXML
    private Button comp0;

    @FXML
    private Button comp1;

    @FXML
    private Button comp2;

    @FXML
    private Button comp3;

    @FXML
    private TextField apuesta;

    @FXML
    private Label principal;

    @FXML
    private Label moneyleft;

    @FXML
    public void initialize(){
        choosecomp(cantidadcomputadoras);
        apuesta.setText(Integer.toString(PlayerSession.bet));
        stablishmoney();
    }

    @FXML
    void comp0(ActionEvent event){
        comp0.setDisable(true);
        comp1.setDisable(false);
        comp2.setDisable(false);
        comp3.setDisable(false);
        cantidadcomputadoras = 0;
    }

    @FXML
    void comp1(ActionEvent event){
        comp0.setDisable(false);
        comp1.setDisable(true);
        comp2.setDisable(false);
        comp3.setDisable(false);
        cantidadcomputadoras = 1;
    }

    @FXML
    void comp2(ActionEvent event){
        comp0.setDisable(false);
        comp1.setDisable(false);
        comp2.setDisable(true);
        comp3.setDisable(false);
        cantidadcomputadoras = 2;
    }

    @FXML
    void comp3(ActionEvent event){
        comp0.setDisable(false);
        comp1.setDisable(false);
        comp2.setDisable(false);
        comp3.setDisable(true);
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
        apuesta.setText(Integer.toString(cantidadfinal));
        cantidadapuesta = cantidadfinal;
    }

    private void stablishmoney(){
        moneyleft.setText("Chips left: "+PlayerSession.chips);
    }

    private void choosecomp(int numcomp){
        switch (numcomp){
            case 1:
                comp0.setDisable(false);
                comp1.setDisable(true);
                comp2.setDisable(false);
                comp3.setDisable(false);
                cantidadcomputadoras = 1;
                break;
            case 2:
                comp0.setDisable(false);
                comp1.setDisable(false);
                comp2.setDisable(true);
                comp3.setDisable(false);
                cantidadcomputadoras = 2;
                break;
            case 3:
                comp0.setDisable(false);
                comp1.setDisable(false);
                comp2.setDisable(false);
                comp3.setDisable(true);
                cantidadcomputadoras= 3;
                break;
            default:
                comp0.setDisable(true);
                comp1.setDisable(false);
                comp2.setDisable(false);
                comp3.setDisable(false);
                cantidadcomputadoras = 0;
                break;
        }
    }

    private boolean validbet(){ //Checa si es valida y si es, establece su cantidad desde un principio
        int tempbet;
        try{
            tempbet = Integer.parseInt(apuesta.getText());
            if (tempbet <= 0){
                principal.setText("Please insert a valid bet.");
                return false;
            } else if (tempbet > PlayerSession.chips){
                principal.setText("Not enough chips!");
                return false;
            } else {
                this.cantidadapuesta = tempbet;
                return true;
            }
        } catch (Exception e){
            principal.setText("Please put a number!");
            return false;
        }
    }



}
