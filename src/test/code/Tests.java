package code;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    public static class testGetLegalMoves {
        Game game;
        Set<Game.Move> actual = new HashSet<>();
        Set<Game.Move> expected = new HashSet<>();

        @BeforeEach
        public void createGame() {
            game = new Game();
            actual.clear();
            expected.clear();
        }

        @Test
        public void testDefaultFirstBlackMove() {
            actual = game.getGameBoard()[2][1].getLegalMoves(false);
            expected.add(new Game.Move(3, 0, null));
            expected.add(new Game.Move(3, 2, null));
            assertEquals(expected, actual);
        }

        @Test
        public void testDefaultFirstWhiteMove() {
            actual = game.getGameBoard()[5][2].getLegalMoves(false);
            expected.add(new Game.Move(4, 1, null));
            expected.add(new Game.Move(4, 3, null));
            assertEquals(expected, actual);
        }

        @Test
        public void testSingleLeftSkipBlackMove() {
            game.getGameBoard()[5][4].move(new Game.Move(3, 2, null));
            actual = game.getGameBoard()[2][3].getLegalMoves(false);
            expected.add(new Game.Move(4, 1, game.getGameBoard()[3][2]));
            assertEquals(expected, actual);
        }

        @Test
        public void testSingleRightSkipBlackMove() {
            game.getGameBoard()[5][4].move(new Game.Move(3, 2, null));
            actual = game.getGameBoard()[2][1].getLegalMoves(false);
            expected.add(new Game.Move(4, 3, game.getGameBoard()[3][2]));
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
            game.getGameBoard()[5][4].move(new Game.Move(3, 2, null));
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
            game.getGameBoard()[5][4].move(new Game.Move(3, 2, null));
            game.getGameBoard()[2][3].move(new Game.Move(4, 3, null));
            actual = game.getGameBoard()[3][2].getLegalMoves(false);
            expected.add(new Game.Move(5, 4, game.getGameBoard()[4][3]));
            assertEquals(expected, actual);
        }

        @Test
        public void testTwoPossibleSkips() {
            game.getGameBoard()[2][1].move(new Game.Move(4, 5, null));
            game.getGameBoard()[2][3].move(new Game.Move(4, 3, null));
            actual = game.getGameBoard()[5][2].getLegalMoves(false);
            expected.add(new Game.Move(3, 4, game.getGameBoard()[4][3]));
            assertEquals(expected, actual);
            expected.clear();
            actual = game.getGameBoard()[5][4].getLegalMoves(false);
            expected.add(new Game.Move(3, 2, game.getGameBoard()[4][3]));
            expected.add(new Game.Move(3, 6, game.getGameBoard()[4][5]));
            assertEquals(expected, actual);
            expected.clear();
            actual = game.getGameBoard()[5][6].getLegalMoves(false);
            expected.add(new Game.Move(3, 4, game.getGameBoard()[4][5]));
            assertEquals(expected, actual);
            expected.clear();
            actual = game.getGameBoard()[5][0].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testThreeSkipsInRow() {
            game.getGameBoard()[2][3].move(new Game.Move(3, 2, null));
            game.getGameBoard()[2][5].move(new Game.Move(3, 4, null));
            game.getGameBoard()[2][7].move(new Game.Move(3, 6, null));
            //check if other pieces can move freely
            expected.clear();
            actual = game.getGameBoard()[5][2].getLegalMoves(false);
            expected.add(new Game.Move(4, 3, null));
            expected.add(new Game.Move(4, 1, null));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[5][0].move(new Game.Move(4, 1, null));
            actual = game.getGameBoard()[5][4].getLegalMoves(false);
            assertEquals(expected, actual);
            actual = game.getGameBoard()[2][1].getLegalMoves(false);
            assertEquals(expected, actual);
            actual = game.getGameBoard()[4][1].getLegalMoves(false);
            expected.add(new Game.Move(2, 3, game.getGameBoard()[3][2]));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[4][1].move(new Game.Move(2, 3, game.getGameBoard()[3][2])); //skip #1
            //pieces must not be able to move during move
            actual = game.getGameBoard()[1][4].getLegalMoves(false);
            assertEquals(expected, actual);
            actual = game.getGameBoard()[5][6].getLegalMoves(false);
            assertEquals(expected, actual);
            expected.add(new Game.Move(4, 5, game.getGameBoard()[3][4]));
            actual = game.getGameBoard()[2][3].getLegalMoves(false);
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[2][3].move(new Game.Move(4, 5, game.getGameBoard()[3][4])); //skip #2
            expected.add(new Game.Move(2, 7, game.getGameBoard()[3][6]));
            actual = game.getGameBoard()[4][5].getLegalMoves(false);
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[4][5].move(new Game.Move(2, 7, game.getGameBoard()[3][6])); //skip #3
            actual = game.getGameBoard()[2][7].getLegalMoves(false);
            assertEquals(expected, actual);
            //check if other pieces can move freely
            actual = game.getGameBoard()[2][1].getLegalMoves(false);
            expected.add(new Game.Move(3, 0, null));
            expected.add(new Game.Move(3, 2, null));
            assertEquals(expected, actual);
            expected.clear();
            actual = game.getGameBoard()[5][2].getLegalMoves(false);
            expected.add(new Game.Move(4, 3, null));
            expected.add(new Game.Move(4, 1, null));
            assertEquals(expected, actual);
        }

        @Test
        public void testAllyCantSkipDuringAnotherSkip() {
            game.getGameBoard()[2][1].move(new Game.Move(3, 0, null));
            game.getGameBoard()[2][3].move(new Game.Move(3, 2, null));
            game.getGameBoard()[2][5].move(new Game.Move(3, 4, null));
            game.getGameBoard()[2][7].move(new Game.Move(3, 6, null));
            game.getGameBoard()[5][2].move(new Game.Move(4, 1, null));
            game.getGameBoard()[5][6].move(new Game.Move(4, 7, null));
            game.getGameBoard()[4][1].move(new Game.Move(2, 3, game.getGameBoard()[3][2]));
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
            //only one piece allowed to move
            actual = game.getGameBoard()[2][3].getLegalMoves(false);
            expected.add(new Game.Move(4, 5, game.getGameBoard()[3][4]));
            assertEquals(expected, actual);
        }
    }

    public static class testGetLegalKingMoves {
        Game game;
        Set<Game.Move> actual = new HashSet<>();
        Set<Game.Move> expected = new HashSet<>();

        @BeforeEach
        public void createGame() {
            game = new Game();
            actual.clear();
            expected.clear();
        }

        private void printBoard() {
            System.out.println(Arrays.deepToString(game.getGameBoard()));
        }

        private void leaveOneRowEachSide() {
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

        @Test
        public void testDefaultSkipMove() {
            leaveOneRowEachSide();
            game.getGameBoard()[0][1].move(new Game.Move(3, 2, null));
            game.getGameBoard()[7][6].setKing();
            expected.add(new Game.Move(2, 1, game.getGameBoard()[3][2]));
            actual = game.getGameBoard()[7][6].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testTwoPossibleSkips() {
            leaveOneRowEachSide();
            game.getGameBoard()[0][1].move(new Game.Move(3, 2, null));
            game.getGameBoard()[0][7].move(new Game.Move(3, 6, null));
            game.getGameBoard()[7][6].setKing();
            game.getGameBoard()[7][6].move(new Game.Move(5, 4, null));
            expected.add(new Game.Move(2, 1, game.getGameBoard()[3][2]));
            expected.add(new Game.Move(2, 7, game.getGameBoard()[3][6]));
            actual = game.getGameBoard()[5][4].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testThreeSkipsInRow() {
            leaveOneRowEachSide();
            game.getGameBoard()[0][1].move(new Game.Move(4, 3, null));
            game.getGameBoard()[0][7].move(new Game.Move(3, 6, null));
            game.getGameBoard()[0][3].move(new Game.Move(2, 3, null));
            game.getGameBoard()[7][6].setKing();
            game.getGameBoard()[7][6].move(new Game.Move(5, 4, null));
            expected.add(new Game.Move(3, 2, game.getGameBoard()[4][3]));
            expected.add(new Game.Move(2, 7, game.getGameBoard()[3][6]));
            actual = game.getGameBoard()[5][4].getLegalMoves(false);
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[5][4].move(new Game.Move(3, 2, game.getGameBoard()[4][3]));
            actual = game.getGameBoard()[3][2].getLegalMoves(false);
            expected.add(new Game.Move(1, 4, game.getGameBoard()[2][3]));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[3][2].move(new Game.Move(1, 4, game.getGameBoard()[2][3]));
            actual = game.getGameBoard()[1][4].getLegalMoves(false);
            expected.add(new Game.Move(4, 7, game.getGameBoard()[3][6]));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[1][4].move(new Game.Move(4, 7, game.getGameBoard()[3][6]));
            expected.add(new Game.Move(5, 6, null));
            expected.add(new Game.Move(6, 5, null));
            expected.add(new Game.Move(1, 4, null));
            expected.add(new Game.Move(3, 6, null));
            expected.add(new Game.Move(2, 5, null));
            expected.add(new Game.Move(0, 3, null));
            actual = game.getGameBoard()[4][7].getLegalMoves(false);
            assertEquals(expected, actual);
        }

        @Test
        public void testUpgradeToKing() {
            leaveOneRowEachSide();
            game.getGameBoard()[0][1].move(new Game.Move(1, 2, null));
            game.getGameBoard()[0][5].move(new Game.Move(1, 0, null));
            game.getGameBoard()[0][3].move(new Game.Move(3, 6, null));
            game.getGameBoard()[7][6].setKing();
            game.getGameBoard()[7][6].move(new Game.Move(3, 0, null));
            actual = game.getGameBoard()[3][0].getLegalMoves(false);
            expected.add(new Game.Move(0, 3, game.getGameBoard()[1][2]));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[3][0].move(new Game.Move(0, 3, game.getGameBoard()[1][2]));
            actual = game.getGameBoard()[0][3].getLegalMoves(false);
            expected.add(new Game.Move(4, 7, game.getGameBoard()[3][6]));
            assertEquals(expected, actual);
            expected.clear();
            game.getGameBoard()[0][3].move(new Game.Move(4, 7, game.getGameBoard()[3][6]));
            expected.add(new Game.Move(5, 6, null));
            expected.add(new Game.Move(6, 5, null));
            expected.add(new Game.Move(1, 4, null));
            expected.add(new Game.Move(3, 6, null));
            expected.add(new Game.Move(2, 5, null));
            expected.add(new Game.Move(0, 3, null));
            actual = game.getGameBoard()[4][7].getLegalMoves(false);
            assertEquals(expected, actual);
            //check if other pieces can freely move
            expected.clear();
            actual = game.getGameBoard()[7][4].getLegalMoves(false);
            expected.add(new Game.Move(6, 3, null));
            expected.add(new Game.Move(6, 5, null));
            assertEquals(expected, actual);
        }
    }
}
//
//        @Test
//        public void testMoveWithTwoSkips() {
//            game.getGameBoard()[2][3].move(new Game.Move(4, 3, new HashSet<>()));
//            game.getGameBoard()[1][2].move(new Game.Move(2, 3, new HashSet<>()));
//            game.getGameBoard()[0][7].move(new Game.Move(3, 0, new HashSet<>()));
//            actual = game.getGameBoard()[5][2].getLegalMoves();
//            expectedSkipped.add(game.getGameBoard()[4][3]);
//            expectedSkipped.add(game.getGameBoard()[2][3]);
//            expected.add(new Game.Move(1, 2, expectedSkipped));
//            assertEquals(expected, actual);
//        }
//
//        @Test
//        public void testMoveWithThreeSkipsBothSides() {
//            game.getGameBoard()[2][3].move(new Game.Move(4, 3, new HashSet<>()));
//            game.getGameBoard()[1][2].move(new Game.Move(2, 3, new HashSet<>()));
//            actual = game.getGameBoard()[5][2].getLegalMoves();
//            expectedSkipped.add(game.getGameBoard()[4][3]);
//            expectedSkipped.add(game.getGameBoard()[2][3]);
//            expectedSkipped.add(game.getGameBoard()[2][1]);
//            expected.add(new Game.Move(3, 0, expectedSkipped));
//            assertEquals(expected, actual);
//        }
//
//        @Test
//        public void testMoveWithSkipsInBothSides() {
//            game.getGameBoard()[6][3].remove();
//            game.getGameBoard()[2][1].move(new Game.Move(4,1,new HashSet<>()));
//            actual = game.getGameBoard()[4][1].getLegalMoves();
//            expectedSkipped.add(game.getGameBoard()[5][2]);
//            expectedSkipped.add(game.getGameBoard()[5][4]);
//            expected.add(new Game.Move(4, 5, expectedSkipped));
//            assertEquals(expected, actual);
//        }
//
//        @Test
//        public void testFourSkipsRingBlack() {
//            game.getGameBoard()[6][3].move(new Game.Move(3,2,new HashSet<>()));
//            game.getGameBoard()[5][0].move(new Game.Move(3,4,new HashSet<>()));
//            actual = game.getGameBoard()[2][3].getLegalMoves();
//            expectedSkipped.add(game.getGameBoard()[3][2]);
//            expectedSkipped.add(game.getGameBoard()[3][4]);
//            expectedSkipped.add(game.getGameBoard()[5][2]);
//            expectedSkipped.add(game.getGameBoard()[5][4]);
//            expected.add(new Game.Move(2, 3, expectedSkipped));
//            assertEquals(expected, actual);
//        }
//
//        @Test
//        public void testFourSkipsRingWhite() {
//            game.getGameBoard()[1][2].move(new Game.Move(4,1,new HashSet<>()));
//            game.getGameBoard()[1][0].move(new Game.Move(4,3,new HashSet<>()));
//            actual = game.getGameBoard()[5][2].getLegalMoves();
//            expectedSkipped.add(game.getGameBoard()[2][1]);
//            expectedSkipped.add(game.getGameBoard()[2][3]);
//            expectedSkipped.add(game.getGameBoard()[4][1]);
//            expectedSkipped.add(game.getGameBoard()[4][6]);
//            expected.add(new Game.Move(5, 2, expectedSkipped));
//            assertEquals(expected, actual);
//        }
//