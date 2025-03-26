package dk.ee.zg.managers;

import com.badlogic.gdx.InputAdapter;
import dk.ee.zg.common.data.GameData;

public class InputManager extends InputAdapter {

    @Override
    public final boolean keyDown(final int keycode) {
        handleKey(keycode, true);
        return true;
    }

    @Override
    public final boolean keyUp(final int keycode) {
        handleKey(keycode, false);
        return true;
    }

    @Override
    public final boolean touchUp(final int screenX, final int screenY,
                                 final int pointer, final int button) {
        return true;
    }

    private void handleKey(final int keycode, final boolean bool) {
        GameData.getInstance().getGameKey().setKey(keycode, bool);
    }

}
