package dk.ee.zg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player implements IEntity {
    private Sprite sprite;

    public Player() {
        sprite = new Sprite(new Texture("player.png")); // Load texture and create sprite
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    public void dispose() {
        sprite.getTexture().dispose(); // Don't forget to dispose the texture

    }

}
