/****************************************************
 * Bullet.java
 * 
 * @author mweppler@gmail.com
 * @since 7/29/09
 ***************************************************/
package info.mattweppler.asteroids;

import java.awt.Rectangle;

/****************************************************
 * Bullet class - polygonal shape of a buillet
 ***************************************************/
public class Bullet extends BaseVectorShape
{
    Bullet()
    {
        // create the bullet shape
        setShape(new Rectangle(0, 0, 1, 1));
        setAlive(false);
    }
    
    // bounding rectangle
    public Rectangle getBounds()
    {
        Rectangle r;
        r = new Rectangle((int) getX(), (int) getY(), 1, 1);
        return r;
    }
}