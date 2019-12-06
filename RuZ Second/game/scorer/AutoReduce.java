package game.scorer;

import game.GameState;

class AutoReduce implements Runnable {
    GameState state;
    Scorer scorer;
    long startTime;
    long pendingSleep;
    volatile boolean dead = false;

    AutoReduce(GameState state, Scorer scorer, long startTime, long pendingSleep) {
        this.state = state;
        this.scorer = scorer;
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
                scorer.goalScore = Math.max(0, scorer.goalScore - scorer.goalScoreReductionRate);
                state.notify();
                try {
                    state.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(scorer.goalScoreReductionPeriod);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}