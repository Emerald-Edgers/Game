package dk.ee.zg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import dk.ee.zg.managers.InputManager;
import dk.ee.zg.managers.ScreenManager;
import dk.ee.zg.screens.GameScreen;

public class Game extends com.badlogic.gdx.Game {
    /**
     * Game initialization logic, runs once on game start
     */
    @Override
    public void create() {

        Gdx.input.setInputProcessor(new InputManager());

        Screen screen = new GameScreen();
        ScreenManager screenManager = new ScreenManager();
        screenManager.switchScreen(screen.getClass().getSimpleName());
        this.setScreen(screenManager.getActiveScreen());
    }
}