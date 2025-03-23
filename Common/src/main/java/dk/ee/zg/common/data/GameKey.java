package dk.ee.zg.common.data;

import com.badlogic.gdx.Input;

import java.util.HashMap;

public class GameKey {

    public HashMap<KeyAction, Integer> getActionToKey() {
        return actionToKey;
    }

    private HashMap<KeyAction, Integer> actionToKey = new HashMap<KeyAction,Integer>();

    //currently pressed
    private HashMap<Integer,Boolean> keys = new HashMap<Integer,Boolean>();
    //just pressed
    private HashMap<Integer,Boolean> pkeys = new HashMap<Integer,Boolean>();


    public GameKey(){
        for (int i = 0; i < Input.Keys.MAX_KEYCODE; i++) {
            keys.put(i, false);
            pkeys.put(i, false);
        }

        //handling keybinds
        bindActionToKey(KeyAction.MOVE_UP, Input.Keys.UP);
        bindActionToKey(KeyAction.MOVE_DOWN, Input.Keys.DOWN);
        bindActionToKey(KeyAction.MOVE_LEFT, Input.Keys.LEFT);
        bindActionToKey(KeyAction.MOVE_RIGHT, Input.Keys.RIGHT);
        bindActionToKey(KeyAction.SELECT, Input.Keys.ENTER);
        bindActionToKey(KeyAction.INTERACT, Input.Keys.E);
        bindActionToKey(KeyAction.Attack, Input.Keys.SPACE);
    }

    /**
     * binds a KeyAction to a libgdx Keys keycode, to keybind
     * @param action - KeyAction enum value e.g MOVE_UP
     * @param keyCode - LIBGDX Keys keycode e.g KEYS.A = 1
     */
    public void bindActionToKey(KeyAction action, int keyCode) {
        actionToKey.put(action, keyCode);
    }


    /**
     * evaluates all pkeys equal to keys, to register if any keys in keys was just evaluated true.
     */
    public void checkJustPressed() {
        for (int i = 0; i < keys.size(); i++) {
            pkeys.put(i, keys.get(i));
        }
    }

    /**
     * resets keys and pkeys to all false
     */
    public void reset(){
        for (int i = 0; i < keys.size(); i++) {
            keys.put(i, false);
            pkeys.put(i, false);
        }
    }


    /**
     * sets the boolean value of keys element
     * @param k - keycode
     * @param bool - pressed or not bool
     */
    public void setKey(int k,boolean bool){
        keys.put(k, bool);
    }

    /**
     * returns boolean value of keys element at keycode, i.e is this key pressed
     * @param k - keycode
     * @return - returns boolean value of keys element at keycode
     */
    public boolean isDown(int k){
        return keys.get(k);
    }

    /**
     * returns boolean value of pkeys element at keycode, i.e was this key just pressed
     * @param k - keycode
     * @return - returns boolean value of pkeys element at keycode
     */
    public boolean isPressed(int k){
        return keys.get(k) && !pkeys.get(k);
    }
}
