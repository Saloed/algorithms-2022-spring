package game.components;

import game.Type;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

public class AiMove {

    public void moveEntity(Type entity, int y, int x) {
        assert entity != null;
        getGameWorld().getSingleton(entity).setAnchoredPosition((x + 1) * 50, (y + 1) * 50);
    }
}
