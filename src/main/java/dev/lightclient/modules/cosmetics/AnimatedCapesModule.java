package dev.lightclient.modules.cosmetics;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;

/** Adds smooth wave animation to the equipped cape. */
public final class AnimatedCapesModule extends Module {
    private final NumberSetting speed = register(new NumberSetting("Speed", "Wave speed", 1.0, 0.1, 3.0, 0.1));
    private final NumberSetting waviness = register(new NumberSetting("Waviness", "Wave amplitude", 1.0, 0.1, 3.0, 0.1));

    public AnimatedCapesModule() {
        super("Animated Capes", "Smoothly animated capes", Category.COSMETICS);
    }

    public float getSpeed() {
        return speed.getFloat();
    }

    public float getWaviness() {
        return waviness.getFloat();
    }
}
