package common;

import java.util.Objects;

public class Coordinate {
    public int row;
    public int col;
    public Coordinate(int row, int col){
        this.row = row;
        this.col = col;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate coordinate = (Coordinate) o;
        return row == coordinate.row && col == coordinate.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
