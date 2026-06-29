package dev.lightclient.modules.performance;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;

/** Applies a user-defined framerate cap to balance smoothness and efficiency. */
public final class AdaptiveFpsLimiterModule extends Module {
    private final NumberSetting target =
            register(new NumberSetting("Target FPS", "Maximum frames per second", 144, 30, 360, 1));

    private Integer originalMaxFps;

    public AdaptiveFpsLimiterModule() {
        super("Adaptive FPS Limiter", "Smart framerate cap", Category.PERFORMANCE);
    }

    @Override
    public void onEnable() {
        if (mc.options != null) {
            originalMaxFps = mc.options.getMaxFps().getValue();
            mc.options.getMaxFps().setValue(target.getInt());
        }
    }

    @Override
    public void onDisable() {
        if (mc.options != null && originalMaxFps != null) {
            mc.options.getMaxFps().setValue(originalMaxFps);
            originalMaxFps = null;
        }
    }

    @Override
    public String getDisplayInfo() {
        return String.valueOf(target.getInt());
    }
}
