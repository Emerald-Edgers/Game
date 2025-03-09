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

        //Launch game in fullscreen mode
        config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

        // Launch Game in windowed fullscreen
       //config.setWindowedMode(Lwjgl3ApplicationConfiguration.getDisplayMode().width, Lwjgl3ApplicationConfiguration.getDisplayMode().height);

        // Launch the Game in 1080p
//        config.setWindowedMode(1920, 1080);

        config.setForegroundFPS(gameData.getMaxFps());
        config.setIdleFPS(gameData.getMaxFps());

        return config;
    }

    private static void initOptions() {
        GameData gameData = GameData.getInstance();
        gameData.setMaxFps(30);
    }
}
