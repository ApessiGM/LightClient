package dev.lightclient.modules.player;

import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.setting.ModeSetting;
import net.minecraft.client.option.Perspective;

/**
 * Forces a chosen camera perspective while enabled and restores the previous
 * one when disabled.
 */
public final class PerspectiveModule extends Module {
    private final ModeSetting mode =
            register(new ModeSetting("View", "Camera perspective", "Third Back", "First", "Third Back", "Third Front"));

    private Perspective previous;

    public PerspectiveModule() {
        super("Perspective", "Force a camera perspective", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        if (mc.options == null) {
            return;
        }
        previous = mc.options.getPerspective();
        mc.options.setPerspective(switch (mode.get()) {
            case "First" -> Perspective.FIRST_PERSON;
            case "Third Front" -> Perspective.THIRD_PERSON_FRONT;
            default -> Perspective.THIRD_PERSON_BACK;
        });
    }

    @Override
    public void onDisable() {
        if (mc.options != null && previous != null) {
            mc.options.setPerspective(previous);
            previous = null;
        }
    }

    @Override
    public String getDisplayInfo() {
        return mode.get();
    }
}
