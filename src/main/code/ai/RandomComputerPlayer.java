package code.ai;

import code.game.Checker;
import code.game.Game;
import code.game.Move;
import javafx.util.Pair;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomComputerPlayer extends ComputerPlayer {
    private final Game game;
    private final Game.Side side;

    public RandomComputerPlayer(Game game, Game.Side side) {
        this.game = game;
        this.side = side;
    }

    public Pair<Checker, List<Move>> findBestMove() {
        Set<Pair<Checker, List<Move>>> listOfMoves;
        if (side == Game.Side.WHITE)
            listOfMoves = getPossibleMovesInSet(game.getWhiteLeft(), game);
        else listOfMoves = getPossibleMovesInSet(game.getBlackLeft(), game);
        int size = listOfMoves.size();
        int num = new Random().nextInt(size);
        int i = 0;
        for (Pair<Checker, List<Move>> pair : listOfMoves) {
            if (i == num) return pair;
            i++;
        }
        return null;
    }

}