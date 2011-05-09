//
//  Bullet.java
//  
//
//  Created by Matthew Weppler mweppler@gmail.com on 7/29/09.
//  Copyright 2009 All rights reserved.
//

package info.mattweppler.asteroids;

import java.awt.*;
import java.awt.Rectangle;

/****************************************************
 * Bullet class - polygonal shape of a buillet
 ***************************************************/
public class Bullet extends BaseVectorShape {

	//bounding rectangle
	public Rectangle getBounds() {
		Rectangle r;
		r = new Rectangle((int)getX(), (int)getY(), 1, 1);
		return r;
	}

	Bullet() {
		//create the bullet shape
		setShape(new Rectangle(0, 0, 1, 1));
		setAlive(false);
	}

}
