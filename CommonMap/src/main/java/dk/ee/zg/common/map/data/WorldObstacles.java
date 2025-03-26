package dk.ee.zg.common.map.data;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import dk.ee.zg.common.data.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldObstacles {

    /**
     * Map of all obstacles.
     */
    private Map<UUID, Rectangle> obstaclesMap;

    /**
     * Map of all obstacles in players viewport.
     */
    private Map<UUID, Rectangle> currentVisibleObstaclesMap;

    /**
     * Constructor for worldobstacles.
     */
    public WorldObstacles() {
        this.obstaclesMap = new HashMap<>();
        this.currentVisibleObstaclesMap = new HashMap<>();
    }

    /**
     * Add an obstacle to the map of all obstacles.
     * @param obstacleID UUID of obstacle
     * @param obstacle Rectangle with position of obstacle
     */
    public void addObstacle(final UUID obstacleID, final Rectangle obstacle) {
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
    public void removeObstacle(final UUID obstacleID) {
        obstaclesMap.remove(obstacleID);
    }

    /**
     * Get obstacle from UUID.
     * @param obstacleID UUID for obstacle to be returned.
     * @return Obstacle Rectangle.
     */
    public Rectangle getObstacle(final UUID obstacleID) {
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
        GameData gameData = GameData.getInstance();
        OrthographicCamera camera = gameData.getCamera();
        float x =
                camera.position.x - (camera.viewportWidth / 2);
        float y =
                camera.position.y - (camera.viewportHeight / 2);
        Rectangle viewportRectangle = new Rectangle(x, y,
                camera.viewportWidth, camera.viewportHeight);
        for (Map.Entry<UUID, Rectangle> entry : obstaclesMap.entrySet()) {
            if (entry.getValue().overlaps(viewportRectangle)) {
                currentVisibleObstaclesMap.put(
                        entry.getKey(), entry.getValue());
            }
        }
    }

}
