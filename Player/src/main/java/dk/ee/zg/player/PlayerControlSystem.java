package dk.ee.zg.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.data.KeyAction;
import dk.ee.zg.common.enemy.data.Enemy;
import dk.ee.zg.common.map.data.AnimationState;
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

    boolean movingLeft = false, movingRight = false, movingUp = false, movingDown = false;

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


        Optional<Player> player = worldEntities.getEntityByClass(Player.class);

        if (!player.isPresent()) {
            return;
        }


        Player player1 = player.get();

        if (player1.getHp() > 0) {
            double regen = player1.getHealthRegen() * Gdx.graphics.getDeltaTime();
            double hp = Math.min(player1.getMaxHP(), player1.getHp() + regen);
            player1.setHp(hp);
        }

        //if attack was just pressed
        if (gameData.getGameKey().isPressed(gameData.getGameKey()
                .getActionToKey().get(KeyAction.Attack))) {
            if (player1.isAnimationFinished()
                    || player1.getCurrentState() == AnimationState.IDLE
                    || player1.getCurrentState() == AnimationState.RUN) {
                isAttacking = true;
            } else {
                return;
            }
        }

        moveDirection = MoveDirection.NONE;
        Vector2 dirVec = new Vector2(0, 0);

        movingUp = false;
        movingDown = false;
        movingLeft = false;
        movingRight = false;

        //if keys are down
        if (gameData.getGameKey().isDown(gameData.getGameKey()
                .getActionToKey().get(KeyAction.MOVE_LEFT))) {
            dirVec.add(-1, 0);
            movingLeft = true;
        }
        if (gameData.getGameKey().isDown(gameData.getGameKey()
                .getActionToKey().get(KeyAction.MOVE_RIGHT))) {
            dirVec.add(1, 0);
            movingRight = true;
        }
        if (gameData.getGameKey().isDown(gameData.getGameKey()
                .getActionToKey().get(KeyAction.MOVE_UP))) {
            dirVec.add(0, 1);
            movingUp = true;
        }
        if (gameData.getGameKey().isDown(gameData.getGameKey()
                .getActionToKey().get(KeyAction.MOVE_DOWN))) {
            dirVec.add(0, -1);
            movingDown = true;
        }


        if (movingRight && movingUp) {
            moveDirection = MoveDirection.RIGHT;
            setPlayerAnimationState(player1,AnimationState.RUN, AttackDirection.RIGHT);

        } else if (movingLeft && movingUp) {
            moveDirection = MoveDirection.LEFT;
            setPlayerAnimationState(player1, AnimationState.RUN, AttackDirection.LEFT);

        } else if (movingLeft && movingDown) {
            moveDirection = MoveDirection.LEFT;
            setPlayerAnimationState(player1, AnimationState.RUN, AttackDirection.LEFT);

        } else if (movingRight && movingDown) {
            moveDirection = MoveDirection.RIGHT;
            setPlayerAnimationState(player1,AnimationState.RUN, AttackDirection.RIGHT);

        } else if (movingLeft && !movingRight) {
            moveDirection = MoveDirection.LEFT;
            setPlayerAnimationState(player1, AnimationState.RUN, AttackDirection.LEFT);

        } else if (movingRight && !movingLeft) {
            moveDirection = MoveDirection.RIGHT;
            setPlayerAnimationState(player1,AnimationState.RUN, AttackDirection.RIGHT);

        } else if (movingUp && !movingDown) {
            moveDirection = MoveDirection.UP;
            setPlayerAnimationState(player1, AnimationState.RUN, AttackDirection.UP);

        } else if (movingDown && !movingUp) {
            moveDirection = MoveDirection.DOWN;
            setPlayerAnimationState(player1, AnimationState.RUN, AttackDirection.DOWN);

        }

        dirVec.nor(); // normalize to ensure diagonal get no speed boost

        AttackDirection currentDirection = AttackDirection.valueOf(player1.getFacingDirection().name());

        move(player1,dirVec);

        if (isAttacking) {
            attack(worldEntities);
        }

        if (player.isPresent()) {
            //move(player.get(), dirVec);

            Weapon weapon = player.get().getWeapon();

            //if move direction is not none, then set attack direction.
            if (weapon != null && moveDirection != MoveDirection.NONE) {
                weapon.setAttackDirection(
                        AttackDirection.valueOf(moveDirection.name()));
            }
        }

        updatePlayerAnimation(player1);
    }

    /**
     * method for attacking with loaded weapon.
     * @param worldEntities - Object of WorldEntities,
     *                            contains a map of all entities on map.
     */
    private void attack(final WorldEntities worldEntities) {
        Optional<Player> player = worldEntities.getEntityByClass(Player.class);
      
        if (player.isPresent()) {
            Player player1 = player.get();

            Weapon weapon = player.get().getWeapon();

            if (isAttacking && weapon != null) {
                Vector2 center = player.get().getPosition();
                Vector2 size = new Vector2();
                player.get().getHitbox().getSize(size);
                attackHitbox = weapon.attack(
                        center, size);
                setPlayerAnimationState(player1, AnimationState.ATTACK, weapon.getAttackDirection());

                isAttacking = false;
              
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
                        if (e instanceof Enemy) {
                            Enemy t = (Enemy) e;
                            Player p = player.get();
                            double effDefense = Math.max(
                                    0, t.getDefense() - p.getPenetration());
                            double baseDamage = p.getAttackDamage() -
                                    (effDefense * 0.25);
                            boolean isCrit = Math.random() < p.getCritChance();

                            double finalDamage = baseDamage *
                                    (isCrit ? p.getCritDamage() : 1.0);
                            e.hit((int) finalDamage);
                        } else {
                            e.hit(player.get().getAttackDamage());
                        }
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

    private void setPlayerAnimationState(Player player, AnimationState state) {
        player.setState(state, player.getFacingDirection());
    }

    private void setPlayerAnimationState(Player player, AnimationState state, AttackDirection direction) {
        if (player.getCurrentState() == AnimationState.ATTACK && !player.isAnimationFinished()) {
            return;
        }
        player.setState(state, direction);
    }

    private void updatePlayerAnimation(Player player) {
        if (isAttacking){
            return;
        }
        if (player.getCurrentState() != AnimationState.IDLE && player.isAnimationFinished()
        && moveDirection == MoveDirection.NONE) {
            setPlayerAnimationState(player, AnimationState.IDLE, player.getFacingDirection());
        }
    }
}
