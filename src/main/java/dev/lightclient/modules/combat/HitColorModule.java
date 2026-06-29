package dev.lightclient.modules.combat;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.ColorSetting;
import dev.lightclient.util.ColorUtil;

/**
 * Tints entities with a custom colour when they take damage. The colour is
 * exposed for the rendering layer to consume.
 */
public final class HitColorModule extends Module {
    private final ColorSetting color =
            register(new ColorSetting("Color", "Hurt overlay colour", ColorUtil.opaque(0xFF3344)));

    public HitColorModule() {
        super("Hit Color", "Custom entity hurt colour", Category.COMBAT);
    }

    public int getColor() {
        return color.get();
    }
}
