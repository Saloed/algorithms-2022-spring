import com.almasb.fxgl.app.GameApplication;
import game.Main;
import game.PlayerType;
import game.components.AiMove;
import game.components.Algorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
//        init();
//        Algorithm algorithm = new Algorithm(new AiMove());
//
//        GAME_INIT = true;
//
//        while (!algorithm.isGameOver()) {
//            DIFFICULTY = 5;
//            algorithm.minMax(PlayerType.SHEEP, 0);
//            DIFFICULTY = 5;
//            Thread.sleep(500);
//            algorithm.minMax(PlayerType.WOLF, 0);
//        }
    }

}
