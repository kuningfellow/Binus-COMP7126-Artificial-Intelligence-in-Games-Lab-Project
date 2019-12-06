package game.character;

import game.GameState;

class Move implements Runnable {
    GameState state;
    Character character;
    int dir;
    Move(GameState state, Character character, int dir) {
        this.state = state;
        this.character = character;
        this.dir = dir;
    }
    public void run() {
        if (!state.gameOver()) {
            synchronized(state) {
                /**
                    Do some player movement
                */
                character.move(dir);
                state.notify();
            }
        }
    }
}