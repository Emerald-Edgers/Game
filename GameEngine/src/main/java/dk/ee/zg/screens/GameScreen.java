package dk.ee.zg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.World;
import dk.ee.zg.common.map.services.IEntityProcessService;
import dk.ee.zg.common.map.services.IGamePluginService;

import java.util.ServiceLoader;

public class GameScreen implements Screen {

    SpriteBatch batch;
    World world = new World();

    @Override
    public void show() {
        batch = new SpriteBatch(); // Create SpriteBatch

        for (IGamePluginService entity : ServiceLoader.load(IGamePluginService.class)){
            entity.start(world);
        }
    }

    @Override
    public void render(float v) {
        update();
        draw();


        GameData.getInstance().getGameKey().checkJustPressed();
    }

    /**
     * Update loop, part of the main game loop. Is called before {@code draw()}
     */
    private void update() {
        for (IEntityProcessService entity : ServiceLoader.load(IEntityProcessService.class)){
            entity.process(world);
        }
    }

    /**
     *
     */
    private void draw() {
        com.badlogic.gdx.Gdx.gl.glClearColor(0, 0, 0, 1); // Set background color
        com.badlogic.gdx.Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);
        batch.begin(); // Begin drawing

        for (Entity entity : world.getEntities()){
            entity.draw(batch);
        }
        batch.end(); // End drawing

    }

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
