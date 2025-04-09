package dk.ee.zg.common.data;

/**
 * Represents an event triggered when an item is equipped, encapsulating
 * the stat modifications provided by the item.
 * This class implements {@link Events.IEvent}, allowing it to be used as a
 * standardized event in the game's event handling system.
 */
public class EquipItemEvent implements Events.IEvent {

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

    /**
     * Constructs a new EquipItemEvent with the specified combat stat values.
     * This constructor is used to initialize an event that represents equipping
     * an item which affects various combat-related stats of a character.
     *
     * @param critChance   the critical hit chance provided by the item
     * @param critDamage   the critical hit damage.
     * @param defense      the defense value added by the item
     * @param lifesteal    the amount of lifesteal added.
     * @param penetration  the penetration provided by the item
     * @param range        the range stat contributed by the item.
     * @param evasion      the evasion stat from the item.
     * @param healthRegen  the health regeneration rate granted by the item
     */
    @SuppressWarnings({"checkstyle:ParameterNumber", "checkstyle:HiddenField"})
    public EquipItemEvent(final int critChance, final int critDamage,
                          final int defense, final int lifesteal,
                          final int penetration, final int range,
                          final int evasion, final int healthRegen) {
        this.critChance = critChance;
        this.critDamage = critDamage;
        this.defense = defense;
        this.lifesteal = lifesteal;
        this.penetration = penetration;
        this.range = range;
        this.evasion = evasion;
        this.healthRegen = healthRegen;
    }
}
