package dk.ee.zg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IEnemy;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IEntityProcessService;

public class SkeletonControlSystem implements IEntityProcessService, IEnemy {
    private static float attackCooldown = 2.0f;

    @Override
    public Rectangle attack(Entity entity) {

        Vector2 skeletonPos = entity.getPosition();

        float width = 6f;
        float height = 6f;

        //System.out.println("skeleton attacked");
        //System.out.println("skeleton pos: " + skeletonPos);

        /*
        switch (direction){
            case UP -> {

            }
            case DOWN -> {

            }
            case LEFT -> {

            }
            case RIGHT -> {

            }
            default -> { }
        }*/

        Rectangle hitbox = new Rectangle(
                skeletonPos.x - width / 2,
                skeletonPos.y - height / 2,
                width,
                height
        );

        return hitbox;

    }

    public enum attackDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    @Override
    public void process(WorldEntities worldEntities) {

        attackCooldown -= Gdx.graphics.getDeltaTime();

        for (Entity e : worldEntities.getEntities(Skeleton.class)) {
            if (attackCooldown <= 0){
                attack(e);
                attackCooldown = 2.0f;
                //System.out.println("process attack");
            }
        }

    }

}
