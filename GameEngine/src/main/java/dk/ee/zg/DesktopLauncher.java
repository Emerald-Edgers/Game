package dk.ee.zg;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import dk.ee.zg.common.data.GameData;

public class DesktopLauncher {
    public static void main(String[] args) {
        new Lwjgl3Application(new Game(), init());
    }

    private static Lwjgl3ApplicationConfiguration init() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        GameData gameData = GameData.getInstance();
        initOptions();
        config.setTitle("Emerald Edgers");
        config.setWindowedMode(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        config.setForegroundFPS(gameData.getMaxFps());
        config.setIdleFPS(gameData.getMaxFps());
        return config;
    }

    private static void initOptions() {
       GameData gameData = GameData.getInstance();
       gameData.setDisplayWidth(1920);
       gameData.setDisplayHeight(1080);
       gameData.setMaxFps(30);
    }
}
