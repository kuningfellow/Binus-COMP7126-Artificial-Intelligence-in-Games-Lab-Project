package game.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Scanner;
import javax.swing.JPanel;

import game.Game;

// Class for game menu
public class Menu extends JPanel implements KeyListener {
    /**
        TODO buat tampilan menu
     */
    Game game;
    Scanner in = new Scanner(System.in);
    public Menu(Game game) {
        this.game = game;
        this.addKeyListener(this);
    }

    private void initialize() {
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.cyan);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        int key = arg0.getKeyCode();
        synchronized(game) {
            if (key == KeyEvent.VK_1) {
                game.option = 1;
            } else if (key == KeyEvent.VK_2) {
                game.option = 2;
            } else if (key == KeyEvent.VK_3) {
                game.option = 3;
            }
            if (game.option != -1) {
                game.notify();
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent arg0) {
    }
    @Override
    public void keyTyped(KeyEvent arg0) {
	}
}