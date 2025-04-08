package dk.ee.zg.spawner.simple;

import com.badlogic.gdx.graphics.OrthographicCamera;
import dk.ee.zg.common.data.GameData;
import dk.ee.zg.common.enemy.interfaces.IEnemyCreator;
import dk.ee.zg.common.map.data.WorldEntities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimpleEnemySpawnerTest {
    private SimpleEnemySpawner spawner;
    private WorldEntities mockWorld;
    private IEnemyCreator mockEnemy1;
    private IEnemyCreator mockEnemy2;

    private OrthographicCamera camera;

    @BeforeEach
    void setUp() {
        spawner = new SimpleEnemySpawner();
        mockWorld = mock(WorldEntities.class);
        mockEnemy1 = mock(IEnemyCreator.class);
        mockEnemy2 = mock(IEnemyCreator.class);

        camera = new OrthographicCamera();
        camera.position.set(25f, 25f, 0f);
        camera.viewportWidth = 10f;
        camera.viewportHeight = 10f;
    }

    @Test
    void testStartShouldNotThrowWithValidEnemies() {
        try (MockedStatic<ServiceLoader> mockedServiceLoader =
                        mockStatic(ServiceLoader.class)) {
            ServiceLoader<IEnemyCreator> mockedLoader =
                    mock(ServiceLoader.class);
            when(mockedLoader.iterator()).thenReturn(
                    List.of(mockEnemy1, mockEnemy2).iterator());
            mockedServiceLoader.when(
                    () -> ServiceLoader.load(IEnemyCreator.class))
                    .thenReturn(mockedLoader);

            assertDoesNotThrow(() -> spawner.start(mockWorld),
                    "Spawn should never throw an exception");

        }
    }

    @Test
    void testStartShouldNotThrowWithNoEnemies() {
        try (MockedStatic<ServiceLoader> mockedServiceLoader =
                mockStatic(ServiceLoader.class)) {

            ServiceLoader<IEnemyCreator> mockedLoader =
                    mock(ServiceLoader.class);
            when(mockedLoader.iterator()).thenReturn(
                    List.<IEnemyCreator>of().iterator());
            mockedServiceLoader.when(
                    () -> ServiceLoader.load(IEnemyCreator.class))
                    .thenReturn(mockedLoader);

            assertDoesNotThrow(() -> spawner.start(mockWorld),
                    "Spawn should never throw an exception");

        }

    }

    @Test
    void testProcessSpawnsEnemiesWhenEnoughBalance() {
        try (MockedStatic<ServiceLoader> mockedServiceLoader =
                     mockStatic(ServiceLoader.class)) {
            ServiceLoader<IEnemyCreator> mockedLoader =
                    mock(ServiceLoader.class);
            when(mockedLoader.iterator()).thenReturn(
                    List.of(mockEnemy1).iterator());
            mockedServiceLoader.when(
                    () -> ServiceLoader.load(IEnemyCreator.class))
                    .thenReturn(mockedLoader);

            spawner.start(mockWorld);

            try (MockedStatic<GameData> mockedGameData =
                         mockStatic(GameData.class)) {
                GameData mockedData = mock(GameData.class);
                mockedGameData.when(GameData::getInstance).thenReturn(mockedData);
                when(mockedData.getCamera()).thenReturn(camera);
                when(mockEnemy1.getEnemyCost()).thenReturn(1f);

                spawner.process(1f, mockWorld);

                assertDoesNotThrow(() ->
                                verify(mockEnemy1, atLeastOnce())
                                        .spawn(anyFloat(), anyFloat(), eq(mockWorld)),
                        "Expected enemy to spawn at least once, but it didn't"
                );
            }
        }
    }

    @Test
    void testProcessDoesNotThrowWithNoEnemies() {
        try (MockedStatic<ServiceLoader> mockedServiceLoader =
                     mockStatic(ServiceLoader.class)) {
            ServiceLoader<IEnemyCreator> mockedLoader =
                    mock(ServiceLoader.class);
            when(mockedLoader.iterator()).thenReturn(
                    List.<IEnemyCreator>of().iterator());
            mockedServiceLoader.when(
                            () -> ServiceLoader.load(IEnemyCreator.class))
                    .thenReturn(mockedLoader);

            spawner.start(mockWorld);

            try (MockedStatic<GameData> mockedGameData = mockStatic(GameData.class)) {
                GameData mockedData = mock(GameData.class);
                mockedGameData.when(GameData::getInstance).thenReturn(mockedData);
                when(mockedData.getCamera()).thenReturn(camera);
                when(mockEnemy1.getEnemyCost()).thenReturn(1f);

                assertDoesNotThrow(() -> spawner.process(1f, mockWorld));
            }
        }
    }
}