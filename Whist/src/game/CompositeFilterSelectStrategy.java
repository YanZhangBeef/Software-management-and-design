package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * [Thu17:00] Team1
 */

public class CompositeFilterSelectStrategy implements ISelectStrategy{
    private IFilterStrategy filterStrat;
    private ISelectStrategy selectStrat;

    public CompositeFilterSelectStrategy(IFilterStrategy filter, ISelectStrategy select) {
        this.filterStrat = filter;
        this.selectStrat = select;
    }

    @Override
    public Card selectCard(Hand hand, Suit trump, ArrayList<Card> pile) {
        // if the pile is empty, there is no leading card
        // i.e. you are the one who lead.
        Suit lead = (pile.size() > 0) ? (Suit) pile.get(0).getSuit() : null;
        Hand filtered = filterStrat.filterCards(hand, lead, trump);
        return selectStrat.selectCard(filtered, trump, pile);
    }
}
