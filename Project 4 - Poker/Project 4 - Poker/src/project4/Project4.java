package project4;
/**
 * Project 4 - Poker
 * @author Rio Weber
 * @version 1.0
 */
public class Project4
{
    public static void main(String[] args)
    {
        System.out.println("First Deck:");
        Poker p = new Poker();
        p.play();
        
        System.out.println("\nSecond Deck:");
        p.drawHandRoyalFlush();
        System.out.println("Your hand is: " + p.displayHand());
        System.out.println("Do you have a royal flush? " + p.isRoyalFlush());
    }
}