package game;

import java.util.Vector;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.state.State;
import game.session.Session;
import game.menu.Menu;

// Game Class
public class Game implements Runnable {
    public int option;     // -1 = menu, 0 = game over, [1-3] = difficulty
    public JFrame frame;
    Menu menu;
    Session gameSession;

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
        frame.setVisible(true);
    }

    public void run() {
        while (true) {
            synchronized(this) {
                if (option == -1) {
                    if (menu != null) {
                        frame.remove(menu);
                        frame.removeKeyListener(menu);
                    }
                    if (gameSession != null) {
                        frame.remove(gameSession);
                        frame.removeKeyListener(gameSession);
                    }
                    frame.setVisible(true);
                    menu = new Menu(this);
                    frame.add(menu);
                    frame.addKeyListener(menu);
                    frame.setVisible(true);
                    try {
                        this.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (option == 0) {
                    option = -1;
                    String dialogTitle, dialogMessage;
                    if (gameSession.state.playerKilled == true) {
                        dialogTitle = "You Lose";
                        dialogMessage = "You were killed..." + "\nScore: 0";
                    } else {
                        if (gameSession.state.scorer.goalFounder == 0) {
                            dialogTitle = "You Win!";
                            dialogMessage = "You finally found the treasure!" + "\nScore: " + gameSession.state.scorer.getScore();
                        } else {
                            dialogTitle = "You Lose";
                            dialogMessage = "The enemies got it first..." + "\nScore: 0";
                        }
                    }
                    int dialog = JOptionPane.showOptionDialog(frame, dialogMessage + "\nPlay Again?", dialogTitle,
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (dialog == JOptionPane.YES_OPTION) {
                        option = -1;
                    } else if (dialog == JOptionPane.NO_OPTION) {
                        System.exit(0);
                    }
                } else {
                    if (menu != null) {
                        frame.remove(menu);
                        frame.removeKeyListener(menu);
                    }
                    if (gameSession != null) {
                        frame.remove(gameSession);
                        frame.removeKeyListener(gameSession);
                    }
                    gameSession = newGameSession(option);
                    frame.add(gameSession);
                    frame.addKeyListener(gameSession);
                    frame.setVisible(true);
                    gameSession.start();
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