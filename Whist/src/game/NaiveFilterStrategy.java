package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/**
 * [Thu17:00] Team1
 */

public class NaiveFilterStrategy implements IFilterStrategy{

    @Override
    public Hand filterCards(Hand hand, Suit lead, Suit trump) {
        if (lead == null) {
            return hand;
        } else {
            /**
             * REMINDER!
             * `extractCardsWithSuit` only return a new hand of cloned cards.
             */
            Hand filtered = hand.extractCardsWithSuit(lead);
            Hand temp = hand.extractCardsWithSuit(trump);
            filtered.insert(temp, false);
            // if none of the cards consistent with the rule, then you
            // are allow to play any card.
            return (filtered.getNumberOfCards() > 0) ? filtered : hand;
        }
    }
}
