package dk.ee.zg;

import com.badlogic.gdx.Gdx;
import dk.ee.zg.managers.InputManager;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.managers.ScreenManager;

public class Game extends com.badlogic.gdx.Game {
    /**
     * Local reference to {@link GameData}.
     */
    private final GameData gameData = GameData.getInstance();

    /**
     * Game initialization logic, runs once on game start.
     */
    @Override
    public void create() {
        Gdx.input.setInputProcessor(new InputManager());

        // GameData
        gameData.setGame(this);
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        // Set active Screen
        ScreenManager screenManager = new ScreenManager();
        String screenName = "startscreen";
        screenManager.switchScreen(screenName);
        this.setScreen(screenManager.getActiveScreen());
    }
}
