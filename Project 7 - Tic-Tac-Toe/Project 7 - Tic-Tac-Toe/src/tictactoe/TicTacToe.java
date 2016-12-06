package tictactoe;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Project 7 - Tic-Tac-Toe
 * @author Rio Weber
 * @version 1.0
 * 
 * This program creates a graphical representation of the classic game
 * "Tic-Tac-Toe." The game has two player modes and three difficulty modes, and
 * even includes a debug mode. You can choose whether to play against a friend
 * or a computer. When versing the computer, you can set its difficulty to
 * Easy, Medium, or Impossible.
 * 
 * When versing an Easy Difficulty, your opponent will always pick a random open
 * slot.
 * 
 * When versing a Medium Difficulty, your opponent will always attempt to cut
 * you off when you're about to win, but not before finishing one of its own
 * set up ways to win.
 * 
 * The Impossible Difficulty was made to be just that: Impossible. There should
 * be no possible way to beat this difficulty. Because of that, this part of the
 * code is very lengthy.
 */
public class TicTacToe extends JFrame
{
    private final Paint paintComponent;
    private final Mouse mouse;
    private final Cell[][] grid;
    
    private boolean isPlayer1Turn = true;
    private boolean isSinglePlayer = true;
    private boolean isDifficultyEasy = true;
    private boolean isWinState = false;
    private boolean debugMode = false;
    
    private static final int EASY = 0;
    private static final int NORMAL = 1;
    private static final int HARD = 2;
    
    private int difficultyLevel = EASY;
    
    private JDialog difficultyDiag;
    private final JLabel currentPlayer = 
            new JLabel("Current Turn: Player 1");
    private final JLabel currentMode = 
            new JLabel("Current Mode: Single Player, Easy");
    private final JLabel winner = 
            new JLabel("The winner is: ???");
    
    ArrayList<String> winsList = new ArrayList<String>();
    int turns = 0;

    /** Default Constructor
     */
    public TicTacToe()
    {
        setTitle("Tic-Tac-Toe");
        setSize(600, 600);
        setLocationRelativeTo(null); //Puts JFrame in center of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Fill each cell
        grid = new Cell[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                grid[i][j] = new Cell();

        //Create a Menu Bar
        JMenuBar bar = new JMenuBar();
        setJMenuBar(bar);
        
        //Create the "Players" Menu
        JMenu playersMenu = new JMenu("Players");
        bar.add(playersMenu);
        JMenuItem singleItem = new JMenuItem("Single Player");
        JMenuItem multiItem = new JMenuItem("Multiplayer");
        JMenuItem debugItem = new JMenuItem("Debug Mode");
        singleItem.addActionListener(new Click());
        multiItem.addActionListener(new Click());
        debugItem.addActionListener(new Click());
        playersMenu.add(singleItem);
        playersMenu.add(multiItem);
        playersMenu.add(debugItem);
        
        //Create the "Cusomization" Menu
        JMenu menu = new JMenu("Customization");
        bar.add(menu);
        JMenuItem difficultyItem = new JMenuItem("CPU Difficulty");
        difficultyItem.addActionListener(new Click());
        menu.add(difficultyItem);
        
        //Set up the paint component and mouselisteners
        paintComponent = new Paint();
        mouse = new Mouse();
        paintComponent.addMouseListener(mouse);
        paintComponent.addMouseMotionListener(mouse);
        
        //Create the Panels
        JPanel mainPanel = new JPanel();
        JPanel northPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel southPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        northPanel.setLayout(new GridLayout(1,3));
        centerPanel.setLayout(new GridLayout(1,1));
        southPanel.setLayout(new BorderLayout());
        
        //Fill Northern Subpanel
        JPanel subWestPanel = new JPanel();
        JPanel subCenterPanel = new JPanel();
        JPanel subEastPanel = new JPanel();
        subCenterPanel.add(new JLabel("Welcome to Tic-Tac-Toe!"));
        subWestPanel.add(currentPlayer);
        subEastPanel.add(currentMode);
        
        //Add Subpanels to North Panel
        northPanel.add(subWestPanel, BorderLayout.WEST);
        northPanel.add(subCenterPanel, BorderLayout.CENTER);
        northPanel.add(subEastPanel, BorderLayout.EAST);
        
        //Fill other Panels
        centerPanel.add(paintComponent);
        JPanel subSouthPanel = new JPanel();
        JPanel subNorthPanel = new JPanel();
        subNorthPanel.add(winner);
        JButton paly = new JButton("Play Again");
        JButton quit = new JButton("Quit");
        paly.addActionListener(new Click());
        quit.addActionListener(new Click());
        subSouthPanel.add(paly);
        subSouthPanel.add(quit);
        southPanel.add(subNorthPanel, BorderLayout.NORTH);
        southPanel.add(subSouthPanel, BorderLayout.SOUTH);
        
        //Add subpanels to the main panel
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        //Create the clock
        Timer clock = new Timer(17, new Threader());
        clock.start();
        
        //Set the JFrame to visible
        setVisible(true);
    }
    
    /** Reset the Game.
     * Clears the memory, reseting the game.
     */
    private void clear()
    {
        turns = 0; //reset turns
        HardAI.clearMemory(); //clear AI variables
        isPlayer1Turn = true; //player 1 goes first
        isWinState = false; //reset the win variable
        updateTurnLabel(); //sets the turn back to player 1
        updateModeLabel(); //refreshes the mode
        winsList = new ArrayList<String>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                grid[i][j].setValue(Cell.EMPTY); //reset the cells
        turns--; //used as offset the cell because checkForWin() adds 1
        checkForWin(); //Keeps track of turns
        repaint(); //update the paint component
    }
    
    /** Check For A Win.
     * Check each possible way for either player to win
     */
    private void checkForWin()
    {
        //Reset the ArrayList of possible wins
        winsList = new ArrayList<String>();
        int[] cells = new int[3];
        turns++;
        //System.out.println(turns);
        
        //Check Vertical Columns
        for (int i = 0; i < 3; i++)
        {
            //Go through each row
            for (int j = 0; j < 3; j++)
                cells[j]=grid[i][j].getValue();
            if (allAreEqual(cells)) winsList.add(cells[0]+"Ver"+i);
        }
        
        //Check Horizontal Rows
        for (int i = 0; i < 3; i++)
        {
            //Go through each column
            for (int j = 0; j < 3; j++)
                cells[j]=grid[j][i].getValue();
            if (allAreEqual(cells)) winsList.add(cells[0]+"Hor"+i);
        }
        
        //Check Diagonal TopLeft to BottomRight
        for (int i = 0; i < 3; i++)
            cells[i] = grid[i][i].getValue();
        if (allAreEqual(cells)) winsList.add(cells[0]+"Dia0");
        
        //Check Diagonal BottomLeft to TopRight
        for (int i = 0; i < 3; i++)
            cells[i] = grid[i][2-i].getValue();
        if (allAreEqual(cells)) winsList.add(cells[0]+"Dia1");
        
        //Check to see if there are no turns left
        if (turns >= 9) winner.setText("The winner is: Nobody");
        //Update win label
        if (winsList.isEmpty() && turns < 9)
        {
            isWinState = false;
            //System.out.print("None");
            winner.setText("The winner is: ???");
        }
        else if (!winsList.isEmpty())
        {
            isWinState = true;
            int win = Integer.parseInt(winsList.get(0).substring(0,1));
            if (win == Cell.X) winner.setText("The winner is: Player 1");
            if (win == Cell.O) winner.setText("The winner is: Player 2");
        }
        //System.out.println();
            
    }
    
    /** Check the Cells.
     * Check to see if the three selected for a possible win are all the same.
     * @param cells is the array of three cells being checked
     * @return 
     */
    private boolean allAreEqual(int[] cells)
    {
        return cells[0]==cells[1]&&cells[1]==cells[2]&&cells[0]!=Cell.EMPTY;
    }
    
    /** Update the Mode Text.
     */
    private void updateModeLabel()
    {
        String text;
        if (!debugMode)
        {
            if (!isSinglePlayer)
                text = "Current Mode: Multiplayer";
            else 
            {
                if (difficultyLevel == EASY)
                    text = "Current Mode: Single Player, Easy";
                else if (difficultyLevel == NORMAL)
                    text = "Current Mode: Single Player, Normal";
                else
                    text = "Current Mode: Single Player, Impossible";
            }
        } else text = "Current Mode: Debug Mode";
            
        currentMode.setText(text);
        
    }
    
    /** Update the Turn Text.
     */
    private void updateTurnLabel()
    {
        if (isPlayer1Turn) 
            currentPlayer.setText("Current Turn: Player 1");
        else currentPlayer.setText("Current Turn: Player 2");
    }

    /** Subclass for a Cell.
     * There are 9 cells total in a Tic-Tac-Toe board. Each one is always in
     * the state of EMPTY, X, or O.
     */
    class Cell
    {

        private int value;

        public static final int EMPTY = 0;
        public static final int X = 1;
        public static final int O = 2;

        /** Default Constructor
         */
        public Cell()
        {
            value = Cell.EMPTY;
        }

        /** Get the Cell's Value
         * @return the value as an integer
         */
        public int getValue()
        {
            return value;
        }

        /** Set the Cell's Value.
         * @param value is the integer representation for the cell's attribute
         */
        public void setValue(int value)
        {
            this.value = value;
        }
    }

    /** Subclass for the PaintComponent.
     * This class keeps charge of all of the graphical aspects for the JFrame.
     * It also implements proportions to allow the game to be resizable.
     */
    class Paint extends JComponent
    {
        private int screenWidth;
        private int screenHeight;
        //Grid To Screen Proportions
        private double GTSPropX;
        private double GTSPropY;
        
        //Static boolean variables used to determine either X or Y
        public static final boolean GET_X = true;
        public static final boolean GET_Y = false;

        @Override
        public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            //Find Screen Size
            screenWidth = getWidth();
            screenHeight = getHeight();
            //Find Proporitons to draw with
            GTSPropX = (double) screenWidth / 3;
            GTSPropY = (double) screenHeight / 3;

            //Draw Grid Lines
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                {
                    //Horizontal Lines
                    g2.drawLine(
                            0, 
                            (int) (j * GTSPropY),
                            screenWidth, 
                            (int) (j * GTSPropY));
                    //Vertical Lines
                    g2.drawLine(
                            (int) (i * GTSPropX), 
                            0, 
                            (int) (i * GTSPropX), 
                            screenHeight);
                }
            //Draw Individual Cells
            for (int i = 0; i<3; i++)
                for (int j = 0; j<3; j++)
                {
                    //If current cell is an X
                    if (grid[i][j].getValue()==Cell.X)
                    {
                        g2.drawLine(
                                (int)(i*GTSPropX)+5,        //x1
                                (int)(j*GTSPropY)+5,        //y1
                                (int)((i+1)*GTSPropX)-5,    //x2
                                (int)((j+1)*GTSPropY)-5);   //y2   
                        g2.drawLine(
                                (int)((i+1)*GTSPropX)-5,    //x1
                                (int)(j*GTSPropY)+5,        //y1
                                (int)(i*GTSPropX)+5,        //x2
                                (int)((j+1)*GTSPropY)-5);   //y2   
                        
                    } else if (grid[i][j].getValue()==Cell.O)
                    {
                        g2.drawOval(
                                (int)(i*GTSPropX)+5, 
                                (int)(j*GTSPropY)+5, 
                                (int)GTSPropX-10, 
                                (int)GTSPropY-10);
                    }
                }
            
            //Draw Border
            g2.drawRect(0, 0, screenWidth - 1, screenHeight - 1);
            
            //Draw Winning Lines
            if (!winsList.isEmpty())
                for (String t : winsList)
                {
                    String direction = t.substring(1,4);
                    int part = Integer.parseInt(t.substring(4,5));
                    g2.setStroke(new BasicStroke(10));
                    //Use a switch statement to determine which kind of win
                    switch (direction)
                    {
                        case "Hor":
                            g2.drawLine(
                                    (int)(GTSPropX/2),
                                    (int)((part+1)*GTSPropY-GTSPropY/2),
                                    (int)(3*GTSPropX-GTSPropX/2), 
                                    (int)((part+1)*GTSPropY-GTSPropY/2));
                            break;
                        case "Ver":
                            g2.drawLine(
                                    (int)((part+1)*GTSPropX-GTSPropX/2),
                                    (int)(GTSPropY/2),
                                    (int)((part+1)*GTSPropX-GTSPropX/2), 
                                    (int)(3*GTSPropY-GTSPropY/2));
                            break;
                        case "Dia":
                            if (part==0)
                            {
                                g2.drawLine(
                                    (int)(GTSPropX/2),
                                    (int)(GTSPropY/2),
                                    (int)(3*GTSPropX-GTSPropX/2), 
                                    (int)(3*GTSPropY-GTSPropY/2));
                            } else
                            {
                                g2.drawLine(
                                    (int)(3*GTSPropX-GTSPropX/2),
                                    (int)(GTSPropY/2),
                                    (int)(GTSPropX/2), 
                                    (int)(3*GTSPropY-GTSPropY/2));
                            }
                            break;
                    }
                }
        }
        
        /** Return Proportion of Grid:Screen.
         * @param whichProportion is whether it is X or Y
         * @return the correct proportion
         */
        public double getGridToScreen(boolean whichProportion)
        {
            return (whichProportion)?GTSPropX:GTSPropY;
        }
        
        /** Return Proportion of Screen:Grid.
        * @param whichProportion is whether it is X or Y
        * @return the correct proportion
        */
        public double getScreenToGrid(boolean whichProportion)
        {
            return (whichProportion)?(1/GTSPropX):(1/GTSPropY);
        }
    }

    /** Subclass for a custom Thread.
     */
    class Threader implements ActionListener
    {
        private boolean currentCellToggled = false;
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            
            //Make sure that this only happens on the click of the mouse
            if (mouse.mouseHeld && !currentCellToggled)
            {
                //Find out which cell is being clicked
                int x = (int) (mouse.x_Pos * 
                        paintComponent.getScreenToGrid(Paint.GET_X));
                int y = (int) (mouse.y_Pos * 
                        paintComponent.getScreenToGrid(Paint.GET_Y));
                
                //If we are in debug mode
                if (debugMode)
                {
                    //Set the cell to a specific value
                    if (mouse.buttonNum == MouseEvent.BUTTON1)
                    {
                        grid[x][y].setValue(Cell.X);
                    } else if (mouse.buttonNum == MouseEvent.BUTTON3)
                    {
                        grid[x][y].setValue(Cell.O);
                    } else if (mouse.buttonNum == MouseEvent.BUTTON2)
                    {
                        grid[x][y].setValue(Cell.EMPTY);
                    }
                    checkForWin();
                    updateTurnLabel();
                //If we are in a single player game
                } else if (isSinglePlayer && !isWinState)
                {
                    //Make sure the selected cell is EMPTY
                    if (grid[x][y].getValue()==Cell.EMPTY
                            && mouse.buttonNum == MouseEvent.BUTTON1
                            && isPlayer1Turn)
                    {
                        grid[x][y].setValue(Cell.X);
                        checkForWin();
                        updateTurnLabel();
                        if (!isWinState) isPlayer1Turn = false;
                    }
                    
                //If we are in a multiplayer game
                } else if (!isSinglePlayer && !isWinState)
                {
                    //Make sure the selected cell is EMPTY
                    if (grid[x][y].getValue()==Cell.EMPTY
                            &&mouse.buttonNum == MouseEvent.BUTTON1)
                    {
                        if (isPlayer1Turn)
                        {
                            grid[x][y].setValue(Cell.X);
                        } else
                        {
                            grid[x][y].setValue(Cell.O);
                        }
                    
                    checkForWin();
                    updateTurnLabel();
                    if (!isWinState) isPlayer1Turn = !isPlayer1Turn;
                    }
                }
                
                currentCellToggled = true;
                
            //On the Release of the Mouse
            } else if (isSinglePlayer && !isPlayer1Turn && !isWinState)
            {
                        
                if (difficultyLevel==EASY) 
                {
                    try{Thread.sleep(400);}
                        catch(InterruptedException ex){}
                    easyAI();
                }
                else if (difficultyLevel==NORMAL)
                {
                    try{Thread.sleep(600);}
                        catch(InterruptedException ex){}
                    normalAI();
                }
                else
                {
                    try{Thread.sleep(800);}
                        catch(InterruptedException ex){}
                    HardAI.takeTurn(grid, turns);
                }
                isPlayer1Turn = true;
                checkForWin();
                updateTurnLabel();
            } else if (!mouse.mouseHeld)
            {
                currentCellToggled = false;
            }
            repaint();
        }
    }
    
    /** A Subclass for the Mouse
     */
    class Mouse extends MouseAdapter implements MouseMotionListener
    {
        public boolean mouseHeld = false;
        public int x_Pos = 0;
        public int y_Pos = 0;
        public int buttonNum = 0;

        @Override
        public void mouseMoved(MouseEvent e)
        {
            x_Pos = e.getX();
            y_Pos = e.getY();
        }
        
        @Override
        public void mousePressed(MouseEvent e)
        {
            mouseHeld = true;
            buttonNum = e.getButton();
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            mouseHeld = false;
        }
    }
    
    /** A Subclass that contains the code for each button/menu in the program.
     */
    class Click implements ActionListener
    {
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String text;
            //Grab the text of the button or menu
            if (e.getSource() instanceof JButton) 
                text = ((JButton)e.getSource()).getText();
            else text = ((JMenuItem)e.getSource()).getText();
            
            //Determine what to do based off of the text
            switch (text)
            {
                case "Quit":
                    System.exit(0);
                case "Play Again":
                    clear();
                    break;
                case "CPU Difficulty":
                    difficultyDiag = new JDialog();
                    difficultyDiag.setSize(390,200);
                    difficultyDiag.setLocationRelativeTo(null); 
                    difficultyDiag.setAlwaysOnTop(true);
                    difficultyDiag.setResizable(false);
                    difficultyDiag.setTitle("Set CPU Difficulty");
                    
                    JPanel p = new JPanel();
                    
                    JButton easy = new JButton("Easy");
                    JButton normal = new JButton("Normal");
                    JButton hard = new JButton("Impossible");
                    easy.setPreferredSize(new Dimension(120,160));
                    normal.setPreferredSize(new Dimension(120,160));
                    hard.setPreferredSize(new Dimension(120,160));
                    easy.addActionListener(new Click());
                    normal.addActionListener(new Click());
                    hard.addActionListener(new Click());
                    p.add(easy);
                    p.add(normal);
                    p.add(hard);
                       
                    difficultyDiag.add(p);
                    difficultyDiag.setVisible(true);
                    break;
                case "Easy":
                    difficultyDiag.setVisible(false);
                    isSinglePlayer = true;
                    difficultyLevel = EASY;
                    debugMode = false;
                    clear();
                    break;
                case "Normal":
                    difficultyDiag.setVisible(false);
                    isSinglePlayer = true;
                    difficultyLevel = NORMAL;
                    debugMode = false;
                    clear();
                    break;
                case "Impossible":
                    difficultyDiag.setVisible(false);
                    isSinglePlayer = true;
                    difficultyLevel = HARD;
                    debugMode = false;
                    clear();
                    break;
                case "Single Player":
                    isSinglePlayer = true;
                    debugMode = false;
                    clear();
                    break;
                case "Multiplayer":
                    isSinglePlayer = false;
                    debugMode = false;
                    clear();
                    break;
                case "Debug Mode":
                    debugMode = !debugMode;
                    updateModeLabel();
                    if (debugMode == false) clear();
                    break;
            }
        }
    }
    
    /** Code for the EasyAI.
     * This lets the AI pick a random spot that isn't taken.
     */
    void easyAI()
    {
        ArrayList<Cell> available = new ArrayList<Cell>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (grid[i][j].getValue()==Cell.EMPTY) 
                    available.add(grid[i][j]);
        Random rand = new Random();
        if (!available.isEmpty())
        available.get(rand.nextInt(available.size())).setValue(Cell.O);
    }
    
    /** Code for the NormalAI.
     * This lets the AI check for potential winning moves,
     * then it checks for any possible way for the opponent to win
     * then it resorts to picking a random spot.
     */
    void normalAI()
    {
        if (!HardAI.checkForWin(grid).equals("None"))
            HardAI.fixProblem(grid,HardAI.checkForWin(grid),Cell.O);
        else if (!HardAI.checkForProblem(grid).equals("None"))
            HardAI.fixProblem(grid,HardAI.checkForProblem(grid),Cell.O);
        else
            easyAI();
    }
    
    /** Code for the ImpossibleAI.
     * This long part of the code determines where the computer should go in
     * order to never lose.
     */
    static class HardAI
    {
        public static final int UNDECIDED = 0;
        public static final int EDGE = 1;
        public static final int CORNER = 2;
        public static final int CENTER = 3;
        
        public static final int CELL_NOPE = -1;
        public static final int CELL_LEFT = 0;
        public static final int CELL_MIDDLE = 1;
        public static final int CELL_RIGHT = 2;
        
        static int strategy = UNDECIDED;
        static int choice1 = 0;
        static int choice2 = 0;
        
        /** The main method for the AI. 
         * It determines where the AI should go on the current turn.
         * @param grid takes the current layout of the board
         * @param turns takes the current turn
         */
        static void takeTurn(Cell[][] grid, int turns)
        {
            //First Round
            if (strategy == 0)
            {
                //If the player chooses the center
                if (grid[1][1].getValue()==Cell.X)
                {
                    strategy = CENTER;
                    grid[0][2].setValue(Cell.O);
                //If the player chooses a corner
                } else if (
                        grid[0][0].getValue()==Cell.X||
                        grid[0][2].getValue()==Cell.X||
                        grid[2][0].getValue()==Cell.X||
                        grid[2][2].getValue()==Cell.X)
                {
                    if (grid[0][0].getValue()==Cell.X) choice1 = 1;
                    if (grid[0][2].getValue()==Cell.X) choice1 = 7;
                    if (grid[2][0].getValue()==Cell.X) choice1 = 3;
                    if (grid[2][2].getValue()==Cell.X) choice1 = 9;
                    strategy = CORNER;
                    grid[1][1].setValue(Cell.O);
                }
                //If the player chooses an edge
                else 
                {
                    if (grid[1][0].getValue()==Cell.X) choice1 = 2;
                    if (grid[0][1].getValue()==Cell.X) choice1 = 4;
                    if (grid[2][1].getValue()==Cell.X) choice1 = 6;
                    if (grid[1][2].getValue()==Cell.X) choice1 = 8;
                    strategy = EDGE;
                    grid[1][1].setValue(Cell.O);
                }
            }
            //If the opponet first chose the Center, do this strategy
            else if (strategy == CENTER)
            {
                if (turns==3)
                {
                    //If player chooses opposing corner
                    if (grid[2][0].getValue()==Cell.X)
                    {
                        choice1 = 7;
                        grid[0][0].setValue(Cell.O);
                    }
                    else 
                    {
                        if (!checkForWin(grid).equals("None"))
                            fixProblem(grid,checkForWin(grid),Cell.O);
                        else if (!checkForProblem(grid).equals("None"))
                            fixProblem(grid,checkForProblem(grid),Cell.O);
                        else
                            randomChoice(grid);
                    }
                    
                }
                if (turns==5)
                {
                    //If player previously chose opposing corner
                    if (choice1 == 7)
                    {
                        if (grid[0][1].getValue()==Cell.X)
                        {
                            choice2 = 1;
                            grid[2][1].setValue(Cell.O);
                        }
                        else grid[0][1].setValue(Cell.O);
                    }
                    else
                    {
                        if (!checkForWin(grid).equals("None"))
                            fixProblem(grid,checkForWin(grid),Cell.O);
                        else if (!checkForProblem(grid).equals("None"))
                            fixProblem(grid,checkForProblem(grid),Cell.O);
                        else
                            randomChoice(grid);
                    }
                }
                if (turns==7)
                {
                    if (choice2 == 1)
                    {
                        if (grid[1][0].getValue()==Cell.X)
                            grid[1][2].setValue(Cell.O);
                        else grid[1][0].setValue(Cell.O);
                    } else
                    {
                        if (!checkForWin(grid).equals("None"))
                            fixProblem(grid,checkForWin(grid),Cell.O);
                        else if (!checkForProblem(grid).equals("None"))
                            fixProblem(grid,checkForProblem(grid),Cell.O);
                        else
                            randomChoice(grid);
                    }
                }
            }
            //If the opponet first chose a Corner, do this strategy
            else if (strategy == CORNER)
                {
                if (turns==3)
                {
                    //Find out what the player's second choice was
                    if (grid[0][0].getValue()==Cell.X&&choice1!=1) choice2 = 1;
                    if (grid[0][2].getValue()==Cell.X&&choice1!=7) choice2 = 7;
                    if (grid[2][0].getValue()==Cell.X&&choice1!=3) choice2 = 3;
                    if (grid[2][2].getValue()==Cell.X&&choice1!=9) choice2 = 9;
                    
                    if (choice2!=0&&
                            getOppositeCell(grid,choice2).getValue()==Cell.X)
                    {
                        grid[0][1].setValue(Cell.O);
                        choice2 = 10;
                    } else
                    {
                        if (!checkForWin(grid).equals("None"))
                            fixProblem(grid,checkForWin(grid),Cell.O);
                        else if (!checkForProblem(grid).equals("None"))
                            fixProblem(grid,checkForProblem(grid),Cell.O);
                        else
                            randomChoice(grid);
                    }
                }
                if (turns==5)
                {
                    if (!checkForWin(grid).equals("None"))
                        fixProblem(grid,checkForWin(grid),Cell.O);
                    else if (!checkForProblem(grid).equals("None"))
                        fixProblem(grid,checkForProblem(grid),Cell.O);
                    else
                        randomChoice(grid);
                }
                if (turns==7)
                {
                    if (!checkForWin(grid).equals("None"))
                            fixProblem(grid,checkForWin(grid),Cell.O);
                    else if (!checkForProblem(grid).equals("None"))
                        fixProblem(grid,checkForProblem(grid),Cell.O);
                    else
                        randomChoice(grid);
                }
            }
            //If the opponent first chose an edge
            else if (strategy == EDGE)
            {
                //On turn 3
                if (turns==3)
                {
                    System.out.println("HERE");
                    //Find out where the opponent went
                    if (grid[0][0].getValue()==Cell.X) choice2 = 1;
                    if (grid[0][2].getValue()==Cell.X) choice2 = 7;
                    if (grid[2][0].getValue()==Cell.X) choice2 = 3;
                    if (grid[2][2].getValue()==Cell.X) choice2 = 9;
                    if (grid[1][0].getValue()==Cell.X&&choice1!=2) choice2 = 2;
                    if (grid[0][1].getValue()==Cell.X&&choice1!=4) choice2 = 4;
                    if (grid[2][1].getValue()==Cell.X&&choice1!=6) choice2 = 6;
                    if (grid[1][2].getValue()==Cell.X&&choice1!=8) choice2 = 8;
                    
                    //Place the next piece very carefully
                    //Check if we should pick spot 1
                    if ((choice1==4&&(choice2==2||choice2==3))||
                            (choice1==2&&(choice2==4||choice2==7)))
                            grid[0][0].setValue(Cell.O);
                    //Check if we should pick spot 7
                    else if ((choice1==4&&(choice2==8||choice2==9))||
                            (choice1==8&&(choice2==4||choice2==1)))
                            grid[0][2].setValue(Cell.O);
                    //Check if we should pick spot 9
                    else if ((choice1==2&&(choice2==6||choice2==9))||
                            (choice1==6&&(choice2==2||choice2==1)))
                            grid[2][0].setValue(Cell.O);
                    //Check if we should pick spot 3
                    else if ((choice1==6&&(choice2==8||choice2==7))||
                            (choice1==8&&(choice2==6||choice2==3)))
                            grid[2][2].setValue(Cell.O);
                    else
                        randomChoice(grid);
                }
                if (turns==5)
                {
                    if (!checkForWin(grid).equals("None"))
                            fixProblem(grid,checkForWin(grid),Cell.O);
                        else if (!checkForProblem(grid).equals("None"))
                            fixProblem(grid,checkForProblem(grid),Cell.O);
                        else
                            randomChoice(grid);
                }
                if (turns==7)
                {
                    if (!checkForWin(grid).equals("None"))
                            fixProblem(grid,checkForWin(grid),Cell.O);
                        else if (!checkForProblem(grid).equals("None"))
                            fixProblem(grid,checkForProblem(grid),Cell.O);
                        else
                            randomChoice(grid);
                }
            }
        }
        /** Clears the memory of the AI.
         */
        static void clearMemory()
        {
            strategy = UNDECIDED;
            choice1 = UNDECIDED;
            choice2 = UNDECIDED;
        }
        
        /** Choose a random spot on the board that isn't taken.
         * @param grid is the current state of the game board
         */
        static void randomChoice(Cell[][] grid)
        {
            ArrayList<Cell> available = new ArrayList<Cell>();
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (grid[i][j].getValue()==Cell.EMPTY) 
                        available.add(grid[i][j]);
            Random rand = new Random();
            if (!available.isEmpty())
            available.get(rand.nextInt(available.size())).setValue(Cell.O);
        }
        
        /** Get the Cell on the opposite side.
         * @param grid the current state of the game board
         * @param i the particular cell
         * @return the cell on the opposite side
         */
        static Cell getOppositeCell(Cell grid[][], int i)
        {
            int x = (i-1)%3;
            int y = (i-1)/3;
            return grid[2-x][2-y];
        }
        
        /** Check for any possible ways to lose.
         * @param grid is the current state of the game board
         * @return a String containing any or no problems
         */
        private static String checkForProblem(Cell grid[][])
        {
            int[] cells = new int[3];
            String problem = "None";

            //Check Horizontal Row
            for (int i = 0; i < 3; i++)
            {
                //Go through each column
                for (int j = 0; j < 3; j++)
                    cells[j]=grid[i][j].getValue();
                if (getPotentialProblem(cells)>=0) 
                    problem = "Hor"+i+""+getPotentialProblem(cells);
            }

            //Check Vertical columns
            for (int i = 0; i < 3; i++)
            {
                //Go through each row
                for (int j = 0; j < 3; j++)
                    cells[j]=grid[j][i].getValue();
                if (getPotentialProblem(cells)>=0)
                    problem = "Ver"+i+""+getPotentialProblem(cells);
            }

            //Check Diagonal TopLeft to BottomRight
            for (int i = 0; i < 3; i++)
                cells[i] = grid[i][i].getValue();
            if (getPotentialProblem(cells)>=0)
                problem = "Dia0"+getPotentialProblem(cells);

            //Check Diagonal BottomLeft to TopRight
            for (int i = 0; i < 3; i++)
                cells[i] = grid[i][2-i].getValue();
            if (getPotentialProblem(cells)>=0) 
                problem = "Dia1"+getPotentialProblem(cells);

            return problem;
        }
        /** Check for any possible ways to win.
         * @param grid is the current state of the game board
         * @return a String with any or no ways to win
         */
        private static String checkForWin(Cell grid[][])
        {
            int[] cells = new int[3];
            String problem = "None";

            //Check Horizontal Row
            for (int i = 0; i < 3; i++)
            {
                //Go through each column
                for (int j = 0; j < 3; j++)
                    cells[j]=grid[i][j].getValue();
                if (getPotentialWin(cells)>=0) 
                    problem = "Hor"+i+""+getPotentialWin(cells);
            }

            //Check Vertical columns
            for (int i = 0; i < 3; i++)
            {
                //Go through each row
                for (int j = 0; j < 3; j++)
                    cells[j]=grid[j][i].getValue();
                if (getPotentialWin(cells)>=0)
                    problem = "Ver"+i+""+getPotentialWin(cells);
            }

            //Check Diagonal TopLeft to BottomRight
            for (int i = 0; i < 3; i++)
                cells[i] = grid[i][i].getValue();
            if (getPotentialWin(cells)>=0)
                problem = "Dia0"+getPotentialWin(cells);

            //Check Diagonal BottomLeft to TopRight
            for (int i = 0; i < 3; i++)
                cells[i] = grid[i][2-i].getValue();
            if (getPotentialWin(cells)>=0) 
                problem = "Dia1"+getPotentialWin(cells);

            return problem;
        }

        /**
         * Find how the opponent can win and block him.
         * @param cells is the selection of three slots that is being checked
         * @return an integer value of how to block it
         */
        private static int getPotentialProblem(int[] cells)
        {
            //First two are x's [X][X][ ]
            if (cells[0]==cells[1]&&cells[1]==Cell.X&&cells[2]==Cell.EMPTY)
                return CELL_RIGHT;
            //Last two are x's [ ][X][X]
            else if (cells[1]==cells[2]&&cells[1]==Cell.X&&cells[0]==Cell.EMPTY)
                return CELL_LEFT;
            //Both sides are x's [X][ ][X]
            else if (cells[0]==cells[2]&&cells[0]==Cell.X&&cells[1]==Cell.EMPTY)
                return CELL_MIDDLE;
            //While one of the three previous conditions were correct, the
            //"Empty" cell was already filled in by the Player [X][X][O]
            else
                return CELL_NOPE;
        }
        
        /**Find how the CPU can win and ensure victory
         * @param cells is the selection of three slots to be checked [X][X][ ]
         * @return an integer value of how to block it
         */
        private static int getPotentialWin(int[] cells)
        {
            //First two are O's [O][O][ ]
            if (cells[0]==cells[1]&&cells[1]==Cell.O&&cells[2]==Cell.EMPTY)
                return CELL_RIGHT;
            //Last two are O's [ ][O][O]
            else if (cells[1]==cells[2]&&cells[1]==Cell.O&&cells[0]==Cell.EMPTY)
                return CELL_LEFT;
            //Both sides are O's [O][ ][O]
            else if (cells[0]==cells[2]&&cells[0]==Cell.O&&cells[1]==Cell.EMPTY)
                return CELL_MIDDLE;
            //While one of the three previous conditions were correct, the
            //"Empty" cell was already filled in by the CPU [X][O][O]
            else
                return CELL_NOPE;
        }
        
        /**
         * 
         * @param grid contains the game board
         * @param problem contains the string of problem data
         * @param cellValue is either the player of the computer
         */
        private static void fixProblem(Cell[][] grid, String problem, 
                int cellValue)
        {
            System.out.println(problem);
            int blockWhere = Integer.parseInt(problem.substring(4,5));
            int row = Integer.parseInt(problem.substring(3,4));
            switch (problem.substring(0,3))
            {
                case "Hor":
                    if (blockWhere == CELL_LEFT)
                        grid[row][CELL_LEFT].setValue(cellValue);
                    else if (blockWhere == CELL_RIGHT)
                        grid[row][CELL_RIGHT].setValue(cellValue);
                    else 
                        grid[row][CELL_MIDDLE].setValue(cellValue);
                    break;
                case "Ver":
                    if (blockWhere == CELL_LEFT)
                        grid[CELL_LEFT][row].setValue(cellValue);
                    else if (blockWhere == CELL_RIGHT)
                        grid[CELL_RIGHT][row].setValue(cellValue);
                    else
                        grid[CELL_MIDDLE][row].setValue(cellValue);
                    break;
                case "Dia":
                    if (row == 0)
                    {
                        if (blockWhere == CELL_LEFT)
                            grid[0][0].setValue(cellValue);
                        else if (blockWhere == CELL_RIGHT)
                            grid[2][2].setValue(cellValue);
                        else
                            grid[1][1].setValue(cellValue);
                    }
                    else
                    {
                        if (blockWhere == CELL_LEFT)
                            grid[0][2].setValue(cellValue);
                        else if (blockWhere == CELL_RIGHT)
                            grid[2][0].setValue(cellValue);
                        else
                            grid[1][1].setValue(cellValue);
                    }
                    break;
            }
        }
    }
}