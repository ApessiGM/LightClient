package dev.lightclient.hud.elements;

import dev.lightclient.hud.HudModule;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/** Displays how long the current session has been running. */
public final class SessionStatsHud extends HudModule {
    private final long start = System.currentTimeMillis();

    public SessionStatsHud() {
        super("Session Stats", "Shows session uptime", 4, 88);
    }

    @Override
    public void render(DrawContext context) {
        long seconds = (System.currentTimeMillis() - start) / 1000L;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        String uptime = String.format("%02d:%02d:%02d", hours, minutes, secs);
        drawPanel(context, List.of("Session: " + uptime, "FPS: " + mc.getCurrentFps()));
    }
}
