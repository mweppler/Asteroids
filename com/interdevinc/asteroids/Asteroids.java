//
//  Asteroids.java
//  
//
//  Created by matthew weppler on 7/29/09.
//  Copyright 2009 InterDev Inc.. All rights reserved.
//

package com.interdevinc.asteroids;

/*****************************
 * Chapter 3 - ASTEROIDS GAME
 ****************************/

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

/***
 * Primary class for the game
 ***/

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

	/********************
	 * applet init event
	 *******************/
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

	/***
	 * applet update event to redraw the screen
	 ***/
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

	/***
	 * drawShip called by applet update event
	 ***/
	public void drawShip() {
		g2d.setTransform(identity);
		g2d.translate(ship.getX(), ship.getY());
		g2d.rotate(Math.toRadians(ship.getFaceAngle()));
		g2d.setColor(Color.ORANGE);
		g2d.fill(ship.getShape());
	}

	/***
	 * drawBullets called by applet update event
	 ***/
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

	/***
	 * drawAsteroids called by applet update event
	 ***/

}
