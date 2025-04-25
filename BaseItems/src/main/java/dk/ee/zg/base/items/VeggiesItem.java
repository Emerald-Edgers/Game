package dk.ee.zg.base.items;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.item.Item;

import java.util.UUID;

public class VeggiesItem extends Item {

    /**
     * Item that increases health regen and evasion stat.
     */
    public VeggiesItem() {
        super(
                UUID.randomUUID(),
                "veggies.png",
                new Vector2(32f, 32f),
                "Packed with nutrients to keep you lean and healthy. "
                        + "Increases health regen and evasion.",
                0,   // critChance
                0,    // critDamage multiplier
                0,    // defense
                0,    // lifesteal
                0,   // penetration
                0,    // range
                5,    // evasion
                5     // healthRegen
        );
    }
}
