package dk.ee.zg.popups;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.*;

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
        text("LEVELUP!");
        button("Yes");
        button("No");
    }

    @Override
    protected void result(Object object) {
    }

}
