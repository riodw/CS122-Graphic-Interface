package project3;

import java.util.Random;

/** This class contains various methods, constructors, and data for Music.
 * Music is stored as an array of digits that could be either positive
 * or negative.
 */
public class Music
{
    private int[] notes;
    private int length;
    private int digits;
    Random rand = new Random();

    /** Explicit Constructor that takes length
     * @param length is the length of the array
     */
    public Music(int length)
    {
        this.length = length;
        notes = new int[this.length];
        digits = rand.nextInt(this.length+1);
        //Fill the Array to how many digits are allowed
        for (int i = 0; i < digits; i++)
        {
            notes[i] = rand.nextInt(10); //An int 0 to 9
            notes[i] *= rand.nextBoolean() ? 1 : -1; //Positive or Negative
        }
    }

    /** Explicit Constructor that takes length, notes[], and digits
     * @param length is the length of the array
     * @param notes is the array of notes as integers
     * @param digits is how many notes there are in the array
     */
    public Music(int length, int notes[], int digits)
    {
        this.length = length;
        this.notes = notes.clone();
        this.digits = digits;
    }

    /** Eliminate all leading 0's in the array.
     * Ex. Turns 001230 to 1230
     */
    public void trimLeadingSilence()
    {
        while (true)
        {
            if (notes[0] == 0)
            {
                int newArray[] = new int[length - 1];
                for (int j = 0; j < length - 1; j++)
                {
                    newArray[j] = notes[j + 1];
                }
                length = length - 1;
                notes = newArray.clone();
                digits = digits - 1;
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
        for (int i = 0; i < length; i++)
        {
            count += (Math.abs(notes[i]) > Math.abs(limit)) ? 1 : 0;
            notes[i] = (notes[i] > Math.abs(limit)) ? limit : notes[i];
            notes[i] = (notes[i] < -Math.abs(limit)) ? -limit : notes[i];
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
        for (int note : notes)
        {
            out += note + " ";
        }
        return out;
    }
}