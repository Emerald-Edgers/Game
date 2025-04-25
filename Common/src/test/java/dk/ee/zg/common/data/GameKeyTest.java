package dk.ee.zg.common.data;

import com.badlogic.gdx.Input;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameKeyTest {

    private GameKey gameKey;

    @BeforeEach
    void setUp() {
        gameKey = new GameKey();
    }

    @AfterEach
    void tearDown() {
        gameKey.reset();
    }

    @Test
    void testBindingActionToKeyShouldSaveBinding() {
        gameKey.bindActionToKey(KeyAction.MOVE_UP, Input.Keys.W);

        assertEquals(Input.Keys.W,
                gameKey.getActionToKey().get(KeyAction.MOVE_UP),
                "MOVE_UP should be bound to W"
        );
    }

    @Test
    void testBindingActionToKeyShouldOverwriteBinding() {
        gameKey.bindActionToKey(KeyAction.MOVE_UP, Input.Keys.W);
        gameKey.bindActionToKey(KeyAction.MOVE_UP, Input.Keys.S);


        assertNotEquals(Input.Keys.W,
                gameKey.getActionToKey().get(KeyAction.MOVE_UP),
                "MOVE_UP should not be bound to W"
        );

        assertEquals(Input.Keys.S,
                gameKey.getActionToKey().get(KeyAction.MOVE_UP),
                "MOVE_UP should be bound to S"
        );
    }

    @Test
    void testSetKeyShouldActivateKey() {
        int keyCode = Input.Keys.F;
        gameKey.setKey(keyCode, true);

        assertTrue(gameKey.isDown(keyCode),
                "The F key should be down.");
    }

    @Test
    void testResetKeysShouldSetKeysToFalse() {
        int keyCode = Input.Keys.F;

        gameKey.setKey(keyCode, true);
        gameKey.checkJustPressed();

        gameKey.reset();

        assertFalse(gameKey.isDown(keyCode),
                "F key should not be pressed after reset.");
        assertFalse(gameKey.isPressed(keyCode),
                "F key should not be considered pressed after reset.");
    }

    @Test
    void testKeyNotPressed() {
        int keyCode = Input.Keys.F;

        assertFalse(gameKey.isPressed(keyCode),
                "F key should not be considered just pressed.");
    }

    @Test
    void testNewKeyConsideredJustPressed() {
        int keyCode = Input.Keys.F;

        gameKey.setKey(keyCode, true);

        assertTrue(gameKey.isPressed(keyCode),
                "The F key should be considered just pressed.");
    }

    @Test
    void testKeyNotConsideredJustPressedAfterCheck() {
        int keyCode = Input.Keys.F;

        gameKey.setKey(keyCode, true);
        gameKey.checkJustPressed();

       assertFalse(gameKey.isPressed(keyCode),
               "The F key should not be considered just pressed.");
    }

    @Test
    void integrationTestSimulateKeyPressAndRelease() {
        int keyCode = Input.Keys.F;

        // First Frame of press
        gameKey.setKey(keyCode, true);

        // Assert key is considered both down and pressed
        assertTrue(gameKey.isPressed(keyCode),
                "The F key should be considered just pressed.");

        assertTrue(gameKey.isDown(keyCode),
                "The F key should be considered down.");

        // Update to no longer be pressed, Second frame
        gameKey.checkJustPressed();

        // Assert Key is considered down but not pressed
        assertFalse(gameKey.isPressed(keyCode),
                "The F key should not be considered just pressed.");

        assertTrue(gameKey.isDown(keyCode),
                "The F key should be considered down.");

        // Update frame, check for pressing and release key, third frame
        gameKey.checkJustPressed();
        gameKey.setKey(keyCode, false);

        // Ensure Key is not considered down or pressed
        assertFalse(gameKey.isPressed(keyCode),
                "The F key should not be considered just pressed.");

        assertFalse(gameKey.isDown(keyCode),
                "The F key should not be considered down.");
    }
}