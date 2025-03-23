package dk.ee.zg.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.data.KeyAction;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.ICollisionEngine;
import dk.ee.zg.common.map.services.IEntityProcessService;
import dk.ee.zg.common.weapon.AttackDirection;
import dk.ee.zg.common.weapon.Weapon;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

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
     * boolean for if player is attacking.
     */
    private boolean isAttacking = false;

    /**
     * attack hitbox of type Rectangle, to use for attacking enemies.
     */
    private Rectangle attackHitbox = null;

    /**
     * instance of gamedata {@link GameData}.
     */
    private final GameData gameData = GameData.getInstance();

    /**
     * main entrance to player control system, for controlling player,
     * if any movement key is pressed.
     * @param worldEntities - Object of WorldEntities,
     *                      contains a map of all entities on map
     */
    @Override
    public void process(final WorldEntities worldEntities) {

        moveDirection = MoveDirection.NONE;
        Vector2 dirVec = new Vector2(0, 0);

        //if keys are down

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
        dirVec.nor(); // normalize to ensure diagonal get no speed boost



        //if attack was just pressed
        if (gameData.getGameKey().isPressed(gameData.getGameKey()
                .getActionToKey().get(KeyAction.Attack))) {
            isAttacking = true;
        }

        Optional<Player> player = worldEntities.getEntityByClass(Player.class);

        if (player.isPresent()) {
            move(player.get(), dirVec);

            Weapon weapon = player.get().getWeapon();
            //if move direction is not none, then set attack direction.
            if (weapon != null && moveDirection != MoveDirection.NONE) {
                weapon.setAttackDirection(
                        AttackDirection.valueOf(moveDirection.name()));
            }

            if (isAttacking && weapon != null) {
                Vector2 center = new Vector2();
                Vector2 size = new Vector2();
                player.get().getSprite().
                        getBoundingRectangle().getCenter(center);
                player.get().getSprite().getBoundingRectangle().getSize(size);
                attackHitbox = weapon.attack(
                        center, size);

            } else if (!isAttacking) {
                attackHitbox = null;
            }

            if (attackHitbox != null) {

                Optional<ICollisionEngine> collisionEngine = ServiceLoader.
                        load(ICollisionEngine.class).findFirst();
                if (collisionEngine.isPresent()) {
                    List<Entity> enemiesHit = collisionEngine.get()
                            .rectangleCollidesWithEntities(
                                    attackHitbox, worldEntities.getEntities());

                    for (Entity e : enemiesHit) {
                        e.hit(player.get().getAttackDamage());
                    }
                }
            }
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
