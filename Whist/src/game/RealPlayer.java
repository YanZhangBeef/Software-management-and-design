package game;

import ch.aplu.jcardgame.Card;

/**
 * [Thu17:00] Team1
 */

public class RealPlayer extends Player{

    public RealPlayer() {
        super();
    }

    public void play() {
        this.hand.setTouchEnabled(true);
    }

}
