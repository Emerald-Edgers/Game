package dk.ee.zg.common.map.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.data.EventManager;
import dk.ee.zg.common.data.Events;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EntityTest {
    private Entity entity;

    @BeforeAll
    static void setUpBeforeClass() throws IOException {
        if (Gdx.app == null) {
            HeadlessLauncher launcher = new HeadlessLauncher();
            launcher.initHeadlessApplication();
        }

        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;

        Gdx.graphics = mock(Graphics.class);

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, 0xFFFFFFFF);
        File file = new File("src/test/resources/test.png");
        ImageIO.write(image, "png", file);
    }

    @BeforeEach
    void setUp() {
        entity = new Entity(new Vector2(5, 5),
                0, new Vector2(1, 1),
                "src/test/resources/test.png", EntityType.Enemy);
    }

    @AfterAll
    static void tearDownAfterClass() throws IOException {
        if (Gdx.app != null) {
            Gdx.app.exit();
        }

        Files.deleteIfExists(Paths.get("src/test/resources/test.png"));
    }

    @Test
    void testHitShouldDecreaseHealthByDamage() {
        entity.setHp(100);

        entity.hit(75);

        assertEquals(25, entity.getHp(),
                "HP should end up being 100-75=25");
    }

    @Test
    void testHitShouldEmitEnemyKilledEventWhenHpAt0() {
        entity.setHp(100);

        AtomicBoolean enemyKilledCalled = new AtomicBoolean(false);
        EventManager.addListener(Events.EnemyKilledEvent.class, event -> {
            enemyKilledCalled.set(true);
        });

        entity.hit(100);

        assertTrue(enemyKilledCalled.get(),
                "Enemy Killed Event should have been called");
    }

    @Test
    void testHitShouldEmitEnemyKilledEventWhenBelow0Hp() {
        entity.setHp(100);

        AtomicBoolean enemyKilledCalled = new AtomicBoolean(false);
        EventManager.addListener(Events.EnemyKilledEvent.class, event -> {
            enemyKilledCalled.set(true);
        });

        entity.hit(125);

        assertTrue(enemyKilledCalled.get(),
                "Enemy Killed Event should have been called");
    }

    @Test
    void testHitObstacleShouldNotTakeDamage() {
        Entity obstacle = new Entity(new Vector2(5, 5),
                0, new Vector2(1, 1),
                "src/test/resources/test.png", EntityType.Obstacle);

        obstacle.setHp(100);

        obstacle.hit(50);

        assertEquals(100, obstacle.getHp(),
                "Hp should not decrease on obstacles.");
    }
}

