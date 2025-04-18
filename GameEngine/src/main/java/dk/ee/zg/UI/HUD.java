package dk.ee.zg.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.player.Player;

import java.util.Optional;

public class HUD {
    /**
     * camera to draw on.
     */
    private OrthographicCamera camera;
    /**
     * renderer to draw shapes.
     */
    private ShapeRenderer shapeRenderer;
    /**
     * width of the screen.
     */
    private static final int SCREEN_WIDTH =
            GameData.getInstance().getDisplayWidth();
    /**
     * height of the screen.
     */
    private static final int SCREEN_HEIGHT =
            GameData.getInstance().getDisplayHeight();
    /**
     * player instance, used for HUD.
     */
    private Player player;
    /**
     * font for lvl text.
     */
    private final BitmapFont lvlFont = generateFont(Color.WHITE, 64);;
    /**
     * font for timer text.
     */
    private final BitmapFont timerFont = generateFont(Color.WHITE, 64);;
    /**
     * elapsed time in game.
     */
    private float elapsedTimeInSecs;



    /**
     * constructor, sets up camera and renderer.
     */
    public HUD() {
        camera = new OrthographicCamera();
        // HUD is fixed to screen and starts from top left.
        camera.setToOrtho(true, SCREEN_WIDTH, SCREEN_HEIGHT);
        shapeRenderer = new ShapeRenderer();


    }

    /**
     * Draw HUD method.
     * Handles logic regarding drawing the HUD.
     * @param batch batch to draw on.
     * @param worldEntities world entities to get player from.
     */
    public void drawHUD(final SpriteBatch batch,
                        final WorldEntities worldEntities) {
        //get player instance if null
        if (player == null) {
            Optional<Player> optionalPlayer =
                    worldEntities.getEntityByClass(Player.class);
            if (optionalPlayer.isPresent()) {
                player = optionalPlayer.get();
            }
        }

        // set projection matrixes
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        drawItemsEquippedBar();
        drawHPBar();
        drawXPBar(batch);
        drawTimer(batch);


    }

    /**
     * draw method for items equipped Bar.
     */
    private void drawItemsEquippedBar() {

    }

    /**
     * draw method for HP Bar.
     */
    private void drawHPBar() {
        float maxHP = player.getMaxHP();
        float currentHP = player.getHp();

        float barWidth = 320;
        float barHeight = 20;
        float x = 60;
        float y = 60;

        // Background (gray)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, barWidth, barHeight);

        // Foreground (red, scaled to %)
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, (currentHP / maxHP) * barWidth, barHeight);
        shapeRenderer.end();

    }

    /**
     * draw method for XP Bar.
     * @param batch spritebatch to draw on.
     */
    private void drawXPBar(final SpriteBatch batch) {
        float currentXP = player.getExperience();
        float barHeight = 10;
        float x = 0;
        float y = 0;

        // Background (gray)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x, y, SCREEN_WIDTH, barHeight);

        // Foreground (red, scaled to %)
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(x, y, (currentXP / SCREEN_WIDTH), barHeight);
        shapeRenderer.end();

        //text
        batch.begin();
        lvlFont.draw(batch, "LVL: " + player.getLevel(),
                (SCREEN_WIDTH / 2f) - 64, (SCREEN_HEIGHT / 2f) + 64);
        batch.end();
    }


    /**
     * draw method for Timer.
     * @param batch spritebatch to draw on.
     */
    private void drawTimer(final SpriteBatch batch) {
        // Update timer
        elapsedTimeInSecs += Gdx.graphics.getDeltaTime();

        // Format time
        int minutes = (int) (elapsedTimeInSecs / 60);
        int seconds = (int) (elapsedTimeInSecs % 60);
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);

        // Start drawing
        batch.begin();
        timerFont.draw(batch, timeFormatted, (SCREEN_WIDTH / 2f) - 64, 40);
        batch.end();
    }

    /**
     * method for generating a BitmapFont from color and size.
     * @param color - color to use.
     * @param size - size in pixels.
     * @return returns BitmapFont.
     */
    private BitmapFont generateFont(final Color color, final int size) {
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(
                        Gdx.files.internal("fonts/Blacknorth.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size; // Font size in pixels
        parameter.color = color; // Font color
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        parameter.flip = true;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }
}
