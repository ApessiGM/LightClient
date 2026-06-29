package dev.lightclient.modules.cosmetics;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.ModeSetting;

/** Places a floating crown above the player's head. */
public final class CrownsModule extends Module {
    private final ModeSetting style = register(new ModeSetting("Style", "Crown style", "Gold", "Gold", "Diamond", "Amethyst"));

    public CrownsModule() {
        super("Crowns", "Floating head crown", Category.COSMETICS);
    }

    public String getStyle() {
        return style.get();
    }
}
