package game.scorer;

import game.state.State;

class ScoredKill implements Runnable {
    State state;
    Scorer scorer;
    ScoredKill(State state, Scorer scorer) {
        this.state = state;
        this.scorer = scorer;
    }
    public void run() {
        if (!state.gameOver()) {
            synchronized(state) {
                scorer.score += scorer.killScore;
                state.notify();
            }
        }
    }
}