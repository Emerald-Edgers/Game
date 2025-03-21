package dk.ee.zg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.enemy.interfaces.IEnemySpawner;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.data.WorldObstacles;
import dk.ee.zg.common.map.interfaces.IMap;
import dk.ee.zg.common.map.services.ICollisionEngine;
import dk.ee.zg.common.map.services.IEntityProcessService;
import dk.ee.zg.common.map.services.IGamePluginService;

import java.util.Optional;
import java.util.ServiceLoader;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    /**
     * Instance of the singleton class {@link GameData}.
     */
    private final GameData gameData;

    /**
     * Instance of {@link WorldEntities} used for interacting with entities.
     */
    private final WorldEntities worldEntities;

    /**
     * Instance of {@link WorldObstacles}
     * used for interacting with world obstacles.
     */
    private final WorldObstacles worldObstacles;

    /**
     * Instance of {@link OrthographicCamera} used by the game.
     * Is also saved to {@link GameData}
     */
    private OrthographicCamera camera;

    /**
     * Instance of {@link Viewport} used by the camera.
     */
    private Viewport viewport;

    /**
     * Instance of {@link IMap} currently loaded.
     */
    private IMap map;

    /**
     * Instance of {@link dk.ee.zg.common.enemy.interfaces.IEnemySpawner} currently loaded.
     */
    private IEnemySpawner enemySpawner;

    /**
     * Instance of {@link SpriteBatch} used for drawing sprites to the screen.
     */
    private SpriteBatch batch;

    /**
     * The width of the viewport in world units.
     * This is how much of the x-axis the player should see at once.
     */
    private static final float VIEWPORT_WIDTH = 8;

    /**
     * The height of the viewport in world units.
     * This is how much of the y-axis the player should see at once.
     */
    private static final float VIEWPORT_HEIGHT = 8;

    /**
     * The amount of pixels a singular unit represents.
     * (E.g.) set to 1/32, 1 unit = 32 px.
     */
    private static final float UNIT_SCALE = 1 / 32f;

    /**
     * The width of the map in pixels.
     * Should eventually be capable of calculating this based upon the map.
     */
    private static final int MAP_WIDTH_PIXELS = 100 * 16;

    /**
     * The height of the map in pixels.
     * Should eventually be capable of calculating this based upon the map.
     */
    private static final int MAP_HEIGHT_PIXELS = 150 * 16;

    /**
     * Constructor for GameScreen.
     * Instantiates required values for the rest of the class.
     */
    public GameScreen() {
        gameData = GameData.getInstance();
        worldEntities = new WorldEntities();
        worldObstacles = new WorldObstacles();
        Entity e1 = new Entity(new Vector2(2, 0),
                0, new Vector2(0.1F, 0.1F),
                "placeholder32x32.png", EntityType.Enemy);
        worldEntities.addEntity(e1);
    }


    /**
     * Automatically executed when screen is shown.
     * Calls setup methods for the world, and entities in the world.
     */
    @Override
    public void show() {
        batch = new SpriteBatch(); // Create SpriteBatch

        for (IGamePluginService entity
                : ServiceLoader.load(IGamePluginService.class)) {
            entity.start(worldEntities);
        }

        initCamera();
        initSpawner();
        initMap("main-map.tmx");
    }

    /**
     * Loads the first implementation of {@link IMap} that the
     * {@link ServiceLoader} finds.
     * Sets the local variable {@code map} to found implementation.
     *
     * @param mapPath The name of the map. Use relative path from the
     *                resource folder of implementing class.
     */
    private void initMap(final String mapPath) {
        for (IMap mapImpl : ServiceLoader.load(IMap.class)) {
            if (map == null) {
                map = mapImpl;
                map.loadMap(mapPath, UNIT_SCALE);
            }
        }
    }

    /**
     * Loads the first implementation of {@link IEnemySpawner} that the
     * {@link ServiceLoader} finds.
     * Sets the local variable {@code enemySpawner} to the found implementation.
     */
    private void initSpawner() {
        ServiceLoader<IEnemySpawner> spawnerLoader = ServiceLoader.load(IEnemySpawner.class);

        enemySpawner = spawnerLoader.findFirst().orElse(null);

        if (enemySpawner != null) {
            enemySpawner.start();
        }
    }

    /**
     * Creates an {@link OrthographicCamera}
     * and a {@link com.badlogic.gdx.utils.viewport.FitViewport} to
     * manage the game rendering area.
     * The viewport ensures that the visible game world is consistent across
     * aspect ratios, will draw black bars to achieve this.
     */
    private void initCamera() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        viewport.apply();

        // Set the camera to the middle of the screen
        camera.position.set(VIEWPORT_WIDTH / 2f, VIEWPORT_HEIGHT / 2f, 0);
        camera.update();

        gameData.setCamera(camera);
    }

    private void checkBounds() {
        float effectiveViewportWidth = camera.viewportWidth / 2;
        float effectiveViewportHeight = camera.viewportWidth / 2;

        // Left boundary
        if (camera.position.x < effectiveViewportWidth) {
            camera.position.x = effectiveViewportWidth;
        }

        // Right boundary
        if (camera.position.x
                > MAP_WIDTH_PIXELS * UNIT_SCALE - effectiveViewportWidth) {
            camera.position.x =
                    MAP_WIDTH_PIXELS * UNIT_SCALE - effectiveViewportWidth;
        }

        // Bottom boundary
        if (camera.position.y < effectiveViewportHeight) {
            camera.position.y = effectiveViewportHeight;
        }

        // Top boundary
        if (camera.position.y
                > MAP_HEIGHT_PIXELS * UNIT_SCALE - effectiveViewportHeight) {
            camera.position.y =
                    MAP_HEIGHT_PIXELS * UNIT_SCALE - effectiveViewportHeight;
        }

        camera.update();
    }

    /**
     * Main game loop. Calls {@code update()} & {@code draw()} every frame.
     *
     * @param v The delta-time of the current frame.
     */
    @Override
    public void render(final float v) {
        update(v);
        draw();
        collisionCheck();
        GameData.getInstance().getGameKey().checkJustPressed();
    }

    /**
     * Update loop, part of the main game loop. Is called before {@code draw()}.
     * Handles logic related to updating the game.
     * @param v The delta-time of the current frame.
     */
    private void update(final float v) {
        for (IEntityProcessService entity
                : ServiceLoader.load(IEntityProcessService.class)) {
            entity.process(worldEntities);
        }

        enemySpawnerUpdate(v);

        for (Entity entity : worldEntities.getEntities()) {
            if (entity.getEntityType() == EntityType.Player) {
                float cameraX = entity.getPosition().x
                        + entity.getSprite().getWidth() / 2;
                float cameraY = entity.getPosition().y
                        + entity.getSprite().getHeight() / 2;
                camera.position.set(cameraX, cameraY, 0);
                checkBounds();
            }
        }
        camera.update();
    }

    /**
     * Handles the update of the spawner.
     * Runs without error if no spawners were found.
     * @param v The delta-time of the current frame.
     */
    private void enemySpawnerUpdate(final float v) {
        if (enemySpawner != null) {
            enemySpawner.process(v);
        }
    }

    /**
     * Draw loop, part of the main game loop. Is after {@code update()}.
     * Handles logic regarding drawing the scene.
     */
    private void draw() {
        // Set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        if (map != null) {
            map.renderMap(); // Render the map
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin(); // Begin drawing
        for (Entity entity : worldEntities.getEntities()) {
            entity.draw(batch);
        }
        batch.end(); // End drawing
    }


    /**
     * checks collision by processing collision engine with service loader.
     */
    private void collisionCheck() {
        Optional<ICollisionEngine> collisionEngine =
                ServiceLoader.load(ICollisionEngine.class).findFirst();
        collisionEngine.ifPresent(iCollisionEngine ->
                iCollisionEngine.process(worldEntities, worldObstacles));
    }


    /**
     * Automatically executed when a window resize occurs.
     *
     * @param width  The new width of the window in pixels.
     * @param height The new height of the window in pixels.
     */
    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height, true);
        gameData.setDisplayWidth(width);
        gameData.setDisplayHeight(height);
    }

    /**
     * Automatically executed when game is paused.
     */
    @Override
    public void pause() {

    }

    /**
     * Automatically executed when game is resumed.
     */
    @Override
    public void resume() {

    }

    /**
     * Automatically executed when window is minimized.
     */
    @Override
    public void hide() {

    }

    /**
     * Automatically called when screen is terminated.
     * Should free up resources from memory.
     */
    @Override
    public void dispose() {
        batch.dispose();
        map.getRenderer().dispose();
    }
}
