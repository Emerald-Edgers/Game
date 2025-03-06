package dk.ee.zg.managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import dk.ee.zg.common.data.GameData;

public class InputManager extends InputAdapter {

    @Override
    public boolean keyDown(int keycode){
        handleKey(keycode, true);
        return true;
    }

    @Override
    public boolean keyUp(int keycode){
        handleKey(keycode, false);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    private void handleKey(int keycode, boolean bool){
        GameData.getInstance().getGameKey().setKey(keycode,bool);
    }

}
