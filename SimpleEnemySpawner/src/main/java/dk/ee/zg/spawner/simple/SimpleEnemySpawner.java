package dk.ee.zg.spawner.simple;

import dk.ee.zg.common.enemy.interfaces.IEnemyCreator;
import dk.ee.zg.common.enemy.interfaces.IEnemySpawner;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.map.data.WorldEntities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
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
     * The player which spawning should happen around.
     */
    private Entity player;

    /**
     * The instance of random to use for rng in this class.
     */
    private final Random random = new Random();

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
        if (loadedEnemies.isEmpty()) {
            return;
        }
    }

    /**
     * Code which is run every frame of the game.
     * Updates the coin balance, and checks for when to start a spawn.
     */
    @Override
    public void process(final float frameDelta) {
        if (loadedEnemies.isEmpty()) {
            return;
        }

        balance += BALANCE_GAIN_RATE_SECONDS * frameDelta;
    }

    /**
     * Code which should be run on game startup.
     * Checks for potential enemy candidates.
     */
    @Override
    public void start(WorldEntities world) {
        loadedEnemies = findLoadedEnemies();
        player = getPlayer(world);
        balance = START_BALANCE;
    }

    private List<IEnemyCreator> generateWave(int enemyAmount) {
        // Turn set into list, so we can access its elements without looping.
        List<IEnemyCreator> enemies = new ArrayList<>(loadedEnemies);
        List<IEnemyCreator> wave = new ArrayList<>();

        for (int i = 0; i < enemyAmount; i++) {
            int randIndex = random.nextInt(enemies.size());
            IEnemyCreator randomEnemy = enemies.get(randIndex);
            wave.add(randomEnemy);
        }

        return wave;
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

    /**
     * Looks through the spawned entities and attempts to find a player.
     * @param world the world in which the player is can be found.
     * @return the player object as a {@link Entity},
     * is null if no entity with {@link EntityType} player is found.
     */
    private Entity getPlayer(WorldEntities world) {
        for (Entity entity : world.getEntities()) {
            if (entity.getEntityType() == EntityType.Player) {
                return entity;
            }
        }
        return null;
    }
}
