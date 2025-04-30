package dk.ee.zg.enemeSkeleton;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IAnimatable;
import dk.ee.zg.common.map.data.AnimationState;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.enemy.data.Enemy;

public class Skeleton extends Enemy implements IAnimatable {

    private AnimationState currentState = AnimationState.IDLE;

    /**
     * Main constructor for the skeleton.
     * @param attackDamage The amount of attack damage the skeleton should have.
     * @param attackSpeed The amount of attack speed the skeleton should have.
     * @param moveSpeed The amount of move speed the skeleton should have.
     * @param hitpoints The amount of hp the skeleton should have.
     * @param defense The amount of defense the skeleton should have.
     * @param cost The cost skeleton should have.
     * @param spawnPoint This is a set of x,y coordiantes to determine where it
     *                   should be spawned
     */
    public Skeleton(final int attackDamage, final int attackSpeed,
                    final int moveSpeed, final int hitpoints, final int defense,
                    final float cost, final Vector2 spawnPoint) {
        super(spawnPoint, 0, new Vector2(1 / 25f, 1 / 25f),
                "Idle-Sheet-Skeleton.png", EntityType.Enemy,
                attackDamage, attackSpeed, moveSpeed, hitpoints,
                defense, cost);

        initializeAnimations();

        setHitbox(new Rectangle(0, 0, 0.7f, 1.1f));

        setState(AnimationState.RUN);

    }

    @Override
    public void updateHitboxPlacement() {
        getHitbox().setCenter(getPosition().x, getPosition().y - 0.65f);
    }

    public void initializeAnimations() {
        createAnimation("RUN", "Run-Sheet-Skeleton.png", 6, 1, 1f/6f, Animation.PlayMode.LOOP);
        createAnimation("IDLE", "Idle-Sheet-Skeleton.png", 4, 1, 1f/4f, Animation.PlayMode.LOOP);
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
                case MELEEATTACK:
                    setCurrentAnimation("ATTACK");
                    break;
            }
        }

    }

    public AnimationState getCurrentState() {
        return currentState;
    }
}
