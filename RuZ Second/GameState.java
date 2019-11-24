// Class concerned with the state of the game
public class GameState {
    int score;
    int goalScore, goalScoreReductionRate;
    int mazeSize, tileSize;
    int enemyMoveCooldown, killScore;
    final int enemyCount = 3;
    int[][] position = new int[enemyCount + 1][2];       // (row, col) position for 3 enemies + 1 player
    int[][] map;
    GameState(int x) {
        if (x == 1) {           // Easy
            mazeSize = 15;
            tileSize = 40;
            enemyMoveCooldown = 600;
            goalScore = 100;
            goalScoreReductionRate = 3;
            killScore = 20;
        } else if (x == 2) {    // Medium
            mazeSize = 27;
            tileSize = 22;
            enemyMoveCooldown = 400;
            goalScore = 250;
            goalScoreReductionRate = 4;
            killScore = 40;
        } else if (x == 3) {    // Hard
            mazeSize = 35;
            tileSize = 17;
            enemyMoveCooldown = 200;
            goalScore = 800;
            goalScoreReductionRate = 5;
            killScore = 50;
        }
        score = 0;
        map = new int[mazeSize][mazeSize];
    }
}