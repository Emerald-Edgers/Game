package dk.ee.zg.common.data;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * manages events listeners and triggering of events.
 * uses {@link Consumer} and {@link Events.IEvent}
 */
public final class EventManager {

    /**
     * private constructor, since new instance is forbidden,
     * class is only utility and static.
     */
    private EventManager() { }
    /**
     * all listeners registered.
     */
    private static final Map<Class<? extends Events.IEvent>,
            List<Consumer<? extends Events.IEvent>>>
            LISTENERS = new HashMap<>();

    /**
     * Register a listener using Event and Consumer<T>.
     * @param eventClass - Class of event e.g. PlayerLevelUpEvent.class
     * @param listener - Listener Consumer {@link Consumer} method
     * @param <T> - class type parameter of IEvent
     * e.g. :
     *      EventManager.addListener(Events.PlayerLevelUpEvent.class, event ->
     *          System.out.println("Lambda: Player scored ")
     *      );
     */
    public static <T extends Events.IEvent> void
    addListener(final Class<T> eventClass, final Consumer<T> listener) {
        LISTENERS.computeIfAbsent(eventClass, e ->
                new ArrayList<>()).add(listener);
    }

    /**
     * triggers an event across all listeners for the event.
     * @param event - event class to trigger listeners for
     * @param <T> - class type parameter of IEvent
     * e.g. :
     *           EventManager.triggerEvent(new Events.PlayerLevelUpEvent());
     */
    public static <T extends  Events.IEvent> void triggerEvent(final T event) {
        List<Consumer<? extends Events.IEvent>> eventListeners =
                LISTENERS.get(event.getClass());
        if (eventListeners != null) {
            for (Consumer<? extends Events.IEvent> listener
                    : new ArrayList<>(eventListeners)) {
                ((Consumer<T>) listener).accept(event);
            }
        }
    }

}
