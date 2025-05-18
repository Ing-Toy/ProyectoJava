import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack {
    public static void main(String[] args) {
        int seed = Integer.parseInt(args[0]);
        Deck deck = new Deck();       // Create the deck.
        deck.shuffle(seed);            // Shuffle the deck.
        
        /* Your code */
        //Creating Players, House and Set of computers
        int numplayers= Integer.parseInt(args[1])+1;
        Random random = new Random(seed);
        if ((numplayers >=2) && (numplayers <=5)) {
	        House Casa = new House();
	        Player Jugador = new Player();
	        ArrayList<Computer> Computadoras = new ArrayList<Computer>();
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
	        	for (int player = 1;player < numplayers; player++) {
		        	System.out.println("---Player "+player+" turn.---");
		        	switch (player) {
		        	case 1:
		        		Scanner move =new Scanner (System.in);
		        		Jugador.playTurn(move, deck);
		        		break;
		        	default:
		        		int numcomp = player - 2;
		        		Computer currentcomp = Computadoras.get(numcomp);
		        		
		        		System.out.println("Player"+player+currentcomp.HandStrprint());
		        		if (currentcomp.TotalSum()==21) {
		        			break;
		        		}
		        		boolean compplaying = true;
		        		while ((compplaying) && (currentcomp.IsPlaying)){
			        		if (currentcomp.Plays()) {
			        			System.out.println("Hit");
			        			currentcomp.addCard(deck.dealCard());
			        			System.out.println("Player"+player+currentcomp.HandStrprint());
			        		} else {
			        			System.out.println("Stand");
			        			compplaying = false;
			        			break;
			        		}        		
			        	}
		        	}
		        }
        		System.out.println("------ House Turn ------ ");
	        	boolean houseplaying = true;
        		System.out.println("House hand: "+Casa.HandStrprint());
	        	while ((houseplaying) && (Casa.IsPlaying)){
	        		if (Casa.Plays()) {
	        			System.out.println("Hit");
	        			Casa.addCard(deck.dealCard());
	        			System.out.println("House hand: "+Casa.HandStrprint());
	        		} else {
	        			System.out.println("Stand");
	        			houseplaying = false;
	        			break;
	        		}}
        		EndGame(Jugador,Casa,Computadoras);
        		
	        }
        }
        else {
        	System.out.println("Invalid number of Players.");
        }
    }
    public static void printStatus(Player play, House hous, ArrayList<Computer> CompArray) {
    	System.out.println("House: HIDDEN, "+hous.getHand().get(1).Face());
    	System.out.println("Player 1:"+play.HandStrprint());
    	int countercomp = 2;
    	for (Computer comput: CompArray) {
    		System.out.println("Player "+countercomp+":"+comput.HandStrprint());
    		countercomp++;
    	}
    }
    public static void EndGame(Player play,House hous,ArrayList<Computer> CompArray) {
    	System.out.println("-----Game Results -----");
    	System.out.println("House: "+hous.HandStrprint());
    	System.out.println(EvalHand(play,hous)+"Player 1:"+play.HandStrprint());
    	int countercomp = 2;
    	for (Computer comput: CompArray) {
    		System.out.println(EvalHand(comput,hous)+"Player "+countercomp+":"+comput.HandStrprint());
    		countercomp++;
    	}
    }
    public static String EvalHand(Hand mano, Hand casamano) {
    	String state;
    	if (mano.IsPlaying && casamano.IsPlaying) {
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
}    

class Card {
	public int value;
	public String suit;
    public Card(int theValue, String theSuit) {
    	this.suit = theSuit;
    	this.value = theValue;
    }
    public Integer getValue(){
    	if (this.value > 10) {
    		return 10;
    	} else {
    		return this.value;
    	}
    }
    public String Face() {
    	switch (this.value) {
    	case 1:
    		return "A" + this.suit;
    	case 11:
    		return "J" + this.suit;
    	case 12:
    		return "Q" + this.suit;
    	case 13:
    		return "K" + this.suit;
    	default:
    		return Integer.toString(value) + this.suit;
    	}
    }
}

class Deck {
    private Card[] deck = new Card[52];
    private String suits[] = {"c","h","d","s"};
    private int cardsUsed;
    public Deck() {
    	int counter = 0;
    	for (int i = 1;i<=13;i++) {
    		for (String s:this.suits) {
    			this.deck[counter] = new Card(i,s);
    			counter++;
    		}
    	}
    	/* Different arrangement of the deck(1st try)
    	for (String s:this.suits) {
    		for (int i = 1;i<=13;i++) {
        		this.deck[counter] = new Card(i,s);
        		counter++;
        	}
    	}*/	
    	cardsUsed = 0;
    }
    public void shuffle(int seed) {
        Random random = new Random(seed);
        for (int i = deck.length - 1; i > 0; i--) {
            int rand = (int)(random.nextInt(i + 1));
            Card temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        cardsUsed = 0;
    }
    public Card dealCard() {
        if (cardsUsed == deck.length)
            throw new IllegalStateException("No cards are left in the deck.");

        cardsUsed++;
        return deck[cardsUsed - 1];
    }
    public Card[] getdeck() {
    	return this.deck;
    }
}

class Hand { //Set of Cards
	private ArrayList<Card> hand = new ArrayList<Card>();
	public int aces = 0;
	public boolean IsPlaying = true;
	public void addCard(Card toAdd) {
	    this.hand.add(toAdd);
	    if (toAdd.getValue() == 1) {
	        this.aces++;
	    }

	    if (this.TotalSum() > 21) {
	        this.IsPlaying = false;
	    }
	}
	public ArrayList<Card> getHand(){
		return this.hand;
	}
	public boolean IsBlackjack() {
		if ((this.hand.get(0).getValue() == 1) && (this.hand.get(1).getValue() >= 10)) {
			return true;
		} else if ((this.hand.get(1).getValue() == 1) && (this.hand.get(0).getValue() >= 10)) {
			return true;
		} else {
			return false;
		}
	}
	public int TotalSum() {
	    int sum = 0;
	    int internalAces = 0;
	    
	    for (Card card : this.hand) {
	        sum += card.getValue();
	        if (card.getValue() == 1) {
	            internalAces++;
	        }
	    }

	    while (internalAces > 0 && sum + 10 <= 21) {
	        sum += 10;
	        internalAces--;
	    }
	    
	    return sum;
	}
	public StringBuilder HandStrprint() {
		if (this.TotalSum()<=21) {
			String finalstr = "  ";
			for (Card card:this.hand) {
				finalstr+=card.Face()+", ";
			}
			int index = finalstr.length()-2;
			StringBuilder sentence = new StringBuilder(finalstr);
			sentence.setCharAt(index, ' ');
			return sentence.append("("+Integer.toString(this.TotalSum())+")");
		} else {
			String finalstr = "  ";
			for (Card card:this.hand) {
				finalstr+=card.Face()+", ";
			}
			int index = finalstr.length()-2;
			StringBuilder sentence = new StringBuilder(finalstr);
			sentence.setCharAt(index, ' ');
			return sentence.append("("+Integer.toString(this.TotalSum())+") - Bust!");
		}
	}
}

class Computer extends Hand { // Player automatically participates
	private Random random;
	public Computer(Random random) {
		this.random = random;
	}
	public boolean Plays() {
		if (this.TotalSum() < 14 ) {
			return true;
		} else if (this.TotalSum() > 17) {
			return false;
		} else {
			int is_hit = (int)(random.nextInt(2));
			if (is_hit == 0) {
				return false;
			} else {
				return true;
			}
		}
	}
}   
class Player extends Hand {
	public void playTurn(Scanner scanner, Deck deck) {
        if (TotalSum() == 21) {
            return; 
        }
        boolean playerPlaying = true;
        while (playerPlaying && this.IsPlaying) {
            System.out.println("Player: " + HandStrprint());
            String option = scanner.nextLine();

            if (option.equalsIgnoreCase("Hit")) {
                addCard(deck.dealCard());
            } else if (option.equalsIgnoreCase("Stand")) {
                playerPlaying = false;
            } else {
                System.out.println("Not a valid option.");
            }
        }
        System.out.println("Player hand: " + HandStrprint());
    }
}   
class House extends Hand {
	public boolean Plays() {
		if (this.TotalSum() <= 16 ) {
			return true;
		} else {
			return false;
		} 
	}
}