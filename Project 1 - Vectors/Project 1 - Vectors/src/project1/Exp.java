package project1;

import java.text.DecimalFormat;

/**
 * This class contains methods used to approximate e.
 * It contains a method eProximate which is used to estimate e, and a recursive
 * factorial method, which aids eProximate in the effort of calculating e.
 */

public class Exp 
{
    static int aprox = 30;
    DecimalFormat df = new DecimalFormat("0.##");
    
    /** Recursively Calculate Factorial
     * @param n is the factorial to be calculated
     * @return the factorial of n
     */
    private static double factorial(int n)
    {
        if (n<=1)
        {
            return 1.0;
        }
        return n*factorial(n-1);
    }
    
    /** Approximate e
     * @return the approximation of e
     */
    public static double eProximate()
    {
        double e = 1.0;
        //aprox is the number of fractions added to 1
        for (int i = 1; i <= aprox; i++)
        {
            e +=  1.0/factorial(i);
        }
        return e;
    }
}