import java.util.Vector;
import java.util.Scanner;

// Class concered with the dynamics of the game state
public class GameEngine {
    private GameState state;

    // Class for Character (player and enemies)
    private class CharacterMovement {
        int id;
        AutoMove autoMove = null;
        CharacterMovement(int id) {
            this.id = id;
        }
        void pauseAutoMove() {
            if (this.autoMove != null) {
                long curT = System.currentTimeMillis();
                this.autoMove.kill();
                state.characters.get(id).timePhase = ( state.characters.get(id).timePhase + curT - this.autoMove.startTime ) % state.enemyMoveCooldown;
                this.autoMove = null;
            }
        }
        void resumeAutoMove() {
            if (this.autoMove == null) {
                long curT = System.currentTimeMillis();
                this.autoMove = new AutoMove(curT, state.enemyMoveCooldown - state.characters.get(id).timePhase);
                Thread t = new Thread(this.autoMove);
                t.start();
            }
        }
        class AutoMove implements Runnable {
            long startTime;
            long pendingSleep;
            volatile boolean dead = false;

            // character being moved, start time of thread
            AutoMove(long startTime, long pendingSleep) {
                this.startTime = startTime;
                this.pendingSleep = pendingSleep;
            }
            public void kill() {
                this.dead = true;
            }
            public void run() {
                try {
                    Thread.sleep(pendingSleep);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (!dead) {
                    synchronized(state) {
                        /**
                            Do some automatic character movement
                        */
                        state.characters.get(id).autoMove();
                        state.notify();
                        try {
                            state.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // Wait for a period before an enemy can move
                    try {
                        Thread.sleep(state.enemyMoveCooldown);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // Class to move player
        class Move implements Runnable {
            public void run() {
                synchronized(state) {
                    /**
                        Do some player movement
                    */
                    state.notify();
                }
            }
        }
    }


    // Class for handling a single gameplay timeline (starting, pausing, resuming, etc)
    class Session {
        Vector<CharacterMovement> enemies = new Vector<CharacterMovement>();
        CharacterMovement player;
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
            player = new CharacterMovement(0);
            for (int i = 1; i <= state.enemyCount; i++) {
                enemies.add(new CharacterMovement(i));
            }
            Thread r = new Thread(new Release());
            r.start();
        }
        void start() {
            resume();
        }
        // stops all AutoMoves;
        void pause() {
            for (int i = 0; i < state.enemyCount; i++) {
                enemies.get(i).pauseAutoMove();
            }
        }
        // runs all AutoMoves
        void resume() {
            for (int i = 0; i < state.enemyCount; i++) {
                enemies.get(i).resumeAutoMove();
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
            } else {
                Thread m = new Thread(session.player.new Move());
                m.start();
            }
            // System.out.println(state.position[1][0]);
        }
    }

    // runner for testing
    public static void main(String[] args) {
        new GameEngine();
    }
}