package game.character;

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