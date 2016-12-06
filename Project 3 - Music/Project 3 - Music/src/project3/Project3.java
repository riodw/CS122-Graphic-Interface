package project3;

import java.util.ArrayList;

/**
 * Project 3 - Music
 * @author Rio Weber
 * @version 1.0
 */
public class Project3
{

    public static void main(String[] args)
    {
        Music m = new Music(10);
        System.out.println("A default Music object: \t\t\t\t" + m);
        int[] array =
                {
                    0, 0, 9, 0, 2, -6, 0
                };
        
        Music m2 = new Music(7, array, 7);
        System.out.println("An explicit Music object: \t\t\t\t" + m2);
        m2.trimLeadingSilence();
        System.out.println("The same object trimmed: \t\t\t\t" + m2);
        System.out.println("The number of eliminated noises with a limit"
                + " of 5: \t"
                + m2.eliminateNoise(5));
        System.out.println("The same object trimmed with a noise "
                + "elimination of 5: \t" + m2);

        System.out.println();

        MusicArrayList mal = new MusicArrayList(10);
        System.out.println("A default Music object made with "
                + "ArrayLists: \t\t" + mal);
        ArrayList arrlist = new ArrayList();
        arrlist.add(0);
        arrlist.add(0);
        arrlist.add(9);
        arrlist.add(0);
        arrlist.add(2);
        arrlist.add(-6);
        arrlist.add(0);
        MusicArrayList mal2 = new MusicArrayList(7, arrlist, 7);
        System.out.println("An explicit Music object "
                + "made with ArrayLists: \t\t" + mal2);
        mal2.trimLeadingSilence();
        System.out.println("The same object trimmed: \t\t\t\t" + mal2);
        System.out.println("The number of eliminated noises with a "
                + "limit of 5: \t" + mal2.eliminateNoise(5));
        System.out.println("The same object trimmed with a noise "
                + "elimination of 5: \t" + mal2);
        System.out.println();

        MagicSquare ms = new MagicSquare(5);
        System.out.println("Magic Square of dimension 5:");
        System.out.println(ms); //regular Magic Square
        System.out.println("TransposeOne Magic Square:");
        ms.transposeOne(); //flip around line from top-left to bottom-right
        System.out.println(ms);
        ms.transposeOne(); //flip back to normal
        System.out.println("TransposeTwo Magic Square:");
        ms.transposeTwo(); //flip around line from top-left to bottom-right
        System.out.println(ms);

        new PaintWindow(); //The JFrame that will load graphics
    }
}