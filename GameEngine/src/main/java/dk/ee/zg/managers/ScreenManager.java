package dk.ee.zg.managers;

import com.badlogic.gdx.Screen;

import java.util.HashMap;
import java.util.ServiceLoader;

public class ScreenManager {
    private Screen activeScreen;
    private HashMap<String, Screen> screens; // Use class name for screen name

    public ScreenManager() {
        init();
    }

    /**
     * Finds all classes which implement screen, and adds them to screens
     */
    private void init() {
        screens = new HashMap<>();
        for (Screen screen : ServiceLoader.load(Screen.class)) {
            screens.put(screen.getClass().getSimpleName().toLowerCase(), screen);
        }
    }

    public void switchScreen(String screenName) {
        Screen tempScreen = screens.get(screenName.toLowerCase());
        if (tempScreen == null) {
            //TODO: Implement error for wrong screen key
        }

        activeScreen = tempScreen;
    }

    public Screen getActiveScreen() {
        return activeScreen;
    }

    public HashMap<String, Screen> getScreens() {
        return screens;
    }
}
