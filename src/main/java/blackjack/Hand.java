package blackjack;

import java.util.ArrayList;

public class Hand { //Set of Cards
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