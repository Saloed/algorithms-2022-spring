package game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
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
import static game.components.GlobalVars.*;

public class Main extends GameApplication {

    public Algorithm algorithm = new Algorithm();
    public Entity wolf1;
    public Entity wolf2;
    public Entity wolf3;
    public Entity wolf4;
    public Factory factory = new Factory();
    public Entity sheep;

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
                    curEntity = findWolfByCoordinate((int) input.getMouseYWorld() / 50 - 1, (int) input.getMouseXWorld() / 50 - 1);
                    if (curEntity != null) {
                        if (curEntity.getX() == (int) input.getMouseXWorld() / 50 - 1 && curEntity.getY() == (int) input.getMouseYWorld() / 50 - 1) {
                            lastCoordinate = new Pair<>((int) getGameWorld().getSingleton(curEntity).getY(), (int) getGameWorld().getSingleton(curEntity).getX());
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
                if (ifCatch && PLAYER_TYPE == 1) {
                    if (!canSheepStandInThatCell((int) (sheep.getX() / 50) - 1, (int) (sheep.getY() / 50) - 1,
                            SHEEP.getX(), SHEEP.getY(), lastCoordinate)) {
                        getGameWorld().getSingleton(SHEEP).setAnchoredPosition(lastCoordinate.getSecond(), lastCoordinate.getFirst());
                    } else
                        algorithm.minMax(2, 0, -500, +500);

                } else if (ifCatch && PLAYER_TYPE == 2) {
                    if (!canWolfStandInThatCell((int) getGameWorld().getSingleton(curEntity).getY() / 50 - 1, (int) getGameWorld().getSingleton(curEntity).getX() / 50 - 1
                            , curEntity, lastCoordinate)) {
                        getGameWorld().getSingleton(curEntity).setAnchoredPosition(lastCoordinate.getSecond(), lastCoordinate.getFirst());
                    } else
                        algorithm.minMax(1, 0, -500, +500);
                }
                if (isGameOver()) {
                    GAME_INIT = false;
                    resetGame();
                }
                ifCatch = false;
            }
        };
        input.addAction(hitBall, MouseButton.PRIMARY);
    }

    public boolean canWolfStandInThatCell(int y, int x, Type wolf, Pair<Integer, Integer> lastCoordinates) {
        if (y - wolf.getY() == 1 && Math.abs(x - wolf.getX()) == 1) {
            if ((y >= 0 && x >= 0 && y <= 7 && x <= 7)) {
                if (array[y][x] == 0) {
                    if (x < wolf.getX())
                        getGameWorld().getSingleton(wolf).setAnchoredPosition(lastCoordinates.getSecond() - 50, lastCoordinates.getFirst() + 50);
                    if (x > wolf.getX())
                        getGameWorld().getSingleton(wolf).setAnchoredPosition(lastCoordinates.getSecond() + 50, lastCoordinates.getFirst() + 50);

                    wolf.setCoordinate(new Pair<>(y, x));
                    array[y][x] = 0;
                    return true;
                }
            }
        }
        return false;
    }

    public Type findWolfByCoordinate(int y, int x) {
        if (PLAYER_TYPE == 1) return SHEEP;
        for (int i = 1; i < 5; i++) {
            if (Type.values()[i].getY() == y && Type.values()[i].getX() == x) return Type.values()[i];
        }
        return null;
    }

    public boolean isGameOver() {
        if (SHEEP.getX() == 0 && SHEEP.getY() == 7 && array[6][1] != 0)
            return true;
        if (SHEEP.getX() == 0 && SHEEP.getY() < 7)
            if ((array[SHEEP.getY() + 1][SHEEP.getX() + 1] != 0 && array[SHEEP.getY() - 1][SHEEP.getX() + 1] != 0))
                return true;
        if (SHEEP.getY() == 7)
            if (array[SHEEP.getY() - 1][SHEEP.getX() + 1] != 0 && array[SHEEP.getY() - 1][SHEEP.getX() - 1] != 0)
                return true;
        if (SHEEP.getX() == 7)
            if (array[SHEEP.getY() - 1][SHEEP.getX() - 1] != 0 && array[SHEEP.getY() + 1][SHEEP.getX() - 1] != 0)
                return true;
        if (SHEEP.getX() != 0 && SHEEP.getY() != 7 && SHEEP.getX() != 7 && SHEEP.getY() != 0)
            if (array[SHEEP.getY() - 1][SHEEP.getX() - 1] != 0 && array[SHEEP.getY() + 1][SHEEP.getX() - 1] != 0
                    && array[SHEEP.getY() - 1][SHEEP.getX() + 1] != 0 && array[SHEEP.getY() + 1][SHEEP.getX() + 1] != 0)
                return true;
        int countY = 0;
        for (int i = 1; i < 5; i++) {
            if (Type.values()[i].getY() >= SHEEP.getY()) countY++;
        }
        return countY == 4;
    }

    public void resetGame() {
        getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
        setGame();
    }

    public boolean canSheepStandInThatCell(int xNew, int yNew, int xLast, int yLast, Pair<Integer, Integer> lastCoordinates) {
        if (Math.abs(xNew - xLast) == 1 && Math.abs(yNew - yLast) == 1) {
            if ((xNew >= 0 && yNew >= 0 && xNew <= 7 && yNew <= 7)) {

                if (array[yNew][xNew] == 0) {
                    if (yLast > yNew && xLast < xNew)
                        getGameWorld().getSingleton(SHEEP).setAnchoredPosition(lastCoordinates.getSecond() + 50, lastCoordinates.getFirst() - 50);
                    else if (yLast > yNew && xLast > xNew)
                        getGameWorld().getSingleton(SHEEP).setAnchoredPosition(lastCoordinates.getSecond() - 50, lastCoordinates.getFirst() - 50);
                    else if (yLast < yNew && xLast < xNew)
                        getGameWorld().getSingleton(SHEEP).setAnchoredPosition(lastCoordinates.getSecond() + 50, lastCoordinates.getFirst() + 50);//
                    else if (yLast < yNew && xLast > xNew)
                        getGameWorld().getSingleton(SHEEP).setAnchoredPosition(lastCoordinates.getSecond() - 50, lastCoordinates.getFirst() + 50);//
                    SHEEP.setCoordinate(new Pair<>(yNew, xNew));
                    array[yLast][xLast] = 0;
                    return true;
                }
            }
        }
        return false;
    }

    public void setGame() {
        spawn("BGBack");
        spawn("BG");

        WOLF1.setCoordinate(new Pair<>(0, 1));
        wolf1 = spawn("W1", 100, 50);

        WOLF2.setCoordinate(new Pair<>(0, 3));
        wolf2 = spawn("W2", 200, 50);

        WOLF3.setCoordinate(new Pair<>(0, 5));
        wolf3 = spawn("W3", 300, 50);

        WOLF4.setCoordinate(new Pair<>(0, 7));
        wolf4 = spawn("W4", 400, 50);

        sheep = spawn("S", 50, 400);
        SHEEP.setCoordinate(new Pair<>(7, 0));

        algorithm.prepareField();
    }

    @Override
    protected void initUI() {
        Map<String, Runnable> setUpPlayer = new LinkedHashMap<>();
        Map<String, Runnable> setUpDifficulty = new LinkedHashMap<>();

        setUpPlayer.put("Woolf", () -> PLAYER_TYPE = 2);
        setUpPlayer.put("Sheep", () -> PLAYER_TYPE = 1);
        setUpDifficulty.put("Easy", () -> DIFFICULTY = 1);
        setUpDifficulty.put("Medium", () -> DIFFICULTY = 2);
        setUpDifficulty.put("Hard", () -> DIFFICULTY = 3);
        setUpDifficulty.put("Incredible", () -> DIFFICULTY = 3.5);

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
                if (setUpPlayer.containsKey(action))
                    setUpPlayer.get(action).run();
                else if (setUpDifficulty.containsKey(action))
                    setUpDifficulty.get(action).run();
                GAME_INIT = true;
                resetGame();
            }
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