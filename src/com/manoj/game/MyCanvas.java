package com.manoj.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import java.awt.Font;
import java.awt.FontMetrics;

import com.manoj.game.graphics.Background;
import com.manoj.game.graphics.Bullet;
import com.manoj.game.graphics.Enemy;
import com.manoj.game.graphics.Player;
import com.manoj.game.input.Keyboard;

public class MyCanvas extends Canvas implements Runnable {
	private static final long serialVersionUID = 2L;

	private boolean running;
	private Thread thread = null;

	public static final int WIDTH = 320 * 3;
	public static final int HEIGHT = 320 * 2;

	private Keyboard keyboardInput;

	private Background background;
	private Player player;

	private ArrayList<Bullet> bullets;
	private Bullet bullet;
	private int shootTime;

	private ArrayList<Enemy> enemies;
	private Enemy enemy;
	private final Random RANDOM = new Random();
	private int score;

	private boolean playerCollided;

	public MyCanvas() {
		thread = new Thread(this);

		Dimension size = new Dimension(WIDTH, HEIGHT);
		this.setPreferredSize(size);

		this.keyboardInput = new Keyboard();
		this.addKeyListener(keyboardInput);
		this.setFocusable(true);

		background = new Background("/background.png");
		player = new Player();
		player.setStartingParameters(WIDTH / 2 - (player.width / 2), HEIGHT / 2 - (player.height / 2), 270, 0);
		bullets = new ArrayList<Bullet>();

		enemies = new ArrayList<Enemy>();
	}

	private void renderBullets(Graphics2D g2D) {
		for (int i = 0; i < bullets.size(); i++) {
			bullet = bullets.get(i);
			bullet.render(g2D);
		}
	}

	private void updateBullets() {
		for (int i = 0; i < bullets.size(); i++) {
			bullet = bullets.get(i);
			if (bullet != null) {
				bullet.update();
				checkBulletHit();
				if (bullet.check()) {
					bullets.remove(bullet);
				}
			}
		}
	}

	private void renderEnemies(Graphics2D g2D) {
		for (int i = 0; i < enemies.size(); i++) {
			enemy = enemies.get(i);
			if (enemy != null) {
				enemy.render(g2D);
			}
		}
	}

	private void updateEnemies() {
		for (int i = 0; i < enemies.size(); i++) {
			enemy = enemies.get(i);
			if (enemy != null) {
				enemy.update();
				if (enemy.check()) {
					enemies.remove(enemy);

					// updating score
					if (!playerCollided) {
						if (score == 0) {
							score = 0;
						} else {
							score--;
						}
					}

				}
			}
		}
	}

	private void checkBulletHit() {
		for (int i = 0; i < enemies.size(); i++) {
			enemy = enemies.get(i);
			if (enemy != null) {
				Area area = new Area(bullet.getBulletArea());
				area.intersect(enemy.getEnemyArea());
				if (!area.isEmpty()) {
					enemies.remove(enemy);
					bullets.remove(bullet);

					// updating score
					score++;
				}
			}
		}
	}

	private void checkCollision() {
		for (int i = 0; i < enemies.size(); i++) {
			enemy = enemies.get(i);
			if (enemy != null) {
				Area area = new Area(enemy.getEnemyArea());
				area.intersect(player.getPlayerArea());
				if (!area.isEmpty()) {
					playerCollided = true;
				}
			}
		}
	}

	private void generateEnemies() {
		enemy = new Enemy("/enemy.png");
		int x = RANDOM.nextInt(WIDTH - 50) + 25;
		enemy.changeAngle(90);
		enemy.setStartingLocation(x, 0);
		enemies.add(enemy);
	}

	private void renderScore(Graphics2D g2D) {
		AffineTransform oldTransform = g2D.getTransform();

		Font font = new Font("Comic Sans MS", Font.BOLD, 20);
		g2D.setFont(font);
		g2D.setColor(Color.ORANGE);
		g2D.drawString("Score: " + score, 0, 20);

		g2D.setTransform(oldTransform);
	}

	private void restartGame() {
		playerCollided = false;
		player.setStartingParameters(WIDTH / 2 - (player.width / 2), HEIGHT / 2 - (player.height / 2), 270, 0);
		bullets.removeAll(bullets);
		enemies.removeAll(enemies);
		score = 0;
	}
	
	private void gameOverWindow(Graphics2D g2D) {
		AffineTransform oldTransform = g2D.getTransform();
		
		Font font1 = new Font("Comic Sans MS", Font.BOLD, 50);
		Font font2 = new Font("Comic Sans MS", Font.PLAIN, 20);
		
		String message1 = "Game Over!";
		String message2 = "press 'R' to restat the game.";
		
		g2D.setColor(Color.RED);
		
		g2D.setFont(font1);
		FontMetrics fm1 = g2D.getFontMetrics(font1);
		g2D.drawString(message1, (WIDTH/2) - (fm1.stringWidth(message1)/2), 280);
		
		g2D.setFont(font2);
		FontMetrics fm2 = g2D.getFontMetrics(font2);
		g2D.drawString(message2, (WIDTH/2) - (fm2.stringWidth(message2)/2), 320);
		
		g2D.setTransform(oldTransform);
	}

	public void ticks() {
		checkCollision();
		keyboardInput.update();

		if (!playerCollided) {
			if (keyboardInput.right) {
				float angle = player.angle;
				if (angle > 359)
					angle = 0;
				angle += 0.5;
				player.changeAngle(angle);
			} else if (keyboardInput.left) {
				float angle = player.angle;
				if (angle < 0)
					angle = 359;
				angle -= 0.5;
				player.changeAngle(angle);
			}

			if (keyboardInput.space) {
				player.speedUp();
			} else {
				player.speedDown();
			}

			if (keyboardInput.fire) {
				if (shootTime >= 5) {
					bullet = new Bullet(player.width, player.height, player.angle, player.x, player.y);
					bullets.add(bullet);
					shootTime = 0;
				} else {
					shootTime++;
				}
			}
			
			player.update();
		}

		if (keyboardInput.restart) {
			restartGame();
		}
		
		updateBullets();
		updateEnemies();
		System.out.println("Enemies: " + enemies.size() + ", Bullets: " + bullets.size());
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		Graphics2D g2D = (Graphics2D) g;

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHints(rh);

		background.render(g2D);
		renderScore(g2D);
		renderEnemies(g2D);
		renderBullets(g2D);
		if (!playerCollided) {
			player.render(g2D);
		}else {
			gameOverWindow(g2D);
		}
		g2D.dispose();
		bs.show();
	}

	public void start() {
		running = true;
		thread.start();
	}

	public void stop() {
		try {
			running = false;
			thread.join();
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}
	}

	@Override
	public void run() {
		int ups = 0;
		int fps = 0;
		long timer = System.currentTimeMillis();
		long genetareTime = System.currentTimeMillis();

		double intervel = 1000000000 / 60;
		long previousTime = System.nanoTime();
		long currentTime;
		double delta = 0;
		while (running) {
			currentTime = System.nanoTime();
			delta += (currentTime - previousTime) / intervel;
			previousTime = currentTime;
			while (delta >= 1) {
				ticks();
				delta--;
				ups++;
			}
			render();
			fps++;
			if (System.currentTimeMillis() - timer >= 1000) {
				timer += 1000;
				Window.WINDOW.setTitle("Game  |    " + ups + " ups, " + fps + " fps");
				ups = 0;
				fps = 0;
			}

			// Time for enemy generation
			if (System.currentTimeMillis() - genetareTime >= 3000) {
				genetareTime += 3000;
				generateEnemies();
			}
		}
	}
}
