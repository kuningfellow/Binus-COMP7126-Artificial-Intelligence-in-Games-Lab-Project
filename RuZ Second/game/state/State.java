package game.state;

import java.util.Vector;

import game.character.Character;
import game.scorer.Scorer;
import game.maze.Maze;

// Class concerned with the state of the game
public class State {
    public boolean playerKilled;
    public boolean paused;
    public Maze maze;
    public Scorer scorer;
    public Vector<Character> characters = new Vector<Character>();
    public final int enemyCount = 3;
    public int enemyMoveCooldown;
    public State(int x) {
        if (x == 1) {           // Easy
            maze = new Maze(this, 15, 40);
            enemyMoveCooldown = 600;
            scorer = new Scorer(this, 100, 3, 20);
        } else if (x == 2) {    // Medium
            maze = new Maze(this, 27, 22);
            enemyMoveCooldown = 400;
            scorer = new Scorer(this, 250, 4, 40);
        } else if (x == 3) {    // Hard
            maze = new Maze(this, 35, 17);
            enemyMoveCooldown = 200;
            scorer = new Scorer(this, 800, 5, 50);
        }
        for (int i = 0; i <= enemyCount; i++) {
            characters.add(new Character(this, i, maze.corners[i][0], maze.corners[i][1]));
        }
        playerKilled = false;
        paused = false;
    }
    public boolean gameOver() {
        if (playerKilled || scorer.goalFounder != -1) {
            return true;
        } else {
            return false;
        }
    }
}