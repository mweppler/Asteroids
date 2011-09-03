/****************************************************
 * BaseVectorShape.java
 * 
 * @author mweppler@gmail.com 
 * @since 7/29/09
 ***************************************************/
package info.mattweppler.asteroids;

import java.awt.Shape;

/****************************************************
 * Base vector shape class for polygonal shapes
 ***************************************************/
public class BaseVectorShape
{
    private boolean alive;
    private double facingAngle, moveAngle;
    private Shape shape;
    private double velocityX, velocityY;
    private double x, y;

    // default constructor
    BaseVectorShape()
    {
        setShape(null);
        setAlive(false);
        setX(0.0);
        setY(0.0);
        setVelocityX(0.0);
        setVelocityY(0.0);
        setMoveAngle(0.0);
        setFacingAngle(0.0);
    }
    
    // accessor methods
    public double getFacingAngle()
    {
        return facingAngle;
    }

    public double getMoveAngle()
    {
        return moveAngle;
    }

    public Shape getShape()
    {
        return shape;
    }

    public double getVelocityX()
    {
        return velocityX;
    }

    public double getVelocityY()
    {
        return velocityY;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    // mutator and helper methods
    public void increaseFacingAngle(double i)
    {
        this.facingAngle += i;
    }
    
    public void increaseMoveAngle(double i)
    {
        this.moveAngle += i;
    }

    public void increaseVelocityX(double i)
    {
        this.velocityX += i;
    }

    public void increaseVelocityY(double i)
    {
        this.velocityY += i;
    }

    public void increaseX(double i)
    {
        this.x += i;
    }

    public void increaseY(double i)
    {
        this.y += i;
    }

    public boolean isAlive()
    {
        return alive;
    }

    public void setAlive(boolean alive)
    {
        this.alive = alive;
    }

    public void setFacingAngle(double angle)
    {
        this.facingAngle = angle;
    }

    public void setMoveAngle(double angle)
    {
        this.moveAngle = angle;
    }

    public void setShape(Shape shape)
    {
        this.shape = shape;
    }

    public void setVelocityX(double velX)
    {
        this.velocityX = velX;
    }

    public void setVelocityY(double velY)
    {
        this.velocityY = velY;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }
}