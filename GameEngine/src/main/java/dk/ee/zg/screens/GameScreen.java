package dk.ee.zg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.World;
import dk.ee.zg.common.map.services.IEntityProcessService;
import dk.ee.zg.common.map.services.IGamePluginService;

import java.util.ServiceLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dk.ee.zg.common.data.GameData;

public class GameScreen implements Screen {
    private final GameData gameData;
    private OrthographicCamera camera;
    private Viewport viewport;

    public GameScreen() {
        gameData = GameData.getInstance();
    }

    SpriteBatch batch;
    World world = new World();

    @Override
    public void show() {
        batch = new SpriteBatch(); // Create SpriteBatch

        for (IGamePluginService entity : ServiceLoader.load(IGamePluginService.class)){
            entity.start(world);
        }
        initCamera(16, 9); // Does not need to be 16:9 !!
    }

    /**
     * Creates an {@link OrthographicCamera} and a {@link FitViewport} to manage the game rendering area.
     * The viewport ensures that the visible game world is consistent across aspect ratios, will draw black bars to achieve this.
     * @param width The width of the viewport in world units.
     *              This is how much of the x-axis the player should see at once.
     * @param height The height of the viewport in world units.
     *               This is how much of the y-axis the player should see at once.
     */
    private void initCamera(float width, float height) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(width, height, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0); // Set the camera to the middle of the screen
        camera.update();

        gameData.setCamera(camera);
    }

    @Override
    public void render(float v) {
        update();
        draw();


        GameData.getInstance().getGameKey().checkJustPressed();
    }

    /**
     * Update loop, part of the main game loop. Is called before {@code draw()}.
     * Handles logic related to updating the game.
     */
    private void update() {
        for (IEntityProcessService entity : ServiceLoader.load(IEntityProcessService.class)){
            entity.process(world);
        }
        camera.update();
    }

    /**
     * Draw loop, part of the main game loop. Is after {@code update()}.
     * Handles logic regarding drawing the scene.
     */
    private void draw() {
        com.badlogic.gdx.Gdx.gl.glClearColor(0, 0, 0, 1); // Set background color
        com.badlogic.gdx.Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);
        batch.begin(); // Begin drawing

        for (Entity entity : world.getEntities()){
            entity.draw(batch);
        }
        batch.end(); // End drawing

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
