package dk.ee.zg.popups;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import dk.ee.zg.common.item.Item;
import dk.ee.zg.common.item.ItemManager;
import java.util.List;

public class LevelUpPopup extends Dialog {

    /**
     * Makes the Dialog able to run a runnable when it is closed.
     */
    private Runnable onClose;

    /**
     * Constructor for LevelPupUp.
     * @param title Title of the dialog box.
     * @param skin Skin of the dialog box
     * @param onClose which action should be run when dialog is closed.
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public LevelUpPopup(final String title,
                        final Skin skin,
                        final Runnable onClose) {
        super(title, skin);
        this.onClose = onClose;
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
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this.getSkin().getFont("new-bold");
        this.getTitleLabel().setStyle(labelStyle);
        labelStyle.font = this.getSkin().getFont("new");
        Label label = new Label("Choose An Item!", labelStyle);
        text(label);

        TooltipManager.getInstance().instant();
        TooltipManager.getInstance().offsetY = -30f;
        List<Item> itemList = ItemManager.getInstance().createItemSelection(3);
        float imageSize = 100f;
        for (Item item : itemList) {
            Drawable drawable =
                    new TextureRegionDrawable(item.getTexture());
            ImageButton imageButton = new ImageButton(drawable);
            TextTooltip tooltip = new TextTooltip(item.getDescription(),
                    getSkin());
            imageButton.addListener(tooltip);
            imageButton.pad(0f, 20f, 100f, 20f);
            imageButton.setSize(imageSize, imageSize);
            imageButton.getImage().setScaling(Scaling.fill);
            imageButton.getImageCell().minSize(imageSize, imageSize);
            imageButton.setScale(0f);
            imageButton.getColor().a = 0f;
            button(imageButton, item).setSize(imageSize, imageSize);
        }

        getTitleLabel().setAlignment(Align.center);
        getTitleTable().setScale(0.75f);
        this.setModal(false);
        this.setMovable(false);

    }

    /**
     * Result is the selection of the dialog box.
     * @param object
     */
    @Override
    protected void result(final Object object) {
        Item item = (Item) object;
        ItemManager.getInstance().equipItem(item);
        onClose.run();
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
        this.setZIndex(0);


        this.setSize(stage.getWidth() / 3.5f,
                stage.getHeight() / 3.5f);
        this.setPosition(stage.getWidth() / 2 - this.getWidth() / 2,
                stage.getHeight() / 2 - this.getHeight() / 2);

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
        float delay = 0.25f;
        float delayStep = 0.25f;

        for (Actor actor : buttonTable.getChildren()) {
            if (actor instanceof ImageButton) {
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
