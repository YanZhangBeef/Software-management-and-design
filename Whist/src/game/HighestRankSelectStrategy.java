package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/**
 * [Thu17:00] Team1
 */

public class HighestRankSelectStrategy implements ISelectStrategy {
    @Override
    public Card selectCard(Hand hand, Suit trump, ArrayList<Card> pile) {
        ArrayList<Card> sameSuit = new ArrayList<>();

        for (int i = 0; i < hand.getNumberOfCards(); i++) {
            if (hand.get(i).getSuit() == trump) {
                sameSuit.add(hand.get(i));
            }
        }

        if (sameSuit.size() > 0) {
            Card highest = null;
            for (int i = 0; i < sameSuit.size(); i++) {
                if (highest == null) {
                    highest = sameSuit.get(i);
                } else if (sameSuit.get(i).getValue() > highest.getValue()) {
                    highest = sameSuit.get(i);
                }
            }

            return highest;
        } else {
            Card highest = null;
            if (pile.size() == 0) {
                for (int i = 0; i < hand.getNumberOfCards(); i++) {
                    if (highest == null) {
                        highest = hand.get(i);
                    } else if (hand.get(i).getValue() > highest.getValue()) {
                        highest = hand.get(i);
                    }
                }
            } else {
                for (int i = 0; i < hand.getNumberOfCards(); i++) {
                    if (highest == null) {
                        highest = hand.get(i);
                    } else if (highest.getSuit() == pile.get(0).getSuit()) {
                        if (hand.get(i).getValue() > highest.getValue() && hand.get(i).getSuit() == highest.getSuit()) {
                            highest = hand.get(i);
                        }
                    } else if (hand.get(i).getSuit() == pile.get(0).getSuit()) {
                        highest = hand.get(i);
                    } else {
                        if (hand.get(i).getValue() > highest.getValue()) {
                            highest = hand.get(i);
                        }
                    }
                }

            }

            return highest;
        }
    }
}
