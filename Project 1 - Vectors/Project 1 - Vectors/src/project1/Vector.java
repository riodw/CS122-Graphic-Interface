package project1;

import java.text.DecimalFormat;

/**
 * This class contains various methods, constructors, and data for vectors.
 * A vector has three double type components x, y, and z and is usually denoted
 * in the form {x,y,z}. 
 */

public class Vector
{
    double x; //x Component
    double y; //y Components
    double z; //z Component
    DecimalFormat df = new DecimalFormat("0.##");
    
    /** Default Constructor */
    public Vector()
    {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }
    
    /** Explicit Constructor
     * @param xComponent the value of the x Component
     * @param yComponent the value of the y Component
     * @param zComponent the value of the z Component
     */
    public Vector(double xComponent, double yComponent, double zComponent)
    {
        x = xComponent;
        y = yComponent;
        z = zComponent;
    }
    
    /** Add Two Vectors
     * @param v2 is the other vector
     * @return the sum of the two vectors
     */
    public Vector add(Vector v2)
    {
        return new Vector(x+v2.x,y+v2.y,z+v2.z);
    }
    
    
    /** Subtract Two Vectors
     * @param v2 is the other vector
     * @return the difference of the two vectors
     */
    public Vector subtract(Vector v2)
    {
        return new Vector(x-v2.x,y-v2.y,z-v2.z);
    }
    
    /** Find the Length of This Vector
     * @return the length of this vector
     */
    public String length()
    {
        return df.format(Math.sqrt(x*x+y*y+z*z));
    }
    
    /** Compare Two Vectors
     * @param v2 is the other vector
     * @return whether or not the two vectors are equal
     */
    public boolean equals(Vector v2)
    {
        return (x==v2.x && y==v2.y && z==v2.z);
    }
    
    /** Dot Multiply Two Vectors
     * @param v2 is the other vector
     * @return return the dot product of two vectors
     */
    public double dot(Vector v2)
    {
        return (x*v2.x)+(y*v2.y)+(z*v2.z);
    }
    
    /** Cross Multiply Two Vectors
     * @param v2 is the other vector
     * @return return the cross product of two vectors
     */
    public Vector cross(Vector v2)
    {
        double x2 = y*v2.z-z*v2.y;
        double y2 = z*v2.x-x*v2.z;
        double z2 = x*v2.y-y*v2.x;
        
        return new Vector(x2,y2,z2);
    }
    
    /** Convert Vector to String
     * @return a string that looks like {1,2,3}
     */
    @Override
    public String toString()
    {
        Object xOut = x;
        Object yOut = y;
        Object zOut = z;
        
        //If a number is an interger (1.0) then print it out as an integer (1)
        if (x == Math.floor(x)) xOut = (int)x;
        if (y == Math.floor(y)) yOut = (int)y;
        if (z == Math.floor(z)) zOut = (int)z;
        
        return "<"+xOut+","+yOut+","+zOut+">";
    }
}