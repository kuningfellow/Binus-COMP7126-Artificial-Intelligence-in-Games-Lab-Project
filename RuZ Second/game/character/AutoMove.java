package game.character;

import game.state.State;

class AutoMove implements Runnable {
    State state;
    Character character;
    long startTime;
    long pendingSleep;
    volatile boolean dead = false;

    AutoMove(State state, Character character, long startTime, long pendingSleep) {
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
        while (!dead && !state.gameOver()) {
            synchronized(state) {
                // Do some automatic character movement
                character.botMove();
                state.notify();
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