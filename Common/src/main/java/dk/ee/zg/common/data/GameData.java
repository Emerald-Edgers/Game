package dk.ee.zg.common.data;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameData {
    private int displayHeight;
    private int displayWidth;
    private int scaleFactor;
    private int maxFps;
    private float deltaTime;
    private final GameKey gameKey = new GameKey();
    private OrthographicCamera camera;
    private static GameData instance;

    private GameData() {}

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

    public int getMaxFps() {
        return maxFps;
    }

    public void setMaxFps(int maxFps) {
        this.maxFps = maxFps;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

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
}
