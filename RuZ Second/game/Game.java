package game;

import java.util.Vector;
import java.util.Scanner;

import javax.swing.JFrame;

import game.state.State;
import game.session.Session;

// Game Class
public class Game {
    private State state;

    Game() {
        JFrame frame = new JFrame("wow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        state = new State(1);
        Session session = new Session(state);
        frame.add(session);
        frame.addKeyListener(session);
        // frame.pack();
        session.start();
        frame.setVisible(true);
    }

    // runner for testing
    public static void main(String[] args) {
        new Game();
    }
}