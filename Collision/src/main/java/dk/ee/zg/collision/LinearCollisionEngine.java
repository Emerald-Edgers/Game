package dk.ee.zg.collision;

import com.badlogic.gdx.math.Rectangle;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.data.WorldObstacles;
import dk.ee.zg.common.map.services.ICollisionEngine;

import java.util.List;
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
        System.out.println("Collision");
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
        Rectangle e1Rectangle = entity1.getSprite().getBoundingRectangle();
        Rectangle e2Rectangle = entity2.getSprite().getBoundingRectangle();
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
        Rectangle eRectangle = entity.getSprite().getBoundingRectangle();
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
        Rectangle eRectangle = entity.getSprite().getBoundingRectangle();
        for (Entity e : entities) {
            if (eRectangle.overlaps(e.getSprite().getBoundingRectangle())) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    /**
     * check if entity overlaps with any rectangles in list.
     * @param entity - entity to check if overlaps other
     * @param rectangles - rectangles to check overlaps on
     * @return - boolean Optional of rectangle first collided with,
     *          else empty optional
     */
    @Override
    public Optional<Rectangle> collidesWithRectangles(
            final Entity entity, final List<Rectangle> rectangles) {
        Rectangle eRectangle = entity.getSprite().getBoundingRectangle();
        for (Rectangle r : rectangles) {
            if (eRectangle.overlaps(r)) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }
}
