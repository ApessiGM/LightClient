package dev.lightclient.hud.elements;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.hud.HudModule;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

/** Displays the player's horizontal movement speed in blocks per second. */
public final class SpeedHud extends HudModule {
    private double lastX;
    private double lastZ;
    private double blocksPerSecond;

    public SpeedHud() {
        super("Speed Display", "Shows movement speed", 4, 64);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player != null) {
            lastX = mc.player.getX();
            lastZ = mc.player.getZ();
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.player == null) {
            return;
        }
        double dx = mc.player.getX() - lastX;
        double dz = mc.player.getZ() - lastZ;
        double perTick = Math.sqrt(dx * dx + dz * dz);
        blocksPerSecond = perTick * 20.0;
        lastX = mc.player.getX();
        lastZ = mc.player.getZ();
    }

    @Override
    public void render(DrawContext context) {
        drawPanel(context, List.of(String.format("Speed: %.2f b/s", blocksPerSecond)));
    }
}
