package game;

import java.util.Vector;

import game.character.Character;
import game.scorer.Scorer;

// Class concerned with the state of the game
public class GameState {
    public boolean paused;
    public Maze maze;
    Vector<Character> characters = new Vector<Character>();
    final int enemyCount = 3;
    Scorer scorer;
    public int enemyMoveCooldown;
    GameState(int x) {
        if (x == 1) {           // Easy
            maze = new Maze(15, 40);
            enemyMoveCooldown = 600;
            scorer = new Scorer(this, 100, 3, 20);
        } else if (x == 2) {    // Medium
            maze = new Maze(27, 22);
            enemyMoveCooldown = 400;
            scorer = new Scorer(this, 250, 4, 40);
        } else if (x == 3) {    // Hard
            maze = new Maze(35, 17);
            enemyMoveCooldown = 200;
            scorer = new Scorer(this, 800, 5, 50);
        }
        for (int i = 0; i <= enemyCount; i++) {
            characters.add(new Character(this, i, maze.corners[i][0], maze.corners[i][1]));
        }
        paused = false;
    }
}