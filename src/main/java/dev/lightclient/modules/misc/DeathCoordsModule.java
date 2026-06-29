package dev.lightclient.modules.misc;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import net.minecraft.text.Text;

/**
 * Prints the player's coordinates to chat at the moment of death so the death
 * location can be found again.
 */
public final class DeathCoordsModule extends Module {
    private boolean wasAlive = true;
    private int lastX;
    private int lastY;
    private int lastZ;

    public DeathCoordsModule() {
        super("Death Coords", "Shows your coordinates when you die", Category.MISC);
    }

    @Override
    public void onEnable() {
        wasAlive = true;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.player == null) {
            return;
        }
        boolean alive = !mc.player.isDead() && mc.player.getHealth() > 0.0F;
        if (alive) {
            lastX = (int) Math.floor(mc.player.getX());
            lastY = (int) Math.floor(mc.player.getY());
            lastZ = (int) Math.floor(mc.player.getZ());
        } else if (wasAlive) {
            mc.player.sendMessage(Text.literal("§8[§5Light§dClient§8] §cYou died at §f"
                    + lastX + ", " + lastY + ", " + lastZ), false);
        }
        wasAlive = alive;
    }
}
