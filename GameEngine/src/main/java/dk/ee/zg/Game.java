package dk.ee.zg;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Game implements ApplicationListener {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Emeral Edgers - Zesty Gorillaz");
        new Lwjgl3Application(new Game(), config);
    }

    @Override
    public void create() {
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int i, int i1) {

    }
}
