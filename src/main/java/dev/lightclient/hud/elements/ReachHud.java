package dev.lightclient.hud.elements;

import dev.lightclient.hud.HudModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.hit.EntityHitResult;

import java.util.List;

/** Displays the distance to the entity under the crosshair. */
public final class ReachHud extends HudModule {
    private double lastReach;

    public ReachHud() {
        super("Reach HUD", "Shows distance to target", 150, 130);
    }

    @Override
    public void render(DrawContext context) {
        if (mc.player != null && mc.crosshairTarget instanceof EntityHitResult hit) {
            lastReach = mc.player.distanceTo(hit.getEntity());
        }
        drawPanel(context, List.of(String.format("Reach: %.2f", lastReach)));
    }
}
