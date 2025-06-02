package blackjack;

public class Card {
    public int value;
    public String suit;
    public Card(int theValue, String theSuit) {
        this.suit = theSuit;
        this.value = theValue;
    }
    public Card(String face1){
        String face = face1.toUpperCase();
        char palo = face.charAt(face.length() - 1);
        this.suit = String.valueOf(palo).toLowerCase();
        String cara = face.substring(0, face.length() - 1);
        switch (cara) {
            case "A":
                this.value = 1;
                break;
            case "J":
                this.value = 11;
                break;
            case "Q":
                this.value = 12;
                break;
            case "K":
                this.value = 13;
                break;
            default:
                try {
                    this.value = Integer.parseInt(cara);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
        }
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
