package dk.ee.zg.enemeSkeleton;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IAnimatable;
import dk.ee.zg.common.map.data.AnimationState;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;

public class Skeleton extends Entity implements IAnimatable {



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
                "Idle-Sheet-Skeleton.png", EntityType.Enemy,true);

        initializeAnimations();

        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.moveSpeed = moveSpeed;
        this.hitpoints = hitpoints;
        this.defense = defense;
        this.cost = cost;

        setHitbox(new Rectangle(0, 0, 0.7f, 1.1f));

        setState(AnimationState.RUN);

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

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
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
                case ATTACK:
                    setCurrentAnimation("ATTACK");
                    break;
            }
        }

    }

    public AnimationState getCurrentState() {
        return currentState;
    }
}
