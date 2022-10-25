package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

/**
 * [Thu17:00] Team1
 */

public abstract class Player {
    protected Hand hand;

    public Player() {

    }

    public void initHand(Hand hand){
        this.hand = hand;
    }

    public Hand getHand() {
        return this.hand;
    }
}
