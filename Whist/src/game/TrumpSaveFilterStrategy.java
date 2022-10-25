package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/**
 * [Thu17:00] Team1
 */

public class TrumpSaveFilterStrategy implements IFilterStrategy{

    @Override
    public Hand filterCards(Hand hand, Suit lead, Suit trump) {
        if (lead == null) {
            return hand;
        } else {
            // attempt to select the cards in the lead suit first
            Hand filtered = hand.extractCardsWithSuit(lead);
            if (filtered.getNumberOfCards() == 0) {
                // if there is no cards in lead suit, we go for the cards in trump suit
                filtered = hand.extractCardsWithSuit(trump);
            }
            // if none of the cards consistent with the rule, then you
            // are allow to play any card.
            return (filtered.getNumberOfCards() > 0) ? filtered : hand;
        }
    }
}
