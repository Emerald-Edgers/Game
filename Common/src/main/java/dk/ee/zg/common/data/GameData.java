package dk.ee.zg.common.data;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameData {
    private int displayHeight;
    private int displayWidth;
    private int scaleFactor;
    private float deltaTime;
    private final GameKey gameKey = new GameKey();
    private static GameData instance;

    /**
     * The currently active Camera.
     * Used for rendering what is on the screen.
     */
    private OrthographicCamera camera;

    /**
     * The currently active viewport.
     * Used for limiting screen size and defining resize behaviour.
     */
    private Viewport viewport;

    /**
     * The currently active Game
     * Should primarily be used to call methods located on the
     * {@link Game} class.
     */
    private Game game;

    /**
     * The width of the viewport in world units.
     * This is how much of the x-axis the player should see at once.
     */
    private final float VIEWPORT_WIDTH;

    /**
     * The height of the viewport in world units.
     * This is how much of the y-axis the player should see at once.
     */
    private final float VIEWPORT_HEIGHT;

    /**
     * The amount of pixels a singular unit represents.
     * (E.g.) set to 1/32, 1 unit = 32 px.
     */
    private final float UNIT_SCALE;

    /**
     * The maximum fps the game should run at.
     */
    private final int MAXFPS;

    private GameData() {
        VIEWPORT_WIDTH = 8;
        VIEWPORT_HEIGHT = 8;
        UNIT_SCALE = 1 / 32f;
        MAXFPS = 30;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getMAXFPS() {
        return MAXFPS;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    /**
     * Gets the camera instance.
     *
     * @return the {@link OrthographicCamera} instance.
     */
    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * Sets the camera instance.
     *
     * @param camera the {@link OrthographicCamera} instance to set.
     */
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    /**
     * Gets the viewport instance.
     *
     * @return  the viewport instance.
     */
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * Sets the viewport instance.
     * @param viewport instance to set.
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public GameKey getGameKey() {
        return gameKey;
    }

    public static GameData getInstance() {
        if (instance == null) {
            instance = new GameData();
        }
        return instance;
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(int scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    /**
     * Gets the current game instance.
     *
     * @return the game instance
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the game instance.
     *
     * @param game the game instance to set
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Gets the viewport width.
     *
     * @return the viewport width
     */
    public float getVIEWPORT_WIDTH() {
        return VIEWPORT_WIDTH;
    }

    /**
     * Gets the viewport height.
     *
     * @return the viewport height
     */
    public float getVIEWPORT_HEIGHT() {
        return VIEWPORT_HEIGHT;
    }

    /**
     * Gets the unit scale factor.
     *
     * @return the unit scale factor
     */
    public float getUNIT_SCALE() {
        return UNIT_SCALE;
    }
}
