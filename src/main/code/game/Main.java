package code.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {
    public static final double BUTTON_WIDTH = 150.0;
    public static final double BUTTON_HEIGHT = 30.0;
    public static final double BUTTON_TRANSLATE_X = 65.0;
    public static final double FIRST_BUTTON_TRANSLATE_Y = 35.0;
    public static final double SECOND_BUTTON_TRANSLATE_Y = FIRST_BUTTON_TRANSLATE_Y + 50;
    public static final double THIRD_BUTTON_TRANSLATE_Y = FIRST_BUTTON_TRANSLATE_Y + 100;
    public static final double FOURTH_BUTTON_TRANSLATE_Y = FIRST_BUTTON_TRANSLATE_Y + 150;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        setWindow(true, null);
    }

    private void setWindow(boolean firstTime, Board.Player player1) {
        Stage stage = new Stage();
        if (firstTime) stage.setTitle("Select Player 1");
        else stage.setTitle("Select Player 2");
        Button humanButton = new Button("Player");
        Button hardButton = new Button("Computer (Hard)");
        Button mediumButton = new Button("Computer (Medium)");
        Button easyButton = new Button("Computer (Easy)");
        if (firstTime)
            humanButton.setOnAction(event -> {
                setWindow(false, Board.Player.HUMAN);
                stage.close();
            });
        else if (player1 == Board.Player.HUMAN)
            humanButton.setOnAction(event -> {
                startGame(player1, Board.Player.HUMAN);
                stage.close();
            });
        hardButton.setOnAction(event -> {
            if (firstTime) setWindow(false, Board.Player.AIHARD);
            else startGame(player1, Board.Player.AIHARD);
            stage.close();
        });
        mediumButton.setOnAction(event -> {
            if (firstTime) setWindow(false, Board.Player.AIMEDIUM);
            else startGame(player1, Board.Player.AIMEDIUM);
            stage.close();
        });
        easyButton.setOnAction(event -> {
            if (firstTime) setWindow(false, Board.Player.AIEASY);
            else startGame(player1, Board.Player.AIEASY);
            stage.close();
        });
        Pane pane = new Pane();
        pane.setPrefSize(280, 250);
        InputStream iconStream = getClass().getResourceAsStream("/checkers-icon.png");
        Image icon = null;
        if (iconStream != null) {
            icon = new Image(iconStream);
        }
        stage.getIcons().add(icon);
        humanButton.setTranslateX(BUTTON_TRANSLATE_X);
        humanButton.setTranslateY(FIRST_BUTTON_TRANSLATE_Y);
        hardButton.setTranslateX(BUTTON_TRANSLATE_X);
        hardButton.setTranslateY(SECOND_BUTTON_TRANSLATE_Y);
        mediumButton.setTranslateX(BUTTON_TRANSLATE_X);
        mediumButton.setTranslateY(THIRD_BUTTON_TRANSLATE_Y);
        easyButton.setTranslateX(BUTTON_TRANSLATE_X);
        easyButton.setTranslateY(FOURTH_BUTTON_TRANSLATE_Y);
        humanButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        mediumButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        hardButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        easyButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        stage.setOnCloseRequest(event -> {
            try {
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pane.getChildren().addAll(humanButton, hardButton, mediumButton, easyButton);
        stage.setScene(new Scene(pane));
        stage.show();
    }

    private void startGame(Board.Player player1, Board.Player player2) {
        Stage stage = new Stage();
        Game game = new Game();
        Board board = new Board(game, player1, player2);
        stage.setTitle("Checkers");
        InputStream iconStream = getClass().getResourceAsStream("/checkers-icon.png");
        Image icon = null;
        if (iconStream != null) {
            icon = new Image(iconStream);
        }
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(event -> {
            try {
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        stage.setScene(new Scene(board.getRoot()));
        stage.show();
    }

}