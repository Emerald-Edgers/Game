package dk.ee.zg.common.item;

import java.util.UUID;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Abstract class representing an item in the game,
 * containing stats and graphical representation.
 */
public abstract class Item {

    /**
     * UUID {@link UUID} defining the objects' id.
     */
    private UUID id;

    /**
     * File path to the sprite image.
     */
    private String spritePath;

    /**
     * Sprite {@link Sprite} defining the sprite object for the item.
     * */
    private Sprite sprite;

    /**
     * Vector2 {@link Vector2} defining the sprite scale x and y for the weapon.
     */
    private Vector2 scale;

    /**
     * Description of the item.
     */
    private String description;

    /**
     * Critical hit chance percentage.
     */
    private int critChance;

    /**
     * Critical hit damage multiplier.
     */
    private int critDamage;

    /**
     * Defensive stat of the item.
     */
    private int defense;

    /**
     * Amount of lifesteal the item provides.
     */
    private int lifesteal;

    /**
     * Amount of armor or magic penetration the item offers.
     */
    private int penetration;

    /**
     * Attack range stat provided by the item.
     */

    private int range;

    /**
     * Evasion stat, indicating chance to dodge.
     */
    private int evasion;

    /**
     * Health regeneration provided per second or turn.
     */
    private int healthRegen;
}
