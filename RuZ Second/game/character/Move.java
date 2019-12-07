package game.character;

import game.state.State;

class Move implements Runnable {
    State state;
    Character character;
    int dir;
    Move(State state, Character character, int dir) {
        this.state = state;
        this.character = character;
        this.dir = dir;
    }
    public void run() {
        if (!state.gameOver()) {
            synchronized(state) {
                // Do some player movement
                character.move(dir);
                state.notify();
            }
        }
    }
}