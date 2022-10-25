package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/**
 * [Thu17:00] Team1
 */

public interface IFilterStrategy {
    /**
     *
     * @param hand cards in your hand
     * @param lead lead suit
     * @param trump trump suit
     * @return Hand of cards that consistent with the rules
     */
    Hand filterCards(Hand hand, Suit lead, Suit trump);
}
