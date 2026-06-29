package dev.lightclient.modules.render;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.NumberSetting;

/** Adds a configurable hand push-back animation when hitting while blocking. */
public final class BlockHitAnimationModule extends Module {
    private final NumberSetting amount = register(new NumberSetting("Amount", "Push strength", 1.0, 0.1, 2.0, 0.1));

    public BlockHitAnimationModule() {
        super("Blockhit Animation", "Blockhit swing animation", Category.RENDER);
    }

    public float getAmount() {
        return amount.getFloat();
    }
}
