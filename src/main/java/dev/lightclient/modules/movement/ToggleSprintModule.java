package dev.lightclient.modules.movement;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.BooleanSetting;

/**
 * Keeps the player sprinting automatically while moving forward, removing the
 * need to hold the sprint key.
 */
public final class ToggleSprintModule extends Module {
    private final BooleanSetting onlyForward =
            register(new BooleanSetting("Only Forward", "Sprint only when moving forward", true));

    public ToggleSprintModule() {
        super("Toggle Sprint", "Automatically keeps you sprinting", Category.MOVEMENT);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.player == null) {
            return;
        }
        boolean moving = onlyForward.get() ? mc.player.forwardSpeed > 0 : !mc.player.getVelocity().multiply(1, 0, 1).equals(net.minecraft.util.math.Vec3d.ZERO);
        if (moving && !mc.player.isSneaking()) {
            mc.player.setSprinting(true);
        }
    }
}
