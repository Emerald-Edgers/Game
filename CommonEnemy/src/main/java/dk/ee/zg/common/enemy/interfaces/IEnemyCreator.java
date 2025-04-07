package dk.ee.zg.common.enemy.interfaces;

import dk.ee.zg.common.map.data.WorldEntities;

public interface IEnemyCreator {
    /**
     * Should add an entity to the world at the given coordinates.
     * Method is called when a spawner wants to spawn something.
     * @param x the x coordinate to spawn at.
     * @param y the y coordinate to spawn at.
     * @param world the world to add enemy to.
     */
    void spawn(float x, float y, WorldEntities world);

    /**
     * Should return the cost of an enemy.
     * Is required for Spawners to properly work.
     * @return a cost for the operation of spawning an enemy.
     * For spawners to properly work it must be a positive value.
     */
    float getEnemyCost();
}
