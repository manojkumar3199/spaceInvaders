package com.manoj.game.graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {
	public int width, height;

	public float x, y;
	public float angle;
	public float speed;
	
	private final float MAX_SPEED = 2;
	private boolean start;
	private int momentum;

	private Image plane, plane_start;

	public Player() {
		try {
			plane = ImageIO.read(this.getClass().getResource("/plane.png"));
			plane_start = ImageIO.read(this.getClass().getResource("/plane_speed.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		width = plane.getWidth(null);
		height = plane.getHeight(null);
	}

	public void setStartingParameters(float x, float y, float angle, float speed) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.speed = speed;
	}

	public void changeAngle(float angle) {
		this.angle = angle;
	}

	public void speedUp() {
		start = true;
		
		// setting speed
		if (speed >= MAX_SPEED) {
			speed = MAX_SPEED;
		} else {
			speed += 0.1;
		}
	}

	public void speedDown() {
		start = false;
		momentum++;
		
		// setting speed
		if (momentum >= 100)
			momentum = 0;

		if (momentum % 10 == 0) {
			if (speed < 0.1) {
				speed = 0;
			} else {
				speed -= 0.1;
			}
		}
	}

	public Area getPlayerArea() {
		AffineTransform tempTransform = new AffineTransform();
		tempTransform.translate(x, y);
		tempTransform.rotate(Math.toRadians(angle+45), width / 2, height / 2);
		
		//drawing line around player
		Path2D p = new Path2D.Double();
		p.moveTo(63, 1);
		p.lineTo(52, 6);
		p.lineTo(35, 19);
		p.lineTo(12, 18);
		p.lineTo(6, 25);
		p.lineTo(17, 36);
		p.lineTo(15, 39);
		p.lineTo(4, 38);
		p.lineTo(0, 42);
		p.lineTo(20, 63);
		p.lineTo(24, 61);
		p.lineTo(24, 48);
		p.lineTo(28, 45);
		p.lineTo(38, 57);
		p.lineTo(44, 52);
		p.lineTo(44, 28);
		p.lineTo(57, 12);
		p.closePath();
		
		return new Area(tempTransform.createTransformedShape(p));
	}

	public void update() {
		x += Math.cos(Math.toRadians(angle)) * speed;
		y += Math.sin(Math.toRadians(angle)) * speed;
//		System.out.println("x: " + x + ", y: " + y + ", angle: " + angle + ", speed: " + speed + ", momentum: " + momentum);
	}

	public void render(Graphics2D g2D) {
		AffineTransform oldTransform = g2D.getTransform();
		
		AffineTransform newTransform = new AffineTransform();
		newTransform.translate(x, y);
		newTransform.rotate(Math.toRadians(angle + 45), width / 2, height / 2);
		g2D.drawImage(start ? plane_start : plane, newTransform, null);
		
//		Area playerArea = getPlayerArea();
//		g2D.setColor(Color.CYAN);
//		g2D.draw(playerArea);
		
		g2D.setTransform(oldTransform);
	}
}
