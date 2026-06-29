package dev.lightclient.modules.player;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;

/**
 * Automatically requests a respawn the moment the player dies, skipping the
 * death screen so you rejoin the fight instantly.
 */
public final class AutoRespawnModule extends Module {
    public AutoRespawnModule() {
        super("Auto Respawn", "Instantly respawns on death", Category.PLAYER);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.player == null) {
            return;
        }
        if (mc.player.isDead() || mc.player.getHealth() <= 0.0F) {
            mc.player.requestRespawn();
        }
    }
}
