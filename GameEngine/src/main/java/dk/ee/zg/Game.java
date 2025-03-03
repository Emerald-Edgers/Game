package dk.ee.zg;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;


public class Game implements ApplicationListener {
    @Override
    public void create() {
        // Game initialization logic
    }

    @Override
    public void render() {
        // Game rendering logic
    }

    @Override
    public void resize(int width, int height) {
        // Handle resizing
    }

    @Override
    public void pause() {
        // Pause logic
    }

    @Override
    public void resume() {
        // Resume logic
    }

    @Override
    public void dispose() {
        // Clean-up resources
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Emerald Edgers");
        config.setWindowedMode(800,600);
        new Lwjgl3Application(new Game(), config);
    }
}