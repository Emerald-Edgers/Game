package dk.ee.zg.base.items;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.item.Item;

import java.util.UUID;

public class LifeStealItem extends Item {

    /**
     * Item that increases lifesteal stat.
     */
    public LifeStealItem() {
        super(
                UUID.randomUUID(),
                "cloak.png",
                new Vector2(32f, 32f),
                "Drain your enemies' life force with every hit."
                        + "Converts damage dealt into health.",
                0,   // critChance
                0,    // critDamage multiplier
                0,    // defense
                10,    // lifesteal
                0,   // penetration
                0,    // range
                0,    // evasion
                0     // healthRegen
        );
    }
}
