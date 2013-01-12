package snake;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Snake class represents the snake in the snake game.
 * 
 * @author Christopher Lindblom
 * @version 2013-01-10
 */
public class Snake implements Collideable {
    public final static int MOVE_UP = 1;
    public final static int MOVE_DOWN = 2;
    public final static int MOVE_LEFT = 3;
    public final static int MOVE_RIGHT = 4;
    
    private final static int INITIAL_LENGTH = 3;
    private final static int INITIAL_SPEED = 50;
    
    private boolean mStopped = false;
    private int mDirection, mNextDirection, mMoveEvery, mLength;
    private Point mLocation;
    private ArrayList<Point> mTail;
    private long mLastMove;
    
    /**
     * Construcor for the Snake class
     * @param inX horizontal position
     * @param inY vertical position
     * @param inInitialDirection the snakes direction
     */
    public Snake(int inX, int inY, int inInitialDirection) {
        mLocation = new Point(inX,inY);
        mDirection = mNextDirection = inInitialDirection;
        mMoveEvery = 100 - INITIAL_SPEED;
        mLength = INITIAL_LENGTH;
        mTail = new ArrayList();
        mLastMove = System.currentTimeMillis();
    }
    
    /**
     * Gets the current location of the snake
     * @return snakes location
     */
    @Override
    public Point getLocation() {
        return mLocation;
    }
    
    /**
     * Give the snake a new location
     * @param inX horizontal location
     * @param inY vertical location
     */
    public void setLocation(int inX, int inY) {
        mLocation.x = inX;
        mLocation.y = inY;
    }
    
    /**
     * Make the snake grow.
     */
    public void grow() {
        mLength++;
    }
    
    /**
     * Get the tail
     * @return tail
     */
    public Point[] getTail() {
        return mTail.toArray(new Point[0]);
    }
    
    /**
     * Set the direction for the snake.
     * @param inDirection new direction
     */
    public void setDirection(int inDirection) {
        int combo = this.mDirection + inDirection;
        if (combo != 3 && combo != 7 && !mStopped) {
            this.mNextDirection = inDirection;
        }
    }
    
    /**
     * Setter for stopped
     * @param inStopped stop?
     */
    public void setStopped(boolean inStopped) {
        mStopped = inStopped;
    }
    
    /**
     * Getter for stopped
     * @return true if snake is stopped
     */
    public boolean getStopped() {
        return mStopped;
    }
    
    /**
     * Updates the state of the snake.
     * Usually called once per iteration in the game loop.
     */
    public void update() {
        long delta = System.currentTimeMillis() - mLastMove;
        if( mMoveEvery < delta) {
            mLastMove = System.currentTimeMillis();
            
            if(!mStopped) {
                updateTail();

                switch(mDirection) {
                    case MOVE_DOWN:
                        mLocation.y++;
                        break;
                    case MOVE_LEFT:
                        mLocation.x--;
                        break;
                    case MOVE_RIGHT:
                        mLocation.x++;
                        break;

                    case MOVE_UP:
                        mLocation.y--;
                        break;
                }
            }
            
            mDirection = mNextDirection;
        }
    }
    
    /**
     * Updates the tail.
     */
    private void updateTail() {
        mTail.add(new Point(mLocation.x, mLocation.y));
            
        if(mTail.size() > mLength) {
            mTail.remove(0);
        }
    }

    /**
     * Checks whether some other object collide with the snake.
     * 
     * @param inObject the object that collides
     * @return true if there is a collision
     */
    @Override
    public boolean collide(Collideable inObject) {
        boolean collided = false;
        
        for(int i = 0; !collided && i < mTail.size() - 1; i++) {
            if(mTail.get(i).equals(inObject.getLocation())) {
                collided = true;
            }
        }
        
        return collided;
    }
    
    /**
     * All locations that the snake occupy.
     * @return all locations of the snake
     */
    @Override
    public ArrayList<Point> getLocations() {
        ArrayList<Point> locations = new ArrayList();
        locations.add(mLocation);
        locations.addAll(mTail);
        return locations;
    }
}
