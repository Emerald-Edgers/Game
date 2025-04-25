package dk.ee.zg.boss;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.data.Enemy;
import dk.ee.zg.common.enemy.interfaces.IAnimatable;
import dk.ee.zg.common.map.data.AnimationState;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;


public class Boss extends Enemy implements IAnimatable {
    private AnimationState currentState = AnimationState.IDLE;

    /**
     * constructor for the boss, utilizes super class Enemy.
     * {@link Entity}
     * @param attackDamage {@link Enemy#attackDamage}
     * @param attackSpeed {@link Enemy#attackSpeed}
     * @param moveSpeed {@link Enemy#moveSpeed}
     * @param hitpoints {@link Enemy#hitpoints}
     * @param defense {@link Enemy#defense}
     */
    public Boss(final int attackDamage, final int attackSpeed,
                final int moveSpeed, final int hitpoints, final int defense) {
        super(new Vector2(0, 0), 0,
                new Vector2(1 / 15f, 1 / 15f),
                "Idle-Sheet-Boss.png", EntityType.Enemy,
                attackDamage, attackSpeed,
                moveSpeed, hitpoints, defense, 0);

        initializeAnimations();

        setHitbox(new Rectangle(0,0, 1f, 1.7f));

        setState(AnimationState.IDLE);

    }

    public void initializeAnimations() {
        createAnimation("IDLE", "Idle-Sheet-Boss.png", 8, 1, 1f/12f, Animation.PlayMode.LOOP);
        createAnimation("ATTACK", "Ranged-Attack-Boss.png",18,1,1f/12f, Animation.PlayMode.NORMAL);
        createAnimation("DEATH", "Death-Sheet-Boss.png",6,1,1f/6f, Animation.PlayMode.NORMAL);
    }

    public void setState(AnimationState state) {
        if (state != currentState) {
            currentState = state;
            setCurrentAnimation(state.toString());
        }
    }

    public AnimationState getCurrentState() {
        return currentState;
    }
}
