package code;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Game game = new Game();
        Board board = new Board(game);
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(new Scene(board.getRoot()));
        primaryStage.show();
    }

}