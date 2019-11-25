import java.util.List;
import java.util.Arrays;
import java.util.Collections;

class Maze {
    // 0 based indexing
    int mazeSize, tileSize;
    int[][] cell;
    Maze(int mazeSize, int tileSize) {
        this.mazeSize = mazeSize;
        this.tileSize = tileSize;
        cell = new int[mazeSize][mazeSize];
        generate();
    }
    boolean insideMaze(int x, int y) {
        if (x > 0 && x < this.mazeSize-1 && y > 0 && y < this.mazeSize-1) {
            return true;
        } else {
            return false;
        }
    }
    boolean cellVisited(int x, int y) {
        if (x == this.mazeSize/2 && y == this.mazeSize/2) {
            return false;       // Center room can have multiple branches
        } else if (insideMaze(x, y) && this.cell[x][y] == 1) {
            return true;
        } else {
            return false;
        }
    }
    void print() {
        int[][] visit = new int[this.mazeSize][this.mazeSize];
        for (int i = 0; i < this.mazeSize; i++) {
            for (int j = 0; j < this.mazeSize; j++) {
                System.out.printf("%c", this.cell[i][j] == 1 ? ' ' : 'X');
            }
            System.out.println("");
        }
    }
    List<Integer> getBranchingOrder() {
        Integer[] arr = {0, 1, 2, 3};
        List<Integer> ret = Arrays.asList(arr);
        Collections.shuffle(ret);
        return ret;
    }
    void dfs(int x, int y, int dir) {
        if (!insideMaze(x, y)) return;
        if (cellVisited(x, y)) return;
        if (x%2 == 1 && y%2 == 1) {
            // In a room cell
            this.cell[x][y] = 1;
            List<Integer> branchingOrder = getBranchingOrder();
            for (int i = 0; i < branchingOrder.size(); i++) {
                int branch = branchingOrder.get(i);
                if (branch == 0) dfs(x+1, y, branch);
                else if (branch == 1) dfs(x, y+1, branch);
                else if (branch == 2) dfs(x-1, y, branch);
                else if (branch == 3) dfs(x, y-1, branch);
            }
        } else {
            // Joining two rooms
            // Only do so when the other room isn't visited (except center room)
            if (dir == 0 && !cellVisited(x+1, y)) {
                this.cell[x][y] = 1;
                dfs(x+1, y, dir);
            } else if (dir == 1 && !cellVisited(x, y+1)) {
                this.cell[x][y] = 1;
                dfs(x, y+1, dir);
            } else if (dir == 2 && !cellVisited(x-1, y)) {
                this.cell[x][y] = 1;
                dfs(x-1, y, dir);
            } else if (dir == 3 && !cellVisited(x, y-1)) {
                this.cell[x][y] = 1;
                dfs(x, y-1, dir);
            }
        }
    }
    void generate() {
        dfs(mazeSize/2, mazeSize/2, 0);     // Start at center of maze (as requested)
    }
}

// Class concerned with the state of the game
public class GameState {
    Maze maze;
    int score;
    int goalScore, goalScoreReductionRate;
    int enemyMoveCooldown, killScore;
    final int enemyCount = 3;
    int[][] position = new int[enemyCount + 1][2];       // (row, col) position for 3 enemies + 1 player
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
    }
}