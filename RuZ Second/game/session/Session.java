package game.session;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import game.state.State;
import game.Game;

// Class for handling a single gameplay session (starting, pausing, resuming, event handling, painting)
public class Session extends JPanel implements Runnable, KeyListener {
    public Game game;
    public State state;
    public Session(Game game, State state) {
        this.game = game;
        this.state = state;
        Thread r = new Thread(new Releaser(this));
        Thread p = new Thread(new Painter(this));
        r.start();
        p.start();
        Thread t = new Thread(this);
        t.start();
    }

    // Thread to signal parent component when game is over
    public void run() {
        while (true) {
            synchronized(game) {
                synchronized(state) {
                    state.notify();
                    if (state.gameOver()) {
                        game.option = 0;
                        game.notify();
                        break;
                    }
                    try {
                        state.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public void start() {
        resume();
    }
    void pause() {
        state.paused = true;
        state.scorer.pauseAutoReduce();
        for (int i = 1; i < 4; i++) {
            state.characters.get(i).pauseAutoMove();
        }
    }
    void resume() {
        state.paused = false;
        state.scorer.resumeAutoReduce();
        for (int i = 1; i < 4; i++) {
            state.characters.get(i).resumeAutoMove();
        }
    }

    private void initialize() {
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.cyan);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < state.maze.size; i++) {
            for (int j = 0; j < state.maze.size; j++) {
                String color = state.maze.getTileColor(i, j);
                if (color.equals("player")) {
                    g.setColor(new Color(0, 0, 255));
                } else if (color.equals("enemy")) {
                    g.setColor(new Color(255, 0, 0));
                } else if (color.equals("goal")) {
                    g.setColor(new Color(0, 255, 0));
                } else if (color.equals("floor")) {
                    g.setColor(new Color(255, 255, 255));
                } else if (color.equals("wall")) {
                    g.setColor(new Color(0, 0, 0));
                }
                g.fillRect(j*state.maze.tileSize, i*state.maze.tileSize, state.maze.tileSize, state.maze.tileSize);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        int key = arg0.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            state.characters.get(0).manualMove("LEFT");
        } else if (key == KeyEvent.VK_RIGHT) {
            state.characters.get(0).manualMove("RIGHT");
        } else if (key == KeyEvent.VK_UP) {
            state.characters.get(0).manualMove("UP");
        } else if (key == KeyEvent.VK_DOWN) {
            state.characters.get(0).manualMove("DOWN");
        } else if (key == KeyEvent.VK_P) {
            pause();
        } else if (key == KeyEvent.VK_R) {
            resume();
        }
    }
    @Override
    public void keyReleased(KeyEvent arg0) {
    }
    @Override
    public void keyTyped(KeyEvent arg0) {
	}
}