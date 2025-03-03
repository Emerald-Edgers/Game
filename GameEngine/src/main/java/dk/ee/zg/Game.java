package dk.ee.zg;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ServiceLoader;


public class Game implements ApplicationListener {
    private SpriteBatch batch;
    private IEntity player;
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Emerald Edgers");
        config.setWindowedMode(800,600);
        new Lwjgl3Application(new Game(), config);
    }
    @Override
    public void create() {
        // Game initialization logic
        batch = new SpriteBatch();
        ServiceLoader<IEntity> loader = ServiceLoader.load(IEntity.class);
        for (IEntity entity : loader) {
            // Since we know there's only one implementation, we can just take the first one
            player = entity;
            break;
        }
        // Create a player and load a sprite from the game-player module


    }

    @Override
    public void render() {
        // Game rendering logic
        // Clear screen with a color
        com.badlogic.gdx.Gdx.gl.glClearColor(0f, 0f, 0f, 1f); // black background
        com.badlogic.gdx.Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        // Begin drawing with the SpriteBatch
        batch.begin();

        // Draw the player's sprite at position (100, 100)
        if (player != null) {
            player.getSprite().setPosition(100, 100);
            player.getSprite().draw(batch);
        }

        // End drawing
        batch.end();
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


}