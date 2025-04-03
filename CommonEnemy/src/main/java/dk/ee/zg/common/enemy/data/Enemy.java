package dk.ee.zg.common.enemy.data;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IPathFinder;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends Entity {
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
     * Main constructor for the enemy abstract.
     * @param spawnPoint This is a set of x,y coordiantes to determine where it
     *       should be spawned
     * @param rotation rotation of sprite
     * @param scale scale of sprite
     * @param spritePath path to sprite resource
     * @param entityType type of entity
     * @param aDamage {@link Enemy#attackDamage }
     * @param aSpeed {@link Enemy#attackSpeed }
     * @param mSpeed {@link Enemy#moveSpeed}
     * @param hp {@link Enemy#hitpoints}
     * @param def {@link Enemy#defense}
     * @param val {@link Enemy#cost }
     */
    public Enemy(final Vector2 spawnPoint, final float rotation,
                 final Vector2 scale,
                 final String spritePath, final EntityType entityType,
                 final int aDamage, final int aSpeed,
                 final int mSpeed, final int hp, final int def,
                 final float val) {
        super(spawnPoint, rotation, scale, spritePath, entityType);
        this.attackDamage = aDamage;
        this.attackSpeed = aSpeed;
        this.moveSpeed = mSpeed;
        this.hitpoints = hp;
        this.defense = def;
        this.cost = val;
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    public final void setAttackDamage(final int i) {
        this.attackDamage = i;
    }

    public final int getAttackSpeed() {
        return attackSpeed;
    }

    public final void setAttackSpeed(final int i) {
        this.attackSpeed = i;
    }

    public final int getMoveSpeed() {
        return moveSpeed;
    }

    public final void setMoveSpeed(final int i) {
        this.moveSpeed = i;
    }

    public final int getHitpoints() {
        return hitpoints;
    }

    public final void setHitpoints(final int i) {
        this.hitpoints = i;
    }

    public final int getDefense() {
        return defense;
    }

    public final void setDefense(final int i) {
        this.defense = i;
    }

    public final float getCost() {
        return cost;
    }

    public final void setCost(final float f) {
        this.cost = f;
    }


    /**
     * vector 2 positions from pathfinding.
     */
    private List<Vector2> pathfindingVectorPos;
    /**
     * vector 2 positions from pathfinding,
     * translated to smaller steps.
     */
    private List<Vector2> steps = new ArrayList<>();
    /**
     * last triggered movement with pathfinding.
     */
    private long lastTriggered = System.currentTimeMillis();
    /**
     * last triggered pathfinding calculation.
     */
    private long lastTriggeredPath = System.currentTimeMillis();
    /**
     * movement cooldown, how long to wait till taking next steps.
     */
    private final long cooldown = 100;
    /**
     * AI cooldown, time till next AI pathfinding calculation.
     */
    private final long pathFindCooldown = 1000;

    /**
     * move method for using pathfinding.
     * @param pathFinder - instance of pathfinder impl
     * @param player - player entity instance
     */
    public void moveWithPathFinding(final IPathFinder pathFinder,
                                    final Entity player) {

        if (pathfindingVectorPos == null || (System.currentTimeMillis()
                - lastTriggeredPath)  > pathFindCooldown) {
            pathfindingVectorPos = pathFinder.process(this, player);
            System.out.println(pathfindingVectorPos);
            lastTriggeredPath = System.currentTimeMillis();
        }
        if (steps == null){
            generateSteps(10);
        }

        if ((System.currentTimeMillis() - lastTriggered)
                > cooldown && pathfindingVectorPos != null) {
            System.out.println("trigger");
            if (pathfindingVectorPos.size() > 1) {
                Vector2 pos = pathfindingVectorPos.removeFirst();
                this.setPosition(pos);
                System.out.println("taking step: " + pos);
            }
            lastTriggered = System.currentTimeMillis();
        }
    }

    private void generateSteps(int parts){
        for (Vector2 step : pathfindingVectorPos) {
            Vector2 stepDiff = step.sub(this.getPosition());
            //split to a tenth
            Vector2 addititve = new Vector2((stepDiff).scl((float) 1 / parts));
            for (int i = 0; i < parts; i++){
                steps.add(new Vector2(this.getPosition()).add(addititve));
            }

        }
    }
}
