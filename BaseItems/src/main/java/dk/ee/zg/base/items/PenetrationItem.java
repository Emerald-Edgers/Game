package dk.ee.zg.base.items;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.item.Item;

import java.util.UUID;

public class PenetrationItem extends Item {

    /**
     * Item that increases penetration stat.
     */
    public PenetrationItem() {
        super(
                UUID.randomUUID(),
                "penetration.png",
                new Vector2(32f, 32f),
                "Bypass your enemy’s defenses with ease. "
                        + "Increases armor and magic penetration.",
                0,   // critChance
                0,    // critDamage multiplier
                0,    // defense
                0,    // lifesteal
                10,   // penetration
                0,    // range
                0,    // evasion
                0     // healthRegen
        );
    }
}
