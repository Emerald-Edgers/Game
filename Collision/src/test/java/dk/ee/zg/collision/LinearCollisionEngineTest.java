package dk.ee.zg.collision;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.map.data.Entity;
import dk.ee.zg.common.map.data.EntityType;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.map.data.WorldObstacles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
class LinearCollisionEngineTest {
    private LinearCollisionEngine linearCollisionEngine;

    private WorldEntities worldEntities;
    private WorldObstacles worldObstacles;

    private Entity e1;
    private Entity e2;
    private Entity e3;

    private Rectangle obstacle1;
    private Rectangle obstacle2;

    @BeforeEach
    void setUp() {
        linearCollisionEngine = new LinearCollisionEngine();

        worldEntities = Mockito.mock(WorldEntities.class);
        worldObstacles = Mockito.mock(WorldObstacles.class);

        e1 = Mockito.mock(Entity.class);
        e2 = Mockito.mock(Entity.class);
        e3 = Mockito.mock(Entity.class);

        Sprite e1Sprite = Mockito.mock(Sprite.class);
        Sprite e2Sprite = Mockito.mock(Sprite.class);
        Sprite e3Sprite = Mockito.mock(Sprite.class);

        Rectangle e1Rectangle = new Rectangle(0, 0, 10, 10);
        Rectangle e2Rectangle = new Rectangle(5, 5, 10, 10);
        Rectangle e3Rectangle = new Rectangle(100, 100, 10, 10);

        Mockito.when(e1.getSprite()).thenReturn(e1Sprite);
        Mockito.when(e1Sprite.getBoundingRectangle()).thenReturn(e1Rectangle);

        Mockito.when(e2.getSprite()).thenReturn(e2Sprite);
        Mockito.when(e2Sprite.getBoundingRectangle()).thenReturn(e2Rectangle);

        Mockito.when(e3.getSprite()).thenReturn(e3Sprite);
        Mockito.when(e3Sprite.getBoundingRectangle()).thenReturn(e3Rectangle);

        obstacle1 = new Rectangle(0, 0, 10, 10);
        obstacle2 = new Rectangle(100, 100, 10, 10);
    }

    @Test
    void testProcessWithDifferentCollidingEntities() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Mockito.when(e1.getId()).thenReturn(id1);
        Mockito.when(e2.getId()).thenReturn(id2);

        Mockito.when(e1.getEntityType()).thenReturn(EntityType.Enemy);
        Mockito.when(e2.getEntityType()).thenReturn(EntityType.Enemy);

        Mockito.when(e1.getPosition()).thenReturn(new Vector2(0, 0));
        Mockito.when(e2.getPosition()).thenReturn(new Vector2(0, 0));

        Mockito.when(worldEntities.getEntities()).thenReturn(List.of(e1, e2));

        linearCollisionEngine.process(worldEntities, worldObstacles);

        assertDoesNotThrow(() -> {
            Mockito.verify(e1, Mockito.times(1))
                    .setPosition(Mockito.any());
        }, "Entity 1 should collide exactly once");

        assertDoesNotThrow(() -> {
            Mockito.verify(e2, Mockito.times(1))
                    .setPosition(Mockito.any());
        }, "Entity 2 should collide exactly once");
    }

    @Test
    void testProcessWithSameCollidingEntities() {
        UUID id1 = UUID.randomUUID();
        Mockito.when(e1.getId()).thenReturn(id1);
        Mockito.when(e1.getEntityType()).thenReturn(EntityType.Enemy);
        Mockito.when(e1.getPosition()).thenReturn(new Vector2(0, 0));

        Mockito.when(worldEntities.getEntities()).thenReturn(List.of(e1));

        linearCollisionEngine.process(worldEntities, worldObstacles);

        assertDoesNotThrow(() -> {
            Mockito.verify(e1, Mockito.times(0))
                    .setPosition(Mockito.any());
        }, "Same entity should exit early");
    }

    @Test
    void testProcessWithNonCollidingEntities() {
        UUID id1 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();

        Mockito.when(e1.getId()).thenReturn(id1);
        Mockito.when(e1.getEntityType()).thenReturn(EntityType.Enemy);
        Mockito.when(e1.getPosition()).thenReturn(new Vector2(0, 0));

        Mockito.when(e3.getId()).thenReturn(id3);
        Mockito.when(e3.getEntityType()).thenReturn(EntityType.Enemy);
        Mockito.when(e3.getPosition()).thenReturn(new Vector2(100, 100));

        Mockito.when(worldEntities.getEntities()).thenReturn(List.of(e1, e3));

        linearCollisionEngine.process(worldEntities, worldObstacles);

        assertDoesNotThrow(() -> {
            Mockito.verify(e1, Mockito.times(0))
                    .setPosition(Mockito.any());
        }, "Entity 1 should not collide");
        assertDoesNotThrow(() -> {
            Mockito.verify(e3, Mockito.times(0))
                    .setPosition(Mockito.any());
        }, "Entity 3 should not collide");
    }

    @Test
    void testProcessEntityShouldCollideWithRectangle() {
        UUID id1 = UUID.randomUUID();
        Mockito.when(e1.getId()).thenReturn(id1);
        Mockito.when(e1.getEntityType()).thenReturn(EntityType.Enemy);
        Mockito.when(e1.getPosition()).thenReturn(new Vector2(0, 0));

        Mockito.when(worldEntities.getEntities()).thenReturn(List.of(e1));
        Mockito.when(worldObstacles.getVisibleObstacles()).thenReturn(List.of(obstacle1));

        linearCollisionEngine.process(worldEntities, worldObstacles);

        assertDoesNotThrow(() -> {
            Mockito.verify(e1, Mockito.times(1))
                    .setPosition(Mockito.any());
        }, "Entity 1 should collide with rectangle");
    }

    @Test
    void testCollidesWithEntityShouldReturnTrueWhenColliding() {
        assertTrue(linearCollisionEngine.collidesWithEntity(e1, e2),
                "The Entities should be colliding");
    }

    @Test
    void testCollidesWithEntityShouldReturnFalseWhenNotColliding() {
        assertFalse(linearCollisionEngine.collidesWithEntity(e1, e3),
                "The Entities should not be colliding");
    }

    @Test
    void testCollidesWithRectangleShouldReturnTrueWhenColliding() {
        assertTrue(linearCollisionEngine.collidesWithRectangle(e1, obstacle1),
                "The Rectangle should be colliding");
    }

    @Test
    void testCollidesWithRectangleShouldReturnFalseWhenNotColliding() {
        assertFalse(linearCollisionEngine.collidesWithRectangle(e3, obstacle1),
                "The Rectangle should not be colliding");
    }

    @Test
    void testCollidesWithEntitiesShouldReturnPresentOptionalWhenColliding() {
        List<Entity> collisions = new ArrayList<>();
        collisions.add(e2);
        assertTrue(linearCollisionEngine.collidesWithEntities(e1, collisions).isPresent(),
                "The Entities should be colliding");

    }

    @Test
    void testCollidesWithEntitiesReturnsEmptyOptionalWhenNotColliding() {
        List<Entity> collisions = new ArrayList<>();
        collisions.add(e3);
        assertTrue(linearCollisionEngine.collidesWithEntities(e1, collisions).isEmpty(),
                "The Entities should not be colliding");
    }

    @Test
    void testCollidesWithEntitiesReturnsFirstAddedEntityWhenColliding() {
        Entity closeEntity = Mockito.mock(Entity.class);
        Sprite closeSprite = Mockito.mock(Sprite.class);
        Rectangle closeRectangle = new Rectangle(1, 1, 3, 3);

        Mockito.when(closeEntity.getSprite()).thenReturn(closeSprite);
        Mockito.when(closeSprite.getBoundingRectangle()).thenReturn(closeRectangle);

        List<Entity> collisions = new ArrayList<>();
        collisions.add(e2);
        collisions.add(e3);
        collisions.add(closeEntity);

        assertEquals(e2,
                linearCollisionEngine.collidesWithEntities(e1, collisions).get(),
                "There should be a collision with e2");

        // Flush Collision to have close entity on top.
        collisions.remove(e2);
        collisions.add(e2);

        assertEquals(closeEntity,
                linearCollisionEngine.collidesWithEntities(e1, collisions).get(),
                "There should be a collision with closeEntity");
    }

    @Test
    void testCollidesWithRectanglesReturnsPresentOptionalWhenColliding() {
       List<Rectangle> collisions = new ArrayList<>();
       collisions.add(obstacle1);
       assertTrue(linearCollisionEngine.collidesWithRectangles(e1, collisions).isPresent(),
               "The Rectangle should be colliding");
    }

    @Test
    void testCollidesWithRectanglesReturnsEmptyOptionalWhenNotColliding() {
        List<Rectangle> collisions = new ArrayList<>();
        collisions.add(obstacle2);
        assertTrue(linearCollisionEngine.collidesWithRectangles(e1, collisions).isEmpty(),
                "The Rectangle should not be colliding");
    }

    @Test
    void testCollidesWithRectanglesReturnsFirstAddedRectangleWhenColliding() {
        Rectangle closeRectangle = new Rectangle(1, 1, 3, 3);

        List<Rectangle> collisions = new ArrayList<>();
        collisions.add(obstacle1);
        collisions.add(obstacle2);
        collisions.add(closeRectangle);

        assertEquals(obstacle1,
                linearCollisionEngine.collidesWithRectangles(e1, collisions).get(),
                "There should be a collision with obstacle1");

        collisions.remove(obstacle1);
        collisions.add(obstacle1);

        assertEquals(closeRectangle,
                linearCollisionEngine.collidesWithRectangles(e1, collisions).get(),
                "There should be a collision with closeRectangle");

    }

    @Test
    void testRectangleCollidesWithEntitiesReturnsAllPresentEntities() {
        List<Entity> collisions = new ArrayList<>();
        collisions.add(e1);
        collisions.add(e2);

        List<Entity> rectangleOverlaps =
                linearCollisionEngine.rectangleCollidesWithEntities(
                        obstacle1, collisions
                );

        assertTrue(rectangleOverlaps.contains(e1),
                "The Rectangle should be colliding with e1");
        assertTrue(rectangleOverlaps.contains(e2),
                "The Rectangle should be colliding with e2");

    }

    @Test
    void testRectangleCollidesWithEntitiesShouldOnlyReturnCollisions() {
        List<Entity> collisions = new ArrayList<>();
        collisions.add(e1);
        collisions.add(e3);

        List<Entity> rectangleOverlaps =
                linearCollisionEngine.rectangleCollidesWithEntities(
                        obstacle1, collisions
                );

        assertTrue(rectangleOverlaps.contains(e1),
                "The Rectangle should be colliding with e1");
        assertFalse(rectangleOverlaps.contains(e3),
                "The rectangle should not be colliding with e3");
    }

    @Test
    void testRectangleCollidesWithEntitiesShouldReturnEmptyListWhenNotColliding() {
        List<Entity> collisions = new ArrayList<>();
        collisions.add(e1);
        collisions.add(e2);

        List<Entity> rectangleOverlaps =
                linearCollisionEngine.rectangleCollidesWithEntities(obstacle2, collisions);

        assertTrue(rectangleOverlaps.isEmpty(),
                "The Rectangle should not be colliding with anything");
    }
}