package dev.lightclient.hud.elements;

import dev.lightclient.hud.HudModule;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/** Displays the cardinal direction the player is facing. */
public final class DirectionHud extends HudModule {
    private static final String[] NAMES = {"South", "South-West", "West", "North-West",
            "North", "North-East", "East", "South-East"};
    private static final String[] AXIS = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};

    public DirectionHud() {
        super("Direction HUD", "Shows facing direction", 4, 52);
    }

    @Override
    public void render(DrawContext context) {
        if (mc.player == null) {
            drawPanel(context, List.of("Facing: -"));
            return;
        }
        float yaw = mc.player.getYaw() % 360;
        if (yaw < 0) {
            yaw += 360;
        }
        int index = Math.round(yaw / 45f) % 8;
        drawPanel(context, List.of("Facing: " + NAMES[index] + " (" + AXIS[index] + ")"));
    }
}
