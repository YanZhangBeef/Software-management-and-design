package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/**
 * [Thu17:00] Team1
 */

public interface ISelectStrategy {

    /**
     *
     * @param hand is the current cards in the hand
     * @param trump is the current trump suit in this round
     * @return a cloned card from the hand. (has NO reference to our current hand)
     */
    Card selectCard(Hand hand, Suit trump, ArrayList<Card> pile);
}
