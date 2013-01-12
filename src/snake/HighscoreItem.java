package snake;

import java.io.Serializable;

/**
 * HighscoreItem represents one line in a list of high score.
 * 
 * @author Christopher Lindblom
 * @vesion 2013-01-11
 */
public class HighscoreItem implements Serializable {
    // Instance variables for name and points
    private String mName;
    private int mPoints;

    /**
     * Constructor for the Highscore Class.
     * 
     * @param inName name of the high scorer
     * @param inPoints number of points
     */
    public HighscoreItem(String inName, int inPoints) {
        mName = inName;
        mPoints = inPoints;
    }

    /**
     * Gets the name of the high scorer.
     * @return name of high score holder
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets the points for the high score.
     * @return number of points
     */
    public int getPoints() {
        return mPoints;
    }
    
    /**
     * Returns a string representation of the high score item.
     * @return high score as a string with name and points
     */
    @Override
    public String toString() {
        return mName + " - " + mPoints;
    }
}
