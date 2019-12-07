package game.session;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JPanel;

import game.state.State;
import game.Game;

// Class for handling a single gameplay session (starting, pausing, resuming, event handling, painting)
public class Session extends JPanel implements Runnable, KeyListener {
    Game game;
    public State state;
    Screen screen;
    JPanel panel;
    Info info;
    PauseButton pauseButton;

    public Session(Game game, State state) {
        this.addKeyListener(this);
        this.game = game;
        this.state = state;
        this.screen = new Screen(this);
        this.panel = new JPanel();
        this.info = new Info(this);
        this.pauseButton= new PauseButton(this);
        
        this.game.frame.setSize(state.maze.size * state.maze.tileSize + 230, state.maze.size * state.maze.tileSize + this.game.frame.getInsets().top);

        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        this.setLayout(new GridBagLayout());

        // add info to side panel
        c.gridx = 0; c.gridy = 0; c.ipadx = 230; c.ipady = state.maze.size * state.maze.tileSize - 80;
        panel.add(info, c);

        // add pause button to side panel
        c.gridy = 1; c.ipadx = 0; c.ipady = 50;
        panel.add(pauseButton, c);

        // add game screen to session panel
        c.gridx = 0; c.gridy = 0; c.ipadx = c.ipady = state.maze.size * state.maze.tileSize;
        this.add(screen, c);

        // add side panel to session panel
        c.gridx = 1; c.ipadx = c.ipady = 0;
        this.add(panel, c);
    
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
                    // make sure if player scores goal, the goal thread completes
                    if (state.playerKilled ||
                        (
                            state.scorer.goalFounder != -1 && 
                            (state.scorer.goalFounder != 0 || state.scorer.goalScoreAdded)
                        )
                    ) {
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
        setBackground(Color.cyan);
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