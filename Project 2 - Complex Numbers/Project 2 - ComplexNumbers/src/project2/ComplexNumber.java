package project2;

/**
 * This class contains methods, constructors, and data for Complex Numbers. A
 * Complex Number has a real part a and an imaginary part b. It is written as
 * a+bi where i is called the imaginary unit and i^2=-1.
 */
public class ComplexNumber
{

    double r; //real number
    double i; //imaginary component

    /**
     * Default Constructor
     */
    public ComplexNumber()
    {
        r = 0;
        i = 1;
    }

    /**
     * Explicit Constructor
     *
     * @param realNumber is the real part
     * @param imaginaryNumber is the imaginary part
     */
    public ComplexNumber(double realNumber, double imaginaryNumber)
    {
        r = realNumber;
        i = imaginaryNumber;
    }

    /**
     * Add two Complex Numbers
     *
     * @param c is the other Complex Number
     * @return the sum of the two Complex Numbers
     */
    public ComplexNumber add(ComplexNumber c)
    {
        return new ComplexNumber(r + c.r, i + c.i);
    }

    /**
     * Subtract two Complex Numbers
     *
     * @param c is the other Complex Number
     * @return the difference of the two Complex Numbers
     */
    public ComplexNumber subtract(ComplexNumber c)
    {

        return new ComplexNumber(r - c.r, i - c.i);
    }

    /**
     * Calculate the Magnitude
     *
     * @return the magnitude of this Complex Number
     */
    public double magnitude()
    {
        return Math.sqrt(Math.pow(r, 2) + Math.pow(i, 2));
    }

    /**
     * Determine if two Complex Numbers are Equal
     *
     * @param c is the other Complex Number
     * @return whether or not the two Complex Numbers are equal
     */
    public boolean equals(ComplexNumber c)
    {
        return (r == c.r && i == c.i);
    }

    /**
     * Multiply two Complex Numbers
     *
     * @param c is the other Complex Number
     * @return the product of the two Complex Numbers
     */
    public ComplexNumber times(ComplexNumber c)
    {
        return new ComplexNumber(r * c.r - i * c.i, r * c.i + i * c.r);
    }

    /**
     * Divide two Complex Numbers
     *
     * @param c is the other Complex Number
     * @return the quotient of the two Complex Numbers
     */
    public ComplexNumber divides(ComplexNumber c)
    {
        double rNew = ((r * c.r + i * c.i) / (c.r * c.r + c.i * c.i));
        double iNew = ((i * c.r - r * c.i) / (c.r * c.r + c.i * c.i));
        return new ComplexNumber(rNew, iNew);
    }

    /**
     * Convert this Complex Number to String
     *
     * @return a string that looks like "a+bi"
     */
    @Override
    public String toString()
    {
        Object rOut = r;
        Object iOut = i;
        String sign;
        //If i is positive, add + sign. Otherwise, the - sign will be supplied.
        sign = ((double) iOut >= 0 ? "+" : "");

        //If a number is an interger (1.0) then print it out as an integer (1)
        rOut = r == Math.floor(r) ? (int) r : rOut;
        iOut = i == Math.floor(i) ? (int) i : iOut;

        //If r is 0, then omit it completely.
        rOut = r == 0 ? "" : rOut;
        sign = r == 0 ? "" : sign;

        //If i is 1, then remove the 1.
        iOut = i == 1 ? "" : iOut;

        return rOut + sign + iOut + "i";
    }
}