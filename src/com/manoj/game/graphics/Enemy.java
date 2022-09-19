package com.manoj.game.graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.manoj.game.MyCanvas;

public class Enemy {
	private Image enemy;
	private int width, height;

	private float x, y;
	private float angle;

	private float speed = 1;

	public Enemy(String path) {
		try {
			enemy = ImageIO.read(this.getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		width = enemy.getWidth(null);
		height = enemy.getHeight(null);
	}

	public void setStartingLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void changeAngle(float angle) {
		this.angle = angle;
	}

	public Area getEnemyArea() {
		AffineTransform tempTransform = new AffineTransform();
		tempTransform.translate(x, y);
		tempTransform.rotate(Math.toRadians(angle), width / 2, height / 2);
		
		//drawing lines around enemy
		Path2D p = new Path2D.Double();
		p.moveTo(0, width / 2);
		p.lineTo(15, 10);
		p.lineTo(width - 5, 13);
		p.lineTo(width + 10, width / 2);
		p.lineTo(width - 5, width - 13);
		p.lineTo(15, width - 10);
		p.closePath();
		
		return new Area(tempTransform.createTransformedShape(p));
	}
	
	public boolean check() {
		if(y > MyCanvas.HEIGHT) {
			return true;
		}else {
			return false;
		}
	}

	public void update() {
		x += Math.cos(Math.toRadians(angle)) * speed;
		y += Math.sin(Math.toRadians(angle)) * speed;
	}

	public void render(Graphics2D g2D) {
		AffineTransform oldTransform = g2D.getTransform();

		AffineTransform newTransform = new AffineTransform();
		newTransform.translate(x, y);
		newTransform.rotate(Math.toRadians(angle + 45), width / 2, height / 2);
		g2D.drawImage(enemy, newTransform, null);

		g2D.setTransform(oldTransform);
	}
}
