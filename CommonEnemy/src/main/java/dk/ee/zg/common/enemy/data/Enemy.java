package dk.ee.zg.common.enemy.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IPathFinder;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;

import java.util.ArrayList;
import java.util.LinkedList;
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
     * @param hp {@link Entity#setHp(int)}
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
        this.setHp(hp);
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
    private List<Vector2> pathfindingVectorPos = new ArrayList<>();

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
     * used for determining when currently moving to step.
     */
    private Vector2 lastTargetPos = null;  // Store last step to interpolate

    /**
     * vector 2 positions from pathfinding,
     * translated to smaller steps.
     */
    private List<Vector2> steps = new LinkedList<>(); // Stores smaller steps
    /**
     * move method for using pathfinding.
     * @param pathFinder - instance of pathfinder impl
     * @param player - player entity instance
     */
    public void moveWithPathFinding(
            final IPathFinder pathFinder, final Entity player) {

        // Recalculate path if needed
        if (steps.isEmpty() && (pathfindingVectorPos.isEmpty()
                || (System.currentTimeMillis()
                - lastTriggeredPath) > pathFindCooldown)) {
            pathfindingVectorPos = pathFinder.process(this, player);
            lastTriggeredPath = System.currentTimeMillis();
            generateSubSteps(); // Generate smaller steps
        }

        // prepare the next set of substeps on cooldown timer
        if ((System.currentTimeMillis() - lastTriggered) > cooldown
                && !pathfindingVectorPos.isEmpty() && steps.isEmpty()) {
            generateSubSteps();
            lastTriggered = System.currentTimeMillis();
        }

        // Move step-by-step smoothly
        if (!steps.isEmpty()) {
            Vector2 nextStep = steps.removeFirst();
            this.setPosition(nextStep);
        }
    }

    /**
     * Generates sub-steps based on speed and delta time.
     */
    private void generateSubSteps() {
        if (pathfindingVectorPos.isEmpty()) {
            return;
        }
        float speed = this.getMoveSpeed();
        float deltaTime = Gdx.graphics.getDeltaTime();
        Vector2 currentPos = new Vector2(this.getPosition());
        lastTargetPos = pathfindingVectorPos.removeFirst();

        float distance = currentPos.dst(lastTargetPos);
        float stepSize = speed * deltaTime;
        int numSteps = Math.max(1, (int) (distance / stepSize));

        for (int i = 1; i <= numSteps; i++) {
            float t = i / (float) numSteps;
            Vector2 tempStep = new Vector2(
                    currentPos.x + t * (lastTargetPos.x - currentPos.x),
                    currentPos.y + t * (lastTargetPos.y - currentPos.y)
            );
            steps.add(tempStep);
        }
    }

}
