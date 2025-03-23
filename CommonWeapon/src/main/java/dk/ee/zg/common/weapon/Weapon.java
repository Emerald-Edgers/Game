package dk.ee.zg.common.weapon;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;
/**
 * Abstract class representing a weapon.
 * <p>
 * This class is intended to be extended to define specific weapons.
 * Subclasses should override the {@link #attack(Vector2, Vector2)} method
 * to implement weapon-specific behavior.
 * </p>
 *
 * <h2>Extending This Class:</h2>
 * <ul>
 *   <li>Override {@link #attack(Vector2, Vector2)}
 *   to define weapon behavior.</li>
 *   <li>Ensure that subclass constructors properly
 *    initialize required fields.</li>
 * </ul>
 */
public abstract class Weapon {

    /**
     * UUID {@link UUID} defining the objects' id.
     */
    private final UUID id = UUID.randomUUID();

    /**
     * String defining the path to sprite, in the modules resources.
     */
    private String spritePath;

    /**
     * Sprite {@link Sprite} defining the sprite object for the weapon.
     */
    private Sprite sprite;
    /**
     * float defining the sprite rotation for the weapon.
     */
    private float rotation;
    /**
     * Vector2 {@link Vector2} defining the sprite scale x and y for the weapon.
     */
    private Vector2 scale;

    //non-increasing base primary stats
    /**
     * maxHP is the max amount of HP, a player can have (the HP Stat).
     * used to calculate regeneration, and take damage.
     */
    private int maxHP;
    /**
     * attackDamage is the amount of base damage, a player inflicts.
     * used to calculate actual damage to inflict upon enemy.
     */
    private int attackDamage;
    /**
     * attackSpeed is the speed at which, a player attacks.
     * used to calculate attack cooldown.
     */
    private int attackSpeed;
    /**
     * moveSpeed is the speed at which, a player moves.
     * used to calculate a players' movement.
     */
    private int moveSpeed;

    /**
     * sets attack direction.
     */
    private AttackDirection attackDirection = AttackDirection.RIGHT;

    /**
     * basic attack method.
     * left empty to require implementation on specific weapon.
     * @param playerPos - position of player : {@link Vector2}
     * @param playerSize - size of player : {@link Vector2}
     * @return - returns rectangle attack hitbox
     */
    public Rectangle attack(final Vector2 playerPos, final Vector2 playerSize) {
        return null;
    }



    public final UUID getId() {
        return id;
    }

    public final String getSpritePath() {
        return spritePath;
    }

    public final Sprite getSprite() {
        return sprite;
    }

    public final float getRotation() {
        return rotation;
    }


    public final Vector2 getScale() {
        return scale;
    }

    public final int getMaxHP() {
        return maxHP;
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    public final int getAttackSpeed() {
        return attackSpeed;
    }

    public final int getMoveSpeed() {
        return moveSpeed;
    }

    public final AttackDirection getAttackDirection() {
        return attackDirection;
    }

    public final void setAttackDirection(
            final AttackDirection direction) {
        this.attackDirection = direction;
    }

    public final void setSpritePath(final String path) {
        this.spritePath = path;
    }

    public final void setSprite(final Sprite sp) {
        this.sprite = sp;
    }

    public final void setRotation(final float f) {
        this.rotation = f;
    }

    public final void setScale(final Vector2 vec) {
        this.scale = vec;
    }

    public final void setMaxHP(final int i) {
        this.maxHP = i;
    }

    public final void setAttackDamage(final int i) {
        this.attackDamage = i;
    }

    public final void setAttackSpeed(final int i) {
        this.attackSpeed = i;
    }

    public final void setMoveSpeed(final int i) {
        this.moveSpeed = i;
    }
}
