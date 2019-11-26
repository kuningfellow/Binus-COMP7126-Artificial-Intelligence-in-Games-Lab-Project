import java.util.Vector;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

class Maze {
    // 0 based indexing
    int mazeSize, tileSize;
    int[][] cell;
    int[][] corners;
    Maze(int mazeSize, int tileSize) {
        this.mazeSize = mazeSize;
        this.tileSize = tileSize;
        cell = new int[mazeSize][mazeSize];
        corners = new int[4][2];
        corners[0][0] = 1;
        corners[0][1] = 1;
        corners[1][0] = 1;
        corners[1][1] = mazeSize-2;
        corners[2][0] = mazeSize-2;
        corners[2][1] = 1;
        corners[3][0] = mazeSize-2;
        corners[3][1] = mazeSize-2;
        generate();
    }
    boolean insideMaze(int x, int y) {
        if (x > 0 && x < this.mazeSize-1 && y > 0 && y < this.mazeSize-1) {
            return true;
        } else {
            return false;
        }
    }
    boolean isFloor(int x, int y) {
        if (!insideMaze(x, y)) {
            return false;
        } else if ((this.cell[x][y] & 1) == 1) {
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
                if (this.cell[i][j] == 3) {
                    System.out.printf("O");
                } else if (this.cell[i][j] == 1) {
                    System.out.printf(" ");
                } else {
                    System.out.printf("#");
                }
            }
            System.out.println("");
        }
    }
    void enterCell(int x, int y) {
        this.cell[x][y] |= 2;
    }
    void leaveCell(int x, int y) {
        this.cell[x][y] &= 1;
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
    class Character {
        int id;
        int dir;
        class Position {
            int X, Y;
            Position(Position P) {
                this.X = P.X;
                this.Y = P.Y;
            }
            Position(int X, int Y) {
                this.X = X;
                this.Y = Y;
            }
            void move(int dir) {
                if (dir == 0) this.X++;
                else if (dir == 1) this.Y++;
                else if (dir == 2) this.X--;
                else if (dir == 3) this.Y--;
            }
        }
        Position pos;
        long timePhase;
        Character(int id, int x, int y) {
            this.id = id;
            this.pos = new Position(x, y);
        }
        boolean canMove(int dir) {
            Position pos = new Position(this.pos);
            pos.move(dir);
            if (maze.isFloor(pos.X, pos.Y)) {
                return true;
            } else {
                return false;
            }
        }
        void autoMove() {
            if (canMove((dir + 1) % 4)) {
                this.dir = (this.dir + 1) % 4;
            } else if (canMove(dir)) {
            } else if (canMove((dir + 3) % 4)) {
                this.dir = (this.dir + 3) % 4;
            } else {
                this.dir = (this.dir + 2) % 4;
            }
            maze.leaveCell(this.pos.X, this.pos.Y);
            this.pos.move(this.dir);
            maze.enterCell(this.pos.X, this.pos.Y);
        }
    }
    Vector<Character> characters = new Vector<Character>();
    final int enemyCount = 3;
    int score;
    int goalScore, goalScoreReductionRate;
    int enemyMoveCooldown, killScore;
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
            characters.add(new Character(i, maze.corners[i][0], maze.corners[i][1]));
        }
    }
}