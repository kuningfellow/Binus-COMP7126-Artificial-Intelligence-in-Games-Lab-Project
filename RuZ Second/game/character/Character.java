package game.character;

import game.state.State;

public class Character {
    State state;
    AutoMove autoMove;
    int id;
    int dir;
    Position pos;
    long timePhase;
    public Character(State state, int id, int x, int y) {
        this.state = state;
        this.id = id;
        this.pos = new Position(x, y);
        this.autoMove = null;
        this.state.maze.enterCell(this.id, this.pos.X, this.pos.Y);
    }
    boolean canMove(int dir) {
        Position pos = new Position(this.pos);
        pos.move(dir);
        if (this.state.maze.isFloor(pos.X, pos.Y)) {
            return true;
        } else {
            return false;
        }
    }
    void botMove() {
        if (canMove((dir + 1) % 4)) {
            this.dir = (this.dir + 1) % 4;
        } else if (canMove(dir)) {
        } else if (canMove((dir + 3) % 4)) {
            this.dir = (this.dir + 3) % 4;
        } else {
            this.dir = (this.dir + 2) % 4;
        }
        this.state.maze.leaveCell(this.id, this.pos.X, this.pos.Y);
        this.pos.move(this.dir);
        this.state.maze.enterCell(this.id, this.pos.X, this.pos.Y);
    }
    void move(int dir) {
        this.dir = dir;
        if (canMove(dir)) {
            this.state.maze.leaveCell(this.id, this.pos.X, this.pos.Y);
            this.pos.move(this.dir);
            this.state.maze.enterCell(this.id, this.pos.X, this.pos.Y);
        }
    }

    public void kill(int killer) {
        // player is killed
        if (this.id == 0) {
            this.state.maze.leaveCell(this.id, this.pos.X, this.pos.Y);
            this.state.playerKilled = true;
        } else {
            // player is the killer
            if (killer == 0) {
                this.state.scorer.scoredKill();
            }
            pauseAutoMove();
            this.state.maze.leaveCell(this.id, this.pos.X, this.pos.Y);
            int corner = this.state.maze.getRandomCorner();
            this.pos.X = this.state.maze.corners[corner][0];
            this.pos.Y = this.state.maze.corners[corner][1];
            this.state.maze.enterCell(this.id, this.pos.X, this.pos.Y);
            // Reset character clock when killed
            timePhase = 0;
            resumeAutoMove();
        }
    }
    public void pauseAutoMove() {
        if (this.autoMove != null) {
            long curT = System.currentTimeMillis();
            this.autoMove.kill();
            this.timePhase = ( this.timePhase + curT - this.autoMove.startTime ) % state.enemyMoveCooldown;
            this.autoMove = null;
        }
    }
    public void resumeAutoMove() {
        if (this.autoMove == null) {
            long curT = System.currentTimeMillis();
            this.autoMove = new AutoMove(this.state, this, curT, state.enemyMoveCooldown - this.timePhase);
            Thread t = new Thread(this.autoMove);
            t.start();
        }
    }
    public void manualMove(int dir) {
        if (this.state.paused == false) {
            Thread t = new Thread(new Move(this.state, this, dir));
            t.start();
        }
    }
    public void manualMove(String dir) {
        if (dir.equals("UP")) {
            manualMove(2);
        } else if (dir.equals("DOWN")) {
            manualMove(0);
        } else if (dir.equals("LEFT")) {
            manualMove(3);
        } else if (dir.equals("RIGHT")) {
            manualMove(1);
        }
    }
}