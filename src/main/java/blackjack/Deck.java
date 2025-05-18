package blackjack;

import java.util.Random;

public class Deck {
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
    public void shuffle() {
        Random random = new Random();
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
