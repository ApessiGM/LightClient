package dev.lightclient.modules.render;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;

/**
 * Controls the strength of the motion blur post-processing applied by the
 * client's render pipeline.
 */
public final class MotionBlurModule extends Module {
    private final NumberSetting strength =
            register(new NumberSetting("Strength", "Blur amount", 0.5, 0.0, 1.0, 0.05));

    public MotionBlurModule() {
        super("Motion Blur", "Smooth motion blur effect", Category.RENDER);
    }

    public float getStrength() {
        return strength.getFloat();
    }
}
