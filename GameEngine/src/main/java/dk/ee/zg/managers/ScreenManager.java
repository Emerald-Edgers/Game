package dk.ee.zg.managers;

import com.badlogic.gdx.Screen;

import java.util.HashMap;
import java.util.ServiceLoader;

public class ScreenManager {
    /**
     * The currently active screen.
     */
    private Screen activeScreen;

    /**
     * A map of all the registered screens.
     */
    private HashMap<String, Screen> screens; // Use class name for screen name

    /**
     * Constructor: Starts the screen manager and populates the screens map.
     */
    public ScreenManager() {
        init();
    }

    /**
     * Finds all classes which implement screen, and adds them to screens.
     */
    private void init() {
        screens = new HashMap<>();
        for (Screen screen : ServiceLoader.load(Screen.class)) {
            screens.put(
                    screen.getClass().getSimpleName().toLowerCase(), screen);
        }
    }

    /**
     * Changes the currently active screen to one of the given name.
     * @param screenName    The class name of the screen to switch too.
     */
    public final void switchScreen(final String screenName) {
        Screen tempScreen = screens.get(screenName.toLowerCase());
        if (tempScreen == null) {
            System.err.println("Screen " + screenName + " not found");
        }

        activeScreen = tempScreen;
    }

    public final Screen getActiveScreen() {
        return activeScreen;
    }

    public final HashMap<String, Screen> getScreens() {
        return screens;
    }
}
