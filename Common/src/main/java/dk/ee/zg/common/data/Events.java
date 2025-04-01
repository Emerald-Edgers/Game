package dk.ee.zg.common.data;

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
         * constructor for EnemyKilledEvent.
         * @param exp - experience to set
         */
        public EnemyKilledEvent(final int exp) {
            this.experience = exp;
        }

        public int getExperience() {
            return experience;
        }
    }


}
