package dk.ee.zg.common.map.services;

import com.badlogic.gdx.math.Rectangle;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.data.WorldObstacles;

import java.util.List;
import java.util.Optional;

public interface ICollisionEngine {
    /**
     * main entrance to collision engine, for checking if any entities,
     * collides with obstacles or other entities.
     * @param worldEntities - Object of WorldEntities,
     *                      contains a map of all entities on map
     * @param worldObstacles - Object of WorldObstacles,
     *                       contains a map of all obstacles on map
     */
    void process(WorldEntities worldEntities, WorldObstacles worldObstacles);
    /**
     * check if entity1 collides with entity2.
     * @param entity1 - entity to check if collides other
     * @param entity2 - entity to check collides on
     * @return - boolean true if collides, else false
     */
    boolean collidesWithEntity(Entity entity1, Entity entity2);
    /**
     * check if entity collides with rectangle.
     * @param entity - entity to check if collides other
     * @param rectangle - rectangle to check collides on
     * @return - boolean true if collides, else false
     */
    boolean collidesWithRectangle(Entity entity, Rectangle rectangle);
    /**
     * check if entity collides with any entities in list.
     * @param entity - entity to check if collides other
     * @param entities - entities to check collides on
     * @return - boolean Optional of entity first collided with,
     *           else empty optional
     */
    Optional<Entity> collidesWithEntities(Entity entity, List<Entity> entities);
    /**
     * check if entity collides with any rectangles in list.
     * @param entity - entity to check if collides other
     * @param rectangles - rectangles to check collides on
     * @return - Optional of rectangle first collided with,
     *          else empty optional
     */
    Optional<Rectangle> collidesWithRectangles(
            Entity entity, List<Rectangle> rectangles);

    /**
     * check if rectangle collides with any entities in list.
     * @param rectangle - rectangle to check if collides other
     * @param entities - entities to check collides on
     * @return - List of entities collided with
     */
    List<Entity> rectangleCollidesWithEntities(
            Rectangle rectangle, List<Entity> entities);

    /**
     * Checks if a given rectangle collides with another rectangle in a list.
     * @param rectangle The rectangle to base the check upon
     * @param rectangles The rectangles to check against.
     * @return - Optional of the first rectangle to have
     * collided with else empty.
     */
    Optional<Rectangle> rectangleCollidesWithRectangles(
            Rectangle rectangle, List<Rectangle> rectangles);

}
