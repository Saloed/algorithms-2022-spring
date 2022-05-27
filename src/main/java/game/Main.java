package game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import game.components.AiMove;
import game.components.Algorithm;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;
import static game.Type.*;


public class Main extends GameApplication {
    public boolean GAME_INIT;
    public AiMove move = new AiMove();
    public Algorithm algorithm = new Algorithm(move);
    public Entity wolf1;
    public Entity wolf2;
    public Entity wolf3;
    public Entity wolf4;
    public Factory factory = new Factory();
    public Entity sheep;
    public PlayerType PLAYER_TYPE;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(750);
        settings.setHeight(500);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");
    }

    @Override
    protected void initGame() {

        getGameWorld().addEntityFactory(factory);
        setGame();
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();

        UserAction hitBall = new UserAction("Move Sheep") {
            Pair<Integer, Integer> lastCoordinate;
            boolean ifCatch;
            Type curEntity;

            @Override
            protected void onActionBegin() {
                if (GAME_INIT) {
                    curEntity = findWolfByCoordinate(
                            (int) input.getMouseYWorld() / 50 - 1,
                            (int) input.getMouseXWorld() / 50 - 1);
                    if (curEntity != null) {
                        if (algorithm.getX(curEntity) == (int) input.getMouseXWorld() / 50 - 1
                                && algorithm.getY(curEntity) == (int) input.getMouseYWorld() / 50 - 1) {
                            lastCoordinate = new Pair<>(
                                    (int) getGameWorld().getSingleton(curEntity).getY(),
                                    (int) getGameWorld().getSingleton(curEntity).getX());
                            getGameWorld().getSingleton(curEntity).setAnchoredPosition(getInput().getMousePositionWorld());
                            ifCatch = true;
                        }
                    }
                }
            }

            @Override
            protected void onAction() {
                if (ifCatch) {
                    getGameWorld().getSingleton(curEntity).setAnchoredPosition(getInput().getMousePositionWorld());
                }
            }

            @Override
            protected void onActionEnd() {
                if (ifCatch && PLAYER_TYPE == PlayerType.SHEEP) {
                    if (!algorithm.canSheepStandInThatCell(
                                    (int) (sheep.getX() / 50) - 1,
                                    (int) (sheep.getY() / 50) - 1,
                            algorithm.getX(SHEEP), algorithm.getY(SHEEP))) {
                        move.moveEntity(SHEEP, algorithm.getY(SHEEP), algorithm.getX(SHEEP));
                    } else
                        algorithm.callMinMax(PlayerType.WOLF);

                } else if (ifCatch && PLAYER_TYPE == PlayerType.WOLF) {
                    if (!algorithm.canWolfStandInThatCell(
                                    (int) getGameWorld().getSingleton(curEntity).getY() / 50 - 1,
                                    (int) getGameWorld().getSingleton(curEntity).getX() / 50 - 1
                            , curEntity)) {
                        move.moveEntity(curEntity, algorithm.getY(curEntity), algorithm.getX(curEntity));
                    } else
                        algorithm.callMinMax(PlayerType.SHEEP);
                }
                if (algorithm.isGameOver()) {
                    GAME_INIT = false;
                    resetGame();
                }
                ifCatch = false;
            }
        };
        input.addAction(hitBall, MouseButton.PRIMARY);
    }

    public Type findWolfByCoordinate(int y, int x) {
        if (PLAYER_TYPE == PlayerType.SHEEP) return SHEEP;
        for (int i = 1; i < 5; i++) {
            if (algorithm.getY(Type.values()[i]) == y && algorithm.getX(Type.values()[i]) == x) return Type.values()[i];
        }
        return null;
    }

    public void resetGame() {
        getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
        setGame();
    }

    public void setGame() {
        spawn("BGBack");
        spawn("BG");

        wolf1 = spawn("W1", 100, 50);
        wolf2 = spawn("W2", 200, 50);
        wolf3 = spawn("W3", 300, 50);
        wolf4 = spawn("W4", 400, 50);
        sheep = spawn("S", 250, 400);

        algorithm.setFirstCooridnate();
        algorithm.prepareField();
    }

    @Override
    protected void initUI() {
        Map<String, Runnable> setUpPlayer = new LinkedHashMap<>();
        Map<String, Runnable> setUpDifficulty = new LinkedHashMap<>();

        setUpPlayer.put("Wolf", () -> PLAYER_TYPE = PlayerType.WOLF);
        setUpPlayer.put("Sheep", () -> PLAYER_TYPE = PlayerType.SHEEP);
        setUpDifficulty.put("Easy", () -> algorithm.setUpDifficulty(1));
        setUpDifficulty.put("Medium", () -> algorithm.setUpDifficulty(2));
        setUpDifficulty.put("Hard", () -> algorithm.setUpDifficulty(3));
        setUpDifficulty.put("Incredible", () -> algorithm.setUpDifficulty(4));

        ChoiceBox<String> cbDialogs = getUIFactoryService().newChoiceBox(FXCollections.observableArrayList(setUpPlayer.keySet()));
        cbDialogs.getSelectionModel().selectFirst();

        ChoiceBox<String> cbDialogs2 = getUIFactoryService().newChoiceBox(FXCollections.observableArrayList(setUpDifficulty.keySet()));
        cbDialogs2.getSelectionModel().selectFirst();

        Button btn = getUIFactoryService().newButton("Start");
        btn.setOnAction(e -> {
            List<String> dialogType = new ArrayList<>();

            dialogType.add(cbDialogs.getSelectionModel().getSelectedItem());
            dialogType.add(cbDialogs2.getSelectionModel().getSelectedItem());

            for (String action : dialogType) {
                if (setUpPlayer.containsKey(action)) {
                    setUpPlayer.get(action).run();
                } else if (setUpDifficulty.containsKey(action))
                    setUpDifficulty.get(action).run();
                GAME_INIT = true;
            }
            resetGame();
            if (PlayerType.WOLF == PLAYER_TYPE) algorithm.callMinMax(PlayerType.SHEEP);
        });

        VBox vbox = new VBox(10);
        VBox vbox1 = new VBox(10);
        vbox1.setTranslateX(510);
        vbox1.setTranslateY(100);
        vbox1.getChildren().addAll(
                getUIFactoryService().newText("Choose difficulty", Color.WHITE, 18),
                cbDialogs2,
                btn);
        vbox.setTranslateX(510);
        vbox.getChildren().addAll(
                getUIFactoryService().newText("Choose player type", Color.WHITE, 18),
                cbDialogs
        );

        getGameScene().addUINode(vbox);
        getGameScene().addUINode(vbox1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}