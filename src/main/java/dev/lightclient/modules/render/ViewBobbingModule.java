package dev.lightclient.modules.render;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;

/**
 * Disables the camera bob that occurs while walking for a steadier crosshair.
 * The previous setting is restored when the module is turned off.
 */
public final class ViewBobbingModule extends Module {
    private Boolean original;

    public ViewBobbingModule() {
        super("No Bobbing", "Removes the walking view bob", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (mc.options == null) {
            return;
        }
        original = mc.options.getBobView().getValue();
        mc.options.getBobView().setValue(false);
    }

    @Override
    public void onDisable() {
        if (mc.options != null && original != null) {
            mc.options.getBobView().setValue(original);
            original = null;
        }
    }
}
