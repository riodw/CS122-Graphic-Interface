package project4;

import java.util.ArrayList;
import java.util.Random;

/** This class contains various methods, constructors, and
 * data for a Poker game.
 * Poker is a game with 52 cards in a deck. 5 cards are given to the player to
 * hold in their hand. If they have a particular arrangement of cards, they
 * win.
 */
public class Poker
{

    private ArrayList<Card> deck;
    private Card[] hand;

    /** Default Constructor
     */
    public Poker()
    {
        deck = new ArrayList<Card>();
        hand = new Card[5];
        for (int i = 1; i < 14; i++)
        {
            deck.add(new Card(i, "Spade"));
            deck.add(new Card(i, "Club"));
            deck.add(new Card(i, "Diamond"));
            deck.add(new Card(i, "Heart"));
        }
    }

    /** Shuffle the Deck.
     */
    public void shuffleDeck()
    {
        Random rand = new Random();
        for (int i = 0; i <= 51; i++)
        {
            int other = rand.nextInt(52);
            Card temp = deck.get(other);
            deck.set(other, deck.get(i));
            deck.set(i, temp);
        }
    }

    /** Draw Five Cards into the player's hand.
     */
    public void drawHand()
    {
        for (int i = 0; i < 5; i++)
        {
            hand[i] = deck.get(0);
            deck.remove(0);
        }
    }

    /** Explicitly give the player a hand containing cards for a royal flush.
     */
    public void drawHandRoyalFlush()
    {
        hand[0] = new Card(10, "Spade");
        hand[1] = new Card(11, "Spade");
        hand[2] = new Card(12, "Spade");
        hand[3] = new Card(13, "Spade");
        hand[4] = new Card(14, "Spade");
    }

    /** Sort the player's hand from smallest to largest value.
     */
    public void sortHand()
    {
        for (int i = 0; i < hand.length; i++)
        {
            for (int j = i; j < hand.length; j++)
            {
                if (hand[j].getIntValue() > hand[i].getIntValue())
                {
                    Card max = hand[i];
                    hand[i] = hand[j];
                    hand[j] = max;
                }
            }
        }
    }

    /** Display the cards in the deck.
     * @return the cards in the deck in the format [(value1,suit1),...]
     */
    public String displayDeck()
    {
        String str = "[";
        for (int i = 0; i < 51; i++)
        {
            str += deck.get(i) + ", ";
        }
        str += deck.get(51) + "]";
        return str;
    }

    /** Display the cards in the hand.
     * @return the cards in the hand in the format [(value1,suit1),...]
     */
    public String displayHand()
    {
        String str = "[";
        for (int i = 0; i < 4; i++)
        {
            str += hand[i] + ", ";
        }
        str += hand[4] + "]";
        return str;
    }

    /** Determine if the hand is a royal flush.
     * @return whether the player's hand is a royal flush.
     */
    public boolean isRoyalFlush()
    {
        boolean royalSuit = true;
        boolean royalValues[] =
        {
            false, false, false, false, false
        };

        //Check if every suit is the same
        for (int i = 0; i < 5; i++)
        {
            royalSuit = (hand[i].getSuite().equals(hand[0].getSuite()))
                    ? royalSuit : false;
        }

        //Check for 10, J, Q, K, A in any order
        for (int i = 0; i < 5; i++)
        {
            royalValues[0] = (hand[i].getValue().equals("10"))
                    ? true : royalValues[0];
            royalValues[1] = (hand[i].getValue().equals("J"))
                    ? true : royalValues[1];
            royalValues[2] = (hand[i].getValue().equals("Q"))
                    ? true : royalValues[2];
            royalValues[3] = (hand[i].getValue().equals("K"))
                    ? true : royalValues[3];
            royalValues[4] = (hand[i].getValue().equals("A"))
                    ? true : royalValues[4];
        }
        return (royalSuit && royalValues[0] && royalValues[1]
                && royalValues[2] && royalValues[3] && royalValues[4]);
    }

    /** Simulate part of the game.
     */
    public void play()
    {
        shuffleDeck();
        drawHand();
        System.out.println("Your hand is: " + displayHand());
        System.out.println("Do you have a royal flush? " + isRoyalFlush());
    }
}