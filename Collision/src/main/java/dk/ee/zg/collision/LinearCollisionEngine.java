package dk.ee.zg.collision;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.data.WorldObstacles;
import dk.ee.zg.common.map.services.ICollisionEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LinearCollisionEngine implements ICollisionEngine {

    /**
     * main entrance to collision engine, for checking if any entities,
     * collides with obstacles or other entities.
     * @param worldEntities - Object of WorldEntities,
     *                      contains a map of all entities on map
     * @param worldObstacles - Object of WorldObstacles,
     *                       contains a map of all obstacles on map
     */
    @Override
    public void process(final WorldEntities worldEntities,
                        final WorldObstacles worldObstacles) {
        for (Entity e1 : worldEntities.getEntities()) {
            for (Entity e2 : worldEntities.getEntities()) {
                //if the two entities are the same,
                // e.g id=HJKdsf73 & id=HJKdsf73,
                //then don't check collision
                if (e1.getId().equals(e2.getId())) {
                    break;
                }

                if (e1.getEntityType() == EntityType.Projectile) {
                    break;
                }
                if (e2.getEntityType() == EntityType.Projectile) {
                    continue;
                }


                if (collidesWithEntity(e1, e2)) {
                    resolveCollisionEntities(e1, e2);
                }
            }

            for (Rectangle rect : worldObstacles.getVisibleObstacles()) {
                if (collidesWithRectangle(e1, rect)) {
                    if (e1.getEntityType() == EntityType.Projectile) {
                        resolveCollisionProjectileObstacle(e1 , worldEntities);
                    }
                    resolveCollisionObstacles(e1, rect);
                }
            }
        }
    }

    private Rectangle getBoundingRectangleForCollision(Entity entity) {
        if (entity.getHitbox() != null) {
            return entity.getHitbox();
        }

        if (entity.getCurrentAnimation() != null) {
            Map<String, Animation<TextureRegion>> animations = entity.getAnimations();
            String currentAnimationName = entity.getCurrentAnimation();

            if (animations != null && animations.containsKey(currentAnimationName)) {
                Animation<TextureRegion> animation = animations.get(currentAnimationName);
                TextureRegion currentFrame = animation.getKeyFrame(entity.getStateTime(), true);

                Vector2 scale = entity.getScale();
                Vector2 position = entity.getPosition();

                float width = currentFrame.getRegionWidth() * scale.x;
                float height = currentFrame.getRegionHeight() * scale.y;
                float x = position.x - width / 2f;
                float y = position.y - height / 2f;

                return new Rectangle(x, y, width, height);
            }
        }
        return entity.getSprite().getBoundingRectangle();
    }




    private void resolveCollisionProjectileObstacle(final Entity projectile, WorldEntities worldEntities) {
        System.out.println("Projectile hit something");
        worldEntities.removeEntity(projectile.getId());
        //TODO: HIT PROJECTILE
    }

    /**
     * resolves the collision of movable entity1 with movable entity2,
     * by moving enity1 away from entity2, in direction
     * of lest overlapping side.
     * @param entity1 - entity to move away
     * @param entity2 - entity to use for collision calculation
     */
    private void resolveCollisionEntities(final Entity entity1,
                                  final Entity entity2) {
        //Rectangle e1Rect = entity1.getSprite().getBoundingRectangle();
        //Rectangle e2Rect = entity2.getSprite().getBoundingRectangle();

        Rectangle e1Rect = getBoundingRectangleForCollision(entity1);
        Rectangle e2Rect = getBoundingRectangleForCollision(entity2);

        /*
        float xOverlap = Math.min(e1Rect.getX() + e1Rect.getWidth(),
                e2Rect.getX() + e2Rect.getWidth())
                - Math.max(e1Rect.getX(), e2Rect.getX());
        float yOverlap = Math.min(e1Rect.getY() + e1Rect.getHeight(),
                e2Rect.getY() + e2Rect.getHeight())
                - Math.max(e1Rect.getY(), e2Rect.getY());

         */

        float xOverlap = Math.min(e1Rect.x + e1Rect.width,
                e2Rect.x + e2Rect.width)
                - Math.max(e1Rect.x, e2Rect.x);
        float yOverlap = Math.min(e1Rect.y + e1Rect.height,
                e2Rect.y + e2Rect.height)
                - Math.max(e1Rect.y, e2Rect.y);

        if (xOverlap <= 0 || yOverlap <=0) {
            return;
        }


        Vector2 diffVec = new Vector2();

        if (xOverlap < yOverlap) {
            diffVec.x = (e1Rect.getX() < e2Rect.getX()) ? -xOverlap : xOverlap;
        } else {
            diffVec.y = (e1Rect.getY() < e2Rect.getY()) ? -yOverlap : yOverlap;
        }

        //move entity1 by adding a difference vec,
        //move entity2 by subbing a difference vec,
        //to make both move a bit, pushing each other
        entity1.setPosition(entity1.getPosition().add(diffVec.scl(0.8f)));
        entity2.setPosition(entity2.getPosition().sub(diffVec.scl(0.8f)));
    }

    /**
     * resolves the collision of movable entity and non-movable obstacle,
     * by moving enity1 away from obstacle, in direction
     * of lest overlapping side.
     * @param entity1 - entity to move away
     * @param rect - rectangle to use for collision calculation
     */
    private void resolveCollisionObstacles(final Entity entity1,
                                          final Rectangle rect) {
        //Rectangle e1Rect = entity1.getSprite().getBoundingRectangle();

        Rectangle e1Rect = getBoundingRectangleForCollision(entity1);

        float xOverlap = Math.min(e1Rect.getX() + e1Rect.getWidth(),
                rect.getX() + rect.getWidth())
                - Math.max(e1Rect.getX(), rect.getX());
        float yOverlap = Math.min(e1Rect.getY() + e1Rect.getHeight(),
                rect.getY() + rect.getHeight())
                - Math.max(e1Rect.getY(), rect.getY());


        Vector2 diffVec = new Vector2();

        if (xOverlap < yOverlap) {
            diffVec.x = (e1Rect.getX() < rect.getX()) ? -xOverlap : xOverlap;
        } else {
            diffVec.y = (e1Rect.getY() < rect.getY()) ? -yOverlap : yOverlap;
        }

        entity1.setPosition(entity1.getPosition().add(diffVec));
    }


    /**
     * check if entity1 overlaps with entity2.
     * @param entity1 - entity to check if overlaps other
     * @param entity2 - entity to check overlaps on
     * @return - boolean true if overlaps, else false
     */
    @Override
    public boolean collidesWithEntity(final Entity entity1,
                                      final Entity entity2) {
        //Rectangle e1Rectangle = entity1.getSprite().getBoundingRectangle();
        //Rectangle e2Rectangle = entity2.getSprite().getBoundingRectangle();

        Rectangle e1Rectangle = getBoundingRectangleForCollision(entity1);
        Rectangle e2Rectangle = getBoundingRectangleForCollision(entity2);

        return e1Rectangle.overlaps(e2Rectangle);
    }

    /**
     * check if entity overlaps with rectangle.
     * @param entity - entity to check if overlaps other
     * @param rectangle - rectangle to check overlaps on
     * @return - boolean true if overlaps, else false
     */
    @Override
    public boolean collidesWithRectangle(final Entity entity,
                                         final Rectangle rectangle) {

        //Rectangle eRectangle = entity.getSprite().getBoundingRectangle();

        Rectangle eRectangle = getBoundingRectangleForCollision(entity);

        return eRectangle.overlaps(rectangle);
    }

    /**
     * check if entity overlaps with any entities in list.
     * @param entity - entity to check if overlaps other
     * @param entities - entities to check overlaps on
     * @return - boolean Optional of entity first collided with,
     *           else empty optional
     */
    @Override
    public Optional<Entity> collidesWithEntities(final Entity entity,
                                                 final List<Entity> entities) {

        Rectangle eRectangle = getBoundingRectangleForCollision(entity);
        for (Entity e : entities) {

            if (entity.getId().equals(e.getId())) continue;

            if (eRectangle.overlaps(getBoundingRectangleForCollision(e))) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    /**
     * check if entity overlaps with any rectangles in list.
     * @param entity - entity to check if overlaps other
     * @param rectangles - rectangles to check overlaps on
     * @return - Optional of rectangle first collided with,
     *          else empty optional
     */
    @Override
    public Optional<Rectangle> collidesWithRectangles(
            final Entity entity, final List<Rectangle> rectangles) {
        Rectangle eRectangle = getBoundingRectangleForCollision(entity);
        for (Rectangle r : rectangles) {
            if (eRectangle.overlaps(r)) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    /**
     * check if rectangle overlaps with any entities in list.
     * @param rectangle - rectangle to check if overlaps other
     * @param entities - entities to check overlaps on
     * @return - List of entities collided with
     */
    @Override
    public List<Entity> rectangleCollidesWithEntities(
            final Rectangle rectangle, final List<Entity> entities) {
        List<Entity> entitiesCollidedWith = new ArrayList<Entity>();
        for (Entity e : entities) {
            if (rectangle.overlaps(getBoundingRectangleForCollision(e))) {
                entitiesCollidedWith.add(e);
            }
        }
        return entitiesCollidedWith;
    }

}
