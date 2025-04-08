package dk.ee.zg.common.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class EventManagerTest {
    // Define some empty events
    static class SomeTestEvent implements Events.IEvent {}
    static class SomeOtherTestEvent implements Events.IEvent {}

    // Define event which carries data, and methods
    static final class DataTestEvent implements Events.IEvent {
        private final int number;
        private final String text;

        public DataTestEvent(int number, String text) {
            this.number = number;
            this.text = text;
        }

        public int doubleNumber() {
            return number * 2;
        }
    }

    @Test
    void testSingleListenersIsCalled() {
        AtomicBoolean wasCalled = new AtomicBoolean(false);

        EventManager.addListener(SomeTestEvent.class,
                event -> {
                    wasCalled.set(true);
                });

        EventManager.triggerEvent(new SomeTestEvent());

        assertTrue(wasCalled.get(),
                "Triggered event should be called");
    }

    @Test
    void testOnTriggerNoListenersDoesNotCrash() {
       assertDoesNotThrow(() -> {
           EventManager.triggerEvent(new SomeTestEvent());
       });
    }

    @Test
    void testMultipleListenersAreCalled() {
        AtomicInteger calls = new AtomicInteger(0);

        EventManager.addListener(SomeTestEvent.class,
                event -> {
            calls.incrementAndGet();
                });

        EventManager.addListener(SomeTestEvent.class,
                event -> {
                    calls.incrementAndGet();
                });

        EventManager.triggerEvent(new SomeTestEvent());

        assertEquals(2, calls.get(),
                "Both Listeners Should have been called");
    }

    @Test
    void testDifferentEventAreIsolated() {
        AtomicBoolean event1Called = new AtomicBoolean(false);
        AtomicBoolean event2Called = new AtomicBoolean(false);

        EventManager.addListener(SomeTestEvent.class,
                event -> {
            event1Called.set(true);
                });

        EventManager.addListener(SomeOtherTestEvent.class,
                event -> {
            event2Called.set(true);
                });

        EventManager.triggerEvent(new SomeTestEvent());

        assertTrue(event1Called.get(),
                "Triggered event was not called");
        assertFalse(event2Called.get(),
                "Non triggered events should not be called");
    }

    @Test
    void testEventCarryData() {
        int expectedNumber = 10;
        String expectedText = "This is a test";

        final int[] actualNumber = new int[1];
        final String[] actualText = new String[1];

        EventManager.addListener(DataTestEvent.class,
                event -> {
            actualNumber[0] = event.number;
            actualText[0] = event.text;
                });

        EventManager.triggerEvent(
                new DataTestEvent(expectedNumber, expectedText));

        assertEquals(expectedNumber, actualNumber[0],
                "The number should be the same");
        assertEquals(expectedText, actualText[0],
                "The text should be the same");
    }

    @Test
    void testEventCarryMethods() {
       int initialNumber = 10;
       int expectedNumber = initialNumber * 2;
       String initialText = "This is a test";

       final int[] actualNumber = new int[1];

       EventManager.addListener(DataTestEvent.class,
               event -> {
           actualNumber[0] = event.doubleNumber();
               });

       EventManager.triggerEvent(
               new DataTestEvent(initialNumber, initialText));

       assertEquals(expectedNumber, actualNumber[0],
               "The number should be double");
    }
}