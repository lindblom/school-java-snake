package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Board is the where the game lives.
 * 
 * @author Christopher Lindblom
 * @version 2013-01-11
 */
public class Board extends JPanel implements Runnable, KeyListener {
    
    private Image mTileImage, mSnakeHeadImage, mSnakeBodyImage, mFoodImage;
    private int mWidth, mHeight;
    private Snake mPlayer;
    private Fruit mFruit;
    private Random mRandom = new Random();
    private int mPoints;
    private ArrayList<Point> mFreeLocations, mAllLocations;
    private SnakeGame mOwner;
    private HighscoreList mHighscore;
    
    private static int INITIAL_DIRECTION = Snake.MOVE_RIGHT;
    
    /**
     * Constructor of the Board.
     * @param inWidth number of tiles (x)
     * @param inHeight number of tiles (y)
     * @param inOwner owning SnakeGame object
     */
    public Board(int inWidth, int inHeight, SnakeGame inOwner) {
        mWidth = inWidth;
        mHeight = inHeight;
        mOwner = inOwner;
        mHighscore = HighscoreList.load(mOwner.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        
        mAllLocations = new ArrayList();
        initAllLocations();
        
        setBackground(Color.black);
        setPreferredSize(new Dimension(inWidth*20, inHeight*20));
        
        setFocusable(true);
        loadGraphics();
        initGame();
    }
    
    /**
     * Loads all the graphics in to memory
     */
    private void loadGraphics() {
        mTileImage = new ImageIcon(this.getClass().getResource("tile.png")).getImage();
        mSnakeHeadImage = new ImageIcon(this.getClass().getResource("snake-head.png")).getImage();
        mSnakeBodyImage = new ImageIcon(this.getClass().getResource("snake-body.png")).getImage();
        mFoodImage = new ImageIcon(this.getClass().getResource("food.png")).getImage();
    }
    
    /**
     * Initializes the game
     */
    private void initGame() {
        // put game in start up state
        resetGame();
        
        // start a thread using this classes run method (for the game loop)
        Thread thread = new Thread(this);
        thread.start();
        
        // set this class as key listener
        addKeyListener(this);
    }
    
    /**
     * Resets the game to default starting state
     */
    private void resetGame() {
        freeAllLocations();
        mPoints = 0;
        mOwner.setCurrentScore(mPoints);
        
        mOwner.setHighscore(mHighscore.getHighestScore());
        mPlayer = new Snake(mWidth / 2, mHeight / 2, INITIAL_DIRECTION);
        mFruit = new Fruit(mRandom.nextInt(mWidth), mRandom.nextInt(mHeight));
        mFreeLocations.removeAll(mPlayer.getLocations());
        mFreeLocations.removeAll(mFruit.getLocations());
    }
    
    /**
     * Handles the painting of the Board object
     * @param g graphics context
     */
    @Override
    public void paint(Graphics g) {
        
        // paint all tiles as empty
        for (Point currentTile : mAllLocations) {
            g.drawImage(mTileImage, currentTile.x * 20, currentTile.y * 20, this);
        }
        
        // paint tail tiles
        for (Point tailTile : mPlayer.getTail()) {
            g.drawImage(mSnakeBodyImage, tailTile.x * 20, tailTile.y * 20, this);
        }
        
        // paint snake head
        g.drawImage(mSnakeHeadImage, mPlayer.getLocation().x * 20, mPlayer.getLocation().y * 20, this);
        
        // paint fruit
        g.drawImage(mFoodImage, mFruit.getLocation().x * 20, mFruit.getLocation().y * 20, this);
        
    }

    /**
     * This is the game loop, name required by the Runnable interface.
     */
    @Override
    public void run() {
        while(true) {
            repaint();
            
            mPlayer.update();
            
            // move player from one side to other if neccesary
            wormHole();
            
            // update free locations
            freeAllLocations();
            mFreeLocations.removeAll(mPlayer.getLocations());
            mFreeLocations.removeAll(mFruit.getLocations());
            
            // check if player collides with fruit
            if(mFruit.collide(mPlayer)) {
                mPlayer.grow();
                mOwner.setCurrentScore(++mPoints);
                mFruit.setLocation(getRandomFreeLocation());
            }
            
            // check if player collides with it self
            if(mPlayer.collide(mPlayer)) {
                handleHighscore();
                showHighscore();
                reviveOrDie();
            }
            
            try {
                // slow down the loop a bit
                Thread.sleep(17);
            } catch (InterruptedException ex) {
                // logs error
                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Lets the user choose to restart or quit.
     */
    private void reviveOrDie() {
        // ask the user if they want to play again
        if(JOptionPane.showConfirmDialog(mOwner, "Vill du spela igen?", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // reset if they do
            resetGame();
        } else {
            // close window if the dont
            mOwner.dispatchEvent(new WindowEvent(mOwner, WindowEvent.WINDOW_CLOSING));
        }
    }
    
    /**
     * Handles dialog with user about new high score.
     */
    private void handleHighscore() {
        // check if we have a new high score
        if(mHighscore.isHighscore(mPoints)) {
            // get the name of the player
            String name = JOptionPane.showInputDialog(mOwner, "Ditt namn: ", 
"Grattis, topplistan!", JOptionPane.PLAIN_MESSAGE);
            
            // add name and points if the user entered something and pressed ok
            if(name != null && name.trim().length() != 0) {
                mHighscore.add(name.trim(), mPoints);
                try {
                    mHighscore.save();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mOwner, "Det gick inte spara topplistan.", "Fel!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
    }
    
    /**
     * Displays high score.
     */
    private void showHighscore() {
        JOptionPane.showMessageDialog(mOwner, mHighscore.toString(), "Topplistan", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Handles typed keys.
     * @param e key event
     */
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
    /**
     * Handles pressed keys.
     * @param e key event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if(KeyEvent.VK_SPACE == key) {
            if(mPlayer.getStopped()) {
                mPlayer.setStopped(false);
            } else {
                mPlayer.setStopped(true);
                showHighscore();
            }
        }
        
        if(KeyEvent.VK_RIGHT == key) {
            mPlayer.setDirection(Snake.MOVE_RIGHT);
        }
        
        if (KeyEvent.VK_LEFT == key) {
            mPlayer.setDirection(Snake.MOVE_LEFT);
        }
        
        if(KeyEvent.VK_UP == key) {
            mPlayer.setDirection(Snake.MOVE_UP);
        }
        
        if (KeyEvent.VK_DOWN == key) {
            mPlayer.setDirection(Snake.MOVE_DOWN);
        }
    }

    /**
     * Handles releaded key events.
     * @param e key event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
    /**
     * Fills allLocations with a point for every location on the board.
     */
    private void initAllLocations() {
        for (int i = 0; i < mWidth; i++) {
            for (int j = 0; j < mHeight; j++) {
                mAllLocations.add(new Point(i, j));
            }
        }
    }
    
    /**
     * Frees all the locations.
     */
    private void freeAllLocations() {
        mFreeLocations = (ArrayList<Point>)mAllLocations.clone();
    }
    
    /**
     * Gets one random location amongst the free ones.
     * @return random free location
     */
    private Point getRandomFreeLocation() {
        return mFreeLocations.get(mRandom.nextInt(mFreeLocations.size() - 1));
    }
    
    /**
     * Makes the worm go from one side of the board to the other, if necessary.
     */
    private void wormHole() {
        if(mPlayer.getLocation().x >= mWidth) {
            mPlayer.setLocation(0, mPlayer.getLocation().y);
        }

        if(mPlayer.getLocation().x < 0) {
            mPlayer.setLocation(mWidth-1, mPlayer.getLocation().y);
        }

        if(mPlayer.getLocation().y >= mHeight) {
            mPlayer.setLocation(mPlayer.getLocation().x, 0);
        }

        if(mPlayer.getLocation().y < 0) {
            mPlayer.setLocation(mPlayer.getLocation().x, mHeight-1);
        }
    }
    
}
