package dk.ee.zg.common.enemy.interfaces;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldObstacles;

import java.util.List;

/**
 * interface for pathfinding implementation.
 */
public interface IPathFinder {
    /**
     * method for processing pathfinding implementation.
     * @param origin - origin for pathfinding from, and moving
     * @param target - target of pathfinding e.g. player
     * @return - returns path to target from origin,
     * as a list of vector 2 coordinates.
     */
    List<Vector2> process(Entity origin, Entity target);
    /**
     * method for loading the collidable obstacles.
     * @param obstacles - instance of WorldObstacles,
     *                  contains all collidable obstacles.
     * @param map - TiledMap containing all tilemap data.
     */
    void load(WorldObstacles obstacles, TiledMap map);
}
