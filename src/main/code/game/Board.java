package code.game;

import code.ai.ComputerPlayer;
import code.ai.MinimaxComputerPlayer;
import code.ai.RandomComputerPlayer;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.*;

import static javafx.scene.paint.Color.rgb;

public class Board {
    //constants
    public static final double TILE_SIZE = 75.0;
    public static final double BOARD_SIZE = 600.0;
    public static final int CHECKER_CENTER = 38;
    public static final int CHECKER_RADIUS = 33;
    public static final int LEGAL_MOVE_RADIUS = 30;
    //interface
    private final Pane root = new Pane();
    private final Game game;
    private final DisplayChecker[][] gameField;
    private final Set<Circle> highlightedSquares = new HashSet<>();
    private DisplayChecker selectedChecker = null;
    // players settings
//    private final Player firstPlayer = Player.AIEASY; // to comment: for ai vs ai
    private final Player secondPlayer;
    private Game.Side secondPlayerSide;
    //    private ComputerEnemy firstComputerPlayer; // to comment: for ai vs ai

    @SuppressWarnings({"unused", "UnusedAssignment"})
    public Board(Game game, Player secondPlayer) {
        this.game = game;
        this.secondPlayer = secondPlayer;
        if (secondPlayer != Player.HUMAN) {
            Random rnd = new Random();
            int num = rnd.nextBoolean() ? 1 : 0;
            secondPlayerSide = Game.Side.values()[num];
        }
        ComputerEnemy computerPlayer;
        switch (secondPlayer) {
            case AIHARD -> computerPlayer = new ComputerEnemy(new MinimaxComputerPlayer(game, secondPlayerSide, true), secondPlayerSide); // hard vs medium 85% WR
            case AIMEDIUM -> computerPlayer = new ComputerEnemy(new MinimaxComputerPlayer(game, secondPlayerSide, false), secondPlayerSide); // medium vs easy 100% WR
            case AIEASY -> computerPlayer = new ComputerEnemy(new RandomComputerPlayer(game, secondPlayerSide), secondPlayerSide);
        }
        // ai vs ai
//        firstComputerPlayer = new ComputerEnemy(new RandomComputerPlayer(game, secondPlayerSide.getOpposite()), secondPlayerSide.getOpposite()); // to comment: for ai vs ai
//        System.out.println("Random is " + secondPlayerSide.getOpposite());
        // ai vs ai
        gameField = new DisplayChecker[8][8];
        root.setPrefSize(BOARD_SIZE, BOARD_SIZE);
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++) {
                Rectangle rect = new Rectangle(row * TILE_SIZE, col * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                rect.setFill(Color.LIGHTYELLOW);
//                if (firstPlayer == Player.HUMAN) { // to comment: for ai vs ai
                rect.setOnMouseClicked(event -> {
                    if (selectedChecker != null) resetSelected();
                });
//                } // to comment: for ai vs ai
                if ((row % 2 == 0 && col % 2 != 0) || (row % 2 != 0 && col % 2 == 0)) rect.setFill(Color.BROWN);
                root.getChildren().add(rect);
            }
        for (Checker piece : game.getBlackLeft())
            gameField[piece.getRow()][piece.getCol()] = new DisplayChecker(piece);
        for (Checker piece : game.getWhiteLeft())
            gameField[piece.getRow()][piece.getCol()] = new DisplayChecker(piece);
    }

    public Pane getRoot() {
        return root;
    }

    public void resetSelected() {
        for (Circle circle : highlightedSquares) {
            circle.toBack();
            root.getChildren().remove(circle);
        }
        highlightedSquares.clear();
        selectedChecker = null;
    }

    public enum Player {
        AIHARD,
        AIMEDIUM,
        AIEASY,
        HUMAN
    }

    public class DisplayChecker {
        private final Checker parentChecker;
        private final Circle circle;
        private int row;
        private int col;

        public DisplayChecker(Checker parentChecker) {
            this.parentChecker = parentChecker;
            updateCords();
            Color color;
            if (parentChecker.getColor() == Game.Side.BLACK) color = Color.BLACK;
            else color = Color.WHITE;
            circle = new Circle(CHECKER_CENTER, CHECKER_CENTER, CHECKER_RADIUS);
            circle.setFill(color);
            circle.setStrokeWidth(2);
            circle.setStroke(Color.BLACK);
//            if (firstPlayer == Player.HUMAN) { // to comment: for ai vs ai
            if (secondPlayer != Player.HUMAN) {
                if (parentChecker.getColor() == secondPlayerSide.getOpposite()) {
                    circle.setOnMouseClicked(event -> {
                        if (selectedChecker != null) resetSelected();
                        if (game.getTurn() == parentChecker.getColor())
                            select();
                    });
                }
            } else {
                circle.setOnMouseClicked(event -> {
                    if (selectedChecker != null) resetSelected();
                    if (game.getTurn() == parentChecker.getColor())
                        select();
                });
            }
//            } // to comment: for ai vs ai
            root.getChildren().add(this.circle);
            drawChecker();
        }

        public void updateCords() {
            row = parentChecker.getRow();
            col = parentChecker.getCol();
            gameField[row][col] = this;
        }

        public void drawChecker() {
            circle.setTranslateY(row * TILE_SIZE);
            circle.setTranslateX(col * TILE_SIZE);
            if (parentChecker.isKing()) {
                circle.setStroke(Color.YELLOW);
                circle.setStrokeWidth(5);
            }
        }

        public void eraseChecker() {
            gameField[row][col] = null;
            root.getChildren().remove(circle);
        }

        public void select() {
            selectedChecker = this;
            Set<Move> legalMoves = parentChecker.getLegalMoves(false);
            drawLegalMoves(legalMoves);
        }

        public void drawLegalMoves(Set<Move> moves) {
            for (Move move : moves) {
                Circle circleToMove = new Circle(move.getCol() * TILE_SIZE + CHECKER_CENTER, move.getRow() * TILE_SIZE + CHECKER_CENTER, LEGAL_MOVE_RADIUS);
                circleToMove.setFill(rgb(0, 255, 0, 0.5));
//                if (firstPlayer == Player.HUMAN) // to comment: for ai vs ai
                circleToMove.setOnMouseClicked(event -> drawMove(move));
                highlightedSquares.add(circleToMove);
                root.getChildren().add(circleToMove);
            }
        }

        public void drawMove(Move move) {
            parentChecker.move(move);
            //animation
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(0.25));
            transition.setToX(parentChecker.getCol() * TILE_SIZE);
            transition.setToY(parentChecker.getRow() * TILE_SIZE);
            transition.setNode(circle);
            transition.play();
            // TODO: make program wait for animation to end
            //animation
            if (move.getCheckerToRemove() != null)
                gameField[move.getCheckerToRemove().getRow()][move.getCheckerToRemove().getCol()].eraseChecker();
            selectedChecker.updateCords();
            selectedChecker.drawChecker();
            resetSelected();
            if (game.isGameOver()) {
                drawEndGame();
            }
            if (move.getCheckerToRemove() != null) {
                if (!parentChecker.getLegalMoves(true).isEmpty()) select();
            }
        }

        public void drawEndGame() {
            Stage stage = new Stage();
            Pane winnerScreen = new Pane();
            winnerScreen.setPrefSize(300, 30);
            stage.setTitle("Game over");
            javafx.scene.control.Label label = new javafx.scene.control.Label();
            label.setTranslateX(100);
            if (game.getWinner() == Game.Side.BLACK) {
                label.setText("WINNER: BLACK");
            } else {
                label.setText("WINNER: WHITE");
            }
            //for stats
//            Main.games++;
//            if (secondPlayerSide.getOpposite() == game.getWinner()) Main.wins++;
//            System.out.println("Winner: " + game.getWinner());
//            System.out.println(Main.games);
//            System.out.println(Main.wins);
//            Main.mediumButton.fire();
            //for stats
            winnerScreen.getChildren().add(label);
            stage.setScene(new Scene(winnerScreen));
            stage.show();
        }
    }

    public class ComputerEnemy {
        private Pair<Checker, List<Move>> bestMove = new Pair<>(null, new LinkedList<>());

        public ComputerEnemy(ComputerPlayer ai, Game.Side side) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        if (game.getTurn() == side) {
                            if (!game.isGameOver()) {
                                if (bestMove.getValue().isEmpty()) bestMove = ai.findBestMove();
                                makeBestMove();
                            } else {
                                timer.cancel();
                            }
                        }
                    });
                }
            }, 0, 1000);
        }

        public void makeBestMove() {
            DisplayChecker checker = gameField[bestMove.getKey().getRow()][bestMove.getKey().getCol()];
            Checker removingChecker = null;
            Move move = bestMove.getValue().get(0);
            if (move.getCheckerToRemove() != null)
                removingChecker = game.getGameBoard()[move.getCheckerToRemove().getRow()][move.getCheckerToRemove().getCol()];
            checker.select();
            checker.drawMove(new Move(move.getRow(), move.getCol(), removingChecker));
            resetSelected();
            bestMove.getValue().remove(0);
        }

    }

}