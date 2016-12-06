package project3;

import java.util.ArrayList;
import java.util.Random;

/** This class contains various methods, constructors, and data for Music.
 * Music is stored as an array of digits that could be either positive
 * or negative.
 */
public class MusicArrayList
{
    private ArrayList notes;
    private int length;
    private int digits;
    Random rand = new Random();

    /** Explicit Constructor that takes length
     * @param length is the length of the array
     */
    public MusicArrayList(int length)
    {
        this.length = length;
        notes = new ArrayList<Integer>(this.length);
        digits = rand.nextInt(this.length+1);
        //Fill the Array to how many digits are allowed
        for (int i = 0; i < length; i++)
        {
            //A note 0 to 9 that is set either true or false
            if (i < digits)
            {
                notes.add(i, rand.nextInt(10) * (rand.nextBoolean() ? 1 : -1));
            } else
            {
                notes.add(0);
            }
        }
    }

    /** Explicit Constructor that takes length, notes[], and digits
     * @param length is the length of the array
     * @param notes is the array of notes as integers
     * @param digits is how many notes there are in the array
     */
    public MusicArrayList(int length, ArrayList notes, int digits)
    {
        this.length = length;
        this.notes = (ArrayList) notes.clone();
        this.digits = digits;
    }

    /** Eliminate all leading 0's in the array.
     * Ex. Turns 001230 to 1230
     */
    public void trimLeadingSilence()
    {
        while (true)
        {
            if ((int) notes.get(0) == 0)
            {
                digits--;
                length--;
                notes.remove(0);
            } else
            {
                return;
            }
        }
    }

    /** Limit the max and min noise of the notes.
     * Ex. Limiting 5 turns 1 9 4 -8 to 1 5 4 -5
     * @param limit is the max and min that any note can me
     * @return the number of digits changed
     */
    public int eliminateNoise(int limit)
    {
        int count = 0;
        for (int i = 0; i < digits; i++)
        {
            count += (Math.abs((int) notes.get(i)) > Math.abs(limit)) ? 1 : 0;
            notes.set(i, ((int) notes.get(i) > Math.abs(limit))
                    ? limit : notes.get(i));
            notes.set(i, ((int) notes.get(i) < -Math.abs(limit))
                    ? -limit : notes.get(i));
        }
        return count;
    }

    /** Print out as a String
     * @return the music as a sequence of numbers
     */
    @Override
    public String toString()
    {
        String out = "";
        for (int i = 0; i < length; i++)
        {
            out += notes.get(i) + " ";
        }
        return out;
    }
}