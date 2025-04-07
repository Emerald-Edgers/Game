package dk.ee.zg.enemeSkeleton;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.data.Enemy;
import dk.ee.zg.common.map.data.EntityType;


public class Skeleton extends Enemy {



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
                "Skeleton.png", EntityType.Enemy,
                attackDamage, attackSpeed, moveSpeed, hitpoints,
                defense, cost);
    }
}
