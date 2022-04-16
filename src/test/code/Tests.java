package code;

import code.ai.MinimaxComputerPlayer;
import code.game.Game;
import code.game.Move;
import junit.framework.AssertionFailedError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Tests {

    private static void leaveOneRowEachSide(Game game) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row % 2 == 0 && col % 2 != 0) || (row % 2 != 0 && col % 2 == 0)) {
                    if (row < 3) {
                        if (row != 0) {
                            game.getGameBoard()[row][col].remove();
                            game.getBlackLeft().remove(game.getGameBoard()[row][col]);
                        }
                    }
                    if (row > 4) {
                        if (row != 7) {
                            game.getGameBoard()[row][col].remove();
                            game.getWhiteLeft().remove(game.getGameBoard()[row][col]);
                        }
                    }
                }
            }
        }
    }

    private static void leaveSixPiecesEachSide(Game game) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row % 2 == 0 && col % 2 != 0) || (row % 2 != 0 && col % 2 == 0)) {
                    if (row < 3) {
                        if (row != 0 && (row != 1 || col != 0) && (row != 1 || col != 4)) {
                            game.getGameBoard()[row][col].remove();
                            game.getBlackLeft().remove(game.getGameBoard()[row][col]);
                        }
                    }
                    if (row > 4) {
                        if (row != 7 && (row != 6 || col != 7) && (row != 6 || col != 5)) {
                            game.getGameBoard()[row][col].remove();
                            game.getWhiteLeft().remove(game.getGameBoard()[row][col]);
                        }
                    }
                }
            }
        }
    }

    public static class testGetLegalMoves {
        Game game;
        Set<Move> actual = new HashSet<>();
        Set<Move> expected = new HashSet<>();

        @BeforeEach
        public void createGame() {
            game = new Game();
            actual.clear();
            expected.clear();
        }

        @Test
        public void testDefaultFirstBlackMove() {
            actual = game.getGameBoard()[2][1].getLegalMoves(false);
            expected.add(new Move(3, 0, null));
            expected.add(new Move(3, 2, null));
            assertEquals(expected, actual);
        }

        @Test
        public void testDefaultFirstWhiteMove() {
            actual = game.getGameBoard()[5][2].getLegalMoves(false);
            expected.add(new Move(4, 1, null));
            expected.add(new Move(4, 3, null));
            assertEquals(expected, actual);
        }

        @Test
        public void testSingleLeftSkipBlackMove() {
            game.getGameBoard()[5][4].move(new Move(3, 2, null));
            actual = game.getGameBoard()[2][3].getLegalMoves(false);
            expected.add(new Move(4, 1, game.getGameBoard()[3][2]));
            assertEquals(expected, actual);
        }

        @Test
        public void testSingleRightSkipBlackMove() {
            game.getGameBoard()[5][4].move(new Move(3, 2, null));
            actual = game.getGameBoard()[2][1].getLegalMoves(false);
            expected.add(new Move(4, 3, game.getGameBoard()[3][2]));
            assertEquals(expected, actual);
        }

        @Test
        public void testSurroundedByAlliesBlack() {
            actual = game.getGameBoard()[1][2].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testSurroundedByAlliesAndBorderBLack() {
            actual = game.getGameBoard()[0][7].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testSurroundedByEnemiesWhite() {
            game.getGameBoard()[5][4].move(new Move(3, 2, null));
            actual = game.getGameBoard()[3][2].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testSurroundedByAlliesAndBorderWhite() {
            actual = game.getGameBoard()[7][0].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testSurroundedByAlliesWhite() {
            actual = game.getGameBoard()[7][4].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testMoveWithReverseSkipWhite() {
            game.getGameBoard()[5][4].move(new Move(3, 2, null));
            game.getGameBoard()[2][3].move(new Move(4, 3, null));
            actual = game.getGameBoard()[3][2].getLegalMoves(false);
            expected.add(new Move(5, 4, game.getGameBoard()[4][3]));
            assertEquals(expected, actual);
        }

        @Test
        public void testTwoPossibleSkips() {
            game.getGameBoard()[2][1].move(new Move(4, 5, null));
            game.getGameBoard()[2][3].move(new Move(4, 3, null));
            actual = game.getGameBoard()[5][2].getLegalMoves(false);
            expected.add(new Move(3, 4, game.getGameBoard()[4][3]));
            assertEquals(expected, actual);
            expected.clear();
            actual = game.getGameBoard()[5][4].getLegalMoves(false);
            expected.add(new Move(3, 2, game.getGameBoard()[4][3]));
            expected.add(new Move(3, 6, game.getGameBoard()[4][5]));
            assertEquals(expected, actual);
            expected.clear();
            actual = game.getGameBoard()[5][6].getLegalMoves(false);
            expected.add(new Move(3, 4, game.getGameBoard()[4][5]));
            assertEquals(expected, actual);
            expected.clear();
            actual = game.getGameBoard()[5][0].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testThreeSkipsInRow() {
            game.getGameBoard()[2][3].move(new Move(3, 2, null));
            game.getGameBoard()[2][5].move(new Move(3, 4, null));
            game.getGameBoard()[2][7].move(new Move(3, 6, null));
            //check if other pieces can move freely
            expected.clear();
            actual = game.getGameBoard()[5][2].getLegalMoves(false);
            expected.add(new Move(4, 3, null));
            expected.add(new Move(4, 1, null));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[5][0].move(new Move(4, 1, null));
            actual = game.getGameBoard()[5][4].getLegalMoves(false);
            assertEquals(expected, actual);
            actual = game.getGameBoard()[2][1].getLegalMoves(false);
            assertEquals(expected, actual);
            actual = game.getGameBoard()[4][1].getLegalMoves(false);
            expected.add(new Move(2, 3, game.getGameBoard()[3][2]));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[4][1].move(new Move(2, 3, game.getGameBoard()[3][2])); //skip #1
            //pieces must not be able to move during move
            actual = game.getGameBoard()[1][4].getLegalMoves(false);
            assertEquals(expected, actual);
            actual = game.getGameBoard()[5][6].getLegalMoves(false);
            assertEquals(expected, actual);
            expected.add(new Move(4, 5, game.getGameBoard()[3][4]));
            actual = game.getGameBoard()[2][3].getLegalMoves(false);
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[2][3].move(new Move(4, 5, game.getGameBoard()[3][4])); //skip #2
            expected.add(new Move(2, 7, game.getGameBoard()[3][6]));
            actual = game.getGameBoard()[4][5].getLegalMoves(false);
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[4][5].move(new Move(2, 7, game.getGameBoard()[3][6])); //skip #3
            actual = game.getGameBoard()[2][7].getLegalMoves(false);
            assertEquals(expected, actual);
            //check if other pieces can move freely
            actual = game.getGameBoard()[2][1].getLegalMoves(false);
            expected.add(new Move(3, 0, null));
            expected.add(new Move(3, 2, null));
            assertEquals(expected, actual);
            expected.clear();
            actual = game.getGameBoard()[5][2].getLegalMoves(false);
            expected.add(new Move(4, 3, null));
            expected.add(new Move(4, 1, null));
            assertEquals(expected, actual);
        }

        @Test
        public void testAllyCantSkipDuringAnotherSkip() {
            game.getGameBoard()[2][1].move(new Move(3, 0, null));
            game.getGameBoard()[2][3].move(new Move(3, 2, null));
            game.getGameBoard()[2][5].move(new Move(3, 4, null));
            game.getGameBoard()[2][7].move(new Move(3, 6, null));
            game.getGameBoard()[5][2].move(new Move(4, 1, null));
            game.getGameBoard()[5][6].move(new Move(4, 7, null));
            game.getGameBoard()[4][1].move(new Move(2, 3, game.getGameBoard()[3][2]));
            //no piece must be able to move (or skip) during another piece's skip
            actual = game.getGameBoard()[4][7].getLegalMoves(false);
            assertEquals(expected, actual);
            actual = game.getGameBoard()[3][0].getLegalMoves(false);
            assertEquals(expected, actual);
            actual = game.getGameBoard()[5][0].getLegalMoves(false);
            assertEquals(expected, actual);
            actual = game.getGameBoard()[5][4].getLegalMoves(false);
            assertEquals(expected, actual);
            actual = game.getGameBoard()[3][4].getLegalMoves(false);
            assertEquals(expected, actual);
            actual = game.getGameBoard()[3][6].getLegalMoves(false);
            assertEquals(expected, actual);
            //only one piece allowed moving
            actual = game.getGameBoard()[2][3].getLegalMoves(false);
            expected.add(new Move(4, 5, game.getGameBoard()[3][4]));
            assertEquals(expected, actual);
        }
    }

    public static class testGetLegalKingMoves {
        Game game;
        Set<Move> actual = new HashSet<>();
        Set<Move> expected = new HashSet<>();

        @BeforeEach
        public void createGame() {
            game = new Game();
            actual.clear();
            expected.clear();
        }

        @Test
        public void testDefaultSkipMove() {
            leaveOneRowEachSide(game);
            game.getGameBoard()[0][1].move(new Move(3, 2, null));
            game.getGameBoard()[7][6].setKing();
            expected.add(new Move(2, 1, game.getGameBoard()[3][2]));
            actual = game.getGameBoard()[7][6].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testTwoPossibleSkips() {
            leaveOneRowEachSide(game);
            game.getGameBoard()[0][1].move(new Move(3, 2, null));
            game.getGameBoard()[0][7].move(new Move(3, 6, null));
            game.getGameBoard()[7][6].setKing();
            game.getGameBoard()[7][6].move(new Move(5, 4, null));
            expected.add(new Move(2, 1, game.getGameBoard()[3][2]));
            expected.add(new Move(2, 7, game.getGameBoard()[3][6]));
            actual = game.getGameBoard()[5][4].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testThreeSkipsInRow() {
            leaveOneRowEachSide(game);
            game.getGameBoard()[0][1].move(new Move(4, 3, null));
            game.getGameBoard()[0][7].move(new Move(3, 6, null));
            game.getGameBoard()[0][3].move(new Move(2, 3, null));
            game.getGameBoard()[7][6].setKing();
            game.getGameBoard()[7][6].move(new Move(5, 4, null));
            expected.add(new Move(3, 2, game.getGameBoard()[4][3]));
            expected.add(new Move(2, 7, game.getGameBoard()[3][6]));
            actual = game.getGameBoard()[5][4].getLegalMoves(false);
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[5][4].move(new Move(3, 2, game.getGameBoard()[4][3]));
            actual = game.getGameBoard()[3][2].getLegalMoves(false);
            expected.add(new Move(1, 4, game.getGameBoard()[2][3]));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[3][2].move(new Move(1, 4, game.getGameBoard()[2][3]));
            actual = game.getGameBoard()[1][4].getLegalMoves(false);
            expected.add(new Move(4, 7, game.getGameBoard()[3][6]));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[1][4].move(new Move(4, 7, game.getGameBoard()[3][6]));
            expected.add(new Move(5, 6, null));
            expected.add(new Move(6, 5, null));
            expected.add(new Move(1, 4, null));
            expected.add(new Move(3, 6, null));
            expected.add(new Move(2, 5, null));
            expected.add(new Move(0, 3, null));
            actual = game.getGameBoard()[4][7].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testUpgradeToKing() {
            leaveOneRowEachSide(game);
            game.getGameBoard()[0][1].move(new Move(1, 2, null));
            game.getGameBoard()[0][5].move(new Move(1, 0, null));
            game.getGameBoard()[0][3].move(new Move(3, 6, null));
            game.getGameBoard()[7][6].setKing();
            game.getGameBoard()[7][6].move(new Move(3, 0, null));
            actual = game.getGameBoard()[3][0].getLegalMoves(false);
            expected.add(new Move(0, 3, game.getGameBoard()[1][2]));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[3][0].move(new Move(0, 3, game.getGameBoard()[1][2]));
            actual = game.getGameBoard()[0][3].getLegalMoves(false);
            expected.add(new Move(4, 7, game.getGameBoard()[3][6]));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[0][3].move(new Move(4, 7, game.getGameBoard()[3][6]));
            expected.add(new Move(5, 6, null));
            expected.add(new Move(6, 5, null));
            expected.add(new Move(1, 4, null));
            expected.add(new Move(3, 6, null));
            expected.add(new Move(2, 5, null));
            expected.add(new Move(0, 3, null));
            actual = game.getGameBoard()[4][7].getLegalMoves(false);
            assertEquals(expected, actual);
            //check if other pieces can freely move
            expected.clear();
            actual = game.getGameBoard()[7][4].getLegalMoves(false);
            expected.add(new Move(6, 3, null));
            expected.add(new Move(6, 5, null));
            assertEquals(expected, actual);
        }
    }

    public static class testGameOver {
        Game game;

        @BeforeEach
        public void createGame() {
            game = new Game();
        }

        @Test
        public void testNoPiecesEnd() {
            leaveOneRowEachSide(game);
            game.getGameBoard()[0][1].remove();
            game.getGameBoard()[0][3].remove();
            game.getGameBoard()[0][5].remove();
            game.getGameBoard()[0][7].move(new Move(6, 1, null));
            game.getGameBoard()[7][0].move(new Move(5, 2, game.getGameBoard()[6][1]));
            assertTrue(game.isGameOver());
            assertEquals(game.getWinner(), Game.Side.WHITE);
        }

        @Test
        public void testNoMovesEnd() {
            leaveOneRowEachSide(game);
            game.getGameBoard()[0][1].remove();
            game.getGameBoard()[0][3].remove();
            game.getGameBoard()[0][5].remove();
            game.getGameBoard()[7][4].move(new Move(1, 6, null));
            game.setTurn(Game.Side.WHITE);
            game.getGameBoard()[7][2].move(new Move(2, 5, null));
            assertTrue(game.isGameOver());
            assertEquals(game.getWinner(), Game.Side.WHITE);
        }
    }

    public static class testAITraverse {
        Game game;
        MinimaxComputerPlayer aiWhite;
        MinimaxComputerPlayer aiBlack;
        Set<List<Move>> expected = new HashSet<>();
        Set<List<Move>> actual = new HashSet<>();
        LinkedList<Move> currentQueue = new LinkedList<>();

        @BeforeEach
        public void createGame() {
            game = new Game();
            aiWhite = new MinimaxComputerPlayer(game, Game.Side.WHITE, false);
            aiBlack = new MinimaxComputerPlayer(game, Game.Side.BLACK, false);
            actual.clear();
            expected.clear();
        }

        public void assertAndClear() {
            assertEquals(expected.size(), actual.size());
            for (List<Move> exp : expected) {
                boolean eq = false;
                for (List<Move> act : actual) {
                    if (exp.equals(act)) {
                        eq = true;
                        break;
                    }
                }
                if (!eq) throw new AssertionFailedError("NOT EQUAL");
            }
            currentQueue.clear();
            expected.clear();
        }

        @Test
        public void testTraverseOneSkip() {
            leaveOneRowEachSide(game);
            //white side
            game.getGameBoard()[0][1].move(new Move(6, 1, null));
            actual = aiWhite.traverse(game, game.getGameBoard()[7][0], new Move(5, 2, game.getGameBoard()[6][1]));
            currentQueue.add(new Move(5, 2, game.getGameBoard()[6][1]));
            expected.add(currentQueue);
            assertAndClear();
            currentQueue.add(new Move(5, 0, game.getGameBoard()[6][1]));
            expected.add(currentQueue);
            actual = aiWhite.traverse(game, game.getGameBoard()[7][2], new Move(5, 0, game.getGameBoard()[6][1]));
            assertAndClear();
            //black side
            game.getGameBoard()[7][6].move(new Move(1, 6, null));
            actual = aiBlack.traverse(game, game.getGameBoard()[0][7], new Move(2, 5, game.getGameBoard()[1][6]));
            currentQueue.add(new Move(2, 5, game.getGameBoard()[1][6]));
            expected.add(currentQueue);
            assertAndClear();
            actual = aiBlack.traverse(game, game.getGameBoard()[0][5], new Move(2, 7, game.getGameBoard()[1][6]));
            currentQueue.add(new Move(2, 7, game.getGameBoard()[1][6]));
            expected.add(currentQueue);
            assertAndClear();
        }

        @Test
        public void testTraverseTwoSkips() {
            leaveOneRowEachSide(game);
            //white side
            game.getGameBoard()[0][1].move(new Move(6, 1, null));
            game.getGameBoard()[0][3].move(new Move(4, 3, null));
            actual = aiWhite.traverse(game, game.getGameBoard()[7][0], new Move(5, 2, game.getGameBoard()[6][1]));
            currentQueue.add(new Move(5, 2, game.getGameBoard()[6][1]));
            currentQueue.add(new Move(3, 4, game.getGameBoard()[4][3]));
            expected.add(currentQueue);
            assertAndClear();
            //black side
            game.getGameBoard()[7][6].move(new Move(1, 6, null));
            game.getGameBoard()[7][4].move(new Move(3, 6, null));
            actual = aiBlack.traverse(game, game.getGameBoard()[0][7], new Move(2, 5, game.getGameBoard()[1][6]));
            currentQueue.add(new Move(2, 5, game.getGameBoard()[1][6]));
            currentQueue.add(new Move(4, 7, game.getGameBoard()[3][6]));
            expected.add(currentQueue);
            assertAndClear();
            actual = aiBlack.traverse(game, game.getGameBoard()[0][5], new Move(2, 7, game.getGameBoard()[1][6]));
            currentQueue.add(new Move(2, 7, game.getGameBoard()[1][6]));
            currentQueue.add(new Move(4, 5, game.getGameBoard()[3][6]));
            expected.add(currentQueue);
            assertAndClear();
        }

        @Test
        public void testTraverseTwoQueuesOfSkips() {
            leaveSixPiecesEachSide(game);
            //white side
            game.getGameBoard()[0][1].move(new Move(6, 1, null));
            game.getGameBoard()[0][3].move(new Move(4, 1, null));
            game.getGameBoard()[0][5].move(new Move(4, 3, null));
            game.getGameBoard()[0][7].move(new Move(2, 5, null));
            game.getGameBoard()[1][0].move(new Move(2, 1, null));
            actual = aiWhite.traverse(game, game.getGameBoard()[7][0], new Move(5, 2, game.getGameBoard()[6][1]));
            currentQueue.add(new Move(5, 2, game.getGameBoard()[6][1]));
            currentQueue.add(new Move(3, 4, game.getGameBoard()[4][3]));
            currentQueue.add(new Move(1, 6, game.getGameBoard()[2][5]));
            expected.add(new LinkedList<>(currentQueue));
            currentQueue.clear();
            currentQueue.add(new Move(5, 2, game.getGameBoard()[6][1]));
            currentQueue.add(new Move(3, 0, game.getGameBoard()[4][1]));
            currentQueue.add(new Move(1, 2, game.getGameBoard()[2][1]));
            expected.add(new LinkedList<>(currentQueue));
            assertAndClear();
        }

        @Test
        public void testTraverseFourPossibleBranchesOfSkips() {
            leaveSixPiecesEachSide(game);
            //white side
            game.getGameBoard()[0][1].move(new Move(6, 1, null));
            game.getGameBoard()[0][3].move(new Move(4, 1, null));
            game.getGameBoard()[0][5].move(new Move(4, 3, null));
            game.getGameBoard()[0][7].move(new Move(2, 5, null));
            game.getGameBoard()[1][0].move(new Move(4, 5, null));
            game.getGameBoard()[1][4].move(new Move(2, 3, null));
            actual = aiWhite.traverse(game, game.getGameBoard()[7][0], new Move(5, 2, game.getGameBoard()[6][1]));
            currentQueue.add(new Move(5, 2, game.getGameBoard()[6][1]));
            currentQueue.add(new Move(3, 4, game.getGameBoard()[4][3]));
            currentQueue.add(new Move(1, 6, game.getGameBoard()[2][5]));
            expected.add(new LinkedList<>(currentQueue));
            currentQueue.clear();
            currentQueue.add(new Move(5, 2, game.getGameBoard()[6][1]));
            currentQueue.add(new Move(3, 0, game.getGameBoard()[4][1]));
            expected.add(new LinkedList<>(currentQueue));
            currentQueue.clear();
            currentQueue.add(new Move(5, 2, game.getGameBoard()[6][1]));
            currentQueue.add(new Move(3, 4, game.getGameBoard()[4][3]));
            currentQueue.add(new Move(5, 6, game.getGameBoard()[4][5]));
            expected.add(new LinkedList<>(currentQueue));
            currentQueue.clear();
            currentQueue.add(new Move(5, 2, game.getGameBoard()[6][1]));
            currentQueue.add(new Move(3, 4, game.getGameBoard()[4][3]));
            currentQueue.add(new Move(1, 2, game.getGameBoard()[2][3]));
            expected.add(new LinkedList<>(currentQueue));
            assertAndClear();
        }

    }

    public static class testAICalcScore {
        Game game = new Game();
        MinimaxComputerPlayer aiWhite = new MinimaxComputerPlayer(game, Game.Side.WHITE, false);
        MinimaxComputerPlayer aiBlack = new MinimaxComputerPlayer(game, Game.Side.BLACK, false);

        @Test
        public void testCalcScore() {
            leaveSixPiecesEachSide(game);
            double actual = aiWhite.calcScore(game, Game.Side.WHITE);
            double expected = 0;
            assertEquals(expected, actual);
            actual = aiBlack.calcScore(game, Game.Side.BLACK);
            assertEquals(expected, actual);
            game = new Game();
            leaveOneRowEachSide(game);
            actual = aiWhite.calcScore(game, Game.Side.WHITE);
            assertEquals(expected, actual);
            actual = aiBlack.calcScore(game, Game.Side.BLACK);
            assertEquals(expected, actual);
        }
    }

}