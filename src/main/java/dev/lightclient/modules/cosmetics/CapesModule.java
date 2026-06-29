package dev.lightclient.modules.cosmetics;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.ModeSetting;

/** Equips a client cape rendered on top of your player model. */
public final class CapesModule extends Module {
    private final ModeSetting design =
            register(new ModeSetting("Design", "Cape design", "Neon", "Neon", "Crystal", "Galaxy", "Carbon"));

    public CapesModule() {
        super("Capes", "Custom client capes", Category.COSMETICS);
    }

    public String getDesign() {
        return design.get();
    }

    @Override
    public String getDisplayInfo() {
        return design.get();
    }
}
