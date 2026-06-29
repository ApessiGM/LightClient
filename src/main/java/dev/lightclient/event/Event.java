package dev.lightclient.event;

/**
 * Base type for every event dispatched through the {@link EventBus}.
 * Events may be cancellable; cancelling stops the default game behaviour
 * where the dispatching site honours {@link #isCancelled()}.
 */
public abstract class Event {
    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
