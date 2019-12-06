package game.character;

import game.GameState;

class AutoMove implements Runnable {
    GameState state;
    Character character;
    long startTime;
    long pendingSleep;
    volatile boolean dead = false;

    // character being moved, start time of thread
    AutoMove(GameState state, Character character, long startTime, long pendingSleep) {
        this.state = state;
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
                character.botMove();
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