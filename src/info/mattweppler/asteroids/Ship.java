/****************************************************
 * Ship.java
 * 
 * @author mweppler@gmaile.com
 * @since 7/29/09
 ***************************************************/
package info.mattweppler.asteroids;

import java.awt.Polygon;
import java.awt.Rectangle;

/****************************************************
 * Ship class - polygonal shape of the player's ship
 ***************************************************/
public class Ship extends BaseVectorShape
{
    // define he ship polygon
    private int[] shipX = { -6, -3, 0, 3, 6, 0 };
    private int[] shipY = { 6, 7, 7, 7, 6, -7 };

    Ship()
    {
        setShape(new Polygon(shipX, shipY, shipX.length));
        setAlive(true);
    }
    
    // bounding rectangle
    public Rectangle getBounds()
    {
        Rectangle r;
        r = new Rectangle((int) getX() - 6, (int) getY() - 6, 12, 12);
        return r;
    }
}