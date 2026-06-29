package dev.lightclient.modules.performance;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.BooleanSetting;

/** Disables expensive view bobbing and entity shadow animations. */
public final class SmartAnimationCullingModule extends Module {
    private final BooleanSetting viewBob = register(new BooleanSetting("Disable Bobbing", "Turn off view bobbing", true));
    private final BooleanSetting shadows = register(new BooleanSetting("Disable Shadows", "Turn off entity shadows", false));

    private Boolean originalBob;
    private Boolean originalShadows;

    public SmartAnimationCullingModule() {
        super("Smart Animation Culling", "Cuts costly animations", Category.PERFORMANCE);
    }

    @Override
    public void onEnable() {
        if (mc.options == null) {
            return;
        }
        if (viewBob.get()) {
            originalBob = mc.options.getBobView().getValue();
            mc.options.getBobView().setValue(false);
        }
        if (shadows.get()) {
            originalShadows = mc.options.getEntityShadows().getValue();
            mc.options.getEntityShadows().setValue(false);
        }
    }

    @Override
    public void onDisable() {
        if (mc.options == null) {
            return;
        }
        if (originalBob != null) {
            mc.options.getBobView().setValue(originalBob);
            originalBob = null;
        }
        if (originalShadows != null) {
            mc.options.getEntityShadows().setValue(originalShadows);
            originalShadows = null;
        }
    }
}
