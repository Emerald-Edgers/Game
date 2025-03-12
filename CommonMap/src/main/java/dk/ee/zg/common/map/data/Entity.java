package dk.ee.zg.common.map.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

public class Entity {

    /**
     * id for entity.
     */
    private final UUID id;
    private final EntityType entityType;
    private Vector2 position;
    private float rotation;
    private Vector2 scale;
    private String sprite_path;

    private Sprite sprite;


    public Sprite getSprite() {
        return sprite;
    }



    public Entity(Vector2 position, float rotation, Vector2 scale, String sprite_path, EntityType entityType) {
        this.id = UUID.randomUUID();
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.sprite_path = sprite_path;
        this.entityType = entityType;
        Texture img = new Texture(sprite_path); // Load texture from file
        this.sprite = new Sprite(img); // Create a sprite from the texture
        sprite.setScale(scale.x,scale.y);
    }

    public UUID getId() {
        return id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

    public String getSprite_path() {
        return sprite_path;
    }

    public void setSprite_path(String sprite_path) {
        this.sprite_path = sprite_path;
    }

    public void draw(SpriteBatch batch) {

        sprite.setPosition(position.x, position.y);
        sprite.draw(batch); // Draw the sprite

    }
}
