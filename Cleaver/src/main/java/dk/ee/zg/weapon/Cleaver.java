package dk.ee.zg.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.weapon.Weapon;

public class Cleaver extends Weapon {

    /**
     * constructor for Cleaver, initializes and sets all Weapon variables.
     * {@link Weapon}
     */
    public Cleaver() {
        this.setSpritePath("cleaver.png");
        this.setRotation(0);
        this.setScale(new Vector2(1, 1));
        Texture img = new Texture(getSpritePath()); // Load texture from file
        this.setSprite(new Sprite(img)); // Create a sprite from the texture

        //stats
        this.setMaxHP(120);
        this.setAttackDamage(20);
        this.setAttackSpeed(5);
        this.setMoveSpeed(5);
    }

    /**
     * cleaver attack - sweep like attack hit box.
     * @param playerPos - position of player : {@link Vector2}
     * @param playerSize - size of player : {@link Vector2}
     * @return - returns hitbox
     */
    @Override
    public Rectangle attack(final Vector2 playerPos, final Vector2 playerSize) {
        float attackOffsetX = 0.5f;
        float attackOffsetY = 0.5f;
        float width = 1;
        float height = 1;
        switch (getAttackDirection()) {
            case LEFT -> {
                height = 2;
                attackOffsetX = -(playerSize.x / 2) - width - 0.25f;
                attackOffsetY = -(height / 2);
            }
            case RIGHT -> {
                height = 2;
                attackOffsetX = (playerSize.x / 2) + 0.25f;
                attackOffsetY = -(height / 2);
            }
            case UP -> {
                width = 2;
                attackOffsetY = playerSize.y / 2 + 0.25f;
                attackOffsetX = -(playerSize.x / 2) - width / 2;
            }
            case DOWN -> {
                width = 2;
                attackOffsetY = -playerSize.y / 2 -  0.25f;
                attackOffsetX = -(playerSize.x / 2) - width / 2;
            }
            default -> { }
        }
        Rectangle hitbox = new Rectangle(
                playerPos.x + attackOffsetX,
                playerPos.y + attackOffsetY,
                width,
                height);
        return hitbox;
    }
}
