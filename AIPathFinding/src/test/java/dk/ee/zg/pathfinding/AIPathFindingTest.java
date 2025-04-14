package dk.ee.zg.pathfinding;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.WorldObstacles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AIPathFindingTest {
    private TiledMap map;
    private TiledMapTileLayer layer;
    private MapLayers layers;
    private WorldObstacles obstacles;
    private AIPathFinding pathFinder;

    @BeforeEach
    void setUp() {
        map = mock(TiledMap.class);
        layer = mock(TiledMapTileLayer.class);
        layers = mock(MapLayers.class);
        obstacles = new WorldObstacles();

        when(map.getLayers()).thenReturn(layers);
        when(layers.get("Collision")).thenReturn(layer);
        when(layer.getWidth()).thenReturn(10);
        when(layer.getHeight()).thenReturn(10);
        when(layer.getTileWidth()).thenReturn(32);
        when(layer.getTileHeight()).thenReturn(32);

        pathFinder = new AIPathFinding();
    }

    @Test
    void testLoadCorrectlyMarksObstaclesAsNonWalkable() {
        // Add a known obstacle at tile (2, 3)
        Rectangle obstacle = new Rectangle(2, 3, 1, 1);

        obstacles.addObstacle(UUID.randomUUID(), obstacle);

        pathFinder.load(obstacles, map);

        boolean[][] grid = AIPathFinding.getWalkableGrid();

        assertFalse(grid[2][3], "Tile (2,3) should be marked non-walkable");
        assertTrue(grid[0][0], "Tile (0,0) should still be walkable");
        assertTrue(grid[9][9], "Tile (9,9) should still be walkable");
    }

    @Test
    void testProcessReturnsNonEmptyPath() {
        pathFinder.load(obstacles, map); // no obstacles

        Entity start = mock(Entity.class);
        Entity end = mock(Entity.class);

        // Positions that land on grid tiles (2,2) and (6,6)
        when(start.getPosition()).thenReturn(new Vector2(2, 2));
        when(end.getPosition()).thenReturn(new Vector2(6, 6));

        List<Vector2> path = pathFinder.process(start, end);

        assertNotNull(path, "Path should not be null");
        assertFalse(path.isEmpty(), "Path should not be empty");
        assertEquals(2, path.get(0).x);
        assertEquals(2, path.get(0).y);
    }
}