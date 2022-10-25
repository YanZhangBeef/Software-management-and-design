package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/**
 * [Thu17:00] Team1
 */

public class SmartSelectStrategy implements ISelectStrategy {
    @Override
    public Card selectCard(Hand hand, Suit trump, ArrayList<Card> pile) {
        Card selected = hand.get(0);
        int rank;
        if (pile.size() == 0) { // be the first player
            rank = 4;
            // this for loop only operate when has trump > 10
            for (int i = 0; i < hand.getNumberOfCards(); i++) {
                //has trump > 10
                if (hand.get(i).getSuit() == trump && hand.get(i).getRankId() < rank) {
                    rank = hand.get(i).getRankId();
                    selected = hand.get(i);
                }
            }
            // if don't has trump > 10, find the largest normal card
            if (rank > 3) {
                rank = 13;
                for (int i = 0; i < hand.getNumberOfCards(); i++) {
                    if (hand.get(i).getSuit() != trump && hand.get(i).getRankId() < rank) {
                        selected = hand.get(i);
                        rank = hand.get(i).getRankId();
                    }
                }
            }
        } else { // be the second or third or forth player
            Card winningCard = pile.get(0);
            Suit lead;
            lead = (Suit) winningCard.getSuit();
            // find the winning card on the desk
            for (int i = 1; i < pile.size(); i++) {
                selected = pile.get(i);
                if ( // beat current winner with higher card
                        (selected.getSuit() == winningCard.getSuit() && selected.getRankId() < winningCard.getRankId()) ||
                                // trumped when non-trump was winning
                                (selected.getSuit() == trump && winningCard.getSuit() != trump)) {
                    winningCard = selected;
                }
            }

            ArrayList<Card> sameSuit = new ArrayList<>();
            ArrayList<Card> trumpList = new ArrayList<>();

            for (int i = 0; i < hand.getNumberOfCards(); i++) {
                if (hand.get(i).getSuit() == lead) {
                    sameSuit.add(hand.get(i));
                }
                if (hand.get(i).getSuit() == trump) {
                    trumpList.add(hand.get(i));
                }
            }

            // be the second or third player
            if (pile.size() == 1 || pile.size() == 2) {
                selected = null;
                // lead is not same as trump
                // has lead and no one used trump
                if (!lead.equals(trump)) {
                    if (winningCard.getSuit() == lead && sameSuit.size() > 0) {
                        if (sameSuit.get(0).getRankId() < winningCard.getRankId()) {
                            // if you can win, select the largest leading
                            selected = sameSuit.get(0);
                        } else {
                            // if cannot win, select the smallest leading
                            selected = sameSuit.get(sameSuit.size() - 1);
                        }
                    } else if (sameSuit.size() > 0 && winningCard.getSuit() == trump) {
                        // you has lead and someone used trump, so you cannot win
                        // select the smallest lead
                        selected = sameSuit.get(sameSuit.size() - 1);
                    } else if (sameSuit.size() == 0 && trumpList.size() > 0 && winningCard.getSuit() == lead) {
                        // you don't have lead but have trump, no one used trump before
                        // select the largest trump
                        selected = trumpList.get(0);
                    } else if (sameSuit.size() == 0 && trumpList.size() > 0 && winningCard.getSuit() == trump) {
                        // you don't have lead but have trump, someone used trump before

                        // if have trump greater than wining trump
                        if (trumpList.get(0).getRankId() < winningCard.getRankId()) {
                            // select the largest trump
                            selected = trumpList.get(0);
                        }
                    }
                } else { // lead is the same as trump
                    if (sameSuit.size() > 0) {
                        // select the largest trump if can win the winning card
                        if (sameSuit.get(0).getRankId() < winningCard.getRankId()) {
                            selected = sameSuit.get(0);
                        } else { // select the smallest trump if cannot win
                            selected = sameSuit.get(sameSuit.size() - 1);
                        }
                    }
                }

                // if don't have lead or trump, have no way to win, select the smallest card
                if (selected == null) {
                    rank = -1;
                    for (int i = 0; i < hand.getNumberOfCards(); i++) {
                        if (hand.get(i).getRankId() > rank) {
                            selected = hand.get(i);
                            rank = hand.get(i).getRankId();
                        }
                    }
                }
            } else if (pile.size() == 3) { // if you are the forth player
                selected = null;
                if (!lead.equals(trump)) { // if lead is not same as trump
                    //1.have lead and win is lead
                    if (winningCard.getSuit() == lead && sameSuit.size() > 0) {
                        // have card greater than wining card
                        if (sameSuit.get(0).getRankId() < winningCard.getRankId()) {
                            // select the smallest card but greater than winning
                            rank = winningCard.getRankId();
                            for (int i = sameSuit.size() - 1; i >= 0; i--) {
                                if (sameSuit.get(i).getRankId() < rank) {
                                    selected = sameSuit.get(i);
                                    break;
                                }
                            }
                        } else { // don't have card greater than wining card
                            // select the smallest lead

                            selected = sameSuit.get(sameSuit.size() - 1);

                        }


                        //2.have lead, wining is trump, already lose, select the smallest lead
                    } else if (sameSuit.size() > 0 && winningCard.getSuit() == trump) {
                        selected = sameSuit.get(sameSuit.size() - 1);
                    }
                    //3. no lead, have trump, win = lead
                    else if (sameSuit.size() == 0 && trumpList.size() > 0 && winningCard.getSuit() == lead) {

                        selected = trumpList.get(trumpList.size() - 1);
                    } else if (sameSuit.size() == 0 && trumpList.size() > 0 && winningCard.getSuit() == trump) {

                        rank = winningCard.getRankId();
                        for (int i = trumpList.size() - 1; i >= 0; i--) {

                            if (trumpList.get(i).getRankId() < rank) {
                                selected = trumpList.get(i);
                                break;
                            }
                        }
                    }
                } else { // lead = trump
                    //6.have trump
                    if (sameSuit.size() > 0) {
                        // have cards greater than winning, then select the smallest of them
                        if (sameSuit.get(0).getRankId() < winningCard.getRankId()) {

                            rank = winningCard.getRankId();

                            for (int i = trumpList.size() - 1; i >= 0; i--) {

                                if (sameSuit.get(i).getRankId() < rank) {
                                    selected = sameSuit.get(i);
                                    break;
                                }
                            }
                        } else { // don't have trump greater than winning, select the smallest trump
                            selected = sameSuit.get(sameSuit.size() - 1);
                        }

                    }
                }

                // have no way to win, select the smallest card
                if (selected == null) {
                    rank = -1;
                    for (int i = 0; i < hand.getNumberOfCards(); i++) {
                        if (hand.get(i).getRankId() > rank) {
                            selected = hand.get(i);
                            rank = hand.get(i).getRankId();
                        }
                    }
                }


            }

        }

        return selected;
    }
}
