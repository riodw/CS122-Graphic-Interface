package project1;

/**
 * Project 1 - Vectors
 * @author Rio Weber
 * @version 1.0
 */
public class Project1 
{
    public static void main(String[] args)
    {
        Vector v1 = new Vector(1,2,3);
        Vector v2 = new Vector(2.5,-1,10);
        
        System.out.print("Vector1 + Vector2 = ");
        System.out.println(v1.add(v2));                 //Add
        System.out.print("Vector1 - Vector2 = ");
        System.out.println(v1.subtract(v2));            //Subtract
        System.out.print("The length of Vector1 is equivalent to ");
        System.out.println(v1.length());                //Length
        System.out.print("Is Vector1 equal to Vector1? ");
        System.out.println(v1.equals(v1));              //Equals True
        System.out.print("Is Vector1 equal to Vector2? ");
        System.out.println(v1.equals(v2));              //Equals False
        System.out.print("Vector1 dot Vector2 = ");
        System.out.println(v1.dot(v2));                 //Dot
        System.out.print("Vector1 cross Vector2 = ");
        System.out.println(v1.cross(v2));               //Cross
        
        System.out.print("An approximation of e where n = 30: ");
        System.out.println(Exp.eProximate());         //e Approximation
    }
}