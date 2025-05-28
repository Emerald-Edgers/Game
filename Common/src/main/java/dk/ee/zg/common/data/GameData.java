package dk.ee.zg.common.data;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;

public final class GameData {
    /**
     * The current height of the game window in pixels.
     */
    private int displayHeight;

    /**
     * The current width of the game window in pixels.
     */
    private int displayWidth;

    /**
     * The factor of which the game should scale sprites.
     */
    private int scaleFactor;

    /**
     * Time that has passed since last frame.
     */
    private float deltaTime;

    /**
     * Instance of {@link GameKey} to use for keyboard input.
     */
    private final GameKey gameKey = new GameKey();

    /**
     * Instance of {@link GameData}. Makes class a singleton.
     */
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
     * The players max hp.
     */
    private int playerMaxHp = 1;

    /**
     * The players current hp.
     */
    private double playerHp = 1;

    /**
     * The players xp.
     */
    private float playerXp = 1;

    /**
     * The players lvl.
     */
    private int playerLvl = 1;

    /**
     * The maximum fps the game should run at.
     */
    private static final int MAXFPS = 0;

    /**
     * The amount of pixels a singular unit represents.
     * (E.g.) set to 1/32, 1 unit = 32 px.
     */
    private static final float UNIT_SCALE = 1 / 32f;


    /**
     * Debug mode, draws various helper visuals for debugging the game.
     */
    private static final boolean DEBUG_MODE = false;


    private GameData() {

    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(final int newHeight) {
        this.displayHeight = newHeight;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(final int newWidth) {
        this.displayWidth = newWidth;
    }

    public int getMAXFPS() {
        return MAXFPS;
    }

    public float getUNIT_SCALE() {
        return UNIT_SCALE;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(final float newTime) {
        this.deltaTime = newTime;
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
     * @param newCam the {@link OrthographicCamera} instance to set.
     */
    public void setCamera(final OrthographicCamera newCam) {
        this.camera = newCam;
    }

    public GameKey getGameKey() {
        return gameKey;
    }

    /**
     * Gets the current intsance of itself. Makes class a singleton.
     * @return  An instance of {@link GameData}. If none exists creates ones.
     */
    public static GameData getInstance() {
        if (instance == null) {
            instance = new GameData();
        }
        return instance;
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(final int newScale) {
        this.scaleFactor = newScale;
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
     * @param newGame the game instance to set
     */
    public void setGame(final Game newGame) {
        this.game = newGame;
    }

    public boolean isDebug() {
        return DEBUG_MODE;
    }

    public int getPlayerMaxHp() {
        return playerMaxHp;
    }

    public void setPlayerMaxHp(int playerMaxHp) {
        this.playerMaxHp = playerMaxHp;
    }

    public double getPlayerHp() {
        return playerHp;
    }

    public void setPlayerHp(double playerHp) {
        this.playerHp = playerHp;
    }

    public float getPlayerXp() {
        return playerXp;
    }

    public void setPlayerXp(float playerXp) {
        this.playerXp = playerXp;
    }

    public int getPlayerLvl() {
        return playerLvl;
    }

    public void setPlayerLvl(int playerLvl) {
        this.playerLvl = playerLvl;
    }
}
