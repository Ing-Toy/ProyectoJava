package blackjack;

public class Card {
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
