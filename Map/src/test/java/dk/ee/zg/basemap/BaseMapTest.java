package dk.ee.zg.basemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import dk.ee.zg.common.map.data.WorldObstacles;
import dk.ee.zg.common.test.HeadlessLauncher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BaseMapTest {

    private BaseMap baseMap;
    private TmxMapLoader mockLoader;
    private TiledMap mockMap;
    private WorldObstacles mockObstacles;
    private MapLayers mockLayers;
    private TiledMapTileLayer mockCollisionLayer;

    @BeforeAll
    static void setUpBeforeClass() {
        if (Gdx.app == null) {
            HeadlessLauncher launcher = new HeadlessLauncher();
            launcher.initHeadlessApplication();
        }

        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;

        Gdx.graphics = mock(Graphics.class);
    }

    @BeforeEach
    void setUp() {
        mockLoader = mock(TmxMapLoader.class);
        mockMap = mock(TiledMap.class);
        mockObstacles = mock(WorldObstacles.class);
        mockLayers = mock(MapLayers.class);
        mockCollisionLayer = mock(TiledMapTileLayer.class);

        when(mockLoader.load("present.tmx")).thenReturn(mockMap);
        when(mockMap.getLayers()).thenReturn(mockLayers);
        when(mockLayers.get("Collision")).thenReturn(mockCollisionLayer);
        when(mockLayers.getIndex("Objects")).thenReturn(2);

        baseMap = new BaseMap() {
            @Override
            public OrthogonalTiledMapRenderer createRenderer(TiledMap map, float unitScale) {
                return mock(OrthogonalTiledMapRenderer.class);
            }
        };
    }

@AfterAll
    static void tearDownAfterClass() {
        if (Gdx.app != null) {
            Gdx.app.exit();
        }
    }

    @Test
    void testLoadMapShouldNotThrowIfMapIsFound() {
        assertDoesNotThrow(() -> {
            baseMap.loadMap("present.tmx", 1f, mockObstacles, mockLoader);
        }, "No Exceptions should occur during normal conditions.");
    }

    @Test
    void testLoadMapShouldThrowNullPointerExceptionIfMapIsNull() {
        assertThrows(NullPointerException.class, () -> {
            baseMap.loadMap(null, 1f, mockObstacles, mockLoader);
        }, "Method should not execute," +
                " and should throw NullPointerException.");
    }

    @Test
    void testGetObstaclesFromLayerAddCorrectObstacles() {
        WorldObstacles obstacles = new WorldObstacles();

        TiledMapTileLayer mockLayer = mock(TiledMapTileLayer.class);
        int tileWidth = 32;
        int tileHeight = 32;

        when(mockLayer.getTileWidth()).thenReturn(tileWidth);
        when(mockLayer.getTileHeight()).thenReturn(tileHeight);
        when(mockLayer.getWidth()).thenReturn(2);
        when(mockLayer.getHeight()).thenReturn(2);

        // Create cell (1,1) with a tile that has the "collision" property
        TiledMapTileLayer.Cell mockCell = mock(TiledMapTileLayer.Cell.class);
        TiledMapTile mockTile = mock(TiledMapTile.class);
        MapProperties properties = new MapProperties();
        properties.put("collision", true);

        when(mockCell.getTile()).thenReturn(mockTile);
        when(mockTile.getProperties()).thenReturn(properties);

        // Only return a valid cell at position (1, 1)
        when(mockLayer.getCell(anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            return (x == 1 && y == 1) ? mockCell : null;
        });

        Collection<Rectangle> result = baseMap.getObstaclesFromLayer(mockLayer, obstacles, 1f);
        assertEquals(1, result.size());
        Rectangle rect = result.iterator().next();
        assertEquals(32f, rect.x);
        assertEquals(32f, rect.y);
        assertEquals(32f, rect.width);
        assertEquals(32f, rect.height);
    }

    @Test
    void testGetObstaclesFromLayerAddsNoObstaclesWhenNoCollisionTiles() {
        WorldObstacles obstacles = new WorldObstacles();

        TiledMapTileLayer mockLayer = mock(TiledMapTileLayer.class);
        int tileWidth = 32;
        int tileHeight = 32;

        when(mockLayer.getTileWidth()).thenReturn(tileWidth);
        when(mockLayer.getTileHeight()).thenReturn(tileHeight);
        when(mockLayer.getWidth()).thenReturn(2);
        when(mockLayer.getHeight()).thenReturn(2);

        // Create cells without a tile (no collision property)
        TiledMapTileLayer.Cell cell = mock(TiledMapTileLayer.Cell.class);
        when(cell.getTile()).thenReturn(null);

        when(mockLayer.getCell(anyInt(), anyInt())).thenReturn(cell);

        baseMap.getObstaclesFromLayer(mockLayer, obstacles, 1f);

        assertTrue(obstacles.getObstacles().isEmpty(),
                "Expected no obstacles to be added.");
    }
}