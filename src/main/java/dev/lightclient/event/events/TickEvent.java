package dev.lightclient.event.events;

import dev.lightclient.event.Event;

/** Fired once per client tick (20 times per second under normal conditions). */
public final class TickEvent extends Event {
    public static final TickEvent INSTANCE = new TickEvent();

    private TickEvent() {
    }
}
