package dev.lightclient.modules.render;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.BooleanSetting;

/** Makes dropped items lie flat on the ground with realistic rotation. */
public final class ItemPhysicsModule extends Module {
    private final BooleanSetting randomRotation =
            register(new BooleanSetting("Random Rotation", "Randomise resting angle", true));

    public ItemPhysicsModule() {
        super("Item Physics", "Realistic dropped item physics", Category.RENDER);
    }

    public boolean randomRotation() {
        return randomRotation.get();
    }
}
