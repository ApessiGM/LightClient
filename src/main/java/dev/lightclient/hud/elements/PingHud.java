package dev.lightclient.hud.elements;

import dev.lightclient.hud.HudModule;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/** Displays the local player's ping to the server. */
public final class PingHud extends HudModule {
    public PingHud() {
        super("Ping HUD", "Shows ping in milliseconds", 4, 28);
    }

    @Override
    public void render(DrawContext context) {
        int ping = 0;
        if (mc.player != null && mc.getNetworkHandler() != null) {
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (entry != null) {
                ping = entry.getLatency();
            }
        }
        drawPanel(context, List.of("Ping: " + ping + "ms"));
    }
}
