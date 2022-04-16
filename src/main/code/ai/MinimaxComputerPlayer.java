package code.ai;

import code.game.Checker;
import code.game.Game;
import code.game.Move;
import javafx.util.Pair;

import java.util.List;
import java.util.Set;

public class MinimaxComputerPlayer extends ComputerPlayer {
    public static final int[][] DEFAULT_TILES_WITH_SCORES = {
            {0, 4, 0, 4, 0, 4, 0, 4},
            {4, 0, 3, 0, 3, 0, 3, 0},
            {0, 3, 0, 2, 0, 2, 0, 4},
            {4, 0, 2, 0, 1, 0, 3, 0},
            {0, 3, 0, 1, 0, 2, 0, 4},
            {4, 0, 2, 0, 2, 0, 3, 0},
            {0, 3, 0, 3, 0, 3, 0, 4},
            {4, 0, 4, 0, 4, 0, 4, 0}
    };
    //        public static final int[][] KINGS_TILES_WITH_SCORES = {
//            {0, 6, 0, 6, 0, 6, 0, 6},
//            {6, 0, 8, 0, 8, 0, 8, 0},
//            {0, 8, 0, 10, 0, 10, 0, 6},
//            {6, 0, 10, 0, 12, 0, 8, 0},
//            {0, 8, 0, 12, 0, 10, 0, 6},
//            {6, 0, 10, 0, 10, 0, 8, 0},
//            {0, 8, 0, 8, 0, 8, 0, 6},
//            {6, 0, 6, 0, 6, 0, 6, 0}
//    };
    public static final int[][] KINGS_TILES_WITH_SCORES = {
            {0, 6, 0, 6, 0, 6, 0, 6},
            {6, 0, 6, 0, 6, 0, 6, 0},
            {0, 6, 0, 6, 0, 6, 0, 6},
            {6, 0, 6, 0, 6, 0, 6, 0},
            {0, 6, 0, 6, 0, 6, 0, 6},
            {6, 0, 6, 0, 6, 0, 6, 0},
            {0, 6, 0, 6, 0, 6, 0, 6},
            {6, 0, 6, 0, 6, 0, 6, 0}
    };

    private final Game game;
    private final Game.Side side;
    private final int depth;

    public MinimaxComputerPlayer(Game game, Game.Side side, boolean hard) {
        this.game = game;
        this.side = side;
        if (hard) depth = 8;
        else depth = 2;
    }

    public Pair<Checker, List<Move>> findBestMove() { // public for Board
        Set<Pair<Checker, List<Move>>> listOfMoves;
        if (side == Game.Side.WHITE)
            listOfMoves = getPossibleMovesInSet(game.getWhiteLeft(), game);
        else listOfMoves = getPossibleMovesInSet(game.getBlackLeft(), game);
        double currentScore;
        double maxScore = Double.NEGATIVE_INFINITY;
        List<Move> bestMove = null;
        Game copyGame;
        Checker checker = null;
        for (Pair<Checker, List<Move>> possibleAction : listOfMoves) {
            copyGame = new Game(makeMove(game, possibleAction.getKey(), possibleAction.getValue()));
            currentScore = minimax(depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, side.getOpposite(), copyGame);
            if (currentScore > maxScore) {
                maxScore = currentScore;
                bestMove = possibleAction.getValue();
                checker = possibleAction.getKey();
            }
        }
        return new Pair<>(checker, bestMove);
    }

    private Double minimax(int depth, double alpha, double beta, Game.Side currentSide, Game currentGame) {
        if (depth == 0 || currentGame.isGameOver()) return calcScore(currentGame, currentSide.getOpposite());
        Set<Pair<Checker, List<Move>>> listOfMoves;
        if (currentSide == Game.Side.WHITE)
            listOfMoves = getPossibleMovesInSet(currentGame.getWhiteLeft(), currentGame);
        else listOfMoves = getPossibleMovesInSet(currentGame.getBlackLeft(), currentGame);
        double currentScore;
        Game gameWithMove;
        Double bestNextMove;
        if (currentSide == side) {
            double maxScore = Double.NEGATIVE_INFINITY;
            for (Pair<Checker, List<Move>> possibleAction : listOfMoves) {
                gameWithMove = makeMove(currentGame, possibleAction.getKey(), possibleAction.getValue());
                bestNextMove = minimax(depth - 1, alpha, beta, currentSide.getOpposite(), gameWithMove);
                currentScore = bestNextMove;
                maxScore = Math.max(maxScore, currentScore);
                alpha = Math.max(alpha, currentScore);    // depth 8 shows profits of pruning
                if (beta <= alpha) break;
            }
            return maxScore;
        } else {
            double minScore = Double.POSITIVE_INFINITY;
            for (Pair<Checker, List<Move>> possibleAction : listOfMoves) {
                gameWithMove = makeMove(currentGame, possibleAction.getKey(), possibleAction.getValue());
                bestNextMove = minimax(depth - 1, alpha, beta, currentSide.getOpposite(), gameWithMove);
                currentScore = bestNextMove;
                minScore = Math.min(minScore, currentScore);
                beta = Math.min(beta, currentScore);
                if (beta <= alpha) break;
            }
            return minScore;
        }
    }

    public Double calcScore(Game gameToCount, Game.Side side) { // public for tests
        int blackMultiplier, whiteMultiplier;
        double score = 0;
        if (side == Game.Side.WHITE) {
            whiteMultiplier = 1;
            blackMultiplier = -1;
        } else {
            whiteMultiplier = -1;
            blackMultiplier = 1;
        }
        score += blackMultiplier * calcScoreOfCheckers(gameToCount.getBlackLeft());
        score += whiteMultiplier * calcScoreOfCheckers(gameToCount.getWhiteLeft());
        return score;
    }

    private double calcScoreOfCheckers(Set<Checker> set) {
        double score = 0;
        for (Checker checker : set) {
            if (checker.isKing()) score += KINGS_TILES_WITH_SCORES[checker.getRow()][checker.getCol()];
            else score += DEFAULT_TILES_WITH_SCORES[checker.getRow()][checker.getCol()];
        }
        return score;
    }

}