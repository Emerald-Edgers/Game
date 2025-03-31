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

}
