package client;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PlayerSession {
    public static String playerName;
    public static int chips = 1000;
    public static int bet = 0;

    public static boolean multiplayermode = false;
    public static boolean conectado = false;
    public static Socket PlayerSocket;
    public static int PlayerSeat;
    public static DataInputStream dis;
    public static DataOutputStream dos;
    public static boolean ready = false;

    public static List<Integer> chipsCpu = new ArrayList<>(List.of(1000, 1000, 1000));

    public static void ConectarMultijugador(){
        final String host = "localhost";
        final int puerto = 5000;
        try{
            Socket sokett = new Socket(host,puerto);
            PlayerSocket = sokett;
            dis = new DataInputStream(sokett.getInputStream());
            dos = new DataOutputStream(sokett.getOutputStream());
            multiplayermode = true;

            String[] cmd = recibircomando();
            if (cmd[0].equalsIgnoreCase("setAsiento")){
                PlayerSeat = Integer.parseInt(cmd[1]);
            }else if (cmd[0].equalsIgnoreCase("fulltable")){
                System.out.println("Casa llena.");
                conectado = false;
                return;
            }else{
                conectado = false;
                return;
            }
            mandarcomando("nombre:"+PlayerSession.playerName);

            conectado = true;
        } catch (IOException e){
            e.printStackTrace();
            conectado = false;
        }
    }

    public static void DesconectarMultijugador(){
        try{
            mandarcomando("exit:");
            dis.close();
            dos.close();
            PlayerSocket.close();
            PlayerSeat = 999;
            multiplayermode = false;
        } catch (IOException si){}
    }

    public static void mandarcomando(String comando) {
        System.out.println("Playersession mandando: "+comando);
        try {
            dos.writeUTF(comando);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] recibircomando() throws IOException{

        String recibido = dis.readUTF();
        String[] partes = recibido.split(":");
        return partes;


    }
}
