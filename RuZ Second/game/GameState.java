package game;

import java.util.Vector;

import game.character.Character;

// Class concerned with the state of the game
public class GameState {
    public Maze maze;
    Vector<Character> characters = new Vector<Character>();
    final int enemyCount = 3;
    int score;
    int goalScore, goalScoreReductionRate;
    public int enemyMoveCooldown, killScore;
    GameState(int x) {
        if (x == 1) {           // Easy
            maze = new Maze(15, 40);
            enemyMoveCooldown = 600;
            goalScore = 100;
            goalScoreReductionRate = 3;
            killScore = 20;
        } else if (x == 2) {    // Medium
            maze = new Maze(27, 22);
            enemyMoveCooldown = 400;
            goalScore = 250;
            goalScoreReductionRate = 4;
            killScore = 40;
        } else if (x == 3) {    // Hard
            maze = new Maze(35, 17);
            enemyMoveCooldown = 200;
            goalScore = 800;
            goalScoreReductionRate = 5;
            killScore = 50;
        }
        score = 0;
        for (int i = 0; i <= enemyCount; i++) {
            characters.add(new Character(this, i, maze.corners[i][0], maze.corners[i][1]));
        }
    }
}