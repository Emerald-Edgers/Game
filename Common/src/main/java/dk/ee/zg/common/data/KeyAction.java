package dk.ee.zg.common.data;

public enum KeyAction {
    /**
     * Action used to interact with items.
     * Stuff like confirm, pickup, open, etc.
     */
    INTERACT,

    /**
     * Action for moving up.
     */
    MOVE_UP,

    /**
     * Action for moving down.
     */
    MOVE_DOWN,

    /**
     * Action for moving left. Also used when navigating left through menus.
     */
    MOVE_LEFT,

    /**
     * Action for moving right. Also used when navigating right through menus.
     */
    MOVE_RIGHT,

    /**
     * Action for selecting an item. Used in menus when confirming something.
     */
    SELECT,
    /**
     * Action for attacking with weapons basic attack.
     */
    Attack,
    /**
     * Action of pausing the game.
     */
    PAUSE,
}
