public class Position {

    public int width;   // width
    public int height;   // height

    public Position(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return width == position.width &&
                height == position.height;
    }

    public int getDx(Position goal) {
        if (goal.width - this.width < 0) {
            return -1;
        }
        if (goal.width - this.width > 0) {
            return 1;
        }
        return 0;
    }

    public int getDy(Position goal) {
        if (goal.height - this.height < 0) {
            return -1;
        }
        if (goal.height - this.height > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "(" + width + ";" + height + ")";
    }
}
