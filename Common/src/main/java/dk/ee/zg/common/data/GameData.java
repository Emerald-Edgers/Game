package dk.ee.zg.common.data;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;

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
     * The currently active Game
     * Should primarily be used to call methods located on the
     * {@link Game} class.
     */
    private Game game;

    /**
     * The maximum fps the game should run at.
     */
    private final int MAXFPS;

    private GameData() {
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
}
