package dk.ee.zg.base.items;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.item.Item;

import java.util.UUID;

public class RangeItem extends Item {

    /**
     * Item that increases range stat.
     */
    public RangeItem() {
        super(
                UUID.randomUUID(),
                "range.png",
                new Vector2(32f, 32f),
                "Your monkey index is rising!"
                        + "Range is increased.",
                0,   // critChance
                0,    // critDamage multiplier
                0,    // defense
                0,    // lifesteal
                0,   // penetration
                10,    // range
                0,    // evasion
                0     // healthRegen
        );
    }
}
