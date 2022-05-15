package code.game;

import java.util.Objects;

public class Move {
    private final int row;
    private final int col;
    private final Checker checkerToRemove;

    public Move(int newRow, int newCol, Checker checkerToRemove) {
        this.col = newCol;
        this.row = newRow;
        this.checkerToRemove = checkerToRemove;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Checker getCheckerToRemove() {
        return checkerToRemove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return row == move.row && col == move.col && Objects.equals(checkerToRemove, move.checkerToRemove);
    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(row, col, checkerToRemove);
//    }

    @Override
    public String toString() {
        return "Move{" + row + ", " + col + ", " + checkerToRemove + '}';
    }
}