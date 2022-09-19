package com.manoj.game.graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Background {
	private Image background = null;
	
	public Background(String path) {
		try {
			background = ImageIO.read(this.getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics2D g2D) {
		AffineTransform reset = g2D.getTransform();
		g2D.drawImage(background, 0, 0, null);
		g2D.setTransform(reset);
	}
}
