package com.manoj.game.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import com.manoj.game.MyCanvas;

public class Bullet {
	private int size = 8;
	private Shape shape = new Ellipse2D.Double(0, 0, size, size);
	private Color color = Color.WHITE;

	private float angle;
	private float x, y;

	private float speed = 4;

	public Bullet(int playerWidth, int playerHeight, float angle, float x, float y) {
		this.angle = angle;
		x += playerWidth / 2 - (size / 2);
		y += playerHeight / 2 - (size / 2);
		this.x = x;
		this.y = y;
	}

	public boolean check() {
		if (y < 0 || y > MyCanvas.HEIGHT || x < 0 || x > MyCanvas.WIDTH) {
			return true;
		} else {
			return false;
		}
	}
	
	public Area getBulletArea() {
		return new Area(new Ellipse2D.Double(x, y, size, size));
	}

	public void update() {
		x += Math.cos(Math.toRadians(angle)) * speed;
		y += Math.sin(Math.toRadians(angle)) * speed;
	}

	public void render(Graphics2D g2D) {
		AffineTransform oldTransform = g2D.getTransform();

		g2D.setColor(color);
		g2D.translate(x, y);
		g2D.fill(shape);

		g2D.setTransform(oldTransform);
	}
}
