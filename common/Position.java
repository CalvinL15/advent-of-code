package common;

import java.util.Objects;

public class Position {
    int row;
    int col;
    Direction direction;

    public Position(int row, int col, Direction direction) {
        this.row = row;
        this.col = col;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col && direction == position.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, direction);
    }
}
