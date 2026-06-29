package dev.lightclient.modules.render;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;

/**
 * Maximises the in-game gamma so dark areas become fully visible. Restores the
 * previous gamma value on disable.
 */
public final class FullbrightModule extends Module {
    private Double originalGamma;

    public FullbrightModule() {
        super("Fullbright", "Brightens the world to maximum", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (mc.options == null) {
            return;
        }
        originalGamma = mc.options.getGamma().getValue();
        mc.options.getGamma().setValue(1.0);
    }

    @Override
    public void onDisable() {
        if (mc.options != null && originalGamma != null) {
            mc.options.getGamma().setValue(originalGamma);
            originalGamma = null;
        }
    }
}
