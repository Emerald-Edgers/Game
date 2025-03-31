package dk.ee.zg.spawner.simple;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.data.GameData;
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
     * The next wave to be spawned.
     */
    private List<IEnemyCreator> nextWave;

    /**
     * The cost of the next wave to be spawned.
     */
    private float costOfWave;

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
     * Constant:    The buffer units around the camera,
     * in which enemies can spawn.
     */
    private static final float SPAWN_BUFFER_UNITS = 3;

    /**
     * Creates and adds an enemy from the found implementations to the world.
     * @param world in which the entities should be added.
     */
    @Override
    public void requestSpawnEnemy(final WorldEntities world) {
        OrthographicCamera camera = GameData.getInstance().getCamera();

        // Rectangle made by the camera.
        float bottomX = camera.position.x - camera.viewportWidth / 2;
        float bottomY = camera.position.y - camera.viewportHeight / 2;
        float topX = camera.position.x + camera.viewportWidth / 2;
        float topY = camera.position.y + camera.viewportHeight / 2;

        // Rectangle made by the spawning boundary.
        float bottomSpawnX = Math.max(bottomX - SPAWN_BUFFER_UNITS, 0);
        float bottomSpawnY = Math.max(bottomY - SPAWN_BUFFER_UNITS, 0);
        float topSpawnX = Math.min(topX + SPAWN_BUFFER_UNITS, 50);
        float topSpawnY = Math.min(topY + SPAWN_BUFFER_UNITS, 75);

       Vector2 bottomSpawnCorner = new Vector2(bottomSpawnX, bottomSpawnY);
       Vector2 topSpawnCorner = new Vector2(topSpawnX, topSpawnY);


        for (IEnemyCreator enemy : nextWave) {
            spawnEnemy(world, enemy, bottomSpawnCorner, topSpawnCorner);
        }

        balance -= costOfWave;
        nextWave = null;
    }

    /**
     * Tells the given {@link IEnemyCreator}
     * to spawn an enemy at a random point.
     * The random point is within the rectangle made by two vectors
     * @param world the world in which the enemy should spawn.
     * @param enemy the EnemyCreator which can create an enemy.
     * @param bottomCorner the bottom corner of the rectangle.
     * @param topCorner the top corner of the rectangle.
     */
    private void spawnEnemy(final WorldEntities world,
                            final IEnemyCreator enemy,
                            final Vector2 bottomCorner,
                            final Vector2 topCorner) {
        float spawnX;
        float spawnY;

        int edge = random.nextInt(4);

        switch (edge) {
            case 0: // Left edge
                spawnX = random.nextFloat(bottomCorner.x,
                        bottomCorner.x + SPAWN_BUFFER_UNITS);
                spawnY = random.nextFloat(bottomCorner.y, topCorner.y);
                break;
            case 1: // Right edge
                spawnX = random.nextFloat(
                        topCorner.x - SPAWN_BUFFER_UNITS, topCorner.x);
                spawnY = random.nextFloat(bottomCorner.y, topCorner.y);
                break;
            case 2: // Top edge
                spawnY = random.nextFloat(topCorner.y
                        - SPAWN_BUFFER_UNITS, topCorner.y);
                spawnX = random.nextFloat(bottomCorner.x, topCorner.x);
                break;
            case 3: // Bottom edge
                spawnY = random.nextFloat(bottomCorner.y,
                        bottomCorner.y + SPAWN_BUFFER_UNITS);
                spawnX = random.nextFloat(bottomCorner.x, topCorner.x);
                break;
            default:
                spawnX = bottomCorner.x;
                spawnY = bottomCorner.y;
                break;
        }

        enemy.spawn(spawnX, spawnY, world);
    }

    /**
     * Code which is run every frame of the game.
     * Updates the coin balance, and checks for when to start a spawn.
     */
    @Override
    public void process(final float frameDelta, final WorldEntities world) {
        // Early exist if no enemies have been found.
        if (loadedEnemies.isEmpty()) {
            return;
        }

        if (nextWave == null) {
            nextWave = generateWave(random.nextInt(1, 8));
            costOfWave = findCostOfWave(nextWave);
        }

        if (costOfWave <= balance) {
            requestSpawnEnemy(world);
        }

        balance += BALANCE_GAIN_RATE_SECONDS * frameDelta;
    }

    /**
     * Code which should be run on game startup.
     * Checks for potential enemy candidates.
     */
    @Override
    public void start(final WorldEntities world) {
        loadedEnemies = findLoadedEnemies();
        balance = START_BALANCE;
    }

    /**
     * Generates a wave, with a specific size.
     * @param enemyAmount the amount of enemies which should be in the wave.
     * @return a list of {@link IEnemyCreator}'s. Which make up the wave.
     */
    private List<IEnemyCreator> generateWave(final int enemyAmount) {
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
     * Finds the cost of a wave.
     * @param wave the wave which we want to find the cost of.
     * @return the total cost of a wave.
     */
    private float findCostOfWave(final List<IEnemyCreator> wave) {
        float totalCost = 0;

        for (IEnemyCreator enemy : wave) {
            totalCost += enemy.getEnemyCost();
        }

        return totalCost;
    }

    /**
     * Gets all instances of classes which implements {@link IEnemyCreator}.
     * @return  A List populated with found {@link IEnemyCreator}'s.
     * List can be empty if no IEnemyCreator implementations are found.
     */
    private Set<IEnemyCreator> findLoadedEnemies() {
        Set<IEnemyCreator> enemies = new HashSet<>();
        for (IEnemyCreator enemy : ServiceLoader.load(IEnemyCreator.class)) {
            enemies.add(enemy);
        }

        if (enemies.isEmpty()) {
            System.err.println("Warning: No Enemies Loaded,"
                    + " spawner will not work.");
        }

        return enemies;
    }
}
