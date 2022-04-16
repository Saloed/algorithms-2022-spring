package code.game;

import java.util.HashSet;
import java.util.Set;

public class Game {
    private Checker[][] gameBoard = new Checker[8][8];
    private Set<Checker> whiteLeft = new HashSet<>();
    private Set<Checker> blackLeft = new HashSet<>();
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

    public Game(Game gameToCopy) { //copy constructor
        this.gameOver = gameToCopy.gameOver;
        this.winner = gameToCopy.winner;
        this.thereAreMovesWithSkip = gameToCopy.thereAreMovesWithSkip;
        if (gameToCopy.movingChecker != null) this.movingChecker = gameToCopy.movingChecker.getCopy(this);
        this.turn = gameToCopy.turn;
        Checker[][] gameBoardCloned = new Checker[8][8];
        Set<Checker> whiteLeftCloned = new HashSet<>();
        Set<Checker> blackLeftCloned = new HashSet<>();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (gameToCopy.gameBoard[i][j] != null) {
                    Checker copyChecker = gameToCopy.gameBoard[i][j].getCopy(this);
                    if(copyChecker.getColor()==Side.BLACK) blackLeftCloned.add(copyChecker);
                    else whiteLeftCloned.add(copyChecker);
                    gameBoardCloned[i][j] = copyChecker;
                }
        this.gameBoard = gameBoardCloned;
        this.whiteLeft = whiteLeftCloned;
        this.blackLeft = blackLeftCloned;
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

    public Checker getMovingChecker() {
        return movingChecker;
    }

    public void setMovingChecker(Checker movingChecker) {
        this.movingChecker = movingChecker;
    }

    public boolean getThereAreMovesWithSkip() {
        return thereAreMovesWithSkip;
    }

    public void changeTurn() {
        thereAreMovesWithSkip = false;
        boolean thereAreNoValidMoves = true;
        movingChecker = null;
        if (turn == Side.WHITE) {
            turn = Side.BLACK;
            for (Checker checker : blackLeft) {
                if (!checker.getLegalMoves(false).isEmpty()) thereAreNoValidMoves = false;
                if (!checker.getLegalMoves(true).isEmpty()) {
                    thereAreMovesWithSkip = true;
                    break;
                }
            }
        } else {
            turn = Side.WHITE;
            for (Checker checker : whiteLeft) {
                if (!checker.getLegalMoves(false).isEmpty()) thereAreNoValidMoves = false;
                if (!checker.getLegalMoves(true).isEmpty()) {
                    thereAreMovesWithSkip = true;
                    break;
                }
            }
        }
        if (thereAreNoValidMoves) {
            if (turn == Side.BLACK) turn = Side.WHITE;
            else turn = Side.BLACK;
            endGame();
        }
    }

    public void setTurn(Side turn) {
        this.turn = turn;
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

    public enum Side {
        BLACK,
        WHITE;

        public Side getOpposite() {
            if (this == WHITE) return BLACK;
            else return WHITE;
        }
    }

}