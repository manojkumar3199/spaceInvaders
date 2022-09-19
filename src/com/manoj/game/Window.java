package com.manoj.game;

import javax.swing.JFrame;

public class Window extends JFrame {
	private static final long serialVersionUID = 1L;
	private MyCanvas gameWindow;

	public static final Window WINDOW = new Window();

	private Window() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Game  |");
		
		gameWindow = new MyCanvas();
		this.add(gameWindow);
		
		this.pack();
		this.setLocationRelativeTo(null);
		
		gameWindow.start();
	}
}
