/** 
 * Project 6 - Bugs
 * @author Rio Weber
 * @version 1.0
 */
package project6;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;

/** This class contains methods, constructors, and data for Bugs.
 * The Bugs object is a JFrame that contains a simulation of 4 bugs at 
 * opposite ends of a 400x400 rectangle heading towards the counterclockwise 
 * bug at 5 pixels per step. The bugs themselves are not saved as objects, as 
 * their only attribute would be position.
 */
public class Bugs extends JFrame
{

    Dimension bugs[]; //This holds the positions of the 4 bugs
    final JTextField[][] text; //This holds 4 groups of 3 TextFields
    ArrayList list[]; //These lists hold every previous position the bugs had
    final Paint paint; //The object that visualizes it in a paint component
    //This allows the JFrame itself to be passed to subclasses in order to get
    //the frame's other attributes
    final Bugs frame = this;

    /** Default Constructor.
     */
    public Bugs()
    {
        //Set up JFrame
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Bug Simulation");

        //Create Main Panel
        JPanel mp = new JPanel();
        mp.setLayout(new BorderLayout());

        //Create the arrays of objects
        JPanel[] panel = new JPanel[4]; //Create the 4 JPanels
        JLabel[][] label = new JLabel[4][3]; //Create the 12 JLabels
        text = new JTextField[4][3]; //Create the 12 JTextFields

        //Fill all of the JLabels and JTextFields
        for (int i = 0; i < 4; i++)
        {
            panel[i] = new JPanel();
            label[i][0] = new JLabel("Red: ");
            label[i][1] = new JLabel("Blue: ");
            label[i][2] = new JLabel("Green: ");
            text[i][0] = new JTextField("0");
            text[i][1] = new JTextField("0");
            text[i][2] = new JTextField("0");
            //Check if this current subpanel is the EAST or WEST subpanel
            if (i == 3 || i == 2)
            {
                //Give it a special vertical BoxLayout
                BoxLayout bl = new BoxLayout(panel[i], BoxLayout.Y_AXIS);
                panel[i].setLayout(bl);
                panel[i].add(Box.createVerticalStrut(getHeight() / 3));
            }
            //Set the sizes for each JTextField
            for (int j = 0; j < 3; j++)
            {
                text[i][j].setPreferredSize(new Dimension(28, 20));
                text[i][j].setMaximumSize(new Dimension(28, 20));
                //Check if it is the EAST or WEST subpanel
                if (i == 3 || i == 2)
                {
                    //Add them to individual subsubpanels
                    JPanel temp = new JPanel();
                    temp.add(label[i][j]);
                    temp.add(text[i][j]);
                    //Add subsubpanels to subpanel
                    panel[i].add(temp);

                } else
                {
                    //Add them right to the subpanel itself
                    panel[i].add(label[i][j]);
                    panel[i].add(text[i][j]);
                }
            }
            //Add this strut to EAST and WEST to keep the components centered
            if (i == 3 || i == 2)
            {
                panel[i].add(Box.createVerticalStrut(getHeight() / 3));
            }
        }

        //Create the Simulate Button and add the ActionListener
        JButton simulate = new JButton("Simulate");
        simulate.addActionListener(new Click());
        panel[1].add(simulate); //It goes on the Southern subpanel

        //Add all of the subpanels to the main panel
        mp.add(panel[0], BorderLayout.NORTH);
        mp.add(panel[2], BorderLayout.EAST);
        mp.add(panel[3], BorderLayout.WEST);
        mp.add(panel[1], BorderLayout.SOUTH);

        //Create the arrays of bug positions and a list of their previous ones
        list = new ArrayList[4];
        bugs = new Dimension[4];
        //Set the position of each bug
        bugs[0] = new Dimension(0, 0);
        bugs[1] = new Dimension(0, 399);
        bugs[2] = new Dimension(399, 399);
        bugs[3] = new Dimension(399, 0);

        //Create ArrayLists to keep track of previous positions
        for (int i = 0; i < 4; i++)
        {
            list[i] = new ArrayList<Dimension>();
            list[i].add(bugs[i]); //Add current position to history
        }

        //Create the paint component and add it to CENTER
        paint = new Paint();
        mp.add(paint, BorderLayout.CENTER);
        add(mp); //Add the main panel to the Frame itself
        setVisible(true);
    }

    /** Simulate one step of the bugs and update their position and history.
     */
    public void simulate()
    {
        //Go through each of the four bugs
        for (int i = 0; i < 4; i++)
        {
            int mainBug = i;
            //The followBug is the one the mainBug is following.
            //If the mainBug the last one in the array (Bug 3), then make sure
            //the followBug is Bug 0.
            int followBug = (i == 3) ? 0 : i + 1;

            //Get the Width and Height of he followBug
            int followBugWidth = ((Dimension) list[followBug].get(
                    list[followBug].size() - 1)).width;
            int followBugHeight = ((Dimension) list[followBug].get(
                    list[followBug].size() - 1)).height;

            //If the mainBug is 3, then the followBug is 0. This means that
            //Bug 0 already moved, but Bug 3 should be following the position
            //it was before it moved. This if statement corrects that.
            if (mainBug == 3)
            {
                followBugWidth = ((Dimension) 
                        list[0].get(list[0].size() - 2)).width;
                followBugHeight = ((Dimension) 
                        list[0].get(list[0].size() - 2)).height;
            }

            //Get the distances between the mainBug and the followBug
            int xDist = bugs[mainBug].width - followBugWidth;
            int yDist = bugs[mainBug].height - followBugHeight;

            //Calculate the angle of the next step using the ArcTanget
            double radian = (Math.atan2(yDist, -xDist));
            double angle = (Math.atan2(yDist, -xDist) * 180 / Math.PI);

            //Use the angle to calculate the x and y distances to a len of 5.
            bugs[i] = new Dimension(
                    (int) Math.round(bugs[i].width + Math.cos(radian) * 5),
                    (int) Math.round(bugs[i].height - Math.sin(radian) * 5));

            //Update the list of the bug's position history
            list[i].add(bugs[i]);

            //Print out debug information to the console.
            DecimalFormat df = new DecimalFormat("##0.00");
            System.out.println("Bug " + mainBug + " faces " + df.format(angle)
                    + " degrees to Bug " + followBug + "\tand moved "
                    + bugs[i].width + "," + bugs[i].height);

        }
        System.out.println("");
    }

    /** This subclass draws with paintComonent on a JComponent.
     * This paint object is designed draw the simulation. It grabs the colors
     * of the text fields and maps them to their respective bug. It draws
     * a gray grid which on any intersection, each bug must be positioned. The
     * grid is 400x400. It also draws a transparent bug for the previous
     * positions of each bug. Everything is drawn proportionately in position.
     */
    private class Paint extends JComponent
    {
        /** This component paints the bugs onto the JComponent
         * @param g is the graphics object used to paint
         */
        @Override
        public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            int height = this.getHeight();
            int width = this.getWidth();
            g2.setColor(Color.lightGray);

            //Draw the grid lines
            for (int i = 0; i < 400; i++)
            {
                g2.drawLine(i * width / 400, 0, i * width / 400, height);
                g2.drawLine(0, i * height / 400, width, i * height / 400);
            }

            //Draw the black border of the component
            g2.setColor(Color.BLACK);
            g2.drawRect(0, 0, width - 1, height - 1);

            //Paint the bugs
            for (int i = 0; i < 4; i++)
            {
                //Grad the colors from the JTextFields
                Color c = new Color(
                        Integer.parseInt(text[i][0].getText()),
                        Integer.parseInt(text[i][2].getText()),
                        Integer.parseInt(text[i][1].getText()));

                //Draw the bugs themselves
                g2.setColor(c);
                g2.fillOval(
                        (bugs[i].width * width / 400) - 1,
                        (bugs[i].height * height / 400) - 1,
                        3, 3);
                g2.setColor(Color.BLACK);
                g2.drawOval(
                        (bugs[i].width * width / 400) - 1,
                        (bugs[i].height * height / 400) - 1,
                        3, 3);
                
                //Get the number of previous positions
                int simulationCount = list[i].size() - 1;
                //Go through every previous location
                for (int j = 0; j < simulationCount; j++)
                {
                    //Get the position of this previous location
                    int previousWidth = ((Dimension) list[i].get(
                            list[i].size() - 2 - j)).width;
                    int previousHeight = ((Dimension) list[i].get(
                            list[i].size() - 2 - j)).height;
                    //Draw the same color of the bug but with transparency
                    g2.setColor(new Color(
                            c.getRed(), c.getGreen(), c.getBlue(),
                            (simulationCount - j) * 255 / simulationCount));
                    g2.fillOval(
                            (previousWidth * width / 400) - 1,
                            (previousHeight * height / 400) - 1,
                            3, 3);
                    g2.setColor(Color.BLACK);
                    g2.drawOval(
                            (previousWidth * width / 400) - 1,
                            (previousHeight * height / 400) - 1,
                            3, 3);
                }
            }
        }
    }

    /** This subclass contains an ActionListener.
     * This ActionListener is to be called upon when the "Simulate" button
     * is clicked. It runs one round of the simulation and repaints the
     * Paint component.
     */
    private class Click implements ActionListener
    {
        /** This method is called when the an action happens.
         * @param ae is the action that was performed
         */
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            frame.simulate();
            paint.repaint();
        }
    }
    
    /** Main method to create Bugs object.
     * @param args is ignored
     */
    public static void main(String[] args)
    {
        Bugs frame = new Bugs();
    }
}
