package dk.ee.zg.popups;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import dk.ee.zg.common.item.Item;
import dk.ee.zg.common.item.ItemManager;
import java.util.List;

public class LevelUpPopup extends Dialog {

    /**
     * Constructor for LevelPupUp.
     * @param title Title of the dialog box.
     * @param skin Skin of the dialog box
     */
    public LevelUpPopup(final String title, final Skin skin) {
        super(title, skin);
    }

    /**
     * Constructor for LevelPupUp.
     * @param title Title of the dialog box.
     * @param skin Skin of the dialog box
     * @param windowStyleName name of the styling of the window.
     */
    public LevelUpPopup(final String title, final Skin skin,
                        final String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    /**
     * Constructor for LevelPupUp.
     * @param title Title of the dialog box.
     * @param windowStyle styling of the window in the dialog box
     */
    public LevelUpPopup(final String title, final WindowStyle windowStyle) {
        super(title, windowStyle);
    }

    {
        List<Item> itemList = ItemManager.getInstance().createItemSelection(3);

        text("Choose an Item");

        TooltipManager.getInstance().instant();
        TooltipManager.getInstance().offsetY = -30f;

        for (Item item : itemList) {
            Drawable drawable =
                    new TextureRegionDrawable(item.getTexture());
            ImageButton imageButton = new ImageButton(drawable);
            TextTooltip tooltip = new TextTooltip(item.getDescription(),
                    getSkin());
            imageButton.addListener(tooltip);
            imageButton.pad(5f);
            imageButton.setScale(0f);
            imageButton.getColor().a = 0f;
            button(imageButton, item);
        }

        getTitleLabel().setAlignment(Align.center);
        this.setModal(false);

    }

    /**
     * Result is the selection of the dialog box.
     * @param object
     */
    @Override
    protected void result(final Object object) {
        Item item = (Item) object;
        ItemManager.getInstance().equipItem(item);
        this.hide(null);
    }

    /**
     * Used to define animation of the dialog pop up and show it on the stage.
     * @param stage the stage which the dialog is on.
     */
    public void animateShow(final Stage stage) {
        this.setScale(0f);
        this.setColor(1f, 1f, 1f, 0f); // Start transparent

        this.show(stage);
        stage.act(); // Force layout

        // Position it in center manually
        float x = (stage.getWidth() - getWidth()) / 2f;
        float y = (stage.getHeight() - getHeight()) / 2f;
        this.setPosition(x, y);

        // Set origin to center for scaling
        this.setOrigin(Align.center);

        // Animate in
        this.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.scaleTo(1f, 1f, 0.4f, Interpolation.swingOut)
                ),
                Actions.run(this::animateItemButtons)
        ));
    }

    private void animateItemButtons() {
        Table buttonTable = getButtonTable(); // Default button table in dialog
        float delay = 0.5f;
        float delayStep = 0.5f;

        for (Actor actor : buttonTable.getChildren()) {
            if (actor instanceof ImageButton) {
                actor.setScale(0f);
                actor.addAction(Actions.sequence(
                        Actions.delay(delay),
                        Actions.parallel(
                                Actions.fadeIn(0.3f),
                                Actions.scaleTo(
                                        1f,
                                        1f,
                                        0.3f, Interpolation.swingOut)
                        )
                ));
                delay += delayStep;
            }
        }
    }


}
