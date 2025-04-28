package dk.ee.zg.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import dk.ee.zg.boss.ranged.Projectile;
import dk.ee.zg.common.enemy.data.BossActions;
import dk.ee.zg.common.enemy.interfaces.IEnemyNetwork;
import dk.ee.zg.common.enemy.interfaces.IPathFinder;
import dk.ee.zg.common.map.data.AnimationState;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.ICollisionEngine;
import dk.ee.zg.common.map.services.IEntityProcessService;
import dk.ee.zg.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;


public class BossControlSystem implements IEntityProcessService {

    /**
     * The base moving vector is 0,0, which means not moving.
     */
    private static Vector2 dir = new Vector2(0, 0);

    /**
     * Cooldown for the melee attack.
     */
    private static float melAttackCooldown = 2f;

    /**
     * Cooldown for the aoe attack.
     */
    private static float aoeCooldown = 4f;

    /**
     * Cooldown for the ranged attack.
     */
    private static float rangedCooldown = 6f;

    /**
     * The prev action the boss should use.
     */
    private static BossActions prevAction = BossActions.FLEE;

    /**
     * Should the movement penalty be applied?
     */
    private static boolean applyMovementPenalty = false;

    /**
     * The duration of the movement penalty.
     */
    private static float penaltyDuration = 1.5f;

    /**
     * Cooldown for switching between movement.
     * Prevents the boss from zig zagging between flee and advance.
     */
    private static float moveCooldown = 0f;

    /**
     * The player object, used to determine the direction of the attack.
     */
    private Entity player;


    /**
     * The cooldown for changing the animation.
     */
    private static float animationChangeCooldown = 1000f;

    /**
     * The current animation state, or one being switched to.
     */
    private AnimationState requestedAnimationState = null;

    /**
     * Has the death animation been played yet?
     */
    private boolean deathPlayed = false;


    /**
     * Entrance to the boss control system for controlling the boss.
     * like moving or using attack's,
     *
     * @param world - object of WorldEntities,
     *              Contains all the entities in the world.
     */
    @Override
    public void process(final WorldEntities world) {
        BossControlSystem.setMoveDirection(Direction.DOWN);

        if (player == null) {
            Optional<Entity> tempPlayer =
                    world.getEntities(Player.class).stream().findFirst();
            tempPlayer.ifPresent(entity -> player = entity);
        }

        Optional<IEnemyNetwork> network =
                ServiceLoader.load(IEnemyNetwork.class).findFirst();

        for (Entity boss : world.getEntities(Boss.class)) {
            Boss bossEntity = (Boss) boss;
            updateBossAnimation(bossEntity);
            deathLogic(world, bossEntity);


            if (network.isPresent()) {
                aiLogic(network.get(), world, bossEntity);
            } else {
                basicLogic(world, bossEntity);
            }
        }
        updateCooldowns();
    }

    private void updateCooldowns() {
        moveCooldown -= Gdx.graphics.getDeltaTime();
        melAttackCooldown -= Gdx.graphics.getDeltaTime();
        aoeCooldown -= Gdx.graphics.getDeltaTime();
        rangedCooldown -= Gdx.graphics.getDeltaTime();
        animationChangeCooldown -= Gdx.graphics.getDeltaTime();
        if (applyMovementPenalty) {
            penaltyDuration -= Gdx.graphics.getDeltaTime();
            if (penaltyDuration <= 0) {
                applyMovementPenalty = false;
                penaltyDuration = 1.5f;
            }
        }
    }

    private void deathLogic(final WorldEntities world, final Boss boss) {
        if (deathPlayed && boss.isAnimationFinished()) {
            world.removeEntity(boss.getId());
        }
        if (boss.getCurrentState() == AnimationState.DEATH) {
            Animation<TextureRegion> deathAnimation =
                    boss.getAnimations().get("DEATH");
            if (deathAnimation != null && deathAnimation
                    .isAnimationFinished(boss.getStateTime())) {
                world.removeEntity(boss.getId());
            }
        } else {
            setBossAnimationState(boss, AnimationState.IDLE);
        }
    }


    /**
     * The logic used when an instance of {@link IEnemyNetwork} is found.
     * This logic is meant to be more complex and use AI.
     * @param network  The instance to depend on.
     * @param world The world in which entities are added.
     *              Must be able to spawn new entities into the world.
     * @param boss  The boss itself, used to determine what/who to act on.
     */
    private void aiLogic(final IEnemyNetwork network,
                         final WorldEntities world, final Boss boss) {
        double playerDistance = 0.0;
        Direction playerDir = Direction.NONE;

        if (player != null) {
            playerDistance = player.getPosition().dst(boss.getPosition());
            playerDir = determinePlayerDir(boss);
        }

        boolean meleeReady = melAttackCooldown <= 0;
        boolean aoeReady = aoeCooldown <= 0;
        boolean rangedReady = rangedCooldown <= 0;

        BossActions action;
        if (prevAction == BossActions.FLEE && moveCooldown > 0) {
            action = BossActions.FLEE;
        } else if (prevAction == BossActions.ADVANCE && moveCooldown > 0) {
            action = BossActions.ADVANCE;
        } else {
            action = network.decideAction(
                    meleeReady, aoeReady, rangedReady,
                    boss.getHp(), playerDistance);
            moveCooldown = 3f;
        }
        switch (action) {
            case FLEE:
                fleeMove(boss);
                break;
            case ADVANCE:
                advanceMove(boss);
                break;
            case MELEE:
                meleeAttack(world, boss, playerDir);
                break;
            case AOE:
                aoeAttack(world, boss);
                break;
            case RANGE:
                rangedAttack(boss, world);
                break;
            default:
                System.err.println("No action was found for the boss.");
        }
        prevAction = action;
    }


    /**
     * The logic to use whenever the boss needs to flee.
     * @param boss  The boss to move with.
     */
    private void fleeMove(final Boss boss) {
        if (player == null) {
            return;
        }

        if (applyMovementPenalty) {
            return;
        }

        Vector2 bossPos = boss.getPosition();

        Vector2 dirToPlayer = new Vector2(
                player.getPosition().x - bossPos.x,
                player.getPosition().y - bossPos.y
        );

        dirToPlayer.nor();

        Vector2 fleeDir = new Vector2(-dirToPlayer.x, -dirToPlayer.y);

        Vector2 targetPos = new Vector2(
                bossPos.x + fleeDir.x * 25,
                bossPos.y + fleeDir.y * 25
        );

        setBossAnimationState(boss, AnimationState.RUN);
        move(boss, targetPos);
    }

    /**
     * The logic to use whenever the boss needs to advance.
     * @param boss The boss to move with.
     */
    private void advanceMove(final Boss boss) {
        if (player == null) {
            return;
        }

        if (applyMovementPenalty) {
            return;
        }
        Optional<IPathFinder> pathFinder =
                ServiceLoader.load(IPathFinder.class).findFirst();

        setBossAnimationState(boss, AnimationState.RUN);
        if (pathFinder.isPresent()) {
            boss.moveWithPathFinding(pathFinder.get(), player);
        } else {
            move(boss, player.getPosition());
        }
    }

    /**
     * Determines the direction from the boss to the player.
     * @param boss The boss to determine from.
     * @return A {@link Direction} value.
     */
    private Direction determinePlayerDir(final Boss boss) {
        Vector2 playerDir = new Vector2(player.getPosition())
                .sub(boss.getPosition());
        playerDir.nor();

        // Av godav det er Boegesvang der har skrevet det her.
        if (Math.abs(playerDir.x) > Math.abs(playerDir.y)) {
            if (playerDir.x > 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (playerDir.y > 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }
    }

    /**
     * Basic logic for the boss, Fallback if no {@link IEnemyNetwork} is found.
     *
     * @param world The world which contains entities.
     * @param boss  The boss which should be used with logic.
     */
    private void basicLogic(final WorldEntities world, final Boss boss) {
        if (rangedCooldown <= 0) {
            rangedAttack(boss, world);
        }
    }

    private boolean setBossAnimationState(final Boss boss,
                                          final AnimationState state) {
        AnimationState currentState = boss.getCurrentState();

        if (currentState == AnimationState.DEATH) {
            return false;
        }

        if (currentState == AnimationState.ATTACK) {
            Animation<TextureRegion> currentAnimation =
                    boss.getAnimations().get("ATTACK");
            if (currentState != null
                    && !currentAnimation
                    .isAnimationFinished(boss.getStateTime())) {
                return false;
            }
        }
        boss.setState(state);
        return true;
    }

    private void updateBossAnimation(final Boss boss) {
        if (animationChangeCooldown <= 0) {
            if (!deathPlayed) {
                setBossAnimationState(boss, AnimationState.DEATH);
                deathPlayed = true;
            }
        } else if (requestedAnimationState != null) {
            if (setBossAnimationState(boss, requestedAnimationState)) {
                requestedAnimationState = null;
            }
        } else if (boss.getCurrentState() != AnimationState.IDLE
                && boss.isAnimationFinished()
                && boss.getCurrentState() != AnimationState.DEATH) {
            setBossAnimationState(boss, AnimationState.IDLE);
        }

    }

    /**
     * Primary method for moving the boss.
     *
     * @param boss   - The boss that should move
     * @param target The target Coordinate
     */
    public void move(final Boss boss, final Vector2 target) {
        Vector2 bossPos = boss.getPosition();
        Vector2 dirV = new Vector2(
                target.x - bossPos.x, target.y - bossPos.y
        );
        dirV.nor();
        float speed = boss.getMoveSpeed();

        Vector2 newPosition = new Vector2(
                bossPos.x + (dirV.x * speed * Gdx.graphics.getDeltaTime()),
                bossPos.y + (dirV.y * speed * Gdx.graphics.getDeltaTime())

        );

        boss.setPosition(newPosition);
    }

    /**
     * Method for setting which Vector values the boss should move in.
     *
     * @param direction Takes the enum direction and uses it to determine
     *                  the vector values
     */
    public static void setMoveDirection(final Direction direction) {
        switch (direction) {
            case UP:
                dir.set(0, 1);
                break;
            case DOWN:
                dir.set(0, -1);
                break;
            case LEFT:
                dir.set(-1, 0);
                break;
            case RIGHT:
                dir.set(1, 0);
                break;
            default:
                dir.set(0, 0);
        }
    }

    /**
     * enum for recognizing direction the boss.
     * should both attack and move, used to direct both
     */
    public enum Direction {
        /**
         * north direction.
         */
        UP,
        /**
         * south direction.
         */
        DOWN,
        /**
         * West Direction.
         */
        LEFT,
        /**
         * East Direction.
         */
        RIGHT,
        /**
         * Not moving.
         */
        NONE
    }


    private void meleeAttack(final WorldEntities world,
                             final Boss boss,
                             final Direction direction) {
            Rectangle attackHitbox = calcMeleeAttack(boss, direction);
            setBossAnimationState(boss, AnimationState.ATTACK);
            damageHurtPlayers(attackHitbox, world, boss.getAttackDamage());
            melAttackCooldown = 2f;
    }

    private void aoeAttack(final WorldEntities world, final Boss boss) {
        Rectangle attackHitbox = calcAoeAttack(boss);
        setBossAnimationState(boss, AnimationState.ATTACK);
        damageHurtPlayers(attackHitbox, world, boss.getAttackDamage());
        aoeCooldown = 4f;
        applyMovementPenalty = true;
    }


    /**
     * Calculates if any player should be hit by an attack and damages them.
     * @param hurtbox   The area where players should be hurt.
     * @param world The world in which the player is present.
     * @param damage    The damage to do to the player.
     */
    private void damageHurtPlayers(final Rectangle hurtbox,
                                   final WorldEntities world,
                                   final int damage) {
        Optional<ICollisionEngine> collisionEngine =
                ServiceLoader.load(ICollisionEngine.class).findFirst();
        if (collisionEngine.isPresent()) {
            List<Entity> entitiesHit =
                    collisionEngine.get().rectangleCollidesWithEntities(
                                    hurtbox, world.getEntities(Player.class)
                            );
            for (Entity entity : entitiesHit) {
//                System.out.println("Hit player");
                entity.hit(damage);
            }
        }


    }

    /**
     * Melee attack for the boss object, close non-aoe attack.
     * in form of a rectangle hitbox
     *
     * @param boss      - Boss object which should use the attack
     * @param direction - Direction of the attack
     * @return - Returns a rectangle in the direction of the attack
     * with the correct offset
     */
    private Rectangle calcMeleeAttack(final Boss boss,
                                      final Direction direction) {
        Vector2 bossPos = boss.getPosition();
        float attackOffsetX = 0;
        float attackOffsety = 0;
        float width = 2f;
        float height = 2f;

        switch (direction) {
            case UP:
                width = 3f;
                attackOffsetX = bossPos.x + (boss.getHitbox().getWidth() / 2);
                attackOffsety = boss.getHitbox().getHeight() / 2;
                break;
            case DOWN:
                width = 3f;
                attackOffsetX = bossPos.x + (boss.getHitbox().getWidth() / 2);
                attackOffsety = -boss.getHitbox().getHeight() / 2 - height;
                break;
            case RIGHT:
                height = 3f;
                attackOffsetX = boss.getHitbox().getWidth() / 2;
                attackOffsety = boss.getHitbox().getHeight() / 2;
                break;
            default:
                height = 3f;
                attackOffsetX = -boss.getHitbox().getWidth() / 2 - width;
                attackOffsety = boss.getHitbox().getHeight() / 2;
        }

        Rectangle meleeAttackArea = new Rectangle(
                bossPos.x + attackOffsetX,
                bossPos.y + attackOffsety,
                width, height
        );

        return meleeAttackArea;

    }

    /**
     * Ranged attack for the boss object, Shoots a fireball towards the player.
     *
     * @param boss  - Boss object which should use the attack
     * @param world Object of worldEntities since
     *              it has to know the player entity to throw it towards
     */
    public void rangedAttack(final Boss boss, final WorldEntities world) {
        setBossAnimationState(boss, AnimationState.ATTACK);

        float animationTimer = 0.80f;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Vector2 target = new Vector2(player.getPosition());

                Vector2 bossPos = boss.getPosition();

                Vector2 projectileDir = new Vector2(target.sub(bossPos));
                projectileDir.nor();

                float speed = boss.getAttackSpeed() * 1.2f;

                Projectile projectile = new Projectile(bossPos,
                        projectileDir, speed, world, player);
                world.addEntity(projectile);
            }
        }, animationTimer);
        rangedCooldown = 4f;
        applyMovementPenalty = true;
    }

    /**
     * AOE attack for the boss object, in form of a rectangle around it.
     *
     * @param boss the boss at where this attack should be used.
     * @return returns a rectangle that should have middle at the
     * center of the boss
     */
    private Rectangle calcAoeAttack(final Boss boss) {
        Vector2 bossPos = boss.getPosition();
        float width = 12f;
        float height = 12f;

        Rectangle aoeAttackArea = new Rectangle(
                bossPos.x - width / 2,
                bossPos.y - height / 2,
                width,
                height
        );

        return aoeAttackArea;
    }
}
