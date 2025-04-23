package dk.ee.zg.base.items;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.item.Item;

import java.util.UUID;

public class HealthRegenItem extends Item {

    /**
     * Item that increases healthregen stat.
     */
    public HealthRegenItem() {
        super(
                UUID.randomUUID(),
                "healthregen.png",
                new Vector2(32f, 32f),
                "Slowly recovers health over time. "
                        + "Keep fighting, your vitality will never fail you.",
                0,   // critChance
                0,    // critDamage multiplier
                0,    // defense
                0,    // lifesteal
                0,   // penetration
                0,    // range
                0,    // evasion
                10     // healthRegen
        );
    }
}
