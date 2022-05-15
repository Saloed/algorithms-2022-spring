package game.components;

import game.Type;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

public class AiMove {

    public void moveWolf(Type entity) {
        assert entity != null;
        getGameWorld().getSingleton(entity).setAnchoredPosition((entity.getX()+1)*50, (entity.getY()+1)*50);
    }
}
