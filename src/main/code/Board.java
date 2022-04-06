package code;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

import static javafx.scene.paint.Color.rgb;

public class Board {
    public static final double TILE_SIZE = 75.0;
    public static final double BOARD_SIZE = 600.0;
    public static final int CHECKER_CENTER = 38;
    public static final int CHECKER_RADIUS = 33;
    public static final int LEGAL_MOVE_RADIUS = 30;

    private final Pane root = new Pane();
    private final Game game;
    private final DisplayChecker[][] gameField;
    private final Set<Circle> highlightedSquares = new HashSet<>();
    private DisplayChecker selectedChecker = null;

    public Board(Game game) {
        this.game = game;
        gameField = new DisplayChecker[8][8];
        root.setPrefSize(BOARD_SIZE, BOARD_SIZE);
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++) {
                Rectangle rect = new Rectangle(row * TILE_SIZE, col * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                rect.setFill(Color.LIGHTYELLOW);
                rect.setOnMouseClicked(event -> {
                    if (selectedChecker != null) resetSelected();
                });
                if ((row % 2 == 0 && col % 2 != 0) || (row % 2 != 0 && col % 2 == 0)) rect.setFill(Color.BROWN);
                root.getChildren().add(rect);
            }
        for (Game.Checker piece : game.getBlackLeft())
            gameField[piece.getRow()][piece.getCol()] = new DisplayChecker(piece);
        for (Game.Checker piece : game.getWhiteLeft())
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

    public class DisplayChecker {
        private final Game.Checker parentChecker;
        private final Circle circle;
        private int row;
        private int col;

        public DisplayChecker(Game.Checker parentChecker) {
            this.parentChecker = parentChecker;
            updateCords();
            Color color;
            if (parentChecker.getColor() == Game.Side.BLACK) color = Color.BLACK;
            else color = Color.WHITE;
            circle = new Circle(CHECKER_CENTER, CHECKER_CENTER, CHECKER_RADIUS);
            circle.setFill(color);
            circle.setOnMouseClicked(event -> {
                if (selectedChecker != null) resetSelected();
                if (game.getTurn() == parentChecker.getColor())
                    select();
            });
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
            if(parentChecker.isKing()) {
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
            Set<Game.Move> legalMoves = parentChecker.getLegalMoves(false);
            drawLegalMoves(legalMoves);
        }

        public void drawLegalMoves(Set<Game.Move> moves) {
            for (Game.Move move : moves) {
                Circle circleToMove = new Circle(move.getCol() * TILE_SIZE + CHECKER_CENTER, move.getRow() * TILE_SIZE + CHECKER_CENTER, LEGAL_MOVE_RADIUS);
                circleToMove.setFill(rgb(0, 255, 0, 0.5));
                circleToMove.setOnMouseClicked(event -> drawMove(move));
                highlightedSquares.add(circleToMove);
                root.getChildren().add(circleToMove);
            }
        }

        public void drawMove(Game.Move move) {
            parentChecker.move(move);
            if (move.getCheckerToRemove() != null)
                gameField[move.getCheckerToRemove().getRow()][move.getCheckerToRemove().getCol()].eraseChecker();
            selectedChecker.updateCords();
            selectedChecker.drawChecker();
            resetSelected();
            if(game.isGameOver()) {
                drawEndGame();
            }
            if (move.getCheckerToRemove() != null)
                if (!parentChecker.getLegalMoves(true).isEmpty()) select();
        }

        public void drawEndGame() {
            Stage stage = new Stage();
            Pane winnerScreen = new Pane();
            winnerScreen.setPrefSize(300, 20);
            stage.setTitle("Game over");
            javafx.scene.control.Label label = new javafx.scene.control.Label();
            label.setTranslateX(100);
            if(game.getWinner() == Game.Side.BLACK) {
                label.setText("WINNER: BLACK");
            } else {
                label.setText("WINNER: WHITE");
            }
            winnerScreen.getChildren().add(label);
            stage.setScene(new Scene(winnerScreen));
            stage.show();
        }
    }

}