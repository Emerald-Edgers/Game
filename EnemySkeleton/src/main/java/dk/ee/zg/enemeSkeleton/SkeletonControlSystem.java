package dk.ee.zg.enemeSkeleton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IEnemy;
import dk.ee.zg.common.map.data.AnimationState;
import dk.ee.zg.common.enemy.interfaces.IPathFinder;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.data.WorldObstacles;
import dk.ee.zg.common.map.services.IEntityProcessService;
import dk.ee.zg.player.Player;

import java.util.Optional;
import java.util.ServiceLoader;

public class SkeletonControlSystem implements IEntityProcessService, IEnemy {

    /**
     * The player which the skeletons should target.
     */
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


    /**
     * Process that runs once per frame, will attempt to locate the player.
     * If no player is found the loop breaks.
     * @param world - Object of WorldEntities,
     *                     contains a map of all entities on map
     */
    @Override
    public void process(final WorldEntities world, final WorldObstacles worldObstacles) {

        if (player == null) {
            Optional<Entity> tempPlayer = world.getEntities(
                    Player.class).stream().findFirst();
            if (tempPlayer.isPresent()) {
                player = tempPlayer.get();
            } else {
                return;
            }
        }

        for (Entity skele : world.getEntities(Skeleton.class)) {
            //System.out.println(skele.getPosition());
            Skeleton skeleton = (Skeleton) skele;
            updateSkeletonAnimation(skeleton);
            setSkeletonAnimationState(skeleton,AnimationState.RUN);
            Optional<IPathFinder> pathFinder =
                    ServiceLoader.load(IPathFinder.class).findFirst();
            // if pathfinder available move with pathfinding,
            // else move with normal movement way.
            if (pathFinder.isPresent()) {
                skeleton.moveWithPathFinding(pathFinder.get(), player);
            } else {
                move(skeleton,worldObstacles);
            }
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
    private void move(final Skeleton skeleton, final WorldObstacles worldObstacles) {

        Vector2 skeletonPos = skeleton.getPosition();
        Rectangle hitbox = skeleton.getHitbox();
        Rectangle tempHitBox = new Rectangle(hitbox);

        Vector2 playerPos = player.getPosition();

        Vector2 dir = new Vector2(
                playerPos.x - skeletonPos.x, playerPos.y - skeletonPos.y);
        dir.nor();
        float speed = skeleton.getMoveSpeed();

        boolean canMoveX = true;
        boolean canMoveY = true;

        tempHitBox.x = hitbox.x + dir.x;
        tempHitBox.y = hitbox.y + dir.y;

        for (Rectangle obstacle : worldObstacles.getVisibleObstacles()) {
            if (tempHitBox.overlaps(obstacle)) {
                canMoveX = false;
                break;
            }
        }

        tempHitBox.y = hitbox.y + dir.y;
        tempHitBox.x = hitbox.x;

        for (Rectangle obstacle : worldObstacles.getVisibleObstacles()) {
            if (tempHitBox.overlaps(obstacle)) {
                canMoveY = false;
                break;
            }
        }

        Vector2 finalMove = new Vector2();

        if(canMoveX) {
            finalMove.x = dir.x;
        }

        if (canMoveY) {
            finalMove.y = dir.y;
        }

        if (canMoveX || canMoveY) {
            skeletonPos.add(finalMove);
            skeleton.setPosition(skeletonPos);
        }

    }

    private boolean setSkeletonAnimationState(Skeleton skeleton, AnimationState state) {
        AnimationState currentState = skeleton.getCurrentState();

        if(currentState == AnimationState.DEATH) {
            return false;
        }
        if (currentState == AnimationState.MELEEATTACK) {
            Animation<TextureRegion> currentAnimation = skeleton.getAnimations().get("ATTACK");
            if (currentAnimation != null && !currentAnimation.isAnimationFinished(skeleton.getStateTime())) {
                return false;
            }
        }
        skeleton.setState(state);
        return true;
    }

    private void updateSkeletonAnimation(Skeleton skeleton) {
        if (skeleton.getCurrentState() != AnimationState.IDLE && skeleton.isAnimationFinished() && skeleton.getCurrentState() != AnimationState.DEATH) {
            setSkeletonAnimationState(skeleton, AnimationState.IDLE);
        }
    }

}
