package snake;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Fruit class represents a fruit in the game.
 * 
 * @author Christopher Lindblom
 * @version 2013-01-11
 */
public class Fruit implements Collideable {
    private Point mLocation;

    /**
     * Constructor of the fruit class
     * @param inX x location
     * @param inY y location
     */
    public Fruit(int inX, int inY) {
        mLocation = new Point(inX, inY);
    }

    /**
     * Give the fruit a new location
     * @param inLocation new location
     */
    public void setLocation(Point inLocation) {
        this.mLocation = inLocation;
    }
    
    /**
     * Gets the current location of the fruit
     * @return fruits location
     */
    @Override
    public Point getLocation() {
        return mLocation;
    }
    
    /**
     * All locations that the fruit occupy.
     * @return all locations of the fruit
     */
    @Override
    public ArrayList<Point> getLocations() {
        ArrayList<Point> locations = new ArrayList();
        locations.add(mLocation);
        return locations;
    }
    
    /**
     * Checks whether some other object collide with the fruit.
     * 
     * @param inObject the object that collides
     * @return true if there is a collision
     */
    @Override
    public boolean collide(Collideable inObject) {
        return mLocation.equals(inObject.getLocation());
    }
}
