package blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {

    private Deck deck;
    private Player Jugador;
    private House Casa;
    private ArrayList<Computer> Computadoras;
    private Random random;

    public void excecuteGame(int numPlayers) {
        deck = new Deck();       // Create the deck.
        deck.shuffle();            // Shuffle the deck.

        /* Your code */
        //Creating Players, House and Set of computers
        int numplayers = numPlayers + 1;
        random = new Random();
        if ((numplayers >=2) && (numplayers <=5)) {
            Casa = new House();
            Jugador = new Player();
            Computadoras = new ArrayList<Computer>();
            for (int numcomputers = 0;numcomputers < (numplayers-2);numcomputers++){
                Computer AItoadd = new Computer(random);
                Computadoras.add(AItoadd);
            }
            for (int indexcarddealt = 0;indexcarddealt <(numplayers*2);indexcarddealt++) {
                if ((indexcarddealt == 0) || (indexcarddealt == (numplayers))) {
                    Jugador.addCard(deck.dealCard());
                }
                else if ((indexcarddealt == numplayers-1) || ((indexcarddealt) == ((numplayers)*2)-1)){
                    Casa.addCard(deck.dealCard());
                }
                else {
                    if ((indexcarddealt > 0) && indexcarddealt<numplayers) {
                        Computadoras.get(indexcarddealt-1).addCard(deck.dealCard());
                    } else {
                        Computadoras.get(indexcarddealt-numplayers-1).addCard(deck.dealCard());
                    }
                }
            }
            //Game Starts
            printStatus(Jugador,Casa,Computadoras);
            if (Casa.IsBlackjack() == true){
                EndGame(Jugador,Casa,Computadoras);
            } else {
                /*
                for (int player = 2;player < numplayers; player++) {
                    //System.out.println("---Player "+player+" turn.---");

                    int numcomp = player - 2;
                    Computer currentcomp = Computadoras.get(numcomp);

                    //System.out.println("Player"+player+currentcomp.HandStrprint());
                    if (currentcomp.TotalSum()==21) {
                        break;
                    }
                    boolean compplaying = true;
                    while ((compplaying) && (currentcomp.IsPlaying)){
                        if (currentcomp.Plays()) {
                            //System.out.println("Hit");
                            currentcomp.addCard(deck.dealCard());
                            //System.out.println("Player"+player+currentcomp.HandStrprint());
                        } else {
                            //System.out.println("Stand");
                            compplaying = false;
                            break;
                        }
                    }
                }


                //System.out.println("------ House Turn ------ ");
                boolean houseplaying = true;
                //System.out.println("House hand: "+Casa.HandStrprint());
                while ((houseplaying) && (Casa.IsPlaying)){
                    if (Casa.Plays()) {
                        //System.out.println("Hit");
                        Casa.addCard(deck.dealCard());
                        //System.out.println("House hand: "+Casa.HandStrprint());
                    } else {
                        //System.out.println("Stand");
                        houseplaying = false;
                        break;
                    }}

                 */
                EndGame(Jugador,Casa,Computadoras);

            }
        }
        else {
            //System.out.println("Invalid number of Players.");
        }
    }
    public static void printStatus(Player play, House hous, ArrayList<Computer> CompArray) {
        //System.out.println("House: HIDDEN, "+hous.getHand().get(1).Face());
        //System.out.println("Player 1:"+play.HandStrprint());
        int countercomp = 2;
        for (Computer comput: CompArray) {
            //System.out.println("Player "+countercomp+":"+comput.HandStrprint());
            countercomp++;
        }
    }
    public static void EndGame(Player play, House hous, ArrayList<Computer> CompArray) {
        //System.out.println("-----Game Results -----");
        //System.out.println("House: "+hous.HandStrprint());
        //System.out.println(EvalHand(play,hous)+"Player 1:"+play.HandStrprint());
        int countercomp = 2;
        for (Computer comput: CompArray) {
            //System.out.println(EvalHand(comput,hous)+"Player "+countercomp+":"+comput.HandStrprint());
            countercomp++;
        }
    }
    public static String EvalHand(Hand mano, Hand casamano) {
        String state;
        if ((mano.IsPlaying && casamano.IsPlaying) && !casamano.IsBlackjack()) {
            if ((mano.TotalSum()<casamano.TotalSum())) {
                state ="[Lose]";
            } else if (mano.TotalSum() == casamano.TotalSum()) {
                state ="[Draw]";
            } else {
                state ="[Win]";
            }
            return state;
        }else if (mano.IsPlaying == false && casamano.IsPlaying) {
            return "[Lose]";
        } else if (mano.IsPlaying && casamano.IsPlaying == false) {
            return "[Win]";
        } else {
            return "[Lose]";
        }
    }

    public Deck getDeck() {
        return deck;
    }

    public Player getJugador() {
        return Jugador;
    }

    public House getCasa() {
        return Casa;
    }

    public ArrayList<Computer> getComputadoras() {
        return Computadoras;
    }

    public Random getRandom() {
        return random;
    }

    public void hitJugador() {
        if (Jugador.IsPlaying) {
            Jugador.addCard(deck.dealCard());
        }
    }

    public boolean gameShouldEndImmediately() {
        return Jugador.IsBlackjack() || Casa.IsBlackjack();
    }
}
