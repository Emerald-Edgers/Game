package dk.ee.zg.common.enemy.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.enemy.interfaces.IPathFinder;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.test.HeadlessLauncher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnemyTest {

    static final class DummyEnemy extends Enemy {
        public DummyEnemy(Vector2 spawnPoint, float rotation, Vector2 scale, String spritePath, EntityType entityType, int aDamage, int aSpeed, int mSpeed, int hp, int def, float val) {
            super(spawnPoint, rotation, scale, spritePath, entityType, aDamage, aSpeed, mSpeed, hp, def, val);
        }
    }

    private Enemy dummyEnemy;
    private final Vector2 spawnPoint = new Vector2(1, 1);

    @BeforeAll
    static void setUpBeforeClass() throws IOException {
        if (Gdx.app == null) {
            HeadlessLauncher launcher = new HeadlessLauncher();
            launcher.initHeadlessApplication();
        }

        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;

        Gdx.graphics = mock(Graphics.class);

        when(Gdx.graphics.getDeltaTime()).thenReturn(0.016f);

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, 0xFFFFFFFF);
        File file = new File("src/test/resources/test.png");
        file.getParentFile().mkdirs();
        ImageIO.write(image, "png", file);
    }

    @BeforeEach
    void setUp() {
        dummyEnemy = new DummyEnemy(
                spawnPoint, 0, new Vector2(1, 1),
                "src/test/resources/test.png", EntityType.Enemy,
                5, 5, 5, 100, 50, 10
        );
    }

    @AfterAll
    static void tearDownAfterClass() throws IOException {
        if (Gdx.app != null) {
            Gdx.app.exit();
        }

        Files.deleteIfExists(Paths.get("src/test/resources/test.png"));
    }

    @Test
    void testIntegrationEnemyPathfindingMovementShouldMoveEnemy() {
        IPathFinder mockPathFinder = mock(IPathFinder.class);
        Entity player = mock(Entity.class);

        List<Vector2> path = new ArrayList<>();
        path.add(new Vector2(10, 1));
        when(mockPathFinder.process(any(Entity.class), any(Entity.class))).thenReturn(path);

        // Simulate 5 frames
        for (int i = 0; i < 5; i++) {
            dummyEnemy.moveWithPathFinding(mockPathFinder, player);
        }

        assertNotEquals(spawnPoint, dummyEnemy.getPosition(),
                "Enemy should have moved away from its spawn");
        assertTrue(dummyEnemy.getPosition().x > 0,
                "Enemy should have moved closer to its target");
    }

    @Test
    void testIntegrationEnemyPathfindingMovementShouldNotMoveEnemyWhenNoPathIsFound() {
        IPathFinder mockPathFinder = mock(IPathFinder.class);
        Entity player = mock(Entity.class);

        List<Vector2> path = new ArrayList<>();
        when(mockPathFinder.process(any(Entity.class), any(Entity.class))).thenReturn(path);

        // Simulate 5 frames
        for (int i = 0; i < 5; i++) {
            dummyEnemy.moveWithPathFinding(mockPathFinder, player);
        }

        assertEquals(spawnPoint, dummyEnemy.getPosition(),
                "Enemy should not have moved without a path");
    }

    @Test
    void testIntegrationEnemyPathFindingMovementShouldWorkWithMultipleSubsteps() {
        IPathFinder mockPathFinder = mock(IPathFinder.class);
        Entity player = mock(Entity.class);

        List<Vector2> path = new ArrayList<>();
        path.add(new Vector2(3, 3));
        path.add(new Vector2(6, 6));

        when(mockPathFinder.process(any(Entity.class), any(Entity.class))).thenReturn(path);

        // Simulate 5 frames
        for (int i = 0; i < 5; i++) {
           dummyEnemy.moveWithPathFinding(mockPathFinder, player);
        }

        assertNotEquals(spawnPoint, dummyEnemy.getPosition(),
                "Enemy should have moved away from its spawn");
        assertTrue(dummyEnemy.getPosition().x > 0,
                "Enemy should have moved closer to its target");

    }
}