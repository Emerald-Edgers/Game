package dk.ee.zg.base.items;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.item.Item;

import java.util.UUID;

public class BootsItem extends Item {

    /**
     * Item that increases evasion stat.
     */
    public BootsItem() {
        super(
                UUID.randomUUID(),
                "boots.png",
                new Vector2(32f, 32f),
                "New Shoes! Increases Evasion",
                0,   // critChance
                0,    // critDamage multiplier
                0,    // defense
                0,    // lifesteal
                0,   // penetration
                0,    // range
                10,    // evasion
                0     // healthRegen
        );
    }
}
