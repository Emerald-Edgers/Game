package dk.ee.zg.common.map.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.AnimationState;
import org.w3c.dom.Text;
import dk.ee.zg.common.data.EventManager;
import dk.ee.zg.common.data.Events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Entity {

    /**
     * id for entity.
     */
    private final UUID id;
    private final EntityType entityType;
    private float rotation;
    private Vector2 scale;
    private String sprite_path;

    private final Sprite sprite;

    private Map<String, Animation<TextureRegion>> animations;
    private String currentAnimation;
    private float stateTime;
    private Rectangle hitbox;
    /**
     * The current flash animation time.
     */
    private float damageFlashTime = 0f;
    /**
     * The duration of the flash.
     */
    private static final float MAX_FLASH_DURATION = 0.3f;
    /**
     * The color of the sprite and batch. Default is white = no color change.
     */
    private Color color = Color.WHITE;

    /**
     * hp is the current amount of health, an entity has.
     */
    private double hp;

    public Sprite getSprite() {
        return sprite;
    }



    public Entity(Vector2 position, float rotation, Vector2 scale, String sprite_path, EntityType entityType) {
        this.id = UUID.randomUUID();
        this.rotation = rotation;
        this.scale = scale;
        this.sprite_path = sprite_path;
        this.entityType = entityType;
        Texture img = new Texture(sprite_path); // Load texture from file
        this.sprite = new Sprite(img); // Create a sprite from the texture
        sprite.setScale(scale.x,scale.y);
        this.setPosition(position);

        this.animations = new HashMap<>();
        this.stateTime = 0f;
    }

    public void addAnimation(String name, Animation<TextureRegion> animation) {
        animations.put(name, animation);
        if (currentAnimation == null) {
            currentAnimation = name;
        }
    }

    public void setCurrentAnimation(String name) {
        if (animations.containsKey(name)) {
            currentAnimation = name;
            stateTime = 0f;
        }
    }

    public boolean isAnimationFinished() {
        if (currentAnimation == null || !animations.containsKey(currentAnimation)) {
            return true;
        }
        Animation<TextureRegion> animation = animations.get(currentAnimation);
        return animation.isAnimationFinished(stateTime);
    }

    public void createAnimation(String name, String sprite_path, int frameCols, int frameRows, float frameDuration, Animation.PlayMode playMode) {
     Texture sheet = new Texture(sprite_path);

     TextureRegion[][] tmp = TextureRegion.split(
             sheet, sheet.getWidth() / frameCols, sheet.getHeight() / frameRows);

     TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
     int index = 0;
     for (int i = 0; i < frameRows; i++) {
         for (int j = 0; j < frameCols; j++) {
             frames[index++] = tmp[i][j];
         }
     }

     Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
     animation.setPlayMode(playMode);
     addAnimation(name, animation);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public UUID getId() {
        return id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Vector2 getPosition() {
        Vector2 position = new Vector2();
        sprite.getBoundingRectangle().getCenter(position);
        return position;
    }

    public void setPosition(Vector2 position) {
        sprite.setCenter(position.x, position.y);
        if (hitbox != null) {
            updateHitboxPlacement();
        }
    }

    public void updateHitboxPlacement() {
        hitbox.setCenter(getPosition().x, getPosition().y);
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

    public double getHp() {
        return hp;
    }

    public void setHp(final double hp) {
        this.hp = hp;
    }

    public void update(float delta) {
        stateTime += delta;
        if (damageFlashTime > 0) {
            damageFlashTime -= delta;
            float alpha = damageFlashTime / MAX_FLASH_DURATION;
            color = new Color(
                    1.0f,
                    1.0f - alpha,
                    1.0f - alpha,
                    1.0f
            );
            if (damageFlashTime <= 0) {
                color = Color.WHITE;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (currentAnimation != null && animations.containsKey(currentAnimation)) {
            TextureRegion currentFrame = animations.get(currentAnimation).getKeyFrame(stateTime);

            currentFrame.flip(sprite.isFlipX(), sprite.isFlipY());

            float width = currentFrame.getRegionWidth() * scale.x;
            float height = currentFrame.getRegionHeight() * scale.y;

            Vector2 position = getPosition();
            float x = position.x - width / 2;
            float y = position.y - height / 2;
            batch.setColor(color);
            batch.draw(
                    currentFrame,
                    x, y,
                    width/2,height/2,
                    width,height,
                    1,1,
                    rotation
            );

            currentFrame.flip(sprite.isFlipX(), sprite.isFlipY());
        } else {
            sprite.setColor(color);
            sprite.setScale(scale.x,scale.y);
            sprite.setRotation(rotation);
            sprite.draw(batch); // Draw the sprite
        }
    }

    /**
     * universal method for getting hit.
     * triggers enemy killed event, with 100 exp added.
     * @param damage - damage taken, dertermined,
     *               by the attacker
     */
    public void hit(final int damage) {
        if (entityType != EntityType.Obstacle) {
            this.hp -= damage;
            color = Color.RED;
            this.damageFlashTime = MAX_FLASH_DURATION;
            //if dead, trigger enemykilledevent
            if (this.hp <= 0) {
                int xp = (int) (Math.random() * 750);
                EventManager.triggerEvent(new Events.EnemyKilledEvent(xp, this.id));
            }
        }
    }

    public String getCurrentAnimation() {
        return currentAnimation;
    }

    public Map<String, Animation<TextureRegion>> getAnimations() {
        return animations;
    };

    public float getStateTime() {
        return stateTime;
    }



}
