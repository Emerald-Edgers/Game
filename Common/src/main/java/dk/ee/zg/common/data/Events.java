package dk.ee.zg.common.data;

import java.util.UUID;

public class Events {

    /**
     * interface to use as base for event.
     */
    public interface IEvent {
    }

    /**
     * player levelled up event.
     * e.g.: used for showing levelup popup.
     */
    public static final class PlayerLevelUpEvent implements IEvent {

    }

    /**
     * enemy killed event.
     * e.g.: used for player gaining exp.
     */
    public static final class EnemyKilledEvent implements IEvent {
        /**
         * Experience value of defeated enemy,
         * to be added to player.
         */
        private int experience;
        /**
         * enemy UUID.
         */
        private UUID uuid;
        /**
         * constructor for EnemyKilledEvent.
         * @param exp - experience to set
         * @param id - uuid of enemy killed
         */
        public EnemyKilledEvent(final int exp, final UUID id) {
            this.experience = exp;
            this.uuid = id;
        }

        public int getExperience() {
            return experience;
        }

        public UUID getUuid() {
            return uuid;
        }
    }

    /**
     * Represents an event triggered when an item is equipped, encapsulating
     * the stat modifications provided by the item.
     * This class implements {@link Events.IEvent}, allowing it to be used as a
     * standardized event in the game's event handling system.
     */
    public static final class EquipItemEvent implements IEvent {

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
         * Constructs a new EquipItemEvent
         * with the specified combat stat values.
         * This constructor is used to initialize an event
         * that represents equipping
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
        @SuppressWarnings({"checkstyle:ParameterNumber",
                "checkstyle:HiddenField"})
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

}
