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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.data.KeyAction;
import dk.ee.zg.common.weapon.Weapon;
import dk.ee.zg.managers.ScreenManager;
import dk.ee.zg.common.weapon.WeaponManager;
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
     * The ui element which structures the selection view.
     */
    private Table weaponSelectTable;

    /**
     * The index of the currently selected weapons in {@code loadedWeapons}.
     */
    private int selectedWeaponIndex;

    /**
     * Constant:    Padding to add to the sides of the table.
     */
    private static final int PADDING = 20;

    /**
     * Constant:    The speed of the blinking animation.
     */
    private static final float ANIMATION_SPEED = 1.25f;

    /**
     * Constant:    The width of the root table.
     */
    private static final int COLUMN_WIDTH = 3;

    /**
     * Constant:    The size of bold text.
     */
    private static final int BOLD_FONT_SIZE = 74;

    /**
     * Constant:    The size of regular text.
     */
    private static final int REGULAR_FONT_SIZE = 56;


    /**
     * Constructor for StartScreen.
     * Instantiates required values for the rest of the class.
     */
    public StartScreen() {
        gameData = GameData.getInstance();
        loadedWeapons = loadWeapons();
        selectedWeaponIndex = 0;
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
//        TextureRegionDrawable bgDrawable = new TextureRegionDrawable
//        (bgTexture);
//
//        table.setBackground(bgDrawable);

        Label headLabel = createLabel("Zesty Gorillas", skin,
                "bold", Color.RED);

        Label playLabel = createLabel("Hit Enter to play", skin,
                "regular", Color.RED);

        Label leftLabel = createLabel("<<", skin,
                "bold", Color.RED);
        leftLabel.setAlignment(Align.center);

        Label rightLabel = createLabel(">>", skin,
                "bold", Color.RED);
        rightLabel.setAlignment(Align.center);

        playLabel.addAction(Actions.forever(
                Actions.sequence(
                        Actions.fadeIn(ANIMATION_SPEED),
                        Actions.fadeOut(ANIMATION_SPEED)
                )
        ));


        if (loadedWeapons.isEmpty()) {
            weaponSelectTable = noWeaponsFound();
        } else {
            weaponSelectTable =
                    createWeaponTable(loadedWeapons.get(selectedWeaponIndex));
        }

        table.add(headLabel).colspan(COLUMN_WIDTH)
                .expand().top().padTop(PADDING).row();


        table.add(leftLabel).pad(PADDING);
        table.add(weaponSelectTable).pad(PADDING);
        table.add(rightLabel).pad(PADDING).row();

        table.add(playLabel).colspan(COLUMN_WIDTH)
                .expand().bottom().padBottom(PADDING);
    }

    /**
     * Load all instances which extend {@link Weapon}.
     *
     * @return A list of found instances. Empty if none is found.
     */
    private List<Weapon> loadWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        for (Weapon weapon : ServiceLoader.load(Weapon.class)) {
            weapons.add(weapon);
        }
        return weapons;
    }

    /**
     * Method for separating the setup of various styles for ui components.
     * Adds the styles to the given {@link Skin}.
     *
     * @param baseSkin instance to populate with styles.
     */
    private void setupStyles(final Skin baseSkin) {
        Label.LabelStyle regularLabel = new Label.LabelStyle();
        regularLabel.font = baseSkin.getFont("regular");
        baseSkin.add("regular", regularLabel);

        Label.LabelStyle boldLabel = new Label.LabelStyle();
        boldLabel.font = baseSkin.getFont("bold");
        baseSkin.add("bold", boldLabel);

        ScrollPane.ScrollPaneStyle scrollStyle =
                new ScrollPane.ScrollPaneStyle();
        baseSkin.add("regular", scrollStyle);
    }

    /**
     * Loads fonts for use with various text objects in the scene.
     * Fonts are added to the local instance of {@link Skin}
     *
     * @param baseSkin instance which fonts should be saved to.
     */
    private void loadFonts(final Skin baseSkin) {
        FreeTypeFontGenerator fontGenerator;
        fontGenerator = new FreeTypeFontGenerator(
                new FileHandle("GameEngine/src/main/resources"
                        + "/CinzelDecorative-Regular.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter regularParams
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        regularParams.size = REGULAR_FONT_SIZE;

        BitmapFont font = fontGenerator.generateFont(regularParams);

        baseSkin.add("regular", font, BitmapFont.class);

        fontGenerator = new FreeTypeFontGenerator(
                new FileHandle("GameEngine/src/main/resources"
                        + "/CinzelDecorative-Bold.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter boldParams
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        boldParams.size = BOLD_FONT_SIZE;

        BitmapFont boldFont = fontGenerator.generateFont(boldParams);

        baseSkin.add("bold", boldFont, BitmapFont.class);

        fontGenerator.dispose();
    }

    /**
     * Wrapper method to create consistent labels.
     *
     * @param text      displayed by the label.
     * @param baseSkin      skin which the label should use.
     * @param styleName which is should be used by the label.
     *                  Must be saved in skin already.
     * @param color     of the text to be displayed.
     * @return new instance of label.
     */
    private Label createLabel(final String text, final Skin baseSkin,
                              final String styleName, final Color color) {
        Label label = new Label(text, baseSkin, styleName);
        label.setColor(color);
        return label;
    }


    /**
     * Creates and adds labels displaying no
     * weapons have been found to a given table.
     *
     * @return Table with labels describing no weapons have been found.
     */
    private Table noWeaponsFound() {
        Table table = new Table();
        Label label = createLabel("No Weapons Found", skin,
                "bold", Color.RED);
        table.add(label).row();
        Label glLabel = createLabel("Good Luck", skin,
                "bold", Color.RED);
        table.add(glLabel).row();
        return table;
    }


    /**
     * Creates the individual tables which each weapon is represented in.
     *
     * @param weapon to create an infographic for.
     * @return the Table which contains weapon infographic.
     */
    private Table createWeaponTable(final Weapon weapon) {
        Table itemTable = new Table();
        VerticalGroup itemInfo = new VerticalGroup();
        itemInfo.columnLeft();

        Texture weaponTexture = weapon.getSprite().getTexture();
        Image weaponImage = new Image(new TextureRegion(weaponTexture));

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

        itemTable.add(weaponImage).size(itemInfo.getPrefHeight()).pad(PADDING);
        itemTable.add(itemInfo).pad(PADDING);
        return itemTable;
    }


    /**
     * Main Loop of the Start screen.
     *
     * @param v The delta-time of the current frame.
     */
    @Override
    public void render(final float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        handleKeyEvents();
    }

    /**
     * Gets the selected weapons based on the current selectedIndex.
     * @return Null or the selected weapon from index.
     */
    private Weapon retrieveSelectedWeapon() {
        if (loadedWeapons.isEmpty()) {
            return null;
        }
        return loadedWeapons.get(selectedWeaponIndex);
    }

    /**
     * Handles the events for the scene.
     * Responsible for acting when enter, left and right arrows are clicked.
     */
    private void handleKeyEvents() {
        if (gameData.getGameKey().isDown(
                gameData.getGameKey().getActionToKey().get(KeyAction.SELECT))) {
            WeaponManager.getInstance().setWeaponSelected(retrieveSelectedWeapon());
            ScreenManager screenManager = new ScreenManager();
            screenManager.switchScreen("GameScreen");
            gameData.getGame().setScreen(screenManager.getActiveScreen());
        }
        if (gameData.getGameKey().isPressed(gameData.getGameKey().
                getActionToKey().get(KeyAction.MOVE_LEFT))) {
            if (selectedWeaponIndex > 0) {
                selectedWeaponIndex--;
                updateWeaponSelect();
            }
        }
        if (gameData.getGameKey().isPressed(gameData.getGameKey().
                getActionToKey().get(KeyAction.MOVE_RIGHT))) {
            if (selectedWeaponIndex < loadedWeapons.size() - 1) {
                selectedWeaponIndex++;
                updateWeaponSelect();
            }
        }

        gameData.getGameKey().checkJustPressed();
    }

    private void updateWeaponSelect() {
        weaponSelectTable.clear();
        Table newWeapon =
                createWeaponTable(loadedWeapons.get(selectedWeaponIndex));
        weaponSelectTable.add(newWeapon);
        weaponSelectTable.invalidate();
        weaponSelectTable.pack();
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
        gameData.setDisplayWidth(width);
        gameData.setDisplayHeight(height);
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
