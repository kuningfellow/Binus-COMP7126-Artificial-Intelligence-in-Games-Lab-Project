package game;

import java.util.Vector;
import java.util.Scanner;

import game.GameState;

// Class concered with the dynamics of the game state
public class GameEngine {
    private GameState state;

    // Class for handling a single gameplay timeline (starting, pausing, resuming, etc)
    class Session {
        // A thread to ensure locks are released regularly
        class Release implements Runnable {
            public void run() {
                while (true) {
                    synchronized(state) {
                        state.maze.print();
                        state.notify();
                        try {
                            state.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        Session() {
            Thread r = new Thread(new Release());
            r.start();
        }
        void start() {
            resume();
        }
        // stops all AutoMoves;
        void pause() {
            state.paused = true;
            state.scorer.pauseAutoReduce();
            for (int i = 1; i < 4; i++) {
                state.characters.get(i).pauseAutoMove();
            }
        }
        // runs all AutoMoves
        void resume() {
            state.paused = false;
            state.scorer.resumeAutoReduce();
            for (int i = 1; i < 4; i++) {
                state.characters.get(i).resumeAutoMove();
            }
        }
    }

    // Just a bunch of initializers
    GameEngine() {
        state = new GameState(1);
        Session session = new Session();
        session.start();
        Scanner in = new Scanner(System.in);
        while (1 < 2) {
            String str = in.nextLine();
            if (str.equals("p")) {
                session.pause();
            } else if (str.equals("r")) {
                session.resume();
            } else if (str.equals("w")) {
                state.characters.get(0).manualMove(2);
            } else if (str.equals("s")) {
                state.characters.get(0).manualMove(0);
            } else if (str.equals("d")) {
                state.characters.get(0).manualMove(1);
            } else if (str.equals("a")) {
                state.characters.get(0).manualMove(3);
            }
        }
    }

    // runner for testing
    public static void main(String[] args) {
        new GameEngine();
    }
}