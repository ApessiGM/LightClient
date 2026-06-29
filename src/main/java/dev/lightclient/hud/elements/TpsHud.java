package dev.lightclient.hud.elements;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.hud.HudModule;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/** Estimates the server tick rate from the spacing of received game ticks. */
public final class TpsHud extends HudModule {
    private long lastTick;
    private double tps = 20.0;

    public TpsHud() {
        super("TPS Display", "Estimated server TPS", 4, 76);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        long now = System.currentTimeMillis();
        if (lastTick != 0) {
            double delta = (now - lastTick) / 1000.0;
            if (delta > 0) {
                double instant = 1.0 / delta;
                tps = tps * 0.9 + Math.min(20.0, instant) * 0.1;
            }
        }
        lastTick = now;
    }

    @Override
    public void render(DrawContext context) {
        drawPanel(context, List.of(String.format("TPS: %.1f", tps)));
    }
}
