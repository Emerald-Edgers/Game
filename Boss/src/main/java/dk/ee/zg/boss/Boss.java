package dk.ee.zg.boss;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;


public class Boss extends Entity {

    /**
     * attackDamage is used to set the amount of damage the boss should do.
     * used to check damage calculations over players defences
     */
    private final int attackDamage;
    /**
     * attackSpeed is used to set the speed at which the boss attacks.
     */
    private final int attackSpeed;
    /**
     * moveSpeed is used to set the speed at which the boss moves.
     */
    private final int moveSpeed;
    /**
     * hitpoints is used to set the amount of health the boss has.
     */
    private final int hitpoints;
    /**
     * defense is used to set the amount of damage the boss can withstand.
     */
    private final int defense;

    /**
     * constructor for the boss, utilizes super class Entity.
     * {@link Entity}
     * @param attackDamage {@link Boss#attackDamage}
     * @param attackSpeed {@link Boss#attackSpeed}
     * @param moveSpeed {@link Boss#moveSpeed}
     * @param hitpoints {@link Boss#hitpoints}
     * @param defense {@link Boss#defense}
     */
    public Boss(final int attackDamage, final int attackSpeed,
                final int moveSpeed, final int hitpoints, final int defense) {
        super(new Vector2(0, 0), 0, new Vector2(1 / 10f, 1 / 10f),
                "Boss4.png", EntityType.Enemy);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.moveSpeed = moveSpeed;
        this.hitpoints = hitpoints;
        this.defense = defense;

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
    public int getHitpoints() {
        return hitpoints;
    }
    public int getDefense() {
        return defense;
    }

}
