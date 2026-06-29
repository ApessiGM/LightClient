package dev.lightclient.modules.render;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.BooleanSetting;

/** Restores 1.7-style item swing and equip animations. */
public final class OldAnimationsModule extends Module {
    private final BooleanSetting oldSwing = register(new BooleanSetting("Old Swing", "1.7 style hand swing", true));
    private final BooleanSetting oldBlocking = register(new BooleanSetting("Old Blocking", "1.7 style sword blocking", true));

    public OldAnimationsModule() {
        super("Old Animations", "Classic 1.7 animations", Category.RENDER);
    }

    public boolean oldSwing() {
        return oldSwing.get();
    }

    public boolean oldBlocking() {
        return oldBlocking.get();
    }
}
