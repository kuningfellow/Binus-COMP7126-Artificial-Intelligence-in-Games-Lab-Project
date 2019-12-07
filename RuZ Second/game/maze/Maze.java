package game.maze;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import game.state.State;

public class Maze {
    State state;
    // 0 based indexing
    public int size, tileSize;
    public int[][] cell;           // 0 if wall, 1 if floor
    public int[][] visitor;        // stores bitmask of characters in a cell
    public int[][] corners;        // stores 4 corners of the maze
    public Maze(State state, int size, int tileSize) {
        this.state = state;
        this.size = size;
        this.tileSize = tileSize;
        cell = new int[size][size];
        visitor = new int[size][size];
        corners = new int[4][2];
        corners[0][0] = 1;
        corners[0][1] = 1;
        corners[1][0] = 1;
        corners[1][1] = size-2;
        corners[2][0] = size-2;
        corners[2][1] = 1;
        corners[3][0] = size-2;
        corners[3][1] = size-2;
        generate();
    }
    public boolean isFloor(int x, int y) {
        if (!insideMaze(x, y)) {
            return false;
        } else if ((this.cell[x][y] & 1) == 1) {
            return true;
        } else {
            return false;
        }
    }
    public void enterCell(int id, int x, int y) {
        this.visitor[x][y] |= 1 << id;
        if (x == this.size/2 && y == this.size/2) {
            state.scorer.goalFounder = id;
            if (id == 0) {
                // If player gets the treasure, add treasure value to total score
                state.scorer.scoredGoal();
            }
        } else {
            for (int i = 0; i <= state.enemyCount; i++) {
                if (i == id) continue;
                if ((this.visitor[x][y] & (1 << i)) > 0) {
                    state.characters.get(i).kill(id);
                }
            }
        }
    }
    public void leaveCell(int id, int x, int y) {
        this.visitor[x][y] |= 1 << id;
        this.visitor[x][y] ^= 1 << id;
    }
    public int getRandomCorner() {
        List<Integer> rand = getBranchingOrder();
        for (int i = 0; i < rand.size(); i++) {
            int ret = rand.get(i);
            if (visitor[ corners[ret][0] ][ corners[ret][1] ] == 0) {
                return ret;
            }
        }
        return -1;
    }
    public String getTileColor(int x, int y) {
        if (this.visitor[x][y] == 1) {
            return "player";
        } else if (this.visitor[x][y] > 0) {
            return "enemy";
        } else if (x == size/2 && y == size/2) {
            return "goal";
        } else if (this.cell[x][y] == 1) {
            return "floor";
        } else {
            return "wall";
        }
    }
    public void print() {
        int[][] visit = new int[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.visitor[i][j] == 1) {
                    System.out.printf("P");
                } else if (this.visitor[i][j] > 0) {
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
    boolean insideMaze(int x, int y) {
        if (x > 0 && x < this.size-1 && y > 0 && y < this.size-1) {
            return true;
        } else {
            return false;
        }
    }
    boolean cellVisited(int x, int y) {
        if (x == this.size/2 && y == this.size/2) {
            return false;       // Center room can have multiple branches
        } else if (insideMaze(x, y) && this.cell[x][y] == 1) {
            return true;
        } else {
            return false;
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
        dfs(size/2, size/2, 0);     // Start at center of maze (as requested)
    }
}