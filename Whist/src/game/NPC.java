package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/**
 * [Thu17:00] Team1
 */

public class NPC extends Player {
    private ISelectStrategy strat;

    public NPC(String[] strats) {
        super();
        if (strats.length == 1) {
            setStrat(strats[0]);
        } else if (strats.length == 2){
            setStrat(strats[0], strats[1]);
        } else {
            throw new IllegalStateException("Error! More than 2 strategies detected!");
        }
    }

    /**
     * Set the NPC strategy
     * @param select
     */
    private void setStrat(String select) {
        this.strat = StrategyFactory.getInstance().getStrategy(select);
    }

    /**
     * Overload methods for setting the NPC strategy
     * @param filter
     * @param select
     */
    private void setStrat(String filter, String select) {
        this.strat = StrategyFactory.getInstance().getStrategy(filter, select);
    }

    /**
     * Overload Player.play method
     * @param trump trump suit in current game
     * @param pile list of card that has been played by others
     * @return Card - a single card the NPC is going to play after going through some strategies.
     */
    public Card play(Suit trump, ArrayList<Card> pile) {
        Card ref = strat.selectCard(this.hand, trump, pile);
        // Set the play card reference to a card in the hand,
        // so that we can successfully remove it later in the code using `transfer`
        return this.hand.getCard(ref.getSuit(), ref.getRank());
    }

}
