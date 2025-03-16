package dk.ee.zg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.data.KeyAction;
import dk.ee.zg.common.weapon.Weapon;
import dk.ee.zg.managers.ScreenManager;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class StartScreen implements Screen {
    /**
     * Instance of the singleton class {@link GameData}.
     */
    private final GameData gameData;

    /**
     * Local instance of stage. Contains all ui elements.
     */
    private Stage stage;

    /**
     * Local instance of skin. Contains information regarding styling.
     */
    private Skin skin;

    /**
     * List which contains all loaded classes that extend {@link Weapon}.
     */
    private final List<Weapon> loadedWeapons;

    /**
     * Constant:    Padding to add to the sides of the table.
     */
    private final static int PADDING = 20;

    /**
     * Constructor for StartScreen.
     * Instantiates required values for the rest of the class.
     */
    public StartScreen() {
        gameData = GameData.getInstance();
        loadedWeapons = new ArrayList<>();
    }

    /**
     * Automatically executed when screen is shown.
     */
    @Override
    public void show() {
        stage = new Stage();

        // Skin
        skin = new Skin();
        loadFonts(skin);
        setupStyles(skin);

        // Root table
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

//        Texture bgTexture = new Texture("background.png");
//        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(bgTexture);
//
//        table.setBackground(bgDrawable);

        Label headLabel = createLabel("Zesty Gorillas", skin,
                "bold", Color.RED);

        Label playLabel = createLabel("Hit Enter to play", skin,
                "regular", Color.RED);

        playLabel.addAction(Actions.forever(
                Actions.sequence(
                        Actions.fadeIn(1.25f),
                        Actions.fadeOut(1.25f)
                )
        ));

        ScrollPane scrollPane = createWeaponSelect();

        table.add(headLabel).expand().top().padTop(PADDING).row();

        table.add(scrollPane).row();

        table.add(playLabel).expand().bottom().padBottom(PADDING).row();

    }

    private void setupStyles(Skin skin) {
        Label.LabelStyle regularLabel = new Label.LabelStyle();
        regularLabel.font = skin.getFont("regular");
        skin.add("regular", regularLabel);

        Label.LabelStyle boldLabel = new Label.LabelStyle();
        boldLabel.font = skin.getFont("bold");
        skin.add("bold", boldLabel);

        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        skin.add("regular", scrollStyle);
    }

    /**
     * Loads fonts for use with various text objects in the scene.
     * Fonts are added to the local instance of {@link Skin}
     *
     * @param skin instance which fonts should be saved to.
     */
    private void loadFonts(Skin skin) {
        FreeTypeFontGenerator fontGenerator;
        fontGenerator = new FreeTypeFontGenerator(
                new FileHandle("GameEngine/src/main/resources/CinzelDecorative-Regular.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter regularParams
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        regularParams.size = 56;

        BitmapFont font = fontGenerator.generateFont(regularParams);

        skin.add("regular", font, BitmapFont.class);

        fontGenerator = new FreeTypeFontGenerator(
                new FileHandle("GameEngine/src/main/resources/CinzelDecorative-Bold.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter boldParams
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        boldParams.size = 74;

        BitmapFont boldFont = fontGenerator.generateFont(boldParams);

        skin.add("bold", boldFont, BitmapFont.class);

        fontGenerator.dispose();
    }

    /**
     * Wrapper method to create consistent labels.
     *
     * @param text      displayed by the label.
     * @param skin      skin which the label should use.
     * @param styleName which is should be used by the label.
     *                  Must be saved in skin already.
     * @param color of the text to be displayed.
     * @return new instance of label.
     */
    private Label createLabel(String text, Skin skin, String styleName, Color color) {
        Label label = new Label(text, skin, styleName);
        label.setColor(color);
        return label;
    }

    /**
     * Creates the {@link ScrollPane} which contains a selection of
     * all the available weapons. Recovers if no weapons are found.
     * @return  a {@link ScrollPane} containing tables of weapons.
     */
    private ScrollPane createWeaponSelect() {
        Table selectTable = new Table();
        selectTable.top().left();

        for (Weapon weapon : ServiceLoader.load(Weapon.class)) {
            loadedWeapons.add(weapon);
            Table itemTable = createWeaponTable(weapon);

            selectTable.add(itemTable).pad(PADDING);
        }

        if (loadedWeapons.isEmpty()) {
            Label label = createLabel("No Weapons Found", skin,
                    "bold", Color.RED);
            selectTable.add(label).row();
            Label glLabel = createLabel("Good Luck", skin,
                    "bold", Color.RED);
            selectTable.add(glLabel).row();
        }

        ScrollPane scrollPane = new ScrollPane(selectTable, skin, "regular");
        scrollPane.setScrollingDisabled(true, false);
        return scrollPane;
    }

    /**
     * Creates the individual tables which each weapon is represented in.
     * @param weapon    to create an infographic for.
     * @return  the Table which contains weapon infographic.
     */
    private Table createWeaponTable(Weapon weapon) {
        Table itemTable = new Table();
        VerticalGroup itemInfo = new VerticalGroup();

        Texture weaponTexture = weapon.getSprite().getTexture();
        Image weaponImage = new Image(new TextureRegion(weaponTexture));
        itemTable.add(weaponImage);


        String hpLabelText =
                String.format("Max HP: + %d", weapon.getMaxHP());
        Label maxHpLabel = createLabel(hpLabelText, skin,
                "regular", Color.WHITE);
        itemInfo.addActor(maxHpLabel);

        String dmgLabelText =
                String.format("Attack Dmg: + %d", weapon.getAttackDamage());
        Label dmgLabel = createLabel(dmgLabelText, skin,
                "regular", Color.WHITE);
        itemInfo.addActor(dmgLabel);

       String aSpeedLabelText =
               String.format("Attack Speed: + %d", weapon.getAttackSpeed());
       Label speedLabel = createLabel(aSpeedLabelText, skin,
               "regular", Color.WHITE);
       itemInfo.addActor(speedLabel);

       String speedLabelText =
               String.format("Movement Speed + %d", weapon.getMoveSpeed());
       Label moveSpeedLabel = createLabel(speedLabelText, skin,
               "regular", Color.WHITE);
       itemInfo.addActor(moveSpeedLabel);

        itemTable.add(itemInfo);
        return itemTable;
    }


    /**
     * Main Loop of the Start screen.
     *
     * @param v The delta-time of the current frame.
     */
    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        handleKeyEvents();
    }

    /**
     * Handles the events for the scene.
     * Responsible for acting when enter, left and right arrows are clicked.
     */
    private void handleKeyEvents() {
        if (gameData.getGameKey().isDown(gameData.getGameKey().getActionToKey().get(KeyAction.SELECT))) {
            ScreenManager screenManager = new ScreenManager();
            screenManager.switchScreen("GameScreen");
            gameData.getGame().setScreen(screenManager.getActiveScreen());
        }
    }

    /**
     * Automatically executed when a window resize occurs.
     *
     * @param width  The new width of the window in pixels.
     * @param height The new height of the window in pixels.
     */
    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Automatically executed when game is paused.
     */
    @Override
    public void pause() {

    }

    /**
     * Automatically executed when game is resumed.
     */
    @Override
    public void resume() {

    }

    /**
     * Automatically executed when window is minimized.
     */
    @Override
    public void hide() {

    }

    /**
     * Automatically called when screen is terminated.
     * Should free up resources from memory.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
