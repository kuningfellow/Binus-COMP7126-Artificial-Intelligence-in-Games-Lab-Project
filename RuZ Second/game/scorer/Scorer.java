package game.scorer;

import game.state.State;

public class Scorer {
    State state;
    int score;
    int goalScore, goalScoreReductionRate;
    public int goalFounder;
    public boolean goalScoreAdded;
    final int goalScoreReductionPeriod = 1000;
    int killScore;
    long timePhase;
    AutoReduce autoReduce;
    public Scorer(State state, int goalScore, int goalScoreReductionRate, int killScore) {
        goalScoreAdded = false;
        score = 0;
        timePhase = 0;
        autoReduce = null;
        goalFounder = -1;
        this.state = state;
        this.goalScore = goalScore;
        this.goalScoreReductionRate = goalScoreReductionRate;
        this.killScore = killScore;
    }
    public void scoredKill() {
        Thread t = new Thread(new ScoredKill(this.state, this));
        t.start();
    }
    public void scoredGoal() {
        Thread t = new Thread(new ScoredGoal(this.state, this));
        t.start();
    }

    public void pauseAutoReduce() {
        if (this.autoReduce != null) {
            long curT = System.currentTimeMillis();
            this.autoReduce.kill();
            this.timePhase = ( this.timePhase + curT - this.autoReduce.startTime ) % goalScoreReductionPeriod;
            this.autoReduce = null;
        }
    }
    public void resumeAutoReduce() {
        if (this.autoReduce == null) {
            long curT = System.currentTimeMillis();
            this.autoReduce = new AutoReduce(this.state, this, curT, goalScoreReductionPeriod - this.timePhase);
            Thread t = new Thread(this.autoReduce);
            t.start();
        }
    }
    public int getScore() {
        return score;
    }
    public int treasureValue() {
        return goalScore;
    }
}