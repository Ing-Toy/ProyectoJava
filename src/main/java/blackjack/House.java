package blackjack;

public class House extends Hand {
    public boolean Plays() {
        if (this.TotalSum() <= 16 ) {
            return true;
        } else {
            return false;
        }
    }
}
