import java.util.Vector;
import java.util.Scanner;

// Class concered with the dynamics of the game state
public class GameEngine {
    GameState state;

    // Class to move enemies
    class AutoMove implements Runnable {
        int character;
        long pendingSleep;
        public long startTime;
        volatile boolean dead = false;

        // character being moved, start time of thread
        AutoMove(int character, long startTime, long pendingSleep) {
            this.character = character;
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

    // Class for handling a single gameplay timeline (starting, pausing, resuming, etc)
    class Session {
        Vector<AutoMove> T = new Vector<AutoMove>();        // Thread Vector for enemy motion
        Vector<Integer> P = new Vector<Integer>();          // Vector for remaining time before moving
        // A thread to ensure locks are released regularly
        class Release implements Runnable {
            public void run() {
                while (true) {
                    synchronized(state) {
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
        void pause() {
            Vector<AutoMove> tmp = new Vector<AutoMove>();
            long curT = System.currentTimeMillis();
            for (int i = 0; i < state.enemyCount; i++) {
                T.get(i).kill();
                P.set(i, (int) ( (P.get(i)+curT-T.get(i).startTime) % state.enemyMoveCooldown) );
            }
            T.clear();
            System.out.println("paused");
        }
        void start() {
            for (int i = 0; i < state.enemyCount; i++) {
                P.add(0);
            }
            resume();
        }
        void resume() {
            long curT = System.currentTimeMillis();
            for (int i = 0; i < state.enemyCount; i++) {
                T.add(new AutoMove(i, curT, state.enemyMoveCooldown-P.get(i)) );
                Thread t = new Thread(T.get(i));
                t.start();
            }
            System.out.println("resumed");
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
                Thread m = new Thread(new Move());
                m.start();
            }
            System.out.println(state.position[1][0]);
        }
    }

    // runner for testing
    public static void main(String[] args) {
        new GameEngine();
    }
}