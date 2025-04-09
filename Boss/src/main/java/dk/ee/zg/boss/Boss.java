package dk.ee.zg.boss;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IAnimatable;
import dk.ee.zg.common.map.data.AnimationState;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;


public class Boss extends Entity implements IAnimatable {

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

    private AnimationState currentState = AnimationState.IDLE;

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
        super(new Vector2(0, 0), 0, new Vector2(1 / 15f, 1 / 15f),
                "Idle-Sheet-Boss.png", EntityType.Enemy, true);

        initializeAnimations();

        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.moveSpeed = moveSpeed;
        this.hitpoints = hitpoints;
        this.defense = defense;

        setState(AnimationState.IDLE);

    }

    public void initializeAnimations() {
        createAnimation("IDLE", "Idle-Sheet-Boss.png", 4, 1, 1f/4f, Animation.PlayMode.NORMAL);
        createAnimation("ATTACK", "Run-Sheet-Boss.png",6,1,1f/6f, Animation.PlayMode.NORMAL);
        createAnimation("DEATH", "Death-Sheet-Boss.png",6,1,1f/6f, Animation.PlayMode.NORMAL);
    }

    public void setState(AnimationState state) {
        if (state != currentState) {
            currentState = state;
            switch (state) {
                case IDLE:
                    setCurrentAnimation("IDLE");
                    break;
                case RUN:
                    setCurrentAnimation("RUN");
                    break;
                case ATTACK:
                    setCurrentAnimation("ATTACK");
                    break;
                case DEATH:
                    setCurrentAnimation("DEATH");
                    break;
            }
        }
    }

    public AnimationState getCurrentState() {
        return currentState;
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
