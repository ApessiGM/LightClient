package dev.lightclient.modules.performance;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;

/** Reduces entity render distance scaling so far-away entities are skipped. */
public final class EntityCullingModule extends Module {
    private final NumberSetting scaling =
            register(new NumberSetting("Distance", "Entity distance scaling", 0.5, 0.0, 1.0, 0.05));

    private Double original;

    public EntityCullingModule() {
        super("Entity Culling", "Skips rendering distant entities", Category.PERFORMANCE);
    }

    @Override
    public void onEnable() {
        if (mc.options != null) {
            original = mc.options.getEntityDistanceScaling().getValue();
            mc.options.getEntityDistanceScaling().setValue(scaling.get());
        }
    }

    @Override
    public void onDisable() {
        if (mc.options != null && original != null) {
            mc.options.getEntityDistanceScaling().setValue(original);
            original = null;
        }
    }
}
