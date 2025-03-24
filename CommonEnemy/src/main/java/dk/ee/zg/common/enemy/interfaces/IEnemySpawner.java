package dk.ee.zg.common.enemy.interfaces;

import dk.ee.zg.common.map.data.WorldEntities;

public interface IEnemySpawner {
    /**
     * Dictates the spawning procedure of a new enemy to the game.
     * @param world the world in which the enemy should be spawned.
     */
    void requestSpawnEnemy(WorldEntities world);

    /**
     * Apart of the main game loop, code which runs once a frame.
     * @param frameDelta The delta-time of the current frame.
     */
    void process(float frameDelta);

    /**
     * Apart of the main game startup sequence,
     * code which runs once on game start.
     * @param world which contains information about
     *              already existing entities in the world.
     */
    void start(WorldEntities world);
}
