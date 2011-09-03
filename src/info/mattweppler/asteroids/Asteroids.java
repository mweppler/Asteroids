//
//  Asteroids.java
//  
//
//  Created by Matthew Weppler mweppler@gmail.com on 7/29/09.
//  Copyright 2009 All rights reserved.
//

package info.mattweppler.asteroids;

/****************************************************
 * Chapter 3 - ASTEROIDS GAME
 ***************************************************/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;

/****************************************************
 * Primary class for the game
 ***************************************************/

public class Asteroids extends JFrame implements Runnable, KeyListener
//public class Asteroids extends Applet implements Runnable, KeyListener
{    
    Thread gameloop; // the main thread becomes the game loop
    BufferedImage backbuffer; // use this as a double buffer
    Graphics2D g2d; // the main drawing object for the back buffer
    boolean showBounds = false; // toggle for drawing bounding boxes
    int ASTEROIDS = 20; // create the asteroid array
    Asteroid[] ast = new Asteroid[ASTEROIDS];
    int BULLETS = 10; // create the bullet array
    Bullet[] bullet = new Bullet[BULLETS];
    int currentBullet = 0;
    Ship ship = new Ship(); // the players ship
    AffineTransform identity = new AffineTransform(); // create the identity transform(0.0)
    Random rand = new Random(); // create a random number generator

    /****************************************************
     * Applet init event
     ***************************************************/
    public static void main(String[] args)
    {
        new Asteroids();
    }
    
    public Asteroids()
    //public void init()
    {
        // create the back buffer for smooth graphics
        backbuffer = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        g2d = backbuffer.createGraphics();

        //set up the ship
        ship.setX(320);
        ship.setY(240);

        // set up the bullets
        for (int n = 0; n < BULLETS; ++n) {
            bullet[n] = new Bullet();
        }

        // create the asteroids
        for (int n = 0; n < ASTEROIDS; ++n) {
            ast[n] = new Asteroid();
            ast[n].setRotationVelocity(rand.nextInt(3) + 1);
            ast[n].setX((double) rand.nextInt(600) + 20);
            ast[n].setY((double) rand.nextInt(440) + 20);
            ast[n].setMoveAngle(rand.nextInt(360));
            double ang = ast[n].getMoveAngle() - 90;
            ast[n].setVelocityX(calculateAngleMoveX(ang));
            ast[n].setVelocityY(calculateAngleMoveY(ang));
        }

        // start the user input listener
        addKeyListener(this);
    }

    /****************************************************
     * Applet update event to redraw the screen
     ***************************************************/
    public void update(Graphics g)
    {
        // start off transforms at identity
        g2d.setTransform(identity);

        // erase the background
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, getSize().width, getSize().height);

        // print some status information
        g2d.setColor(Color.WHITE);
        g2d.drawString(
                "Ship: " + Math.round(ship.getX()) + ","
                        + Math.round(ship.getY()), 5, 10);
        g2d.drawString("Move angle: " + Math.round(ship.getMoveAngle()) + 90,
                5, 25);
        g2d.drawString("Face angle: " + Math.round(ship.getFacingAngle()), 5, 40);

        // draw the game graphics
        drawShip();
        drawBullets();
        drawAsteroids();

        // repaint the applet window
        paint(g);
    }

    /****************************************************
     * drawShip called by applet update event
     ***************************************************/
    public void drawShip()
    {
        g2d.setTransform(identity);
        g2d.translate(ship.getX(), ship.getY());
        g2d.rotate(Math.toRadians(ship.getFacingAngle()));
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fill(ship.getShape());
    }

    /****************************************************
     * drawBullets called by applet update event
     ***************************************************/
    public void drawBullets()
    {
        // iterate through the array of bullets
        for (int n = 0; n < BULLETS; ++n) {
            // is the bullet currently in use?
            if (bullet[n].isAlive()) {
                // draw the bullet
                g2d.setTransform(identity);
                g2d.translate(bullet[n].getX(), bullet[n].getY());
                g2d.setColor(Color.YELLOW);
                g2d.draw(bullet[n].getShape());
            }
        }
    }

    /****************************************************
     * drawAsteroids called by applet update event
     ***************************************************/
    public void drawAsteroids()
    {
        // iterate through the asteroids array
        for (int n = 0; n < ASTEROIDS; n++) {
            // is this asteroids being used?
            if (ast[n].isAlive()) {
                // draw the asteroid
                g2d.setTransform(identity);
                g2d.translate(ast[n].getX(), ast[n].getY());
                g2d.rotate(Math.toRadians(ast[n].getMoveAngle()));
                g2d.setColor(Color.DARK_GRAY);
                g2d.fill(ast[n].getShape());
            }
        }
    }

    /****************************************************
     * ppplet window repaint event - - draw the back buffer
     ***************************************************/
    public void paint(Graphics g)
    {
        // draw the back buffer onto the applet window
        g.drawImage(backbuffer, 0, 0, this);
    }

    /****************************************************
     * thread start event - start the game loop running
     ***************************************************/
    public void start()
    {
        // create the gameloop thread for real - time updates
        gameloop = new Thread(this);
        gameloop.start();
    }

    /****************************************************
     * thread run event (game loop)
     ***************************************************/
    public void run()
    {
        // acquire the current thread
        Thread t = Thread.currentThread();
        // keep going as long as the thread is alive
        while (t == gameloop) {
            try {
                // update the game loop
                gameUpdate();
                // target framerate is 50 fps
                Thread.sleep(20);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            repaint();
        }
    }

    /****************************************************
     * Thread stop event
     ***************************************************/
    public void stop()
    {
        // kill the gameloop thread
        gameloop = null;
    }

    /****************************************************
     * Move and animate the objects in the game
     ***************************************************/
    private void gameUpdate()
    {
        updateShip();
        updateBullets();
        updateAsteroids();
        checkCollisions();
    }

    /****************************************************
     * Update the ship position based on velocity
     ***************************************************/
    public void updateShip()
    {
        // update ship's X position
        ship.increaseX(ship.getVelocityX());
        // wrap around left/right
        if (ship.getX() < -10) {
            ship.setX(getSize().width + 10);
        } else if (ship.getX() > getSize().width + 10) {
            ship.setX(-10);
        }
        // update ship's Y position
        ship.increaseY(ship.getVelocityY());
        // wrap around top/bottom
        if (ship.getY() < -10) {
            ship.setY(getSize().height + 10);
        } else if (ship.getY() > getSize().height + 10) {
            ship.setY(-10);
        }
    }

    /****************************************************
     * Update the ship position based on velocity
     ***************************************************/
    public void updateBullets()
    {
        // move each of the bullets
        for (int n = 0; n < BULLETS; n++) {
            // is this bullet being used?
            if (bullet[n].isAlive()) {
                // update bullet's x position
                bullet[n].increaseX(bullet[n].getVelocityX());
                // bullet disappears as left/right edge
                if (bullet[n].getX() < 0 || bullet[n].getX() > getSize().width) {
                    bullet[n].setAlive(false);
                }
                // update bullet's y position
                bullet[n].increaseY(bullet[n].getVelocityY());
                // bullet disappears at top/bottom edge
                if (bullet[n].getY() < 0 || bullet[n].getY() > getSize().height) {
                    bullet[n].setAlive(false);
                }
            }
        }
    }

    /****************************************************
     * Update the asteroids based on velocity
     ***************************************************/
    public void updateAsteroids()
    {
        // move and rotate the asteroids
        for (int n = 0; n < ASTEROIDS; n++) {
            // is this asteroid being used?
            if (ast[n].isAlive()) {
                // update the asteroid's X value
                ast[n].increaseX(ast[n].getVelocityX());
                // warp the asteroid at screen edges
                if (ast[n].getX() < -20) {
                    ast[n].setX(getSize().width + 20);
                } else if (ast[n].getX() > getSize().width + 20) {
                    ast[n].setX(-20);
                }
                // update the asteroid's Y value
                ast[n].increaseY(ast[n].getVelocityY());
                // warp the asteroid at screen edges
                if (ast[n].getY() < -20) {
                    ast[n].setY(getSize().height + 20);
                } else if (ast[n].getY() > getSize().height + 20) {
                    ast[n].setY(-20);
                }
                // update the asteroid's rotation
                ast[n].increaseMoveAngle(ast[n].getRotationVelocity());
                // keep the angle within 0-359 degrees
                if (ast[n].getMoveAngle() < 0) {
                    ast[n].setMoveAngle(360 - ast[n].getRotationVelocity());
                } else if (ast[n].getMoveAngle() > 360) {
                    ast[n].setMoveAngle(ast[n].getRotationVelocity());
                }
            }
        }
    }

    /****************************************************
     * Test asteroids for collisions with ship or bullets
     ***************************************************/
    public void checkCollisions()
    {
        // iterate through the asteroids array
        for (int m = 0; m < ASTEROIDS; m++) {
            // is this asteroid being used?
            if (ast[m].isAlive()) {
                // check for collision with bullet
                for (int n = 0; n < BULLETS; n++) {
                    // is this bullet being used?
                    if (bullet[n].isAlive()) {
                        // perform the collision test
                        if (ast[m].getBounds().contains(bullet[n].getX(),
                                bullet[n].getY())) {
                            bullet[n].setAlive(false);
                            ast[m].setAlive(false);
                            continue;
                        }
                    }
                }
                // check for collision with ship
                if (ast[m].getBounds().intersects(ship.getBounds())) {
                    ast[m].setAlive(false);
                    ship.setX(320);
                    ship.setY(240);
                    ship.setFacingAngle(0);
                    ship.setVelocityX(0);
                    ship.setVelocityY(0);
                    continue;
                }
            }
        }
    }

    /****************************************************
     * key listener events
     ***************************************************/
    public void keyReleased(KeyEvent k) { }

    public void keyTyped(KeyEvent k) { }

    public void keyPressed(KeyEvent k)
    {
        int keyCode = k.getKeyCode();
        switch (keyCode) {
        case KeyEvent.VK_LEFT:
            // left arrow rotates ship left 5 degrees
            ship.increaseFacingAngle(-5);
            if (ship.getFacingAngle() < 0) {
                ship.setFacingAngle(360 - 5);
            }
            break;
        case KeyEvent.VK_RIGHT:
            // right arrow rotates ship right 5 degrees
            ship.increaseFacingAngle(5);
            if (ship.getFacingAngle() > 360) {
                ship.setFacingAngle(5);
            }
            break;
        case KeyEvent.VK_UP:
            // up arrow adds thrust to ship (1/10 normal speed)
            ship.setMoveAngle(ship.getFacingAngle() - 90);
            ship.increaseVelocityX(calculateAngleMoveX(ship.getMoveAngle()) * 0.1);
            ship.increaseVelocityY(calculateAngleMoveY(ship.getMoveAngle()) * 0.1);
            break;
        case KeyEvent.VK_DOWN:
            // up arrow adds thrust to ship (1/10 normal speed)
            ship.setMoveAngle(ship.getFacingAngle() - 90);
            ship.setVelocityX(0);
            ship.setVelocityY(0);
            break;
        // Ctrl, Enter, or Space can be used to fire weapon
        case KeyEvent.VK_CONTROL:
        case KeyEvent.VK_ENTER:
        case KeyEvent.VK_SPACE:
            // fire a bullet
            currentBullet++;
            if (currentBullet > BULLETS - 1) {
                currentBullet = 0;
            }
            bullet[currentBullet].setAlive(true);
            // point bullet in same direction ship is facing
            bullet[currentBullet].setX(ship.getX());
            bullet[currentBullet].setY(ship.getY());
            bullet[currentBullet].setMoveAngle(ship.getFacingAngle() - 90);
            // fire bullet at angle of the ship
            double angle = bullet[currentBullet].getMoveAngle();
            double svx = ship.getVelocityX();
            double svy = ship.getVelocityY();
            bullet[currentBullet].setVelocityX(svx + calculateAngleMoveX(angle) * 2);
            bullet[currentBullet].setVelocityY(svy + calculateAngleMoveY(angle) * 2);
            break;
        }
    }

    /****************************************************
     * calculate X movement value based on direction angle
     ***************************************************/
    public double calculateAngleMoveX(double angle)
    {
        return (double) (Math.cos(angle * Math.PI / 180));
    }

    /****************************************************
     * calculate Y movement value based on direction angle
     ***************************************************/
    public double calculateAngleMoveY(double angle)
    {
        return (double) (Math.sin(angle * Math.PI / 180));
    }

}
