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

    //for stats
//    public static Pane pane;
//    public static Pane getPane() {
//        return pane;
//    }
//    public static Button mediumButton;
//    public static int games = 0;
//    public static int wins = 0;
    //for stats

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //selecting a game mode
        Stage selectionWindow = new Stage();
        Pane pane = new Pane();
        pane.setPrefSize(280, 250);
        selectionWindow.setTitle("Select game mode");
        InputStream iconStream = getClass().getResourceAsStream("/checkers-icon.png");
        Image icon = null;
        if (iconStream != null) {
            icon = new Image(iconStream);
        }
        selectionWindow.getIcons().add(icon);
        Button humanButton = new Button("vs Player");
        Button hardButton = new Button("vs Computer (Hard)");
        Button mediumButton = new Button("vs Computer (Medium)");
        Button easyButton = new Button("vs Computer (Easy)");
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
        humanButton.setOnAction(event -> startGame(primaryStage, Board.Player.HUMAN));
        hardButton.setOnAction(event -> startGame(primaryStage, Board.Player.AIHARD));
        mediumButton.setOnAction(event -> startGame(primaryStage, Board.Player.AIMEDIUM));
        easyButton.setOnAction(event -> startGame(primaryStage, Board.Player.AIEASY));
        pane.getChildren().addAll(humanButton, hardButton, mediumButton, easyButton);
        selectionWindow.setOnCloseRequest(event -> {
            try {
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        selectionWindow.setScene(new Scene(pane));
        selectionWindow.show();
//        mediumButton.fire(); //for stats
    }

    private void startGame(Stage primaryStage, Board.Player secondPlayer) {
        Game game = new Game();
        Board board = new Board(game, secondPlayer);
        primaryStage.setTitle("Checkers");
        InputStream iconStream = getClass().getResourceAsStream("/checkers-icon.png");
        Image icon = null;
        if (iconStream != null) {
            icon = new Image(iconStream);
        }
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(new Scene(board.getRoot()));
        primaryStage.show();
    }

}