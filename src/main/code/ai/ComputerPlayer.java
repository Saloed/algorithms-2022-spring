package code.ai;

import code.game.Checker;
import code.game.Game;
import code.game.Move;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class ComputerPlayer {

    public abstract Pair<Checker, List<Move>> findBestMove();

    public Set<List<Move>> traverse(Game initialGame, Checker initialChecker, Move initialMove) {
        Set<List<Move>> list = new HashSet<>();
        LinkedList<Move> queue = new LinkedList<>();
        Game copyGame = makeMove(initialGame, initialChecker, List.of(initialMove)); //copy of an initial game
        Checker copyChecker = copyGame.getGameBoard()[initialMove.getRow()][initialMove.getCol()];
        if (!copyChecker.getLegalMoves(true).isEmpty()) {
            Set<List<Move>> tempList = new HashSet<>();
            for (Move newMove : copyChecker.getLegalMoves(true)) {
                tempList.addAll(traverse(copyGame, copyChecker, newMove));
            }
            for (List<Move> queueOfMoves : tempList) {
                queue.addAll(queueOfMoves);
                queue.addFirst(initialMove);
                list.add(new LinkedList<>(queue));
                queue.clear();
            }
        } else {
            queue.addFirst(initialMove);
            list.add(queue);
        }
        return list;
    }

    public Set<Pair<Checker, List<Move>>> getPossibleMovesInSet(Set<Checker> set, Game currentGame) {
        Set<Pair<Checker, List<Move>>> listOfMoves = new HashSet<>();
        for (Checker checker : set) {
            for (Move move : checker.getLegalMoves(false)) {
                if (move.getCheckerToRemove() == null) {
                    List<Move> list = new LinkedList<>();
                    list.add(move);
                    listOfMoves.add(new Pair<>(checker, list));
                } else {
                    for (List<Move> possibleDeque : traverse(currentGame, checker, move))
                        listOfMoves.add(new Pair<>(checker, possibleDeque));
                }
            }
        }
        return listOfMoves;
    }

    public Game makeMove(Game currentGame, Checker movingChecker, List<Move> queueOfMoves) {
        Game copyGame = new Game(currentGame);
        Checker copyChecker = copyGame.getGameBoard()[movingChecker.getRow()][movingChecker.getCol()];
        for (Move move : queueOfMoves)
            copyChecker.move(new Move(move.getRow(), move.getCol(), move.getCheckerToRemove()));
        return copyGame;
    }

}