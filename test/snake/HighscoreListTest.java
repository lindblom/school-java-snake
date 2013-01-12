package snake;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for HighscoreList
 * 
 * @author Christopher Lindblom
 * @version 2013-01-10
 */
public class HighscoreListTest {
    
    private HighscoreList instance;
    
    public HighscoreListTest() {
        instance = new HighscoreList("");
    }

    /**
     * Test of isHighscore method, of class HighscoreList.
     */
    @Test
    public void testIsHighscore() {
        assertTrue(instance.isHighscore(5));
        assertFalse(instance.isHighscore(-1));
    }
    
    /**
     * Test of GetHighestScore method, of class HighscoreList.
     */
    @Test
    public void testGetHighestScore() {
        assertTrue(instance.getHighestScore() == 0);
        instance.add("Christopher", 5);
        assertTrue(instance.getHighestScore() == 5);
    }
}
