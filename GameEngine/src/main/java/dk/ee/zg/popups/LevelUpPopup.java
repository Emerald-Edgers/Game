package dk.ee.zg.popups;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import dk.ee.zg.common.item.Item;
import dk.ee.zg.common.item.ItemManager;
import java.util.List;

public class LevelUpPopup extends Dialog {

    public LevelUpPopup(String title, Skin skin) {
        super(title, skin);
    }

    public LevelUpPopup(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public LevelUpPopup(String title, WindowStyle windowStyle) {
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


    @Override
    protected void result(Object object) {
        Item item = (Item) object;
        ItemManager.getInstance().equipItem(item);
        this.hide(null);
    }

}
