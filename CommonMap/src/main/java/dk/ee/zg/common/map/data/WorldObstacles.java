package dk.ee.zg.common.map.data;

import com.badlogic.gdx.math.Rectangle;
import java.util.Collection;
import java.util.Map;

public class WorldObstacles {

    /**
     * Map of all obstacles.
     */
    private Map<String, Rectangle> obstaclesMap;

    /**
     * Map of all obstacles in players viewport.
     */
    private Map<String, Rectangle> currentVisibleObstaclesMap;

    /**
     * Add an obstacle to the map of all obstacles.
     * @param obstacleID UUID of obstacle
     * @param obstacle Rectangle with position of obstacle
     */
    public void addObstacle(final String obstacleID, final Rectangle obstacle) {
        obstaclesMap.put(obstacleID, obstacle);
    }

    /**
     * Remove an obstacle by specific Rectangle.
     * @param obstacle Rectangle to delete from map.
     */
    public void removeObstacle(final Rectangle obstacle) {
        obstaclesMap.values().remove(obstacle);
    }

    /**
     * Remove an obstacle with UUID.
     * @param obstacleID UUID for the obstacle ro be removed.
     */
    public void removeObstacle(final String obstacleID) {
        obstaclesMap.remove(obstacleID);
    }

    /**
     * Get obstacle from UUID.
     * @param obstacleID UUID for obstacle to be returned.
     * @return Obstacle Rectangle.
     */
    public Rectangle getObstacle(final String obstacleID) {
        return obstaclesMap.get(obstacleID);
    }

    /**
     * Get all obstacles on the map.
     * @return Collection of all obstacle Rectangles.
     */
    public Collection<Rectangle> getObstacles() {
        return obstaclesMap.values();
    }

    /**
     * Get all obstacles visible to the player.
     * @return Collection of all obstacle Rectangles visible to the player.
     */
    public Collection<Rectangle> getVisibleObstacles() {
        return currentVisibleObstaclesMap.values();
    }

    /**
     * Method for updating currentVisibleObstacles based on what is visible
     * to the player.
     */
    public void optimizeObstacles() {
        currentVisibleObstaclesMap.clear();
        for (Map.Entry<String, Rectangle> entry : obstaclesMap.entrySet()) {
            if (isVisibleToPlayer(entry.getValue())) {
                currentVisibleObstaclesMap.put(
                        entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Method for checking if an obstacle is visible to the player.
     * @param obstacle Rectangle to check if visible to player.
     * @return True if visible, false if not.
     */
    private boolean isVisibleToPlayer(final Rectangle obstacle) {
        return true;
    }
}
