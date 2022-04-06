package code;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Game {
    private final Checker[][] gameBoard = new Checker[8][8];
    private final Set<Checker> whiteLeft = new HashSet<>();
    private final Set<Checker> blackLeft = new HashSet<>();
    private boolean gameOver = false;
    private Side winner;
    private boolean thereAreMovesWithSkip = false;
    private Checker movingChecker = null;
    private Side turn;

    public Game() {
        turn = Side.WHITE;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row % 2 == 0 && col % 2 != 0) || (row % 2 != 0 && col % 2 == 0)) {
                    if (row < 3) {
                        Checker checker = new Checker(row, col, Side.BLACK, this);
                        blackLeft.add(checker);
                        gameBoard[row][col] = checker;
                    }
                    if (row > 4) {
                        Checker checker = new Checker(row, col, Side.WHITE, this);
                        whiteLeft.add(checker);
                        gameBoard[row][col] = checker;
                    }
                }
            }
        }
    }

    enum Side {
        BLACK,
        WHITE
    }

    public Checker[][] getGameBoard() {
        return gameBoard;
    }

    public Set<Checker> getWhiteLeft() {
        return whiteLeft;
    }

    public Set<Checker> getBlackLeft() {
        return blackLeft;
    }

    public Side getTurn() {
        return turn;
    }

    public void changeTurn() {
        thereAreMovesWithSkip = false;
        movingChecker = null;
        if (turn == Side.WHITE) {
            turn = Side.BLACK;
            for (Game.Checker checker : blackLeft)
                if (!checker.getLegalMoves(true).isEmpty()) {
                    thereAreMovesWithSkip = true;
                    break;
                }
        } else {
            turn = Side.WHITE;
            for (Game.Checker checker : whiteLeft)
                if (!checker.getLegalMoves(true).isEmpty()) {
                    thereAreMovesWithSkip = true;
                    break;
                }
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void endGame() {
        gameOver = true;
        winner = turn;
    }

    public Side getWinner() {
        return winner;
    }

    public static class Move {
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

        @Override
        public int hashCode() {
            return Objects.hash(row, col, checkerToRemove);
        }

        @Override
        public String toString() {
            return "Move{" + row + ", " + col + ", " + checkerToRemove + '}';
        }
    }

    public class Checker {
        private final Game parentGame;
        private final Side color;
        private boolean isKing = false;
        private int col;
        private int row;

        public Checker(int row, int col, Side color, Game parentGame) {
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

        public Side getColor() {
            return color;
        }

        public boolean isKing() {
            return isKing;
        }

        public void setKing() {
            isKing = true;
        }

        @Override
        public String toString() {
            String side;
            String king;
            if (color == Side.BLACK) side = "black";
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
            return color == checker.color && isKing == checker.isKing && col == checker.col && row == checker.row && Objects.equals(parentGame, checker.parentGame);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentGame, color, isKing, col, row);
        }

        public void move(Move move) {
            gameBoard[move.getRow()][move.getCol()] = this;
            gameBoard[row][col] = null;
            row = move.getRow();
            col = move.getCol();
            if (move.checkerToRemove != null) move.checkerToRemove.remove();
            if (turn == Side.BLACK) {
                if (whiteLeft.isEmpty()) endGame();
                if (!isKing && row == 7) isKing = true;
            } else {
                if (blackLeft.isEmpty()) endGame();
                if (!isKing && row == 0) isKing = true;
            }
            if (getLegalMoves(true).isEmpty() || move.getCheckerToRemove() == null) {
                changeTurn();
            } else movingChecker = this;
        }

        public void remove() {
            gameBoard[row][col] = null;
            if (color == Side.WHITE) {
                whiteLeft.removeIf(checker -> checker.equals(this));
            } else blackLeft.removeIf(checker -> checker.equals(this));

        }

        public Set<Move> kingMoves(boolean onlySkip) {
            Set<Move> moves = new HashSet<>();
            int i = 1;
            while (row + i != 8 && col + i != 8) {
                if (gameBoard[row + i][col + i] == null) {
                    if (!onlySkip)
                        moves.add(new Move(row + i, col + i, null));
                } else if (gameBoard[row + i][col + i].color == color) break;
                else if (gameBoard[row + i][col + i].color != color && row + i + 1 != 8 && col + i + 1 != 8) {
                    if (gameBoard[row + i + 1][col + i + 1] != null) break;
                    if (!onlySkip) moves.clear();
                    onlySkip = true;
                    moves.add(new Move(row + i + 1, col + i + 1, gameBoard[row + i][col + i]));
                }
                i++;
            }
            i = 1;
            while (row + i != 8 && col - i != -1) {
                if (gameBoard[row + i][col - i] == null) {
                    if (!onlySkip)
                        moves.add(new Move(row + i, col - i, null));
                } else if (gameBoard[row + i][col - i].color == color) break;
                else if (gameBoard[row + i][col - i].color != color && row + i + 1 != 8 && col - i - 1 != -1) {
                    if (gameBoard[row + i + 1][col - i - 1] != null) break;
                    if (!onlySkip) moves.clear();
                    onlySkip = true;
                    moves.add(new Move(row + i + 1, col - i - 1, gameBoard[row + i][col - i]));
                }
                i++;
            }
            i = 1;
            while (row - i != -1 && col + i != 8) {
                if (gameBoard[row - i][col + i] == null) {
                    if (!onlySkip)
                        moves.add(new Move(row - i, col + i, null));
                } else if (gameBoard[row - i][col + i].color == color) break;
                else if (gameBoard[row - i][col + i].color != color && row - i - 1 != -1 && col + i + 1 != 8) {
                    if (gameBoard[row - i - 1][col + i + 1] != null) break;
                    if (!onlySkip) moves.clear();
                    onlySkip = true;
                    moves.add(new Move(row - i - 1, col + i + 1, gameBoard[row - i][col + i]));
                }
                i++;
            }
            i = 1;
            while (row - i != -1 && col - i != -1) {
                if (gameBoard[row - i][col - i] == null) {
                    if (!onlySkip)
                        moves.add(new Move(row - i, col - i, null));
                } else if (gameBoard[row - i][col - i].color == color) break;
                else if (gameBoard[row - i][col - i].color != color && row - i - 1 != -1 && col - i - 1 != -1) {
                    if (gameBoard[row - i - 1][col - i - 1] != null) break;
                    if (!onlySkip) moves.clear();
                    onlySkip = true;
                    moves.add(new Move(row - i - 1, col - i - 1, gameBoard[row - i][col - i]));
                }
                i++;
            }
            return moves;
        }

        public Set<Move> defaultCheckerMoves(boolean onlySkip) {
            Set<Move> moves = new HashSet<>();
            if (row + 1 != 8 && col + 1 != 8) {
                if (gameBoard[row + 1][col + 1] == null) {
                    if (!onlySkip && color == Side.BLACK)
                        moves.add(new Move(row + 1, col + 1, null));
                } else if (gameBoard[row + 1][col + 1].color != color && row + 2 != 8 && col + 2 != 8 && gameBoard[row + 2][col + 2] == null) {
                    onlySkip = true;
                    moves.add(new Move(row + 2, col + 2, gameBoard[row + 1][col + 1]));
                }
            }
            if (row + 1 != 8 && col - 1 != -1) {
                if (gameBoard[row + 1][col - 1] == null) {
                    if (!onlySkip && color == Side.BLACK)
                        moves.add(new Move(row + 1, col - 1, null));
                } else if (gameBoard[row + 1][col - 1].color != color && row + 2 != 8 && col - 2 != -1 && gameBoard[row + 2][col - 2] == null) {
                    if (!onlySkip) moves.clear();
                    onlySkip = true;
                    moves.add(new Move(row + 2, col - 2, gameBoard[row + 1][col - 1]));
                }
            }
            if (row - 1 != -1 && col + 1 != 8) {
                if (gameBoard[row - 1][col + 1] == null) {
                    if (!onlySkip && color == Side.WHITE)
                        moves.add(new Move(row - 1, col + 1, null));
                } else if (gameBoard[row - 1][col + 1].color != color && row - 2 != -1 && col + 2 != 8 && gameBoard[row - 2][col + 2] == null) {
                    if (!onlySkip) moves.clear();
                    onlySkip = true;
                    moves.add(new Move(row - 2, col + 2, gameBoard[row - 1][col + 1]));
                }
            }
            if (row - 1 != -1 && col - 1 != -1) {
                if (gameBoard[row - 1][col - 1] == null) {
                    if (!onlySkip & color == Side.WHITE)
                        moves.add(new Move(row - 1, col - 1, null));
                } else if (gameBoard[row - 1][col - 1].color != color && row - 2 != -1 && col - 2 != -1 && gameBoard[row - 2][col - 2] == null) {
                    if (!onlySkip) moves.clear();
                    moves.add(new Move(row - 2, col - 2, gameBoard[row - 1][col - 1]));
                }
            }
            return moves;
        }

        public Set<Move> getLegalMoves(boolean onlySkip) {
            Set<Move> moves = new HashSet<>();
            if (movingChecker != null) {
                if (this != movingChecker) return moves;
            }
            if (thereAreMovesWithSkip) onlySkip = true;
            if (this.isKing) {
                return kingMoves(onlySkip);
            } else {
                return defaultCheckerMoves(onlySkip);
            }
        }
    }
}
//            int left = col - 1;
//            int right = col + 1;
//            int start = row;
//            if (color) {
//                moves.addAll(leftDiagonal(isKing, false, start - 1, true, left, new HashSet<>(), null));
//                moves.addAll(rightDiagonal(isKing, false, start - 1, true, right, new HashSet<>(), null));
//                moves.addAll(leftDiagonal(isKing, true, start + 1, true, left, new HashSet<>(), null));
//                moves.addAll(rightDiagonal(isKing, true, start + 1, true, right, new HashSet<>(), null));
//            }
//            if (!color) {
//                moves.addAll(leftDiagonal(isKing, false, start + 1, false, left, new HashSet<>(), null));
//                moves.addAll(rightDiagonal(isKing, false, start + 1, false, right, new HashSet<>(), null));
//                moves.addAll(leftDiagonal(isKing, true, start - 1, false, left, new HashSet<>(), null));
//                moves.addAll(rightDiagonal(isKing, true, start - 1, false, right, new HashSet<>(), null));
//            }
//            if(onlySkip)moves.removeIf(move ->move.checkerToRemove ==null);
//            return moves;
//    }

//        public Set<Move> leftDiagonal(boolean isKing, boolean reverse, int start, boolean color, int left, Set<Checker> skipped, Checker lastSkipped) {
//            Set<Move> moves = new HashSet<>();
//            int step;
//            if (reverse) {
//                if (color) step = 1;
//                else step = -1;
//            } else {
//                if (color) step = -1;
//                else step = 1;
//            }
//            int stop;
//            if (!reverse) {
//                if (step == -1)
//                    stop = max(start - 2, -1);
//                else
//                    stop = min(start + 2, 8);
//            } else {
//                if (step == 1)
//                    stop = min(start + 2, 8);
//                else
//                    stop = max(start - 2, -1);
//
//            }
//            while (start != stop) {
//                if (left < 0)
//                    break;                   //исключен выход за левую границу, i и left - координаты текущей клетки
//                if (gameBoard[start][left] == null) {
//                    if (lastSkipped != null) {
//                        skipped.add(lastSkipped);
//                        lastSkipped = null;
//                        int curSize = moves.size();
//                        if(!reverse) {
//                            moves.addAll(this.leftDiagonal(isKing, false, start + step, color, left - 1, skipped, null));
//                            moves.addAll(this.rightDiagonal(isKing, false, start + step, color, left + 1, skipped, null));
//                            moves.addAll(this.leftDiagonal(isKing, true, start - step, color, left - 1, skipped, null));
//                        } else {
//                            moves.addAll(this.leftDiagonal(isKing, true, start + step, color, left - 1, skipped, null));
//                            moves.addAll(this.rightDiagonal(isKing, true, start + step, color, left + 1, skipped, null));
//                            moves.addAll(this.leftDiagonal(isKing, false, start - step, color, left - 1, skipped, null));
//                        }
//                        if (moves.size() == curSize)
//                            moves.add(new Move(start, left, skipped)); // если дальше нет вражеских шашек оставить текущий ход
//                    } else if (skipped.isEmpty() && isKing) {
//                        moves.add(new Move(start, left, new HashSet<>()));
//                    } else if (skipped.isEmpty() && !reverse) {
//                        moves.add(new Move(start, left, new HashSet<>()));
//                        break;
//                    }
//                } else if (gameBoard[start][left].getColor() == color)
//                    break;
//                else {
//                    if (lastSkipped != null)
//                        break;
//                    else {
//                        lastSkipped = gameBoard[start][left];
//                    }
//                }
//                left -= 1;
//                start += step;
//            }
//            return moves;
//        }
//
//        public Set<Move> rightDiagonal(boolean isKing, boolean reverse, int start, boolean color, int right, Set<Checker> skipped, Checker lastSkipped) {
//            Set<Move> moves = new HashSet<>();
//            int step;
//            if (reverse) {
//                if (color) step = 1;
//                else step = -1;
//            } else {
//                if (color) step = -1;
//                else step = 1;
//            }
//            int stop;
//            if (!reverse) {
//                if (step == -1)
//                    stop = max(start - 2, -1);
//                else
//                    stop = min(start + 2, 8);
//            } else {
//                if (step == 1)
//                    stop = min(start + 2, 8);
//                else
//                    stop = max(start - 2, -1);
//            }
//            while (start != stop) {
//                if (right >= 8)
//                    break;                   //исключен выход за левую границу, i и right - координаты текущей клетки
//                if (gameBoard[start][right] == null) {
//                    if (lastSkipped != null) {
//                        skipped.add(lastSkipped);
//                        lastSkipped = null;
//                        int curSize = moves.size();
//                        if (!reverse) {
//                            moves.addAll(this.leftDiagonal(isKing, false, start + step, color, right - 1, skipped, null));
//                            moves.addAll(this.rightDiagonal(isKing, true, start - step, color, right + 1, skipped, null));
//                            moves.addAll(this.rightDiagonal(isKing, false, start + step, color, right + 1, skipped, null));
//                        } else {
//                            moves.addAll(this.leftDiagonal(isKing, true, start + step, color, right - 1, skipped, null));
//                            moves.addAll(this.rightDiagonal(isKing, true, start + step, color, right + 1, skipped, null));
//                            moves.addAll(this.rightDiagonal(isKing, false, start - step, color, right + 1, skipped, null));
//                        }
//                        if (moves.size() == curSize)
//                            moves.add(new Move(start, right, skipped)); // если дальше нет вражеских шашек оставить текущий ход
//                    } else if (skipped.isEmpty() && !reverse) {
//                        moves.add(new Move(start, right, new HashSet<>()));
//                        break;
//                    }
//                } else if (gameBoard[start][right].getColor() == color)
//                    break;
//                else {
//                    if (lastSkipped != null)
//                        break;
//                    else {
//                        lastSkipped = gameBoard[start][right];
//                    }
//                }
//                right += 1;
//                start += step;
//            }
//            return moves;
//        }