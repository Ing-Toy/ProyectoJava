package blackjack;

import java.util.Random;

public class Computer extends Hand { // Player automatically participates
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
