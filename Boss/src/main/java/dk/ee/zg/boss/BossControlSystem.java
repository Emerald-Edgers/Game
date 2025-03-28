package dk.ee.zg.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.boss.ranged.Projectile;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IEntityProcessService;



public class BossControlSystem implements IEntityProcessService {

    /**
     * The base moving vector is 0,0, which means not moving.
     */
    private static Vector2 dir = new Vector2(0, 0);
    /**
     * Cooldown for the melee attack,
     * used to prevent multiple attacks in a short time.
     */
    private static float melAttackCooldown = 2f;
    /**
     * The player object, used to determine the direction of the attack.
     */
    private Entity player;


    /**
     * Entrance to the boss controle system for controlling the boss.
     * like moving or using attack's,
     * @param world - object of WorldEntities,
     *              Contains all the entities in the world.
     */
    @Override
    public void process(final WorldEntities world) {

        BossControlSystem.setMoveDirection(Direction.DOWN);

        melAttackCooldown -= Gdx.graphics.getDeltaTime();

        if (player == null) {
            for (Entity e1 : world.getEntities()) {
                if (e1.getEntityType() == EntityType.Player) {
                    //System.out.println(e1.getPosition());
                    player = e1;
                    break;
                }
            }
        }

        for (Entity boss : world.getEntities(Boss.class)) {

            //move((Boss) boss, dir);

            if (melAttackCooldown <= 0) {
                //rangedAttack((Boss) boss, world);
                melAttackCooldown = 2f;
            }

        }

    }

    /**
     * Primary method for moving the boss.
     * @param boss - The boss that should move
     * @param dirVec - The vector values in which the boss moves
     */
    public void move(final Boss boss, final Vector2 dirVec) {
        Vector2 vec = boss.getPosition();

        vec.add(dirVec.scl(
                boss.getMoveSpeed() * Gdx.graphics.getDeltaTime()));

        boss.setPosition(vec);

    }

    /**
     * Method for setting which Vector values the boss should move in.
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

    /**
     * Melee attack for the boss object, close non-aoe attack.
     * in form of a ractangle hitbox
     * @param boss - Boss object which should use the attack
     * @param direction - Direction of the attak
     * @return - Returns a rectangle in the direction of the attack
     *              with the correct offset
     */
    public Rectangle meleeAttack(final Boss boss, final Direction direction) {

        Vector2 bossPos = boss.getPosition();
        float attackOffsetX = 0;
        float attackOffsety = 0;
        float width = 2f;
        float height = 2f;

        switch (direction) {
            case UP:
                width = 3f;
                attackOffsetX = bossPos.x + (boss.getSprite().getWidth() / 2);
                attackOffsety = boss.getSprite().getHeight() / 2;
                break;
            case DOWN:
                width = 3f;
                attackOffsetX = bossPos.x + (boss.getSprite().getWidth() / 2);
                attackOffsety = -boss.getSprite().getHeight() / 2 - height;
                break;
            case LEFT:
                height = 3f;
                attackOffsetX = -boss.getSprite().getWidth() / 2 - width;
                attackOffsety = boss.getSprite().getHeight() / 2;
                break;
            case RIGHT:
                height = 3f;
                attackOffsetX = boss.getSprite().getWidth() / 2;
                attackOffsety = boss.getSprite().getHeight() / 2;
                break;
            default:
                height = 3f;
                attackOffsetX = -boss.getSprite().getWidth() / 2 - width;
                attackOffsety = boss.getSprite().getHeight() / 2;
        }

        Rectangle meleeAttackArea = new Rectangle(
                bossPos.x + attackOffsetX,
                bossPos.y + attackOffsety,
                width, height
        );

        //System.out.println("Boss melee attacked");

        return meleeAttackArea;

    }

    /**
     * Ranged attack for the boss object, Shoots a fireball towards the player.
     * @param boss - Boss object which should use the attack
     * @param world Object of worldEntities since
     *              it has to know the player entity to throw it towards
     */
    public void rangedAttack(final Boss boss, final WorldEntities world) {
        Vector2 target = new Vector2(player.getPosition());
        //TODO: Implement spawning order
        //Vector2 target = new Vector2(5,5);
        System.out.println("Target:" + target);

        Vector2 bossPos = boss.getPosition();
        System.out.println("Boss:" + bossPos);
        //Vector2 targetPos = target.getPosition(); //TODO

        Vector2 projectileDir = new Vector2(target.sub(bossPos));
        System.out.println("projDir:" + projectileDir);
        projectileDir.nor();

        float speed = boss.getAttackSpeed() * 2f;

        Projectile projectile = new Projectile(boss.getPosition(),
                projectileDir, speed, world, player);
        world.addEntity(projectile);
    }

    /**
     * AOE attack for the boss object, in form of a rectangle around it.
     * @param boss the boss at where this attack should be used.
     * @return returns a rectangle that should have middle at the
     * center of the boss
     */
    public Rectangle aoeAttack(final Boss boss) {

        Vector2 bossPos = boss.getPosition();
        float width = 6f;
        float height = 6f;

        Rectangle aoeAttackArea = new Rectangle(
                bossPos.x - width / 2,
                bossPos.y - height / 2,
                width,
                height
        );

        System.out.println("Boss aoe attacked");

        return aoeAttackArea;
    }

}
