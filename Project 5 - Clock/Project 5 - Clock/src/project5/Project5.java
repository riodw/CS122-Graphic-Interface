package project5;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.sound.sampled.*;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Project 5 - Clock
 * @author Rio Weber
 * @version 1.0
 */
public class Project5
{

    public static void main(String[] args) throws Exception
    {
        //Make the very unique frame
        final JFrame frame = new JFrame();
        frame.setName("Clock");
        frame.setUndecorated(true);
        frame.setSize(180, 180);
        //Make the frame lightly transparent with an invisible background
        frame.setOpacity((float) 0.8);
        frame.setBackground(new Color(255, 255, 255, 0));
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Clock");

        //Set the time up
        final ClockComponent c = new ClockComponent(0, 0, 0);
        final GregorianCalendar gc = new GregorianCalendar();

        //Set the sound up
        URL url = new URL("http://www.wavlist.com/soundfx/020/clock-tick1.wav");
        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
        final Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl
                = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-30.0f); // Reduce volume by 10 decibels.

        /** This sub class contains an action listener which updates
         * the clock drawing and sounds a tick.
         */
        class Time implements ActionListener
        {

            /** Perform an action every 1000 nanoseconds
             * @param e is the action event
             */
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Get the current time
                Date d1 = new Date();
                gc.setTime(d1);
                c.setTime(
                        gc.get(GregorianCalendar.SECOND),
                        gc.get(GregorianCalendar.MINUTE),
                        gc.get(GregorianCalendar.HOUR_OF_DAY));

                //Repaint the paint component
                c.repaint();

                //Play the tick twice whenever the second hand moves
                clip.setFramePosition(0);
                clip.loop(1);
            }
        }
        /** This sub class contains an action listener which will update
         * the position of the clock every 20 nanoseconds.
         */
        class Position implements ActionListener
        {

            /** Perform an action when called through a timer
             * @param e is the action event
             */
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Keep the clock at a fixed position above the cursor
                frame.setLocation(
                        MouseInfo.getPointerInfo().getLocation().x,
                        MouseInfo.getPointerInfo().getLocation().y - 180);
            }
        }

        //Create and start the timers
        Timer t1 = new Timer(1000, new Time());
        Timer t2 = new Timer(20, new Position());
        t1.start();
        t2.start();

        //Add the paint clockcomponent to the frame and show it
        frame.add(c);
        frame.setVisible(true);
    }
}