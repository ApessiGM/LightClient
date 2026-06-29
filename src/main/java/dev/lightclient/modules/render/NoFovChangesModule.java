package dev.lightclient.modules.render;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;

/**
 * Removes the field-of-view warping caused by sprinting and speed effects by
 * zeroing the FOV effect scale, keeping the view stable during combat.
 */
public final class NoFovChangesModule extends Module {
    private Double original;

    public NoFovChangesModule() {
        super("No FOV Changes", "Stops sprint/speed FOV warping", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (mc.options == null) {
            return;
        }
        original = mc.options.getFovEffectScale().getValue();
        mc.options.getFovEffectScale().setValue(0.0);
    }

    @Override
    public void onDisable() {
        if (mc.options != null && original != null) {
            mc.options.getFovEffectScale().setValue(original);
            original = null;
        }
    }
}
