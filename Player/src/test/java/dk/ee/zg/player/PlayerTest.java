package dk.ee.zg.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import dk.ee.zg.common.data.EventManager;
import dk.ee.zg.common.data.Events;
import dk.ee.zg.common.map.data.WorldEntities;
import dk.ee.zg.common.test.HeadlessLauncher;
import dk.ee.zg.common.weapon.Weapon;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerTest {

    private Player player;

    @BeforeAll
    static void setUpBeforeClass() {
        if (Gdx.app == null) {
            HeadlessLauncher launcher = new HeadlessLauncher();
            launcher.initHeadlessApplication();
        }

        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;

        Gdx.graphics = mock(Graphics.class);

        when(Gdx.graphics.getDeltaTime()).thenReturn(0.016f);
    }

    @BeforeEach
    void setUp() {
        player = new Player(
                10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10
        );
    }

    @AfterAll
    static void tearDownAfterClass() {
        if (Gdx.app != null) {
            Gdx.app.exit();
        }
    }

    @Test
    void testPlayerShouldGetXpWhenEnemyKilledEventTriggers() {
        player.setExperience(0);
        EventManager.triggerEvent(new Events.EnemyKilledEvent(20, UUID.randomUUID()));
        assertEquals(20, player.getExperience());
    }

    @Test
    void testPlayerShouldLevelUpWhenAbove2000XpIsReached() {
        player.setLevel(1);
        EventManager.triggerEvent(new Events.EnemyKilledEvent(2001, UUID.randomUUID()));

        assertEquals(2, player.getLevel());
    }

    @Test
    void testPlayerShouldLevelUpMultipleTimes() {
        player.setLevel(1);
        EventManager.triggerEvent(new Events.EnemyKilledEvent(99999, UUID.randomUUID()));

        assertTrue(player.getLevel() > 2);
    }

    @Test
    void testPlayerShouldNotLevelUpWhen100XpIsReached() {
        player.setLevel(1);
        EventManager.triggerEvent(new Events.EnemyKilledEvent(100, UUID.randomUUID()));

        assertEquals(1, player.getLevel());
    }

    @Test
    void testPlayerShouldEmitLevelUpEventWhenLevelingUp() {
        player.setLevel(1);

        AtomicBoolean levelUpCalled = new AtomicBoolean(false);
        EventManager.addListener(Events.PlayerLevelUpEvent.class, event -> {
            levelUpCalled.set(true);
        });

        EventManager.triggerEvent(new Events.EnemyKilledEvent(100001, UUID.randomUUID()));


        assertTrue(levelUpCalled.get());
    }

    @Test
    void testPlayerShouldLoadStatsFromWeaponWhenSet() {
        Weapon weapon = mock(Weapon.class);
        when(weapon.getMaxHP()).thenReturn(100);
        when(weapon.getAttackDamage()).thenReturn(100);
        when(weapon.getAttackSpeed()).thenReturn(100);
        when(weapon.getMoveSpeed()).thenReturn(100);

        assertEquals(10, player.getMaxHP());
        assertEquals(10, player.getAttackDamage());
        assertEquals(10, player.getAttackSpeed());
        assertEquals(10, player.getMoveSpeed());

        player.setWeapon(weapon);
        player.loadStatsFromWeapon();

        assertEquals(100, player.getMaxHP());
        assertEquals(100, player.getAttackDamage());
        assertEquals(100, player.getAttackSpeed());
        assertEquals(100, player.getMoveSpeed());

    }

    @Test
    void testPlayerShouldNotThrowWhenNoWeaponIsSet() {
        player.setWeapon(null);

        assertDoesNotThrow(() -> {
            player.loadStatsFromWeapon();
        });
    }

    @Test
    void testPlayerPluginShouldAddPlayerToWorld() {
        PlayerPlugin playerPlugin = new PlayerPlugin();
        WorldEntities worldEntities = mock(WorldEntities.class);

        playerPlugin.start(worldEntities);

        verify(worldEntities, times(1)).addEntity(any(Player.class));
    }
}