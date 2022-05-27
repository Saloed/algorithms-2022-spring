package game;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.pathfinding.CellMoveComponent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static game.Textures.*;
import static game.Type.*;

public class Factory implements EntityFactory {

    public final int CELL_SIZE = 50;

    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return FXGL.entityBuilder()
                .view(BG_TEXTURE)
                .zIndex(-1)
                .build();
    }
    @Spawns("BGBack")
    public Entity newBackgroundBack(SpawnData data) {
        return FXGL.entityBuilder()
                .view(new Rectangle(750, 500, Color.BLACK))
                .zIndex(-1)
                .build();
    }

    @Spawns("S")
    public Entity newSheep(SpawnData data) {
        return entityBuilder(data)
                .type(SHEEP)
                .viewWithBBox(texture(SHEEP_TEXTURE, CELL_SIZE, CELL_SIZE))
                .build();
    }

    @Spawns("W1")
    public Entity newWolf1(SpawnData data) {

        return entityBuilder(data)
                .type(WOLF1)
                .viewWithBBox(texture(WOLF_TEXTURE, CELL_SIZE, CELL_SIZE))
                .with(new CellMoveComponent(CELL_SIZE, CELL_SIZE, 300))
                .build();
    }

    @Spawns("W2")
    public Entity newWolf2(SpawnData data) {

        return entityBuilder(data)
                .type(WOLF2)
                .viewWithBBox(texture(WOLF_TEXTURE, CELL_SIZE, CELL_SIZE))
                .with(new CellMoveComponent(CELL_SIZE, CELL_SIZE, 300))
                .build();
    }

    @Spawns("W3")
    public Entity newWolf3(SpawnData data) {

        return entityBuilder(data)
                .type(WOLF3)
                .viewWithBBox(texture(WOLF_TEXTURE, CELL_SIZE, CELL_SIZE))
                .with(new CellMoveComponent(CELL_SIZE, CELL_SIZE, 300))
                .build();
    }

    @Spawns("W4")
    public Entity newWolf4(SpawnData data) {

        return entityBuilder(data)
                .type(WOLF4)
                .viewWithBBox(texture(WOLF_TEXTURE, CELL_SIZE, CELL_SIZE))
                .with(new CellMoveComponent(CELL_SIZE, CELL_SIZE, 300))
                .build();
    }
}
