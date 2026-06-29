package dev.lightclient.modules.cosmetics;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.ModeSetting;

/** Plays a flashy effect at the location of a defeated opponent. */
public final class KillEffectsModule extends Module {
    private final ModeSetting effect =
            register(new ModeSetting("Effect", "Kill effect", "Lightning", "Lightning", "Explosion", "Crystals", "Souls"));

    public KillEffectsModule() {
        super("Kill Effects", "Effects on eliminating a target", Category.COSMETICS);
    }

    public String getEffect() {
        return effect.get();
    }
}
