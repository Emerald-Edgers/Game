package dk.ee.zg.common.weapon;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class WeaponTest {

    // Test Implementation of weapon
    static final class TestWeapon extends Weapon {
        @Override
        public Rectangle attack(
                final Vector2 playerPos, final Vector2 playerSize) {
            // Return a simple rectangle for testing purposes.
            // This is a mock.
            return new Rectangle(playerPos.x, playerPos.y, playerSize.x, playerSize.y);
        }
    }

    private final Weapon testWeapon = new TestWeapon();

    @Test
    void testAttackShouldReturnCorrectRectangle() {
        Vector2 playerPos = new Vector2(10, 10);
        Vector2 playerSize = new Vector2(32, 32);

        Rectangle hitbox = testWeapon.attack(playerPos, playerSize);

        assertNotNull(hitbox, "The hitbox should not be null.");
        assertEquals(playerPos.x, hitbox.x,
                "Attack hitbox X position should match player position X");
        assertEquals(playerPos.y, hitbox.y,
                "Attack hitbox Y position should match player position Y");
        assertEquals(playerSize.x, hitbox.width,
                "Attack hitbox width should match player size width");
        assertEquals(playerSize.y, hitbox.height,
                "Attack hitbox height should match player size height");
    }

}