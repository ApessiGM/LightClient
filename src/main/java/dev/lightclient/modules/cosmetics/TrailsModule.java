package dev.lightclient.modules.cosmetics;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.ColorSetting;
import dev.lightclient.setting.NumberSetting;
import dev.lightclient.util.ColorUtil;

/** Leaves a colourful trail behind the player as they move. */
public final class TrailsModule extends Module {
    private final ColorSetting color = register(new ColorSetting("Color", "Trail colour", ColorUtil.opaque(0xAA00FF)));
    private final NumberSetting length = register(new NumberSetting("Length", "Trail length", 20, 5, 60, 1));

    public TrailsModule() {
        super("Trails", "Movement trail effect", Category.COSMETICS);
    }

    public int getColor() {
        return color.get();
    }

    public int getLength() {
        return length.getInt();
    }
}
