package dev.lightclient.modules.render;

import dev.lightclient.event.EventTarget;
import dev.lightclient.event.events.TickEvent;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.BooleanSetting;
import net.minecraft.entity.effect.StatusEffects;

/**
 * Continuously clears vision-impairing status effects (blindness, darkness and
 * nausea) so the player always has a clear view in fights.
 */
public final class AntiBlindModule extends Module {
    private final BooleanSetting nausea =
            register(new BooleanSetting("Nausea", "Also clear the nausea wobble", true));

    public AntiBlindModule() {
        super("Anti Blind", "Removes blindness, darkness and nausea", Category.RENDER);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.player == null) {
            return;
        }
        mc.player.removeStatusEffect(StatusEffects.BLINDNESS);
        mc.player.removeStatusEffect(StatusEffects.DARKNESS);
        if (nausea.get()) {
            mc.player.removeStatusEffect(StatusEffects.NAUSEA);
        }
    }
}
