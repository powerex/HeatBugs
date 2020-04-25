public class Position {

    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        this(0,0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    public int getDx(Position goal) {
        if (goal.x - this.x < 0) {
            return -1;
        }
        if (goal.x - this.x > 0) {
            return 1;
        }
        return 0;
    }

    public int getDy(Position goal) {
        if (goal.y - this.y < 0) {
            return -1;
        }
        if (goal.y - this.y > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }
}
