package dev.lightclient.modules.movement;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.BooleanSetting;

/**
 * Forces the player to sprint in any movement direction, including strafing and
 * walking backwards, for consistent PvP movement speed.
 */
public final class SprintModule extends Module {
    private final BooleanSetting keepSneaking =
            register(new BooleanSetting("Allow Sneaking", "Stop sprinting while sneaking", true));

    public SprintModule() {
        super("Sprint", "Always sprint in any direction", Category.MOVEMENT);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.player == null) {
            return;
        }
        boolean moving = mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0;
        boolean blocked = (keepSneaking.get() && mc.player.isSneaking()) || mc.player.isUsingItem();
        if (moving && !blocked) {
            mc.player.setSprinting(true);
        }
    }
}
