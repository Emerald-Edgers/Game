package dk.ee.zg.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.boss.ranged.Projectile;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IEntityProcessService;



public class BossControlSystem implements IEntityProcessService {
    private static Vector2 Dir = new Vector2(0,0);

    private static float melAttackCooldown = 5.0f;

    @Override
    public void process(WorldEntities world) {

        //BossControlSystem.setDirection(Direction.UP);

        melAttackCooldown -= Gdx.graphics.getDeltaTime();

        if (melAttackCooldown % 1 == 0){
            System.out.println(melAttackCooldown);
        }


        for (Entity boss : world.getEntities(Boss.class)) {

            move((Boss) boss, Dir);

            if (melAttackCooldown <= 0) {
                rangedAttack((Boss) boss, world);
                melAttackCooldown = 5.0f;
            }

        }

    }

    public void move(Boss boss, Vector2 dirVec) {
        Vector2 vec = boss.getPosition();

        vec.add(dirVec.scl(
                boss.getMoveSpeed() * Gdx.graphics.getDeltaTime()));

        boss.setPosition(vec);

    }

    public static void setMoveDirection(Direction direction) {
        switch (direction) {
            case UP:
                Dir.set(0, 1);
                break;
            case DOWN:
                Dir.set(0, -1);
                break;
            case LEFT:
                Dir.set(-1, 0);
                break;
            case RIGHT:
                Dir.set(1, 0);
                break;
        }
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public Rectangle meleeAttack(Boss boss, Direction direction) {

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
        }

        Rectangle meleeAttackArea = new Rectangle(
                bossPos.x + attackOffsetX,
                bossPos.y + attackOffsety,
                width, height
        );

        System.out.println("Boss melee attacked");

        return meleeAttackArea;

    }

    public void rangedAttack(Boss boss, WorldEntities world) {

        Vector2 bossPos = boss.getPosition();
        System.out.println("Boss.getposition in ranged method:" + bossPos);

        Sprite bossSprite = boss.getSprite();
        float bossSpriteWidth = bossSprite.getWidth() * boss.getScale().x;
        float bossSpriteHeight = bossSprite.getHeight() * boss.getScale().y;

        Vector2 bossCenterPos = new Vector2(
                boss.getSprite().getBoundingRectangle().getX() + boss.getSprite().getBoundingRectangle().getWidth() * boss.getScale().x,
                boss.getSprite().getBoundingRectangle().getY() + boss.getSprite().getBoundingRectangle().getHeight() * boss.getScale().y);
        bossCenterPos.add(2,2);

        Vector2 projectileDir = new Vector2(Dir);
        if (projectileDir.len() == 0) {
            projectileDir.set(1, 0);
        } else {
            projectileDir.nor();
        }
        float speed = boss.getAttackSpeed() * 2f;

        Projectile projectile = new Projectile(new Vector2(2,2), projectileDir, speed);

        world.addEntity(projectile);


        System.out.println("projectile position:" + projectile.getPosition());
        System.out.println("Projectile bounding sprite" + projectile.getSprite().getBoundingRectangle().getCenter(new Vector2()));
        System.out.println("Boss position: " + bossPos);
        System.out.println("Boss center: " + bossCenterPos);
        System.out.println("Projectile spawned at: " + projectile.getPosition());
        System.out.println("Boss ranged attack in dir: " + projectileDir);
    }

    public Rectangle aoeAttack(Boss boss) {

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
