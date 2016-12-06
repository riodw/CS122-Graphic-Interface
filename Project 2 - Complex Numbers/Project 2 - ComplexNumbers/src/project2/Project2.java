package project2;

/**
 * Project 2 - Complex Numbers
 * @author Rio Weber
 * @version 1.0
 */
public class Project2
{

    public static void main(String[] args)
    {
        ComplexNumber c1 = new ComplexNumber(2, 3);
        ComplexNumber c2 = new ComplexNumber(5, -6);
        ComplexNumber c3 = new ComplexNumber(4, -3);
        ComplexNumber c4 = new ComplexNumber(3, -2);
        ComplexNumber c5 = new ComplexNumber(1, 2);
        ComplexNumber c6 = new ComplexNumber(3, -4);

        //An example of the Default Constructor
        System.out.println("Default Constructor = " + new ComplexNumber());
        //An example of the Add Method
        System.out.println("(" + c1 + ")" + "+" + "(" + c2 + ")" + " = " + c1.add(c2));
        //An example of the Subtract Method
        System.out.println("(" + c1 + ")" + "-" + "(" + c2 + ")" + " = " + c1.subtract(c2));
        //An example of the Magnitude Method
        System.out.println("The magnitude of " + c3 + " = " + c3.magnitude());
        //Two examples of the Equals Method
        System.out.println("(" + c1 + ")" + "=" + "(" + c2 + ")? " + c1.equals(c2));
        System.out.println("(" + c3 + ")" + "=" + "(" + c3 + ")? " + c3.equals(c3));
        //An example of the Times Method
        System.out.println("(" + c1 + ")" + "*" + "(" + c4 + ")" + " = " + c1.times(c4));
        //An example of the Divides Method
        System.out.println("(" + c5 + ")" + "/" + "(" + c6 + ")" + " = " + c5.divides(c6));
    }
}