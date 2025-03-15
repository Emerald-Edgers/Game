package dk.ee.zg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dk.ee.zg.managers.InputManager;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.managers.ScreenManager;

public class Game extends com.badlogic.gdx.Game {
    private final GameData gameData = GameData.getInstance();

    /**
     * Game initialization logic, runs once on game start
     */
    @Override
    public void create() {
        Gdx.input.setInputProcessor(new InputManager());

        // GameData
        gameData.setGame(this);
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        OrthographicCamera camera = new OrthographicCamera();
        gameData.setCamera(camera);

        Viewport viewport = new FitViewport(
                gameData.getVIEWPORT_WIDTH(),
                gameData.getVIEWPORT_HEIGHT(),
                camera
        );
        viewport.apply();
        gameData.setViewport(viewport);

        // Set active Screen
        ScreenManager screenManager = new ScreenManager();
        String SCREEN_NAME = "startscreen";
        screenManager.switchScreen(SCREEN_NAME);
        this.setScreen(screenManager.getActiveScreen());
    }
}