package project3;

import java.awt.Graphics2D;

/** This class contains various methods, constructors, and
 * data for Magic Squares.
 * Magic Squares are squares that the sum of each row, column, and main
 * diagonal all equal the same number.
 *
 * IMPORTANT! While Java two dimension arrays are stored [y][x], I used the
 * classic coordinate plane [x][y] to store my data. I reversed x and y during
 * output in order to get the proper output. It is my belief that thinking
 * about this class as [x][y] is clearer than thinking about it as [y][x].
 */
public class MagicSquare
{
    private int n; //dimension of the square
    private int magic[][]; //the square itself

    /** Explicit Constructor
     * @param dimension is how wide by tall the Magic Square will be
     */
    public MagicSquare(int dimension)
    {
        n = dimension;
        magic = new int[n][n];

        //Use x and y for positioning on the Square
        int x = n / 2; //Find the middle
        int y = n - 1; //Find the bottom
        magic[x][y] = 1; //Place the first value in the bottom-middle
        int count = 2; //Since the first value is placed, start counter at 2
        while (count <= n * n) //check if there are still spaces available
        {
            //Move right 1 and down 1. Check for wrapping.
            x = (x + 1 == n) ? 0 : x + 1;
            y = (y + 1 == n) ? 0 : y + 1;

            if (magic[x][y] != 0) //Check if this space is taken
            {
                //Move left 1 and up 2. Check for wrapping.
                x = (x - 1 == -1) ? n - 1 : x - 1;
                y = (y - 1 == -1) ? n - 1 : y - 1;
                y = (y - 1 == -1) ? n - 1 : y - 1;
            }

            magic[x][y] = count; //Place current number
            count++;
        }
    }

    /** Flip the Values Along the Top-Left to Bottom-Right Diagonal.
     */
    public void transposeOne()
    {
        //i is the row
        //j is the collumn
        for (int i = 0; i < n; i++)
        {
            for (int j = i + 1; j < n; j++)
            {
                //Swap the values
                int temp = magic[i][j];
                magic[i][j] = magic[j][i];
                magic[j][i] = temp;
            }
        }
    }

    /** Flip the Values Along the Bottom-Left to Top-Right Diagonal.
     */
    public void transposeTwo()
    {
        //i is the column
        //j is the row
        for (int i = 0; i < n; i++)
        {
            for (int j = n - i - 2; j >= 0; j--)
            {
                //Swap the values
                int temp = magic[j][i];
                magic[j][i] = magic[n - i - 1][n - j - 1];
                magic[n - i - 1][n - j - 1] = temp;
            }
        }
    }

    /** Print out as a String
     * @return the square as a table of Strings separated by indentation
     */
    @Override
    public String toString()
    {
        String str = "";
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                str += magic[j][i] + "\t";
            }
            str += "\n";
        }
        return str;
    }

    /** Draw the Magic Square
     * @param g is the graphics controller that will do the actual drawing
     * @param x is the starting x coordinate for the square
     * @param y is the starting y coordinate for the square
     */
    public void draw(Graphics2D g, int x, int y)
    {
        int m = (n >= 10) ? 30 : 20; //determine size multiplier based on digits

        for (int i = 0; i <= n; i++) //Draw n^2 Lines
        {
            //Draw Vertical Grid Lines
            g.drawLine(x + (i * m), y, x + (i * m), y + (n * m));
            //Draw Horizontal Grid Lines
            g.drawLine(x, y + (i * m), x + (n * m), y + (i * m));
        }

        for (int i = 0; i < magic.length; i++)
        {
            for (int j = 0; j < magic[0].length; j++)
            {
                g.drawString(String.valueOf(magic[i][j]),
                        x + (i * m) + (m / 6),
                        y + (j * m) + m - (m / 4));
            }
        }
    }
}