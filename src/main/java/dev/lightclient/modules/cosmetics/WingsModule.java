package dev.lightclient.modules.cosmetics;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.ColorSetting;
import dev.lightclient.setting.ModeSetting;
import dev.lightclient.util.ColorUtil;

/** Renders decorative wings behind the player. */
public final class WingsModule extends Module {
    private final ModeSetting type = register(new ModeSetting("Type", "Wing type", "Angel", "Angel", "Dragon", "Crystal", "Energy"));
    private final ColorSetting color = register(new ColorSetting("Color", "Wing colour", ColorUtil.opaque(0xAA00FF)));

    public WingsModule() {
        super("Wings", "Decorative wings", Category.COSMETICS);
    }

    public String getType() {
        return type.get();
    }

    public int getColor() {
        return color.get();
    }
}
