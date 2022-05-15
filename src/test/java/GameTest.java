import com.almasb.fxgl.app.GameApplication;
import game.Main;
import game.components.Algorithm;
import javafx.application.Platform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

import static game.components.GlobalVars.*;

@ExtendWith(RunWithFX.class)
public class GameTest {
    static Thread t;
    static Main main = new Main();

    public static void init() throws InterruptedException {

        t = new Thread(() -> GameApplication.launch(main.getClass(), new String[0]));
        t.start();
        Thread.sleep(1000);
    }

    @Test
    public void gameInit() throws InterruptedException {
        init();
        Algorithm algorithm = new Algorithm();


        GAME_INIT = true;
        DIFFICULTY = 4;

        while (!main.isGameOver()) {
            algorithm.minMax(1, 0, -500, +500);
            Thread.sleep(500);
            algorithm.minMax(2, 0, -500, +500);
            //  Platform.runLater(() -> main.resetGame());
        }
    }
}
