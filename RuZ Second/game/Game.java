package game;

import java.util.Vector;
import java.util.Scanner;

import javax.swing.JFrame;

import game.state.State;
import game.session.Session;
import game.menu.Menu;

// Game Class
public class Game implements Runnable {
    public int option;     // -1 = menu, 0 = game over, [1-3] = difficulty
    Menu menu;
    Session gameSession;
    JFrame frame;

    private Session newGameSession(int difficulty) {
        return new Session(this, new State(difficulty));
    }
    
    Game() {
        frame = new JFrame("RuZ Second");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        option = -1;
        menu = null;
        gameSession = null;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        while (true) {
            synchronized(this) {
                if (option == -1) {
                    System.out.println("Menu");
                    menu = new Menu(this);
                    frame.add(menu);
                    try {
                        this.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (option == 0) {
                    option = -1;
                } else {
                    if (menu != null) {
                        frame.remove(menu);
                    }
                    if (gameSession != null) {
                        frame.remove(gameSession);
                        frame.removeKeyListener(gameSession);
                    }
                    gameSession = newGameSession(option);
                    frame.add(gameSession);
                    frame.addKeyListener(gameSession);
                    gameSession.start();
                    frame.setVisible(true);
                    try {
                        this.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}