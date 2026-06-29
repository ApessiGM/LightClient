package dev.lightclient.hud.elements;

import dev.lightclient.hud.HudModule;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/** Displays the player's block coordinates. */
public final class CoordinatesHud extends HudModule {
    public CoordinatesHud() {
        super("Coordinates", "Shows XYZ position", 4, 40);
    }

    @Override
    public void render(DrawContext context) {
        if (mc.player == null) {
            drawPanel(context, List.of("XYZ: -"));
            return;
        }
        int x = (int) Math.floor(mc.player.getX());
        int y = (int) Math.floor(mc.player.getY());
        int z = (int) Math.floor(mc.player.getZ());
        drawPanel(context, List.of("XYZ: " + x + ", " + y + ", " + z));
    }
}
