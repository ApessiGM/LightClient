package dev.lightclient.modules.cosmetics;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.ColorSetting;
import dev.lightclient.util.ColorUtil;

/** Renders a glowing halo above the player's head. */
public final class HalosModule extends Module {
    private final ColorSetting color = register(new ColorSetting("Color", "Halo colour", ColorUtil.opaque(0x7B2DFF)));

    public HalosModule() {
        super("Halos", "Glowing head halo", Category.COSMETICS);
    }

    public int getColor() {
        return color.get();
    }
}
