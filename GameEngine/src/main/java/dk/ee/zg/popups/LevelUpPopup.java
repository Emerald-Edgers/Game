package dk.ee.zg.popups;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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

        for (Item item : itemList) {
            Drawable drawable =
                    new TextureRegionDrawable(item.getTexture());
            ImageButton imageButton = new ImageButton(drawable);
            button(imageButton, item);
        }

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

}
