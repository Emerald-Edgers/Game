package dk.ee.zg;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IEnemy;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IEntityProcessService;

public class SkeletonControlSystem implements IEntityProcessService, IEnemy {

    /**
     * The main singular attack for the skeleton enemy.
     * @param entity - the Skeleton enemy that uses the attack
     * @return - returns aoe rectangle around the entity
     */
    @Override
    public Rectangle attack(final Entity entity) {

        Vector2 skeletonPos = entity.getPosition();

        float width = 6f;
        float height = 6f;

        Rectangle hitbox = new Rectangle(
                skeletonPos.x - width / 2,
                skeletonPos.y - height / 2,
                width,
                height
        );

        return hitbox;

    }

    @Override
    public void process(final WorldEntities worldEntities) {

        /*attackCooldown -= Gdx.graphics.getDeltaTime();

        for (Entity e : worldEntities.getEntities(Skeleton.class)) {
            if (attackCooldown <= 0){
                attack(e);
                attackCooldown = 2.0f;
                //System.out.println("process attack");
            }
        }

         */

    }

}
