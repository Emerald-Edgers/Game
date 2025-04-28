package dk.ee.zg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dk.ee.zg.UI.HUD;
import dk.ee.zg.boss.ranged.Projectile;
import dk.ee.zg.common.data.EventManager;
import dk.ee.zg.common.data.Events;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.enemy.interfaces.IEnemyNetwork;
import dk.ee.zg.common.enemy.interfaces.IEnemySpawner;
import dk.ee.zg.common.enemy.interfaces.IPathFinder;
import dk.ee.zg.common.item.ItemManager;
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
import dk.ee.zg.popups.LevelUpPopup;
import org.lwjgl.opengl.GL20;

public class GameScreen implements Screen {
    /**
     * Instance of the singleton class {@link GameData}.
     */
    private final GameData gameData;

    /**
     * Instance of the stage being used on this screen.
     * Used for adding pop up.
     */
    private Stage stage;

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
     * Instance of
     * {@link dk.ee.zg.common.enemy.interfaces.IEnemySpawner} currently loaded.
     */
    private IEnemySpawner enemySpawner;

    /**
     * Instance of {@link SpriteBatch} used for drawing sprites to the screen.
     */
    private SpriteBatch batch;

    /**
     * Instance of {@link ShapeRenderer} to use for drawing
     * hitboxes during debug mode.
     */
    private ShapeRenderer debugHitboxRenderer;

    /**
     * The width of the viewport in world units.
     * This is how much of the x-axis the player should see at once.
     */

    private static final float VIEWPORT_WIDTH = 15;

    /**
     * The height of the viewport in world units.
     * This is how much of the y-axis the player should see at once.
     */

    private static final float VIEWPORT_HEIGHT = 15;

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
     * the HUD object for heads up display.
     */
    private final HUD hud = new HUD();

    /**
     * Constructor for GameScreen.
     * Instantiates required values for the rest of the class.
     */
    public GameScreen() {
        gameData = GameData.getInstance();
        debugHitboxRenderer = new ShapeRenderer();
        worldEntities = new WorldEntities();
        worldObstacles = new WorldObstacles();
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

        //Changes input to accept UI input. Stage must not get keyboard focus
        // in order to move player while pop up is showing.
        stage = new Stage(
                new FitViewport(
                        GameData.getInstance().getDisplayWidth(),
                        GameData.getInstance().getDisplayHeight()));
        stage.setKeyboardFocus(null);
        stage.setScrollFocus(null);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(Gdx.input.getInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);

        if (!ItemManager.getInstance().getLoadedItems().isEmpty()) {
            EventManager.addListener(Events.PlayerLevelUpEvent.class, event -> {
                TextureAtlas atlas = new TextureAtlas(
                        new FileHandle("GameEngine/src/main/resources/"
                                + "skin/uiskin.atlas"));

                LevelUpPopup levelUpPopup = new LevelUpPopup("Level Up!",
                        new Skin(
                                new FileHandle(
                                        "GameEngine/src/main/resources/"
                                                + "skin/uiskin.json"), atlas));
                levelUpPopup.animateShow(stage);
            });
        }
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
                map.loadMap(mapPath, UNIT_SCALE,
                        worldObstacles, new TmxMapLoader());
            }
        }
        for (IPathFinder pathFinder : ServiceLoader.load(IPathFinder.class)) {
            pathFinder.load(worldObstacles, map.getMap());
        }

        for (IEnemyNetwork enemyNetwork :
                ServiceLoader.load(IEnemyNetwork.class)) {
            enemyNetwork.buildNetwork();
        }
    }

    /**
     * Loads the first implementation of {@link IEnemySpawner} that the
     * {@link ServiceLoader} finds.
     * Sets the local variable {@code enemySpawner} to the found implementation.
     */
    private void initSpawner() {
        ServiceLoader<IEnemySpawner> spawnerLoader
                = ServiceLoader.load(IEnemySpawner.class);

        enemySpawner = spawnerLoader.findFirst().orElse(null);

        if (enemySpawner != null) {
            enemySpawner.start(worldEntities);
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
        hud.drawHUD(batch, worldEntities);
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

            entity.update(v);

            if (entity instanceof Projectile) {
                ((Projectile) entity).update();
            }
            if (entity.getEntityType() == EntityType.Player) {
                float cameraX = entity.getPosition().x;
                float cameraY = entity.getPosition().y;
                camera.position.set(cameraX, cameraY, 0);
                checkBounds();
            }
        }
        //TODO optimize optimizeObstaclces to get called at a fixed interval.
        worldObstacles.optimizeObstacles();
        camera.update();
    }

    /**
     * Handles the update of the spawner.
     * Runs without error if no spawners were found.
     * @param v The delta-time of the current frame.
     */
    private void enemySpawnerUpdate(final float v) {
        if (enemySpawner != null) {
            enemySpawner.process(v, worldEntities);
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

        batch.setProjectionMatrix(camera.combined);

        if (map != null) {
            map.renderBottom();
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin(); // Begin drawing

        for (Entity entity : worldEntities.getEntities()) {
            entity.draw(batch);
        }

        batch.end(); // End drawing
      
        if (map != null) {
            map.renderTop();
        }
        // Required for input events and tooltips to be processed.
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (gameData.isDebug()) {
            debugDraw();
        }
    }

    private void debugDraw(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        debugHitboxRenderer.setProjectionMatrix(camera.combined);
        debugHitboxRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Entity entity : worldEntities.getEntities()) {
            if (entity.getHitbox() != null) {
                debugHitboxRenderer.setColor(1f, 0f, 0f, 0.8f);
                Rectangle rectangle = entity.getHitbox();
                debugHitboxRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }
        }

        for (Rectangle obstacle : worldObstacles.getObstacles()) {
            debugHitboxRenderer.setColor(1f, 0.5f, 0f, 0.8f);
            debugHitboxRenderer.rect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        debugHitboxRenderer.end();
        debugHitboxRenderer.flush();

        Gdx.gl.glDisable(GL20.GL_BLEND);
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
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        if (debugHitboxRenderer != null) {
            debugHitboxRenderer.dispose();
        }
    }

}
