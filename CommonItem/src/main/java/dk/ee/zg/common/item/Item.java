package dk.ee.zg.common.item;

import java.util.UUID;

import com.badlogic.gdx.graphics.Texture;
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
     * Texture for rendering the item.
     */
    private Texture texture;

    /**
     * Vector2 {@link Vector2} defining the sprite scale x and y for the item.
     */
    private Vector2 scale;

    /**
     * Description of the item.
     */
    private String description;

    /**
     * Critical hit chance percentage.
     */
    private double critChance;

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

    /**
     * Constructor for Item. Allows for creation of child items.
     * @param itemId Objects id
     * @param spriteFilePath path to the sprite
     * @param itemScale defining the sprite scale x and y for the item.
     * @param itemDescription description of the item.
     * @param itemCritChance critChance stat.
     * @param itemCritDamage critDamage stat.
     * @param itemDefense defense stat.
     * @param itemLifesteal lifesteal stat.
     * @param itemPenetration penetration stat.
     * @param itemRange range stat.
     * @param itemEvasion evasion stat.
     * @param itemHealthRegen healthRegen stat.
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public Item(final UUID itemId, final String spriteFilePath,
                final Vector2 itemScale, final String itemDescription,
                final double itemCritChance, final int itemCritDamage,
                final int itemDefense, final int itemLifesteal,
                final int itemPenetration, final int itemRange,
                final int itemEvasion, final int itemHealthRegen) {

        this.id = itemId;
        this.spritePath = spriteFilePath;
        this.scale = itemScale;
        this.description = itemDescription;
        this.critChance = itemCritChance;
        this.critDamage = itemCritDamage;
        this.defense = itemDefense;
        this.lifesteal = itemLifesteal;
        this.penetration = itemPenetration;
        this.range = itemRange;
        this.evasion = itemEvasion;
        this.healthRegen = itemHealthRegen;
        this.texture = new Texture(spritePath);
    }

    /**
     * @return item id.
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return sprite path.
     */
    public String getSpritePath() {
        return spritePath;
    }

    /**
     * @return item scale.
     */
    public Vector2 getScale() {
        return scale;
    }

    /**
     * @return item description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return item crit chance.
     */
    public double getCritChance() {
        return critChance;
    }

    /**
     * @return item crit damage.
     */
    public int getCritDamage() {
        return critDamage;
    }

    /**
     * @return item defense
     */
    public int getDefense() {
        return defense;
    }

    /**
     * @return item lifesteal.
     */
    public int getLifesteal() {
        return lifesteal;
    }

    /**
     * @return item penetration.
     */
    public int getPenetration() {
        return penetration;
    }

    /**
     * @return item range.
     */
    public int getRange() {
        return range;
    }

    /**
     * @return item evastion.
     */
    public int getEvasion() {
        return evasion;
    }

    /**
     * @return item health regen.
     */
    public int getHealthRegen() {
        return healthRegen;
    }

    /**
     * @return item texture.
     */
    public Texture getTexture() {
        return texture;
    }
}
