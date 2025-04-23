package dk.ee.zg.common.map.data;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import dk.ee.zg.common.data.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorldObstaclesTest {

    private WorldObstacles worldObstacles;
    private UUID id1;
    private UUID id2;
    private Rectangle rect1;
    private Rectangle rect2;

    @BeforeEach
    void setUp() {
        worldObstacles = new WorldObstacles();
        id1 = UUID.randomUUID();
        id2 = UUID.randomUUID();

        rect1 = new Rectangle(5, 5, 2, 2); // Inside
        rect2 = new Rectangle(100, 100, 2, 2); // Outside
    }

    @Test
    void testAddAndGetObstacle() {
        worldObstacles.addObstacle(id1, rect1);

        assertEquals(rect1, worldObstacles.getObstacle(id1));
    }

    @Test
    void testRemoveObstacleByUUID() {
        worldObstacles.addObstacle(id1, rect1);

        worldObstacles.removeObstacle(id1);

        assertNull(worldObstacles.getObstacle(id1));
    }

    @Test
    void testRemoveObstacleByRectangle() {
        worldObstacles.addObstacle(id1, rect1);

        worldObstacles.removeObstacle(rect1);

        assertTrue(worldObstacles.getObstacles().isEmpty());
    }

    @Test
    void testRemoveObstacleByRectangleRemovesAllInstances() {
        worldObstacles.addObstacle(id1, rect1);
        worldObstacles.addObstacle(id1, rect1);

        worldObstacles.removeObstacle(rect1);

        assertTrue(worldObstacles.getObstacles().isEmpty());
    }

    @Test
    void testRemoveObstaclesByUUID() {
        worldObstacles.addObstacle(id1, rect1);

        worldObstacles.removeObstacle(id1);

        assertTrue(worldObstacles.getObstacles().isEmpty());
    }

    @Test
    void testRemoveObstaclesByUUIDRemovesAllInstances() {
        worldObstacles.addObstacle(id1, rect1);
        worldObstacles.addObstacle(id1, rect1);

        worldObstacles.removeObstacle(rect1);

        assertTrue(worldObstacles.getObstacles().isEmpty());
    }

    @Test
    void testGetObstaclesReturnsAll() {
        worldObstacles.addObstacle(id1, rect1);
        worldObstacles.addObstacle(id2, rect2);

        Collection<Rectangle> obstacles = worldObstacles.getObstacles();

        assertTrue(obstacles.contains(rect1));
        assertTrue(obstacles.contains(rect2));
    }

    @Test
    void testOptimizeObstaclesReturnsVisibleObstaclesWhenPresent() {
        worldObstacles.addObstacle(id1, rect1);
        worldObstacles.addObstacle(id2, rect2);

        try (MockedStatic<GameData> mockedGameData = mockStatic(GameData.class)) {
            GameData mockGameData = mock(GameData.class);
            OrthographicCamera camera = new OrthographicCamera();
            camera.position.set(5, 5, 0);
            camera.viewportWidth = 10;
            camera.viewportHeight = 10;

            mockedGameData.when(GameData::getInstance).thenReturn(mockGameData);
            when(mockGameData.getCamera()).thenReturn(camera);

            worldObstacles.optimizeObstacles();
        }

        Collection<Rectangle> obstacles = worldObstacles.getVisibleObstacles();
        assertTrue(obstacles.contains(rect1),
                "Rect1 obstacle should be visible");
        assertFalse(obstacles.contains(rect2),
                "Rect2 obstacle should not be visible");
    }

    @Test
    void testOptimizeObstaclesReturnsNoVisibleObstaclesWhenAbsent() {
        worldObstacles.addObstacle(id2, rect2);

        try (MockedStatic<GameData> mockedGameData = mockStatic(GameData.class)) {
            GameData mockGameData = mock(GameData.class);
            OrthographicCamera camera = new OrthographicCamera();
            camera.position.set(5, 5, 0);
            camera.viewportWidth = 10;
            camera.viewportHeight = 10;

            mockedGameData.when(GameData::getInstance).thenReturn(mockGameData);
            when(mockGameData.getCamera()).thenReturn(camera);

            worldObstacles.optimizeObstacles();
        }

        Collection<Rectangle> obstacles = worldObstacles.getVisibleObstacles();
        assertTrue(obstacles.isEmpty(),
                "There should be no visible obstacles");
    }

    @Test
    void testIntegrationOptimizeShouldRemoveObstaclesNoLongerVisible() {
        worldObstacles.addObstacle(id1, rect1);
        worldObstacles.addObstacle(id2, rect2);

        try (MockedStatic<GameData> mockedGameData = mockStatic(GameData.class)) {
            GameData mockGameData = mock(GameData.class);
            OrthographicCamera camera = new OrthographicCamera();
            camera.position.set(5, 5, 0);
            camera.viewportWidth = 10;
            camera.viewportHeight = 10;

            mockedGameData.when(GameData::getInstance).thenReturn(mockGameData);
            when(mockGameData.getCamera()).thenReturn(camera);

            worldObstacles.optimizeObstacles();

            Collection<Rectangle> obstacles = worldObstacles.getVisibleObstacles();

            assertTrue(obstacles.contains(rect1),
                    "Rect1 obstacle should be visible at this time");
            assertFalse(obstacles.contains(rect2),
                    "Rect2 obstacle should not be visible at this time");

            camera.position.set(-10, -10, 0);

            worldObstacles.optimizeObstacles();

            obstacles = worldObstacles.getVisibleObstacles();

            assertTrue(obstacles.isEmpty(),
                    "There should be no visible obstacles at this time");
        }



    }
}