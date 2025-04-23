package dk.ee.zg.base.items;

import com.badlogic.gdx.math.Vector2;
import dk.ee.zg.common.item.Item;

import java.util.UUID;

public class EvasionItem extends Item {

    /**
     * Item that increases defence stat.
     */
    public EvasionItem() {
        super(
                UUID.randomUUID(),
                "defense.png",
                new Vector2(32f, 32f),
                "Increases Defense!",
                0,   // critChance
                0,    // critDamage multiplier
                10,    // defense
                0,    // lifesteal
                0,   // penetration
                0,    // range
                0,    // evasion
                0     // healthRegen
        );
    }
}
