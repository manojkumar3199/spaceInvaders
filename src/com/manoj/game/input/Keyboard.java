package com.manoj.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{
    private boolean keys[] = new boolean[120];
    public boolean space, left, right, fire, restart;
    
    public void update(){
    	space = keys[KeyEvent.VK_SPACE];
        left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
        fire = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
        restart = keys[KeyEvent.VK_R];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
