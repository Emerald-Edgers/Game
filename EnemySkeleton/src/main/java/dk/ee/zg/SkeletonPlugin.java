package dk.ee.zg;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IEnemyCreator;
import dk.ee.zg.common.map.data.WorldEntities;

public class SkeletonPlugin implements IEnemyCreator {
    /**
     * Instance of the skeleton object that will be created.
     */
    private Skeleton skeleton;

    /**
     * THe cost of the enemy, used in spawning mechanic calculations.
     */
    private final float cost = 10f;

    public float getEnemyCost() {
        return cost;
    }

    /**
     * The spawning method for spawning the regular enemy skeleton.
     * Used in the spawning creator
     * @param x - the x coordiante
     * @param y - the y coordianate
     * @param world - Object of all worldEntities,
     *              which contains the world entities
     */
    @Override
    public void spawn(final float x, final float y, final WorldEntities world) {
        skeleton = new Skeleton(10, 10, 10, 100, 10, cost, new Vector2(x, y));

        world.addEntity(skeleton);
    }
}
