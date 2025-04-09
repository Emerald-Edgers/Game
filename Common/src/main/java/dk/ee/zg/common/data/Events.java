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

}
