package dk.ee.zg.base.items;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.item.Item;

import java.util.UUID;

public class CloakItem extends Item {

    /**
     * Item that increases defense and evasion stat.
     */
    public CloakItem() {
        super(
                UUID.randomUUID(),
                "cloak.png",
                new Vector2(32f, 32f),
                "Conceal yourself in the shadows! "
                        + "Slightly increases defense and evasion.",
                0,   // critChance
                0,    // critDamage multiplier
                5,    // defense
                0,    // lifesteal
                0,   // penetration
                0,    // range
                5,    // evasion
                0     // healthRegen
        );
    }
}
