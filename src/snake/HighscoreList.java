package snake;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * HighscoreList represents a list of highscores.
 * 
 * @author Christopher Lindblom
 * @version 2013-01-11
 */
public class HighscoreList implements Serializable {

    private ArrayList<HighscoreItem> mHighscores;
    private static final String FILE_NAME = "highscore.dat";
    private transient String mFilePath;
    
    /**
     * Constructor for HighscoreList
     * @param inFilePath path to folder
     */
    public HighscoreList(String inFilePath) {
        mHighscores = new ArrayList();
        
        for (int i = 0; i < 10; i++) {
            mHighscores.add(new HighscoreItem("", 0));
        }
        
        mFilePath = fixFilePath(inFilePath);
    }
    
    /**
     * Loads high scores from a path if file can be found,
     * otherwise return a new HighscoreList object with the selected path.
     * @param inPath path to folder that contains high score file
     * @return 
     */
    static public HighscoreList load(String inPath) {
        // load from file
        HighscoreList highscoreList;
        String filePath = fixFilePath(inPath);
        try {
            FileInputStream fis;
            fis = new FileInputStream(filePath + FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            highscoreList = (HighscoreList) ois.readObject();
            highscoreList.setFilePath(filePath);
        } catch (IOException | ClassNotFoundException ex) {
            // create a new high score list if there were a problem
            highscoreList = new HighscoreList(inPath);
        }
        
        return highscoreList;
    }
    
    /**
     * Removes jar file name from the path and fix special characters.
     * 
     * @param inPath path
     * @return path without jar file name
     */
    static private String fixFilePath(String inPath) {
        String result = inPath;
        
        if(inPath.endsWith(".jar")) {
            int indexOfSlash = inPath.lastIndexOf("/");
            result = inPath.substring(0, indexOfSlash + 1);
        }
        
        try {
            // Handle special characters in path
            result = URLDecoder.decode(result, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        
        return result;
    }
    
    /**
     * Saves high score to disk
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void save() throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(mFilePath + FILE_NAME);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
    }
    
    /**
     * Setter for file path
     * @param inPath new file path
     */
    public void setFilePath(String inPath) {
        mFilePath = inPath;
    }
    
    /**
     * Getter for the highest score
     * @return highest score
     */
    public int getHighestScore() {
        return mHighscores.get(0).getPoints();
    }
    
    /**
     * Checks if provided score makes the list.
     * @param inScore score
     * @return result
     */
    public boolean isHighscore(int inScore) {
        return getBeaten(inScore) != null;
    }
    
    
    /**
     * Adds a name and score to the high score list.
     * 
     * @param inName name of the player
     * @param inScore points
     */
    public void add(String inName, int inScore) {
        HighscoreItem beaten = getBeaten(inScore);
        if(beaten != null) {
            mHighscores.add(mHighscores.indexOf(beaten), new HighscoreItem(inName, inScore));
        }
        
        while(mHighscores.size() > 10) {
            mHighscores.remove(10);
        }
    }
    
    /**
     * Gets the first item thats beaten by the provided score.
     * @param inScore points to compare with
     * @return beaten high score item
     */
    private HighscoreItem getBeaten(int inScore) {
        HighscoreItem beatenItem = null;
        
        Iterator iterator = mHighscores.iterator();
        while(beatenItem == null && iterator.hasNext()) {
            HighscoreItem item = (HighscoreItem)iterator.next();
            if(item.getPoints() < inScore) {
                beatenItem = item;
            }
        }
        
        return beatenItem;
    }
    
    /**
     * Returns the high score list as a string.
     * @return high score list
     */
    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < mHighscores.size(); i++) {
            HighscoreItem item = mHighscores.get(i);
            // only include real high scores
            if(item.getPoints() > 0) {
                string += (i + 1) + ": " + item + "\n";
            }
        }
        return string;
    }
}
