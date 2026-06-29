package dev.lightclient.hud.elements;

import dev.lightclient.LightClient;
import dev.lightclient.hud.HudModule;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/** Displays left and right clicks per second. */
public final class CpsHud extends HudModule {
    public CpsHud() {
        super("CPS HUD", "Shows clicks per second", 4, 16);
    }

    @Override
    public void render(DrawContext context) {
        var tracker = LightClient.getInstance().getClickTracker();
        drawPanel(context, List.of("CPS: " + tracker.getLeftCps() + " | " + tracker.getRightCps()));
    }
}
