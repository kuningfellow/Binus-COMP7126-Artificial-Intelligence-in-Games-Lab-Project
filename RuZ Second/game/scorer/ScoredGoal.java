package game.scorer;

import game.GameState;

class ScoredGoal implements Runnable {
    GameState state;
    Scorer scorer;
    ScoredGoal(GameState state, Scorer scorer) {
        this.state = state;
        this.scorer = scorer;
    }
    public void run() {
        synchronized(state) {
            scorer.score += scorer.goalScore;
            state.notify();
        }
    }
}