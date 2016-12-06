package project4;

/** This class contains various methods, constructors, and data for cards.
 * These cards are to be used along side the Poker class. A card has a value
 * and a suite. The value ranges from 1-14, with 11, 12, 13, 14 corresponding
 * respectively with J, Q, K, and A. The suites can be Spade, Diamonds, Clubs,
 * and hearts.
 */
public class Card
{

    private int value;
    private String suite;

    /** Explicit Constructor
     * @param value is the value of the card.
     * @param suite is the suite of the card.
     */
    public Card(int value, String suite)
    {
        this.value = value;
        this.suite = suite;
    }

    /** Get the suite of the card.
     * @return the suite of the card as a string.
     */
    public String getSuite()
    {
        return suite;
    }

    /** Get the suite of the card.
     * @return the suite of the card as a string.
     */
    public String getValue()
    {
        String val;
        //Convert numeric values 11-14 to J, Q, K, or A respectively.
        switch (value)
        {
            case 11:
                val = "J";
                break;
            case 12:
                val = "Q";
                break;
            case 13:
                val = "K";
                break;
            case 14:
                val = "A";
                break;
            default:
                val = String.valueOf(value);
                break;
        }
        return val;
    }

    /** Get the integer value of the card.
     * @return the integer value of the card
     */
    public int getIntValue()
    {
        return value;
    }

    /** Print out as a String.
     * @return the card in the format (value,suite)
     */
    @Override
    public String toString()
    {
        return "(" + getValue() + ", " + getSuite() + ")";
    }
}