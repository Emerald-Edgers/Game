package dk.ee.zg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.data.KeyAction;
import dk.ee.zg.managers.ScreenManager;
import org.lwjgl.opengl.GL20;

public class StartScreen implements Screen {
    /**
     * Instance of the singleton class {@link GameData}.
     */
    private final GameData gameData;

    /**
     * Instance of {@link OrthographicCamera} used by the game.
     * Is also saved to {@link GameData}
     */
    private final OrthographicCamera camera;

    /**
     * Instance of {@link Viewport} used by the camera.
     * Is also saved to {@link GameData}
     */
    private final Viewport viewport;

    /**
     * Constructor for StartScreen.
     * Instantiates required values for the rest of the class.
     */
    public StartScreen() {
        gameData = GameData.getInstance();
        camera = gameData.getCamera();
        viewport = gameData.getViewport();
    }

    /**
     * Automatically executed when screen is shown.
     */
    @Override
    public void show() {

    }


    /**
     * Main Loop of the Start screen.
     *
     * @param v The delta-time of the current frame.
     */
    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (gameData.getGameKey().isDown(gameData.getGameKey().getActionToKey().get(KeyAction.SELECT))) {
            ScreenManager screenManager = new ScreenManager();
            screenManager.switchScreen("GameScreen");
            gameData.getGame().setScreen(screenManager.getActiveScreen());
        }

        camera.update();
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
    }
}
