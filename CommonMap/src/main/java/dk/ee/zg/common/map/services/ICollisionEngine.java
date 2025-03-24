package dk.ee.zg.common.map.services;

import com.badlogic.gdx.math.Rectangle;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.data.WorldObstacles;

import java.util.List;
import java.util.Optional;

public interface ICollisionEngine {

    void process(WorldEntities worldEntities, WorldObstacles worldObstacles);

    boolean collidesWithEntity(Entity entity1, Entity entity2);

    boolean collidesWithRectangle(Entity entity, Rectangle rectangle);

    Optional<Entity> collidesWithEntities(Entity entity, List<Entity> entities);

    Optional<Rectangle> collidesWithRectangles(Entity entity, List<Rectangle> rectangles);

}
