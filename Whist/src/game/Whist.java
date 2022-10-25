package game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * [Thu17:00] Team1
 */

@SuppressWarnings("serial")
public class Whist extends CardGame {

    final String trumpImage[] = {"bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif"};

    static final Random random = ThreadLocalRandom.current();

    // return random Enum value
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }

    private final String version = "1.0";

    private static int nbPlayers;
    private static boolean hasRealPlayer;
    private static int nbStartCards;
    private static int winningScore;
    private static boolean enforceRules;
    private static ArrayList<Player> npcs = new ArrayList<Player>();

    private final int handWidth = 400;
    private final int trickWidth = 40;
    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            new Location(650, 575)
    };
    private Actor[] scoreActors = {null, null, null, null};
    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);
    private final int thinkingTime = 2000;

    private Location hideLocation = new Location(-500, -500);
    private Location trumpsActorLocation = new Location(50, 50);

    public void setStatus(String string) {
        setStatusText(string);
    }

    private int[] scores = new int[nbPlayers];

    Font bigFont = new Font("Serif", Font.BOLD, 36);

    private void initScore() {
        for (int i = 0; i < nbPlayers; i++) {
            scores[i] = 0;
            scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    private void updateScore(int player) {
        removeActor(scoreActors[player]);
        scoreActors[player] = new TextActor(String.valueOf(scores[player]), Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    private Card selected;

    private void initRound() {
        Hand[] hands;
        hands = deck.dealingOut(nbPlayers, nbStartCards); // Last element of hands is leftover cards; these are ignored
        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
        }
        // Set up human player for interaction
        if (hasRealPlayer) {
            CardListener cardListener = new CardAdapter()  // Human Player plays card
            {
                public void leftDoubleClicked(Card card) {
                    selected = card;
                    hands[0].setTouchEnabled(false);
                }
            };
            hands[0].addCardListener(cardListener);
        }
        // graphics
        RowLayout[] layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            // layouts[i].setStepDelay(10);
            hands[i].setView(this, layouts[i]);
            hands[i].setTargetArea(new TargetArea(trickLocation));
            hands[i].draw();
        }

        // put initialized hand into each NPC/player
        for (int i = 0; i < npcs.size(); i++) {
            npcs.get(i).initHand(hands[i]);
        }

//	    for (int i = 1; i < nbPlayers; i++)  // This code can be used to visually hide the cards in a hand (make them face down)
//	      hands[i].setVerso(true);
        // End graphics
    }

    private String printHand(ArrayList<Card> cards) {
        String out = "";
        for (int i = 0; i < cards.size(); i++) {
            out += cards.get(i).toString();
            if (i < cards.size() - 1) out += ",";
        }
        return (out);
    }

    private Optional<Integer> playRound() {  // Returns winner, if any
        // Select and display trump suit
        final Suit trumps = randomEnum(Suit.class);
        final Actor trumpsActor = new Actor("sprites/" + trumpImage[trumps.ordinal()]);
        addActor(trumpsActor, trumpsActorLocation);
        // End trump suit
        Hand trick; // A "trick" is a pile of cards
        int winner;
        Card winningCard;
        Suit lead;
        Player current_player;
        int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
        for (int i = 0; i < nbStartCards; i++) {
            trick = new Hand(deck);
            selected = null;
            current_player = npcs.get(nextPlayer);
            /**
             * Handles the lead card ...
             */
            // if the current_player is the human himself
            if (hasRealPlayer && current_player instanceof RealPlayer) {
                ((RealPlayer) current_player).play();
                setStatus("Player 0 double-click on card to lead.");
                while (null == selected) delay(100);
            } else {
                // current player is NPC
                setStatusText("Player " + nextPlayer + " thinking...");
                delay(thinkingTime);
                selected = ((NPC) current_player).play(trumps, trick.getCardList());
            }

            // Lead with selected card
            trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards() + 2) * trickWidth));
            trick.draw();
            selected.setVerso(false);
            // No restrictions on the card being lead
            lead = (Suit) selected.getSuit();
            // The following line does the job of transferring the play card into 'trick'
            selected.transfer(trick, true); // transfer to trick (includes graphic effect)
            winner = nextPlayer;
            winningCard = selected;
            System.out.println("New trick: Lead Player = " + nextPlayer + ", Lead suit = " + selected.getSuit() + ", Trump suit = " + trumps);
            System.out.println("Player " + nextPlayer + " play: " + selected.toString() + " from [" + printHand(current_player.getHand().getCardList()) + "]");
            /**
             * Handles the game after lead card has been played ...
             */
            for (int j = 1; j < nbPlayers; j++) {
                if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first, !nextPlayer already +1 here
                selected = null;
                current_player = npcs.get(nextPlayer);

                // if the current_player is the human himself
                if (hasRealPlayer && current_player instanceof RealPlayer) {
                    ((RealPlayer) current_player).play();
                    setStatus("Player 0 double-click on card to follow.");
                    while (null == selected) delay(100);
                } else {
                    // current player is NPC
                    setStatusText("Player " + nextPlayer + " thinking...");
                    delay(thinkingTime);
                    selected = ((NPC) current_player).play(trumps, trick.getCardList());
                }

                // Follow with selected card
                trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards() + 2) * trickWidth));
                trick.draw();
                selected.setVerso(false);  // In case it is upside down
                // Check: Following card must follow suit if possible
                if (selected.getSuit() != lead && current_player.getHand().getNumberOfCardsWithSuit(lead) > 0) {
                    // Rule violation
                    String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
                    //System.out.println(violation);
                    if (enforceRules)
                        try {
                            throw (new BrokeRuleException(violation));
                        } catch (BrokeRuleException e) {
                            e.printStackTrace();
                            System.out.println("A cheating player spoiled the game!");
                            System.exit(0);
                        }
                }
                // End Check
                selected.transfer(trick, true); // transfer to trick (includes graphic effect)
                System.out.println("Winning card: " + winningCard.toString());
                System.out.println("Player " + nextPlayer + " play: " + selected.toString() + " from [" + printHand(current_player.getHand().getCardList()) + "]");
                if ( // beat current winner with higher card
                        (selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
                                // trumped when non-trump was winning
                                (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
                    winner = nextPlayer;
                    winningCard = selected;
                }
                // End Follow
            }
            delay(600);
            trick.setView(this, new RowLayout(hideLocation, 0));
            trick.draw();
            nextPlayer = winner;
            System.out.println("Winner: " + winner);
            setStatusText("Player " + nextPlayer + " wins trick.");
            scores[nextPlayer]++;
            updateScore(nextPlayer);
            if (winningScore == scores[nextPlayer]) return Optional.of(nextPlayer);
        }
        removeActor(trumpsActor);
        return Optional.empty();
    }

    public Whist() {
        super(700, 700, 30);
        setTitle("Whist (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        initScore();
        Optional<Integer> winner;
        do {
            initRound();
            winner = playRound();
        } while (!winner.isPresent());
        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatusText("Game over. Winner is player: " + winner.get());
        refresh();
    }

    static private void setUpProperties() throws IOException {
        Properties whistProperties = new Properties();
        FileReader inStream = null;
        String current_property_file = "whist.properties";

        try {
            inStream = new FileReader(current_property_file);
            whistProperties.load(inStream);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }

        // Players
        int nplayer = Integer.parseInt(whistProperties.getProperty("Players"));
        System.out.println("#Players: " + nplayer);
        // specify if current game setting has any real player
        if (nplayer == 0) {
            hasRealPlayer = false;
        } else {
            hasRealPlayer = true;
            npcs.add(new RealPlayer());
        }

        // NPCs
        int nNPC = Integer.parseInt(whistProperties.getProperty("NPCs"));
        System.out.println("#NPCs: " + nNPC);

        for (int i=1; i<=nNPC; i++) {
            String[] NPC_strat = whistProperties.getProperty("NPC_"+String.valueOf(i)).split((","));
            System.out.println("#NPC_" + String.valueOf(i) + ": " + Arrays.toString((NPC_strat)));
            npcs.add(new NPC(NPC_strat));
        }

        // Start cards
        nbStartCards = Integer.parseInt(whistProperties.getProperty("Hands"));
        System.out.println("#Hands: " + nbStartCards);
        // Winning score
        winningScore = Integer.parseInt(whistProperties.getProperty("Winning_Score"));
        System.out.println("#Winning_Score: " + winningScore);
        // Enforce Rule
        enforceRules = Boolean.parseBoolean(whistProperties.getProperty("Enforce_Rule"));
        System.out.println("#Enforce_Rule: " + enforceRules);

        // number of players in this game
        nbPlayers = npcs.size();
    }

    public static void main(String[] args) throws IOException {
        setUpProperties();
        new Whist();
    }

}
