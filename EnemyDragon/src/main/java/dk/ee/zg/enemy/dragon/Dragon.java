package dk.ee.zg.enemy.dragon;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.data.Enemy;
import dk.ee.zg.common.map.data.EntityType;

public class Dragon extends Enemy {

    /**
     * Main constructor for the skeleton.
     * @param attackDamage The amount of attack damage the skeleton should have.
     * @param attackSpeed The amount of attack speed the skeleton should have.
     * @param moveSpeed The amount of move speed the skeleton should have.
     * @param hitpoints The amount of hp the skeleton should have.
     * @param defense The amount of defense the skeleton should have.
     * @param cost The cost skeleton should have.
     * @param spawnPoint This is a set of x,y coordinates to determine where it
     *                   should be spawned
     */
    public Dragon(final int attackDamage, final int attackSpeed,
                    final int moveSpeed, final int hitpoints, final int defense,
                    final float cost, final Vector2 spawnPoint) {
        super(spawnPoint, 0, new Vector2(1 / 24f, 1 / 24f),
                "dragonEnemy.png", EntityType.Enemy,
                attackDamage, attackSpeed, moveSpeed, hitpoints,
                defense, cost);
    }
}
