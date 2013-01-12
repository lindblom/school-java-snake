package snake;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * SnakeGame is the window and starting class for the snake game.
 * 
 * @author Christopher Lindblom
 * @version 2013-01-11
 */
public class SnakeGame extends JFrame {

    private JLabel mCurrentScore;
    private JLabel mHighscore;
    
    private final String HIGHSCORE_TEXT = "Bäst hittils: ";
    private final String CURRENT_SCORE_TEXT = "Poäng: ";
    
    /**
     * Constructor for the snake game.
     * @param inWidth number of horizontal tiles
     * @param inHeight number of vertical tiles 
     */
    public SnakeGame(int inWidth, int inHeight) {
        setLayout(new BorderLayout());
        
        JPanel scorePanel = new JPanel(new GridLayout(1, 2));
        
        scorePanel.setBorder(new EmptyBorder(5,5,5,5));
        
        mCurrentScore = new JLabel(CURRENT_SCORE_TEXT + "0");
        mHighscore = new JLabel(HIGHSCORE_TEXT + "0");
        mHighscore.setHorizontalAlignment(SwingConstants.RIGHT);
        
        scorePanel.add(mCurrentScore);
        scorePanel.add(mHighscore);
        
        add(new Board(inWidth, inHeight, this), BorderLayout.CENTER);
        
        add(scorePanel, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Snake");
        pack();
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * Set the on-screen current score display.
     * @param inScore current score
     */
    public void setCurrentScore(int inScore) {
        mCurrentScore.setText(CURRENT_SCORE_TEXT + inScore);
    }
    
    /**
     * Set the on-screen current high score display.
     * @param inScore current high score
     */
    public void setHighscore(int inScore) {
        mHighscore.setText(HIGHSCORE_TEXT + inScore);
    }
    
    /**
     * main method for starting
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SnakeGame(30, 20);
    }
}
