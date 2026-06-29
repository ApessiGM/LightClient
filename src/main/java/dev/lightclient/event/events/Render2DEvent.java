package dev.lightclient.event.events;

import dev.lightclient.event.Event;
import net.minecraft.client.gui.DrawContext;

/** Fired every frame while the in-game HUD is rendered. */
public final class Render2DEvent extends Event {
    private final DrawContext context;
    private final float tickDelta;

    public Render2DEvent(DrawContext context, float tickDelta) {
        this.context = context;
        this.tickDelta = tickDelta;
    }

    public DrawContext getContext() {
        return context;
    }

    public float getTickDelta() {
        return tickDelta;
    }
}
