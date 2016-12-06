package project3;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.JFrame;

/** This class contains a constructor and a JComponent subclass for a JFrame.
 * This JFrame is used to display a paint component containing various Magic
 * Square, demonstrating the MagicSquare.draw() method.
 */
public class PaintWindow extends JFrame
{
    private MagicSquare ms = new MagicSquare(5);
    private MagicSquare ms2 = new MagicSquare(7);
    private MagicSquare ms3 = new MagicSquare(9);
    private MagicSquare ms4 = new MagicSquare(12);

    /** Default Constructor
     */
    public PaintWindow()
    {
        super("Magic Squares of Various Sizes");
        setSize(740, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PaintComponent p = new PaintComponent();
        add(p);
        
        setVisible(true);
    }
    
    /** SubClass for a PaintComponent
     */
    private class PaintComponent extends JComponent
    {
        /** Paint on the Paint Component
         * @param g is the graphics controller that does the painting
         */
        @Override
        protected void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            ms.draw(g2,90,50);
            ms2.draw(g2,70,160);
            ms3.draw(g2,50,315);
            ms4.draw(g2,300,100);
        }
    }
}