package dk.ee.zg;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;

public class Skeleton extends Entity {

    /**
     * The amount of attack damage the skeleton should have.
     */
    private int attackDamage;
    /**
     * How fast the skeletons should use their attacks.
     */
    private int attackSpeed;
    /**
     * How fast the skeletons should move.
     */
    private int moveSpeed;
    /**
     * How much damage the skeleton should be able to take.
     */
    private int hitpoints;
    /**
     * How much damage the skeleton negates each attack.
     */
    private int defense;
    /**
     * The cost of the Eneemy for the spawner to know.
     */
    private float cost;

    /**
     * Main constructor for the skeleton.
     * @param attackDamage {@link Skeleton#attackDamage }
     * @param attackSpeed {@link Skeleton#attackSpeed }
     * @param moveSpeed {@link Skeleton#moveSpeed}
     * @param hitpoints {@link Skeleton#hitpoints}
     * @param defense {@link Skeleton#defense}
     * @param cost {@link Skeleton#cost }
     * @param spawnPoint This is a set of x,y coordiantes to determine where it
     *                   should be spawned
     */
    public Skeleton(final int attackDamage, final int attackSpeed,
                    final int moveSpeed, final int hitpoints, final int defense,
                    final float cost, final Vector2 spawnPoint) {
        super(spawnPoint, 0, new Vector2(1 / 25f, 1 / 25f),
                "Skeleton.png", EntityType.Enemy);

    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(final int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(final int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(final int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(final int defense) {
        this.defense = defense;
    }

}
