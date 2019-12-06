package game.scorer;

import game.GameState;

class ScoredKill implements Runnable {
    GameState state;
    Scorer scorer;
    ScoredKill(GameState state, Scorer scorer) {
        this.state = state;
        this.scorer = scorer;
    }
    public void run() {
        synchronized(state) {
            /**
                Do some player movement
            */
            scorer.score += scorer.killScore;
            state.notify();
        }
    }
}