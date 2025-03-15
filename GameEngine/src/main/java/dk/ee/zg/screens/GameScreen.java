package dk.ee.zg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.map.data.World;
import dk.ee.zg.common.map.interfaces.IMap;
import dk.ee.zg.common.map.services.IEntityProcessService;
import dk.ee.zg.common.map.services.IGamePluginService;

import java.util.ServiceLoader;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    /**
     * Instance of the singleton class {@link GameData}.
     */
    private final GameData gameData;

    /**
     * Instance of {@link World} used for interacting with entities.
     */
    private final World world;

    /**
     * Instance of {@link OrthographicCamera} used by the game.
     * Is also saved to {@link GameData}
     */
    private final OrthographicCamera camera;

    /**
     * Instance of {@link Viewport} used by the camera.
     */
    private final Viewport viewport;

    /**
     * Instance of {@link IMap} currently loaded.
     */
    private IMap map;

    /**
     * Instance of {@link SpriteBatch} used for drawing sprites to the screen.
     */
    private SpriteBatch batch;

    /**
     * Constructor for GameScreen.
     * Instantiates required values for the rest of the class.
     */
    public GameScreen() {
        gameData = GameData.getInstance();
        world = new World();
        camera = gameData.getCamera();
        viewport = gameData.getViewport();
    }


    /**
     * Automatically executed when screen is shown.
     * Calls setup methods for the world, and entities in the world.
     */
    @Override
    public void show() {
        batch = new SpriteBatch(); // Create SpriteBatch

        for (IGamePluginService entity : ServiceLoader.load(IGamePluginService.class)) {
            entity.start(world);
        }

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
                map.loadMap(mapPath, gameData.getUNIT_SCALE());
            }
        }
    }

    private void checkBounds() {
        float effectiveViewportWidth = camera.viewportWidth / 2;
        float effectiveViewportHeight = camera.viewportWidth / 2;

        // Left boundary
        if (camera.position.x < effectiveViewportWidth) {
            camera.position.x = effectiveViewportWidth;
        }

        // Right boundary
        if (camera.position.x > 960 * gameData.getUNIT_SCALE() - effectiveViewportWidth) {
            camera.position.x = 960 * gameData.getUNIT_SCALE() - effectiveViewportWidth;
        }

        // Bottom boundary
        if (camera.position.y < effectiveViewportHeight) {
            camera.position.y = effectiveViewportHeight;
        }

        // Top boundary
        if (camera.position.y > 960 * gameData.getUNIT_SCALE() - effectiveViewportHeight) {
            camera.position.y = 960 * gameData.getUNIT_SCALE() - effectiveViewportHeight;
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
        update();
        draw();

        GameData.getInstance().getGameKey().checkJustPressed();
    }

    /**
     * Update loop, part of the main game loop. Is called before {@code draw()}.
     * Handles logic related to updating the game.
     */
    private void update() {
        for (IEntityProcessService entity : ServiceLoader.load(IEntityProcessService.class)) {
            entity.process(world);
        }
        for (Entity entity : world.getEntities()) {
            if (entity.getEntityType() == EntityType.Player) {
                camera.position.set(entity.getPosition().x + entity.getSprite().getWidth() / 2, entity.getPosition().y + entity.getSprite().getHeight() / 2, 0);
                checkBounds();
            }
        }
        camera.update();
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
        for (Entity entity : world.getEntities()) {
            entity.draw(batch);
        }
        batch.end(); // End drawing
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
