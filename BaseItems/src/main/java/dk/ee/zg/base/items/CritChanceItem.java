package dk.ee.zg.base.items;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.item.Item;

import java.util.UUID;

public class CritChanceItem extends Item {

    /**
     * Item that increases crit chance stat.
     */
    public CritChanceItem() {
        super(
                UUID.randomUUID(),
                "critchance.png",
                new Vector2(32f, 32f),
                "Focus your strikes with unmatched precision."
                        + " Increases critical hit chance.",
                10,   // critChance
                0,    // critDamage multiplier
                0,    // defense
                0,    // lifesteal
                0,   // penetration
                0,    // range
                0,    // evasion
                0     // healthRegen
        );
    }
}
