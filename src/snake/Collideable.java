package snake;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Collidable is a simple interface for making object collidable.
 * 
 * @author Christopher Lindblom
 * @version 2013-01-10
 */
public interface Collideable {
    
    /**
     * Gets the current location of the object
     * @return objects location
     */
    Point getLocation();
    
    /**
     * All locations that the object occupy.
     * @return all locations of the object
     */
    ArrayList<Point> getLocations();
    
    /**
     * Checks whether two objects collide.
     * 
     * @param inObject the object that collides
     * @return true if there is a collision
     */
    boolean collide(Collideable inObject);
    
}
