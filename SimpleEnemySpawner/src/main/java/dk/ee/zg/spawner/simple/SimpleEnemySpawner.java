package dk.ee.zg.spawner.simple;

import dk.ee.zg.common.enemy.interfaces.IEnemyCreator;
import dk.ee.zg.common.enemy.interfaces.IEnemySpawner;
import dk.ee.zg.common.map.data.WorldEntities;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

public class SimpleEnemySpawner implements IEnemySpawner {
    /**
     * A set of found implementations of  {@link IEnemyCreator}.
     * Can be empty if none were found
     */
    private Set<IEnemyCreator> loadedEnemies;

    /**
     * The current amount of coins the spawner has to spend.
     */
    private float balance;

    /**
     * Constant:    The amount of balance ready from the start.
     */
    private static final float START_BALANCE = 100;

    /**
     * Constant:    The rate at which balance climbs, per second.
     */
    private static final float BALANCE_GAIN_RATE_SECONDS = 3;

    /**
     * Creates and adds an enemy from the found implementations to the world.
     * @param world in which the entities should be added.
     */
    @Override
    public void requestSpawnEnemy(WorldEntities world) {
    }

    /**
     * Code which is run every frame of the game.
     * Updates the coin balance, and checks for when to start a spawn.
     */
    @Override
    public void process(final float frameDelta) {
        balance += BALANCE_GAIN_RATE_SECONDS * frameDelta;
    }

    /**
     * Code which should be run on game startup.
     * Checks for potential enemy candidates.
     */
    @Override
    public void start() {
        loadedEnemies = findLoadedEnemies();
        balance = START_BALANCE;
    }

    /**
     * Gets all instances of classes which implements {@link IEnemyCreator}.
     * @return  A List populated with found {@link IEnemyCreator}'s.
     * List can be empty if no IEnemyCreator implementations are found.
     */
    private Set<IEnemyCreator> findLoadedEnemies() {
        Set<IEnemyCreator> loadedEnemies = new HashSet<>();
        for (IEnemyCreator enemy : ServiceLoader.load(IEnemyCreator.class)) {
            loadedEnemies.add(enemy);
        }
        return loadedEnemies;
    }
}
