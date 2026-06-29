package dev.lightclient.modules.combat;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.ColorSetting;
import dev.lightclient.setting.NumberSetting;
import dev.lightclient.util.ColorUtil;

/** Controls the red screen tint shown when the local player is hurt. */
public final class DamageTintModule extends Module {
    private final ColorSetting color =
            register(new ColorSetting("Color", "Screen tint colour", ColorUtil.withAlpha(0xFF0033, 90)));
    private final NumberSetting strength =
            register(new NumberSetting("Strength", "Tint opacity multiplier", 1.0, 0.0, 2.0, 0.1));

    public DamageTintModule() {
        super("Damage Tint", "Custom hurt screen tint", Category.COMBAT);
    }

    public int getColor() {
        return color.get();
    }

    public float getStrength() {
        return strength.getFloat();
    }
}
