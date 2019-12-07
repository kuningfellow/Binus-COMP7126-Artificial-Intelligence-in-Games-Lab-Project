package game.scorer;

import game.state.State;

class ScoredGoal implements Runnable {
    State state;
    Scorer scorer;
    ScoredGoal(State state, Scorer scorer) {
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