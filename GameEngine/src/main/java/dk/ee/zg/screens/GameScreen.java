package dk.ee.zg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dk.ee.zg.UI.HUD;
import dk.ee.zg.boss.ranged.Projectile;
import dk.ee.zg.common.data.EventManager;
import dk.ee.zg.common.data.Events;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.data.KeyAction;
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
     * Value if game is running.
     */
    static final int GAME_RUNNING = 0;
    /**
     * Value if game is paused.
     */
    static final int GAME_PAUSED = 1;

    /**
     * State for checking if game is paused or running.
     */
    private int state;
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
     * Skin used for styling UI Elements in the stage.
     */
    private Skin skin;

    /**
     * Window shown when pausing.
     */
    private Window pauseWindow;

    /**
     * Constructor for GameScreen.
     * Instantiates required values for the rest of the class.
     */
    public GameScreen() {
        gameData = GameData.getInstance();
        debugHitboxRenderer = new ShapeRenderer();
        worldEntities = new WorldEntities();
        worldObstacles = new WorldObstacles();

        setupStage();
        createPauseWindow();
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
        switch (state) {
            case GAME_RUNNING:
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
                worldObstacles.optimizeObstacles();
                camera.update();
                if (gameData.getGameKey().isPressed((gameData.getGameKey()
                    .getActionToKey().get(KeyAction.PAUSE)))) {
                    pause();
                }
                break;
            case GAME_PAUSED:
                if (gameData.getGameKey().isPressed((gameData.getGameKey()
                        .getActionToKey().get(KeyAction.PAUSE)))) {
                    resume();
                }
                break;
            default:
                break;
        }
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

        if (gameData.isDebug()) {
            debugDraw();
        }
        // Required for input events and tooltips to be processed.
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void debugDraw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        debugHitboxRenderer.setProjectionMatrix(camera.combined);
        debugHitboxRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Entity entity : worldEntities.getEntities()) {
            if (entity.getHitbox() != null) {
                debugHitboxRenderer.setColor(1f, 0f, 0f, 0.8f);
                Rectangle rectangle = entity.getHitbox();
                debugHitboxRenderer.rect(rectangle.x,
                        rectangle.y,
                        rectangle.width,
                        rectangle.height);
            }
        }

        for (Rectangle obstacle : worldObstacles.getObstacles()) {
            debugHitboxRenderer.setColor(1f, 0.5f, 0f, 0.8f);
            debugHitboxRenderer.rect(obstacle.x,
                    obstacle.y,
                    obstacle.width,
                    obstacle.height);
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
        if (state == GAME_RUNNING) {
            state = GAME_PAUSED;
        }
        pauseWindow.setVisible(true);
        hud.stopStartTimer();
    }

    /**
     * Automatically executed when game is resumed.
     */
    @Override
    public void resume() {
        if (state == GAME_PAUSED) {
            state = GAME_RUNNING;
        }
        pauseWindow.setVisible(false);
        hud.stopStartTimer();
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

    private void createPauseWindow() {
        Window pause = new Window("", skin);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("new");
        pause.getTitleLabel().setSize(Gdx.graphics.getWidth(), pause.getRowHeight(0));
        pause.getTitleLabel().setPosition(0,Gdx.graphics.getHeight()-pause.getRowHeight(0)*2);
        pause.getTitleLabel().setAlignment(Align.center);
        pause.getTitleLabel().setText("Paused");
        pause.getTitleLabel().setStyle(labelStyle);
        pause.getTitleTable().setSize(Gdx.graphics.getWidth(), 100);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable("default-round");  // or whatever drawable you want
        buttonStyle.down = skin.getDrawable("default-round-down");
        buttonStyle.font = skin.getFont("new");

        TextButton continueButton = new TextButton("Continue", buttonStyle);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event,
                                final float x, final float y) {
                resume();
                pause.setVisible(false);
            }
        });
        pause.add(continueButton).row();
        pause.row().pad(20f);

        pause.add(new TextButton("Exit", buttonStyle)).row();

        pause.setSize(stage.getWidth() / 3f, stage.getHeight() / 3f);
        pause.setPosition(stage.getWidth() / 2 - pause.getWidth() / 2,
                stage.getHeight() / 2 - pause.getHeight() / 2);
        pause.setResizable(false);
        pause.setMovable(false);
        pause.setVisible(false);
        pause.setZIndex(1000);
        stage.addActor(pause);
        pauseWindow = pause;
    }


    private void setupStage() {
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
        TextureAtlas atlas = new TextureAtlas(
                new FileHandle("GameEngine/src/main/resources/"
                        + "skin/uiskin.atlas"));

        skin = new Skin(new FileHandle("GameEngine/src/main/resources/"
                + "skin/uiskin.json"), atlas);
        loadFonts(skin);
        if (!ItemManager.getInstance().getLoadedItems().isEmpty()) {
            EventManager.addListener(Events.PlayerLevelUpEvent.class, event -> {
                LevelUpPopup levelUpPopup = new LevelUpPopup("Level Up!", skin);
                levelUpPopup.animateShow(stage);
            });
        }
    }
    /**
     *
     * Loads fonts for use with various text objects in the scene.
     * Fonts are added to the local instance of {@link Skin}
     *
     * @param baseSkin instance which fonts should be saved to.
     */
    private void loadFonts(final Skin baseSkin) {
        FreeTypeFontGenerator fontGenerator;
        fontGenerator = new FreeTypeFontGenerator(
                new FileHandle("GameEngine/src/main/resources"
                        + "/CinzelDecorative-Regular.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter regularParams
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        regularParams.size = 20;

        BitmapFont font = fontGenerator.generateFont(regularParams);

        baseSkin.add("new", font, BitmapFont.class);

        fontGenerator = new FreeTypeFontGenerator(
                new FileHandle("GameEngine/src/main/resources"
                        + "/CinzelDecorative-Bold.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter boldParams
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        boldParams.size = 20;

        BitmapFont boldFont = fontGenerator.generateFont(boldParams);

        baseSkin.add("new", boldFont, BitmapFont.class);

        fontGenerator.dispose();
    }

}
