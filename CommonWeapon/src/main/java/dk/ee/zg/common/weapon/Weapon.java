package dk.ee.zg.common.weapon;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

public abstract class Weapon {

    /**
     * UUID {@link UUID} defining the objects' id.
     */
    private final UUID id = UUID.randomUUID();

    /**
     * String defining the path to sprite, in the modules resources.
     */
    protected String spritePath;

    /**
     * Sprite {@link Sprite} defining the sprite object for the weapon.
     */
    protected Sprite sprite;
    /**
     * float defining the sprite rotation for the weapon.
     */
    protected float rotation;
    /**
     * Vector2 {@link Vector2} defining the sprite scale x and y for the weapon.
     */
    protected Vector2 scale;

    //non-increasing base primary stats
    /**
     * maxHP is the max amount of HP, a player can have (the HP Stat).
     * used to calculate regeneration, and take damage.
     */
    protected int maxHP;
    /**
     * attackDamage is the amount of base damage, a player inflicts.
     * used to calculate actual damage to inflict upon enemy.
     */
    protected int attackDamage;
    /**
     * attackSpeed is the speed at which, a player attacks.
     * used to calculate attack cooldown.
     */
    protected int attackSpeed;
    /**
     * moveSpeed is the speed at which, a player moves.
     * used to calculate a players' movement.
     */
    protected int moveSpeed;

    /**
     * basic attack method.
     * left empty to require implementation on specific weapon.
     */
    public void attack() {

    }


    public UUID getId() {
        return id;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getRotation() {
        return rotation;
    }


    public Vector2 getScale() {
        return scale;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }
}
