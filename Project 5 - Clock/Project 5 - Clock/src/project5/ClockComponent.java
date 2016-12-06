package project5;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/** This class contains various methods, constructors, and
 * data for a Clock.
 * The clock uses seconds, minutes, and hours to display the time as a
 * non-digital clock.
 */
public class ClockComponent extends JComponent
{

    private int s; //for seconds
    private int m; //for miutes
    private int h; //for hours

    /** Explicit Constructor
     * @param s seconds
     * @param m minutes
     * @param h hours
     */
    public ClockComponent(int s, int m, int h)
    {
        this.s = s;
        this.m = m;
        this.h = h;
    }

    /** Set the Time of the Clock.
     * This method is called upon by a timer to update the clock.
     * @param s seconds
     * @param m minutes
     * @param h hours
     */
    public void setTime(int s, int m, int h)
    {
        this.s = s;
        this.m = m;
        this.h = h;
    }

    /** Paint the Clock.
     * @param g is the tool used to draw the graphics
     */
    @Override
    public void paint(Graphics g)
    {
        //Cast the old graphics object to a better one.
        Graphics2D g2 = (Graphics2D) g;
        //Make the lines appear smooth using Anti-Aliasing
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //Find the center of the clock
        int centerX = this.getWidth() / 2;
        int centerY = this.getHeight() / 2;

        //Set the lengths of the different hands
        int secondLen = 60;
        int minuteLen = 50;
        int hourLen = 30;

        //Draw the White Fixture
        int clockSize = secondLen + 8;
        g2.setColor(Color.black);
        g2.drawOval(centerX - clockSize, centerY - clockSize,
                clockSize * 2, clockSize * 2); //Draw the border
        g2.setColor(Color.white);
        g2.fillOval(centerX - clockSize, centerY - clockSize,
                clockSize * 2, clockSize * 2); //Fill the border

        //Draw Second Ticks
        g2.setColor(Color.BLACK);
        for (int i = 0; i < 60; i++)
        {
            g2.drawLine(
                    centerX + (int) ((secondLen + 3) * Math.sin(
                            i * 2 * Math.PI / 60)),
                    centerY - (int) ((secondLen + 3) * Math.cos(
                            i * 2 * Math.PI / 60)),
                    centerX + (int) ((secondLen + 5) * Math.sin(
                            i * 2 * Math.PI / 60)),
                    centerY - (int) ((secondLen + 5) * Math.cos(
                            i * 2 * Math.PI / 60)));
        }

        //Draw Hour Ticks (Every 5 ticks should be wider)
        g2.setStroke(new BasicStroke(3));
        for (int i = 1; i < 13; i++)
        {
            g2.drawLine(
                    centerX + (int) ((secondLen) * Math.sin(
                            i * 2 * Math.PI / 12)),
                    centerY - (int) ((secondLen) * Math.cos(
                            i * 2 * Math.PI / 12)),
                    centerX + (int) ((secondLen + 4) * Math.sin(
                            i * 2 * Math.PI / 12)),
                    centerY - (int) ((secondLen + 4) * Math.cos(
                            i * 2 * Math.PI / 12)));
        }

        //Draw the Hour Numbers 1-12
        for (int i = 1; i < 13; i++)
        {
            g2.drawString(String.valueOf(i),
                    centerX - 5 + (int) ((secondLen - 10) * Math.sin(
                            i * 2 * Math.PI / 12)),
                    centerY + 5 - (int) ((secondLen - 10) * Math.cos(
                            i * 2 * Math.PI / 12)));
        }

        //Draw AM/PM
        String AMPM = (h < 12) ? "AM" : "PM";
        g2.drawString(AMPM, centerX - 10, centerY + 30);

        //Draw Hour Hand
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(centerX, centerY,
                centerX + (int) (hourLen * Math.sin(
                        (h * 5 + m / 12) * 2 * Math.PI / 60)),
                centerY - (int) (hourLen * Math.cos(
                        (h * 5 + m / 12) * 2 * Math.PI / 60)));

        //Draw Minute Hand
        g2.drawLine(centerX, centerY,
                centerX + (int) (minuteLen * Math.sin(m * 2 * Math.PI / 60)),
                centerY - (int) (minuteLen * Math.cos(m * 2 * Math.PI / 60)));

        //Draw Second Hand
        g2.setColor(Color.red);
        g2.drawLine(centerX, centerY,
                centerX + (int) (secondLen * Math.sin(s * 2 * Math.PI / 60)),
                centerY - (int) (secondLen * Math.cos(s * 2 * Math.PI / 60)));
    }
}