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

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

/****************************************************
 * Primary class for the game
 ***************************************************/

public class Asteroids {

	//the main thread becomes the game loop
	Thread gameloop;

	//use this as a double buffer
	BufferedImage backbuffer;

	//the main drawing object for the back buffer
	Graphics2D g2d;

	//toggle for drawing bounding boxes
	boolean showBounds = false;

	//create the asteroid array
	int ASTEROIDS = 20;
	Asteroid[] ast = new Asteroid[ASTEROIDS];

	//create the bullet array
	int BULLETS = 10;
	Bullet[] bullet = new Bullet[BULLETS];
	int currentBullet = 0;

	//the players ship
	Ship ship = new Ship();

	//create the identity transform(0.0)
	AffineTransform identity = new AffineTransform();

	//create a random number generator
	Random rand = new Random();

	/****************************************************
	 * Applet init event
	 ***************************************************/
	public void init() {
		//create the back buffer for smooth graphics
		backbuffer = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();

		//set up the ship
		ship.setX(320);
		ship.setY(240);
		
		//set up the bullets
		for (int n = 0; n < BULLETS; ++n) {
			bullet[n] = new Bullet();
		}

		//create the asteroids
		for (int n = 0; n < ASTEROIDS; ++n) {
			ast[n] = new Asteroid();
			ast[n].setRotationVelocity(rand.nextInt(3) + 1);
			ast[n].setX((double)rand.nextInt(600) + 20);
			ast[n].setY((double)rand.nextInt(440) + 20);
			ast[n].setMoveAngle(rand.nextInt(360));
			double ang = ast[n].getMoveAngle() - 90;
			ast[n].setVelX(calcAngleMoveX(ang));
			ast[n].setVelY(calcAngleMoveY(ang));
		}

		//start the user input listener
		addKeyListener(this);
	}

	/****************************************************
	 * Applet update event to redraw the screen
	 ***************************************************/
	public void update(Graphics g) {
		//start off transforms at identity
		g2d.setTransform(identity);

		//erase the background
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, getSize().width, getSize().height);

		//print some status information
		g2d.setColor(Color.WHITE);
		g2d.drawString("Ship: " + Math.round(ship.getX()) + "," + Math.round(ship.getY()), 5, 10);
		g2d.drawString("Move angle: " + Math.round(ship.getMoveAngle()) + 90, 5, 25);
		g2d.drawString("Face angle: " + Math.round(ship.getFaceAngle()), 5, 40);

		//draw the game graphics
		drawShip();
		drawBullets();
		drawAsteroids();

		//repaint the applet window
		paint(g);
	}

	/****************************************************
	 * drawShip called by applet update event
	 ***************************************************/
	public void drawShip() {
		g2d.setTransform(identity);
		g2d.translate(ship.getX(), ship.getY());
		g2d.rotate(Math.toRadians(ship.getFaceAngle()));
		g2d.setColor(Color.ORANGE);
		g2d.fill(ship.getShape());
	}

	/****************************************************
	 * drawBullets called by applet update event
	 ***************************************************/
	public void drawBullets() {
		//iterate through the array of bullets
		for (int n = 0; n < BULLETS; ++n) {
			// is the bullet currently in use?
			if (bullet[n].isAlive()) {
				//draw the bullet
				g2d.setTransform(identity);
				g2d.translate(bullet[n].getX(), bullet[n].getY());
				g2d.setColor(Color.MAGENTA);
				g2d.draw(bullet[n].getShape());
			}
		}
	}

	/****************************************************
	 * drawAsteroids called by applet update event
	 ***************************************************/
  public void drawAsteroids() {
    //iterate through the asteroids array
    for (int n = 0; n < ASTEROIDS; n++) {
      //is this asteroids being used?
      if (ast[n].isAlive()) {
        //draw the asteroid
        g2d.setTransform(identity);
        g2d.translate(ast[n].getX(), ast[n].getY())
        g2d.rotate(Math.toRadians(ast[n].getMoveAngle()));
        g2d.setColor(Color.DARK_GRAY);
        g2d.fill(ast[n].getShape());
      }
    }
  }

	/****************************************************
	 * Applet window repaint event - - draw the back buffer
	 ***************************************************/
  public void paint(Graphics g) {
    //draw the back buffer onto the applet window
    g.drawImage(backBuffer, 0, 0, this);
  }
  
	/****************************************************
	 * Thread start event - start the game loop running
	 ***************************************************/
  public void start() {
    //create the gameloop thread for real - time updates
    gameloop = new Thread(this);
    gameloop.start();
  }
  
	/****************************************************
	 * Thread run event (game loop)
	 ***************************************************/
	public void run() {
	  //acquire the current thread
	  Thread t = Thread.currentThread();
	  //keep going as long as the thread is alive
	  while (t == gameloop) {
	    try {
	      //update the game loop
	      gameUpdate();
	      //target framerate is 50 fps
	      Thread.sleep(20);
	    } catch (InterruptedException ie) {
	      id.printStackTrace();
	    }
	    repaint();
	  }
	}

	/****************************************************
	 * Thread stop event
	 ***************************************************/
  public void stop() {
    //kill the gameloop thread
    gameloop = null;
  }

	/****************************************************
	 * Move and animate the objects in the game
	 ***************************************************/
  private void gameUpdate() {
    updateShip();
    updateBullets();
    updateAsteroids();
    checkCollisions();
  }

	/****************************************************
	 * Update the ship position based on velocity
	 ***************************************************/
  public void updateShip() {
    //update ship's X position
    ship.incX(ship.getVelX());
    //wrap around left/right
    if (ship.getX() < -10) {
      ship.setX(getSize().width + 10);
    } else if (ship.getX() > getSize().width + 10) {
      ship.setX(-10);
    }
    //update ship's Y position
    ship.incY(ship.getVelY());
    //wrap around top/bottom
    if (ship.getY() < -10) {
      ship.setY(getSize().height + 10);
    } else if (ship.getY > getSize().height + 10) {
      ship.setY(-10);
    }
  }

	/****************************************************
	 * Update the ship position based on velocity
	 ***************************************************/
  public void updateBullets() {
    //move each of the bullets
    for (int n = 0; n < BULLETS; n++) {
      //is this bullet being used?
      if (bullet[n].isAlive()) {
        //update bullet's x position
        bullet[n].incX(bullet[n].getVelX());
        //bullet disappears as left/right edge
        if (bullet[n].getX() < 0 || bullet[n].getX() > getSize().width) {
          bullet[n].setAlive(false);
        }
        //update bullet's y position
        bullet[n].incY(bullet[n].getVelY());
        //bullet disappears at top/bottom edge
        if (bullet[n].getY() < 0 || bullet[n].getY() > getSize().height) {
          bullet[n].setAlive(false);
        }
      }
    }
  }

	/****************************************************
	 * Update the asteroids based on velocity
	 ***************************************************/
  // page 67...

}
