package dk.ee.zg.enemy.dragon;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IEnemyCreator;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IGamePluginService;

public class DragonPlugin implements IEnemyCreator, IGamePluginService {

    /**
     * The cost of the enemy.
     */
    private final float cost = 20f;

    /**
     * The spawning method for spawning the regular enemy dragon.
     * Used in the spawning creator
     * @param x - the x coordinate
     * @param y - the y coordinate
     * @param world - Object of all worldEntities,
     *              which contains the world entities
     */
    @Override
    public void spawn(final float x, final float y, final WorldEntities world) {
        Dragon dragon = new Dragon(20, 15, 3, 40, 1, cost, new Vector2(x, y));

        world.addEntity(dragon);
    }

    /**
     * Gets the cost for the enemy, used when spawning the enemy.
     * @return  a float equaling the cost of the object.
     * This is a constant value.
     */
    @Override
    public float getEnemyCost() {
        return cost;
    }

    @Override
    public void start(WorldEntities worldEntities) {
        for(int i = 0; i <= 1; i++) {
            spawn(10, 10, worldEntities);
        }
    }

    @Override
    public void stop(WorldEntities worldEntities) {

    }
}
