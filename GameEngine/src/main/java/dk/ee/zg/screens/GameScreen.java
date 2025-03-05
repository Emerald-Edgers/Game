package dk.ee.zg.screens;

import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        update();
        draw();
    }

    /**
     * Update loop, part of the main game loop. Is called before {@code draw()}
     */
    private void update() {


    }

    /**
     *
     */
    private void draw() {}

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
