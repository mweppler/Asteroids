/****************************************************
 * Asteroid.java
 * 
 * @author mweppler@gmail.com
 * @since 7/29/09
 ***************************************************/
package info.mattweppler.asteroids;

import java.awt.Polygon;
import java.awt.Rectangle;

/****************************************************
 * Asteroid class - for polygonal asteroid shapes
 ***************************************************/
public class Asteroid extends BaseVectorShape
{
    // define the asteroid polygon shape
    private int[] asteroidX = { -20, -13, 0, 20, 22, 20, 12, 2, -10, -22, -16 };
    private int[] asteroidY = { 20, 23, 17, 20, 16, -20, -22, -14, -17, -20, -5 };
    // rotation speed
    protected double rotationVelocity;

    // default constructor
    Asteroid()
    {
        setShape(new Polygon(asteroidX, asteroidY, asteroidX.length));
        setAlive(true);
        setRotationVelocity(0.0);
    }

    // bounding rectangle
    public Rectangle getBounds()
    {
        Rectangle r;
        r = new Rectangle((int) getX() - 20, (int) getY() - 20, 40, 40);
        return r;
    }
    
    public double getRotationVelocity()
    {
        return rotationVelocity;
    }

    public void setRotationVelocity(double v)
    {
        rotationVelocity = v;
    }
}