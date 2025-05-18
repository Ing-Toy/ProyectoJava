package blackjack;

import java.util.Scanner;

public class Player extends Hand {
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
