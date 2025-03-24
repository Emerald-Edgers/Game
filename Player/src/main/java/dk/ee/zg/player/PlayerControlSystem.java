package dk.ee.zg.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.data.KeyAction;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IEntityProcessService;

public class PlayerControlSystem implements IEntityProcessService {

    /**
     * enum for recognizing move direction,
     * for use in animation.
     */
    private enum MoveDirection {
        /**
         * Not moving.
         */
        NONE,
        /**
         * Moving LEFT.
         */
        LEFT,
        /**
         * Moving RIGHT.
         */
        RIGHT,
        /**
         * Moving UP.
         */
        UP,
        /**
         * Moving DOWN.
         */
        DOWN,
    }

    /**
     * sets move direction.
     */
    private MoveDirection moveDirection = MoveDirection.NONE;



    /**
     * main entrance to player control system, for controlling player,
     * if any movement key is pressed.
     * @param worldEntities - Object of WorldEntities,
     *                      contains a map of all entities on map
     */
    @Override
    public void process(final WorldEntities worldEntities) {
        GameData gameData = GameData.getInstance();
        moveDirection = MoveDirection.NONE;
        Vector2 dirVec = new Vector2(0, 0);

        if (gameData.getGameKey().isDown(gameData.getGameKey()
                .getActionToKey().get(KeyAction.MOVE_LEFT))) {
            moveDirection = MoveDirection.LEFT;
            dirVec.add(-1, 0);
        }
        if (gameData.getGameKey().isDown(gameData.getGameKey()
                .getActionToKey().get(KeyAction.MOVE_RIGHT))) {
            moveDirection = MoveDirection.RIGHT;
            dirVec.add(1, 0);
        }
        if (gameData.getGameKey().isDown(gameData.getGameKey()
                .getActionToKey().get(KeyAction.MOVE_UP))) {
            moveDirection = MoveDirection.UP;
            dirVec.add(0, 1);
        }
        if (gameData.getGameKey().isDown(gameData.getGameKey()
                .getActionToKey().get(KeyAction.MOVE_DOWN))) {
            moveDirection = MoveDirection.DOWN;
            dirVec.add(0, -1);
        }
        dirVec.nor(); // normalize if diagonal to get no speed boost
        for (Entity player : worldEntities.getEntities(Player.class)) {
            move((Player) player, dirVec);
        }

    }

    /**
     * moves the player with vector movement vector,
     * normalized to account for diagonal movement speed.
     * @param player - player to move
     * @param dirVec - direction vector to use
     */
    private void move(final Player player, final Vector2 dirVec) {
        Vector2 vec = player.getPosition();

        vec.add(dirVec.scl(
                player.getMoveSpeed() * Gdx.graphics.getDeltaTime()));


        player.setPosition(vec);
    }
}
