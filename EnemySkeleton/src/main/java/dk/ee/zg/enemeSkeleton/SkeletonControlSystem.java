package dk.ee.zg.enemeSkeleton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IEnemy;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.services.IEntityProcessService;
import dk.ee.zg.player.Player;

public class SkeletonControlSystem implements IEntityProcessService, IEnemy {

    private Entity player;

    /**
     * The main singular attack for the skeleton enemy.
     *
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
    public void process(final WorldEntities world) {

        if (player == null) {
            for (Entity e1 : world.getEntities(Player.class)) {
                if (e1 != null) {
                    player = world.getEntities(Player.class).getFirst();
                }
            }
        }


        for (Entity skeleton : world.getEntities(Skeleton.class)) {
            move((Skeleton) skeleton);
        }


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

    /**
     * Moves the skeleton based on the player's position.
     * @param skeleton {@link Skeleton}
     */
    public void move(final Skeleton skeleton) {

        Vector2 skeletonPos = skeleton.getPosition();
        Vector2 playerPos = player.getPosition();

        //System.out.println("playerPos:" + playerPos);
        //System.out.println("skeletonPos:" + skeletonPos);

        Vector2 dir = new Vector2(playerPos.x - skeletonPos.x, playerPos.y - skeletonPos.y);
        dir.nor();
        float speed = skeleton.getMoveSpeed();

        Vector2 newPosition = new Vector2(
                skeletonPos.x + dir.x * speed * Gdx.graphics.getDeltaTime(),
                skeletonPos.y + dir.y * speed * Gdx.graphics.getDeltaTime()
        );

        skeleton.setPosition(newPosition);



    }

}
