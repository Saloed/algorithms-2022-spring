package code.game;

import java.util.HashSet;
import java.util.Set;

public class Checker {
    private final Game parentGame;
    private final Game.Side color;
    private boolean isKing = false;
    private int col;
    private int row;

    public Checker(int row, int col, Game.Side color, Game parentGame) {
        this.col = col;
        this.row = row;
        this.color = color;
        this.parentGame = parentGame;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Game.Side getColor() {
        return color;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing() {
        isKing = true;
    }

    public Checker getCopy(Game game) {
        Checker copy = new Checker(this.row, this.col, this.color, game);
        copy.isKing = this.isKing;
        return copy;
    }

    @Override
    public String toString() {
        String side;
        String king;
        if (color == Game.Side.BLACK) side = "black";
        else side = "white";
        if (isKing) king = "king";
        else king = "";
        return "{" + side + " " + king + "[" + row + ", " + col + "]}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checker checker = (Checker) o;
        return color == checker.color && isKing == checker.isKing && col == checker.col && row == checker.row;
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(parentGame, color, isKing, col, row);
//    }

    public void move(Move move) {
        parentGame.getGameBoard()[move.getRow()][move.getCol()] = this;
        parentGame.getGameBoard()[row][col] = null;
        row = move.getRow();
        col = move.getCol();
        if (move.getCheckerToRemove() != null)
            parentGame.getGameBoard()[move.getCheckerToRemove().getRow()][move.getCheckerToRemove().getCol()].remove();
//            move.getCheckerToRemove().remove();
        if (parentGame.getTurn() == Game.Side.BLACK) {
            if (parentGame.getWhiteLeft().isEmpty()) parentGame.endGame();
            if (!isKing && row == 7) isKing = true;
        } else {
            if (parentGame.getBlackLeft().isEmpty()) parentGame.endGame();
            if (!isKing && row == 0) isKing = true;
        }
        if (getLegalMoves(true).isEmpty() || move.getCheckerToRemove() == null) {
            parentGame.changeTurn();
        } else parentGame.setMovingChecker(this);
    }

    public void remove() {
        parentGame.getGameBoard()[row][col] = null;
        if (color == Game.Side.WHITE) {
            parentGame.getWhiteLeft().removeIf(checker -> checker.equals(this));
        } else parentGame.getBlackLeft().removeIf(checker -> checker.equals(this));
    }

    public Set<Move> kingMoves(boolean onlySkip) {
        Set<Move> moves = new HashSet<>();
        int i = 1;
        while (row + i != 8 && col + i != 8) {
            if (parentGame.getGameBoard()[row + i][col + i] == null) {
                if (!onlySkip)
                    moves.add(new Move(row + i, col + i, null));
            } else if (parentGame.getGameBoard()[row + i][col + i].color == color) break;
            else if (parentGame.getGameBoard()[row + i][col + i].color != color && row + i + 1 != 8 && col + i + 1 != 8) {
                if (parentGame.getGameBoard()[row + i + 1][col + i + 1] != null) break;
                if (!onlySkip) moves.clear();
                onlySkip = true;
                moves.add(new Move(row + i + 1, col + i + 1, parentGame.getGameBoard()[row + i][col + i]));
                break;
            }
            i++;
        }
        i = 1;
        while (row + i != 8 && col - i != -1) {
            if (parentGame.getGameBoard()[row + i][col - i] == null) {
                if (!onlySkip)
                    moves.add(new Move(row + i, col - i, null));
            } else if (parentGame.getGameBoard()[row + i][col - i].color == color) break;
            else if (parentGame.getGameBoard()[row + i][col - i].color != color && row + i + 1 != 8 && col - i - 1 != -1) {
                if (parentGame.getGameBoard()[row + i + 1][col - i - 1] != null) break;
                if (!onlySkip) moves.clear();
                onlySkip = true;
                moves.add(new Move(row + i + 1, col - i - 1, parentGame.getGameBoard()[row + i][col - i]));
                break;
            }
            i++;
        }
        i = 1;
        while (row - i != -1 && col + i != 8) {
            if (parentGame.getGameBoard()[row - i][col + i] == null) {
                if (!onlySkip)
                    moves.add(new Move(row - i, col + i, null));
            } else if (parentGame.getGameBoard()[row - i][col + i].color == color) break;
            else if (parentGame.getGameBoard()[row - i][col + i].color != color && row - i - 1 != -1 && col + i + 1 != 8) {
                if (parentGame.getGameBoard()[row - i - 1][col + i + 1] != null) break;
                if (!onlySkip) moves.clear();
                onlySkip = true;
                moves.add(new Move(row - i - 1, col + i + 1, parentGame.getGameBoard()[row - i][col + i]));
                break;
            }
            i++;
        }
        i = 1;
        while (row - i != -1 && col - i != -1) {
            if (parentGame.getGameBoard()[row - i][col - i] == null) {
                if (!onlySkip)
                    moves.add(new Move(row - i, col - i, null));
            } else if (parentGame.getGameBoard()[row - i][col - i].color == color) break;
            else if (parentGame.getGameBoard()[row - i][col - i].color != color && row - i - 1 != -1 && col - i - 1 != -1) {
                if (parentGame.getGameBoard()[row - i - 1][col - i - 1] != null) break;
                if (!onlySkip) moves.clear();
                moves.add(new Move(row - i - 1, col - i - 1, parentGame.getGameBoard()[row - i][col - i]));
                break;
            }
            i++;
        }
        return moves;
    }

    public Set<Move> defaultCheckerMoves(boolean onlySkip) {
        Set<Move> moves = new HashSet<>();
        if (row + 1 != 8 && col + 1 != 8) {
            if (parentGame.getGameBoard()[row + 1][col + 1] == null) {
                if (!onlySkip && color == Game.Side.BLACK)
                    moves.add(new Move(row + 1, col + 1, null));
            } else if (parentGame.getGameBoard()[row + 1][col + 1].color != color && row + 2 != 8 && col + 2 != 8 && parentGame.getGameBoard()[row + 2][col + 2] == null) {
                onlySkip = true;
                moves.add(new Move(row + 2, col + 2, parentGame.getGameBoard()[row + 1][col + 1]));
            }
        }
        if (row + 1 != 8 && col - 1 != -1) {
            if (parentGame.getGameBoard()[row + 1][col - 1] == null) {
                if (!onlySkip && color == Game.Side.BLACK)
                    moves.add(new Move(row + 1, col - 1, null));
            } else if (parentGame.getGameBoard()[row + 1][col - 1].color != color && row + 2 != 8 && col - 2 != -1 && parentGame.getGameBoard()[row + 2][col - 2] == null) {
                if (!onlySkip) moves.clear();
                onlySkip = true;
                moves.add(new Move(row + 2, col - 2, parentGame.getGameBoard()[row + 1][col - 1]));
            }
        }
        if (row - 1 != -1 && col + 1 != 8) {
            if (parentGame.getGameBoard()[row - 1][col + 1] == null) {
                if (!onlySkip && color == Game.Side.WHITE)
                    moves.add(new Move(row - 1, col + 1, null));
            } else if (parentGame.getGameBoard()[row - 1][col + 1].color != color && row - 2 != -1 && col + 2 != 8 && parentGame.getGameBoard()[row - 2][col + 2] == null) {
                if (!onlySkip) moves.clear();
                onlySkip = true;
                moves.add(new Move(row - 2, col + 2, parentGame.getGameBoard()[row - 1][col + 1]));
            }
        }
        if (row - 1 != -1 && col - 1 != -1) {
            if (parentGame.getGameBoard()[row - 1][col - 1] == null) {
                if (!onlySkip & color == Game.Side.WHITE)
                    moves.add(new Move(row - 1, col - 1, null));
            } else if (parentGame.getGameBoard()[row - 1][col - 1].color != color && row - 2 != -1 && col - 2 != -1 && parentGame.getGameBoard()[row - 2][col - 2] == null) {
                if (!onlySkip) moves.clear();
                moves.add(new Move(row - 2, col - 2, parentGame.getGameBoard()[row - 1][col - 1]));
            }
        }
        return moves;
    }

    public Set<Move> getLegalMoves(boolean onlySkip) {
        Set<Move> moves = new HashSet<>();
        if (parentGame.getMovingChecker() != null) {
            if (this != parentGame.getMovingChecker()) return moves;
        }
        if (parentGame.getThereAreMovesWithSkip()) onlySkip = true;
        if (this.isKing) {
            return kingMoves(onlySkip);
        } else {
            return defaultCheckerMoves(onlySkip);
        }
    }
}