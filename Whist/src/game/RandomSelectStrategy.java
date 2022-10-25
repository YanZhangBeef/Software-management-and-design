package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * [Thu17:00] Team1
 */

public class RandomSelectStrategy implements ISelectStrategy {
    private final Random random = ThreadLocalRandom.current();

    @Override
    public Card selectCard(Hand hand, Suit trump, ArrayList<Card> pile) {
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}
